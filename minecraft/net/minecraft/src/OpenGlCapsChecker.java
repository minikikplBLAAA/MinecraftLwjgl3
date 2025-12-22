package net.minecraft.src;
 

import org.lwjgl.opengl.GL;

public class OpenGlCapsChecker
{

    public OpenGlCapsChecker()
    {
    }

    public boolean checkARBOcclusion()
    {
        return tryCheckOcclusionCapable && GL.getCapabilities().GL_ARB_occlusion_query;
    }

    private static boolean tryCheckOcclusionCapable = false;

}
