package net.minecraft.src;
 

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class FontAllowedCharacters
{

    public FontAllowedCharacters()
    {
    }

    private static String func_20161_a()
    {
        String s = "";
        try
        {
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader((FontAllowedCharacters.class).getResourceAsStream("/font.txt"), "UTF-8"));
            String s1 = "";
            do
            {
                String s2;
                if((s2 = bufferedreader.readLine()) == null)
                {
                    break;
                }
                if(!s2.startsWith("#"))
                {
                    s = (new StringBuilder()).append(s).append(s2).toString();
                }
            } while(true);
            bufferedreader.close();
        }
        catch(Exception exception) { }
        return s;
    }

    public static final String field_20162_a = func_20161_a();
    public static final char field_22175_b[] = {
        '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', 
        '<', '>', '|', '"', ':'
    };

}
