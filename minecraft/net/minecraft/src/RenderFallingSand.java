package net.minecraft.src;
 

import org.lwjgl.opengl.GL11;

public class RenderFallingSand extends Render
{

    public RenderFallingSand()
    {
        field_197_d = new RenderBlocks();
        shadowSize = 0.5F;
    }

    public void func_156_a(EntityFallingSand entityfallingsand, double d, double d1, double d2, 
            float f, float f1)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        loadTexture("/terrain.png");
        Block block = Block.blocksList[entityfallingsand.blockID];
        World world = entityfallingsand.func_465_i();
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        field_197_d.func_1243_a(block, world, MathHelper.floor_double(entityfallingsand.posX), MathHelper.floor_double(entityfallingsand.posY), MathHelper.floor_double(entityfallingsand.posZ));
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glPopMatrix();
    }

    public void doRender(Entity entity, double d, double d1, double d2, 
            float f, float f1)
    {
        func_156_a((EntityFallingSand)entity, d, d1, d2, f, f1);
    }

    private RenderBlocks field_197_d;
}
