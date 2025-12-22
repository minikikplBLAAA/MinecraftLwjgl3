package net.minecraft.src;
 

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ColorizerFoliage
{

    public ColorizerFoliage()
    {
    }

    public static int func_4146_a(double d, double d1)
    {
        d1 *= d;
        int i = (int)((1.0D - d) * 255D);
        int j = (int)((1.0D - d1) * 255D);
        return field_6529_a[j << 8 | i];
    }

    public static int func_21175_a()
    {
        return 0x619961;
    }

    public static int func_21174_b()
    {
        return 0x80a755;
    }

    static Class _mthclass$(String s)
    {
        try
        {
            return Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    private static final int field_6529_a[];

    static 
    {
        field_6529_a = new int[0x10000];
        try
        {
            BufferedImage bufferedimage = ImageIO.read((ColorizerFoliage.class).getResource("/misc/foliagecolor.png"));
            bufferedimage.getRGB(0, 0, 256, 256, field_6529_a, 0, 256);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }
}
