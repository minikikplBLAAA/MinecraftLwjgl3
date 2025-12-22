package net.minecraft.src;

import org.lwjgl.opengl.GL11;
import java.nio.FloatBuffer;

public class GLUHelper {
    
    public static void gluPerspective(float fovy, float aspect, float zNear, float zFar) {
        float fh = (float) Math.tan(Math.toRadians(fovy) / 2.0f) * zNear;
        float left = -fh * aspect;
        float right = fh * aspect;
        float bottom = -fh;
        float top = fh;
        
        // GL11.glFrustum(left, right, bottom, top, zNear, zFar); // Deprecated in OpenGL 4.6
    }
    
    public static String gluErrorString(int errorCode) {
        switch(errorCode) {
            case GL11.GL_NO_ERROR: return "GL_NO_ERROR";
            case GL11.GL_INVALID_ENUM: return "GL_INVALID_ENUM";
            case GL11.GL_INVALID_VALUE: return "GL_INVALID_VALUE";
            case GL11.GL_INVALID_OPERATION: return "GL_INVALID_OPERATION";
            case GL11.GL_STACK_OVERFLOW: return "GL_STACK_OVERFLOW";
            case GL11.GL_STACK_UNDERFLOW: return "GL_STACK_UNDERFLOW";
            case GL11.GL_OUT_OF_MEMORY: return "GL_OUT_OF_MEMORY";
            default: return "Unknown error: " + errorCode;
        }
    }
}
