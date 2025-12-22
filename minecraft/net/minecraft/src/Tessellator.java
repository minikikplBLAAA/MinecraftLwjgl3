package net.minecraft.src;

import java.nio.*;
import java.nio.ByteOrder;
import org.lwjgl.opengl.GL11;

public class Tessellator
{
    // --- Zmieniona implementacja na Immediate Mode (Fix dla Intel Iris Xe / Modern GL) ---

    private Tessellator(int i)
    {
        vertexCount = 0;
        hasColor = false;
        hasTexture = false;
        hasNormals = false;
        rawBufferIndex = 0;
        addedVertices = 0;
        isColorDisabled = false;
        isDrawing = false;
        bufferSize = i;
        rawBuffer = new int[i];
    }

    public void draw()
    {
        if(!isDrawing)
        {
            throw new IllegalStateException("Not tesselating!");
        }
        isDrawing = false;
        if(vertexCount > 0)
        {
            GL11.glBegin(drawMode);
            for(int i = 0; i < vertexCount; i++)
            {
                int k = i * 8;

                if(hasTexture)
                {
                    float u = Float.intBitsToFloat(rawBuffer[k + 3]);
                    float v = Float.intBitsToFloat(rawBuffer[k + 4]);
                    GL11.glTexCoord2f(u, v);
                }

                if(hasColor)
                {
                    int col = rawBuffer[k + 5];
                    // FIX NAPISÓW: Rzutujemy na floaty, żeby uniknąć problemów z minusem w bajtach
                    float a = (float)(col >> 24 & 0xff) / 255.0F;
                    float r = (float)(col >> 16 & 0xff) / 255.0F; // Uwaga: Minecraft trzyma kolory jako ARGB lub ABGR zależnie od Endian
                    float g = (float)(col >> 8 & 0xff) / 255.0F;
                    float b = (float)(col & 0xff) / 255.0F;
                    
                    // Bezpieczniejsza metoda dla Javy i LWJGL
                    if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                         GL11.glColor4f(r, g, b, a); // Dostosuj jeśli kolory będą zamienione (np. niebieski zamiast czerwonego)
                    } else {
                         GL11.glColor4f(b, g, r, a);
                    }
                }

                if(hasNormals)
                {
                    int norm = rawBuffer[k + 6];
                    // Tu też bezpieczniej rzutować
                    GL11.glNormal3f((float)(norm & 0xff)/127f, (float)(norm >> 8 & 0xff)/127f, (float)(norm >> 16 & 0xff)/127f);
                }

                float x = Float.intBitsToFloat(rawBuffer[k + 0]);
                float y = Float.intBitsToFloat(rawBuffer[k + 1]);
                float z = Float.intBitsToFloat(rawBuffer[k + 2]);
                GL11.glVertex3f(x, y, z);
            }
            GL11.glEnd();
        }
        reset();
    }

    private void reset()
    {
        vertexCount = 0;
        rawBufferIndex = 0;
        addedVertices = 0;
    }

    public void startDrawingQuads()
    {
        startDrawing(7); // GL_QUADS
    }

    public void startDrawing(int i)
    {
        if(isDrawing)
        {
            throw new IllegalStateException("Already tesselating!");
        } else
        {
            isDrawing = true;
            reset();
            drawMode = i;
            hasNormals = false;
            hasColor = false;
            hasTexture = false;
            isColorDisabled = false;
            return;
        }
    }

    public void setTextureUV(double d, double d1)
    {
        hasTexture = true;
        textureU = d;
        textureV = d1;
    }

    public void setColorOpaque_F(float f, float f1, float f2)
    {
        setColorOpaque((int)(f * 255F), (int)(f1 * 255F), (int)(f2 * 255F));
    }

    public void setColorRGBA_F(float f, float f1, float f2, float f3)
    {
        setColorRGBA((int)(f * 255F), (int)(f1 * 255F), (int)(f2 * 255F), (int)(f3 * 255F));
    }

    public void setColorOpaque(int i, int j, int k)
    {
        setColorRGBA(i, j, k, 255);
    }

    public void setColorRGBA(int i, int j, int k, int l)
    {
        if(isColorDisabled)
        {
            return;
        }
        // Clamp values
        if(i > 255) i = 255; if(j > 255) j = 255; if(k > 255) k = 255; if(l > 255) l = 255;
        if(i < 0) i = 0; if(j < 0) j = 0; if(k < 0) k = 0; if(l < 0) l = 0;

        hasColor = true;
        if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
        {
            color = l << 24 | k << 16 | j << 8 | i;
        } else
        {
            color = i << 24 | j << 16 | k << 8 | l;
        }
    }

    public void addVertexWithUV(double d, double d1, double d2, double d3, double d4)
    {
        setTextureUV(d3, d4);
        addVertex(d, d1, d2);
    }

    public void addVertex(double d, double d1, double d2)
    {
        addedVertices++;
        if(drawMode == 7 && convertQuadsToTriangles && addedVertices % 4 == 0)
        {
            for(int i = 0; i < 2; i++)
            {
                int j = 8 * (3 - i);
                if(hasTexture)
                {
                    rawBuffer[rawBufferIndex + 3] = rawBuffer[(rawBufferIndex - j) + 3];
                    rawBuffer[rawBufferIndex + 4] = rawBuffer[(rawBufferIndex - j) + 4];
                }
                if(hasColor)
                {
                    rawBuffer[rawBufferIndex + 5] = rawBuffer[(rawBufferIndex - j) + 5];
                }
                rawBuffer[rawBufferIndex + 0] = rawBuffer[(rawBufferIndex - j) + 0];
                rawBuffer[rawBufferIndex + 1] = rawBuffer[(rawBufferIndex - j) + 1];
                rawBuffer[rawBufferIndex + 2] = rawBuffer[(rawBufferIndex - j) + 2];
                vertexCount++;
                rawBufferIndex += 8;
            }
        }
        if(hasTexture)
        {
            rawBuffer[rawBufferIndex + 3] = Float.floatToRawIntBits((float)textureU);
            rawBuffer[rawBufferIndex + 4] = Float.floatToRawIntBits((float)textureV);
        }
        if(hasColor)
        {
            rawBuffer[rawBufferIndex + 5] = color;
        }
        if(hasNormals)
        {
            rawBuffer[rawBufferIndex + 6] = normal;
        }
        rawBuffer[rawBufferIndex + 0] = Float.floatToRawIntBits((float)(d + xOffset));
        rawBuffer[rawBufferIndex + 1] = Float.floatToRawIntBits((float)(d1 + yOffset));
        rawBuffer[rawBufferIndex + 2] = Float.floatToRawIntBits((float)(d2 + zOffset));
        rawBufferIndex += 8;
        vertexCount++;
        if(vertexCount % 4 == 0 && rawBufferIndex >= bufferSize - 32)
        {
            draw();
            isDrawing = true;
        }
    }

    public void setColorOpaque_I(int i)
    {
        int j = i >> 16 & 0xff;
        int k = i >> 8 & 0xff;
        int l = i & 0xff;
        setColorOpaque(j, k, l);
    }

    public void setColorRGBA_I(int i, int j)
    {
        int k = i >> 16 & 0xff;
        int l = i >> 8 & 0xff;
        int i1 = i & 0xff;
        setColorRGBA(k, l, i1, j);
    }

    public void disableColor()
    {
        isColorDisabled = true;
    }

    public void setNormal(float f, float f1, float f2)
    {
        hasNormals = true;
        byte byte0 = (byte)(int)(f * 128F);
        byte byte1 = (byte)(int)(f1 * 127F);
        byte byte2 = (byte)(int)(f2 * 127F);
        normal = byte0 | byte1 << 8 | byte2 << 16;
    }

    public void setTranslationD(double d, double d1, double d2)
    {
        xOffset = d;
        yOffset = d1;
        zOffset = d2;
    }

    public void setTranslationF(float f, float f1, float f2)
    {
        xOffset += f;
        yOffset += f1;
        zOffset += f2;
    }

    private static boolean convertQuadsToTriangles = false;
    private int rawBuffer[];
    private int vertexCount;
    private double textureU;
    private double textureV;
    private int color;
    private boolean hasColor;
    private boolean hasTexture;
    private boolean hasNormals;
    private int rawBufferIndex;
    private int addedVertices;
    private boolean isColorDisabled;
    private int drawMode;
    private double xOffset;
    private double yOffset;
    private double zOffset;
    private int normal;
    public static final Tessellator instance = new Tessellator(0x200000);
    private boolean isDrawing;
    private int bufferSize;
}