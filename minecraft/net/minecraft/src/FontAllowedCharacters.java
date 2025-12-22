package net.minecraft.src;
 

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class FontAllowedCharacters
{

    public FontAllowedCharacters()
    {
    }

    private static String getAllowedCharacters()
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

    public static final String allowedCharacters = getAllowedCharacters();
    public static final char field_22286_b[] = {
        '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', 
        '<', '>', '|', '"', ':'
    };

}
