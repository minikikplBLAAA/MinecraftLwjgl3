package net.minecraft.src;
 

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ChunkFolderPattern
    implements FileFilter
{

    private ChunkFolderPattern()
    {
    }

    public boolean accept(File file)
    {
        if(file.isDirectory())
        {
            Matcher matcher = field_22214_a.matcher(file.getName());
            return matcher.matches();
        } else
        {
            return false;
        }
    }

    ChunkFolderPattern(Empty2 empty2)
    {
        this();
    }

    public static final Pattern field_22214_a = Pattern.compile("[0-9a-z]|([0-9a-z][0-9a-z])");

}
