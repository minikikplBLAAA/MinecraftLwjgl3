package net.minecraft.src;

import java.io.IOException;
import java.io.InputStream;

public class CodecMus
{

    public CodecMus()
    {
    }

    protected InputStream openInputStream()
    {
        return null; // Stub implementation - no music codec
    }

    public boolean initialized()
    {
        return false; // Stub - never initialized
    }

    public void cleanup()
    {
        // Stub - no cleanup needed
    }
}
