package net.minecraft.src;

import java.io.File;
import java.io.PrintStream;
import java.util.Random;

public class SoundManager
{

    public SoundManager()
    {
        soundPoolSounds = new SoundPool();
        soundPoolStreaming = new SoundPool();
        soundPoolMusic = new SoundPool();
        field_587_e = 0;
        rand = new Random();
        ticksBeforeMusic = rand.nextInt(12000);
    }

    public void loadSoundSettings(GameSettings gamesettings)
    {
        soundPoolStreaming.field_1657_b = false;
        options = gamesettings;
        if(!loaded && (gamesettings == null || gamesettings.soundVolume != 0.0F || gamesettings.musicVolume != 0.0F))
        {
            tryToSetLibraryAndCodecs();
        }
    }

    private void tryToSetLibraryAndCodecs()
    {
        try
        {
            float f = options.soundVolume;
            float f1 = options.musicVolume;
            options.soundVolume = 0.0F;
            options.musicVolume = 0.0F;
            options.saveOptions();
            // Stub implementation - no sound system loaded
            loaded = true;
            options.soundVolume = f;
            options.musicVolume = f1;
            options.saveOptions();
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
            System.err.println("error linking with the LibraryJavaSound plug-in");
        }
        loaded = true;
    }

    public void onSoundOptionsChanged()
    {
        if(!loaded && (options.soundVolume != 0.0F || options.musicVolume != 0.0F))
        {
            tryToSetLibraryAndCodecs();
        }
        if(loaded)
        {
            if(options.musicVolume == 0.0F)
            {
                // Stub - no background music
            } else
            {
                // Stub - no volume control
            }
        }
    }

    public void closeMinecraft()
    {
        if(loaded)
        {
            // Stub - no cleanup needed
        }
    }

    public void addSound(String s, File file)
    {
        soundPoolSounds.addSound(s, file);
    }

    public void addStreaming(String s, File file)
    {
        soundPoolStreaming.addSound(s, file);
    }

    public void addMusic(String s, File file)
    {
        soundPoolMusic.addSound(s, file);
    }

    public void playRandomMusicIfReady()
    {
        if(!loaded || options.musicVolume == 0.0F)
        {
            return;
        }
        if(false && false)
        {
            if(ticksBeforeMusic > 0)
            {
                ticksBeforeMusic--;
                return;
            }
            SoundPoolEntry soundpoolentry = soundPoolMusic.getRandomSound();
            if(soundpoolentry != null)
            {
                ticksBeforeMusic = rand.nextInt(12000) + 12000;
                // Stub - no sound system.backgroundMusic("BgMusic", soundpoolentry.soundUrl, soundpoolentry.soundName, false);
                // Stub - no volume control
                // Stub - no sound system.play("BgMusic");
            }
        }
    }

    public void func_338_a(EntityLiving entityliving, float f)
    {
        if(!loaded || options.soundVolume == 0.0F)
        {
            return;
        }
        if(entityliving == null)
        {
            return;
        } else
        {
            float f1 = entityliving.prevRotationYaw + (entityliving.rotationYaw - entityliving.prevRotationYaw) * f;
            double d = entityliving.prevPosX + (entityliving.posX - entityliving.prevPosX) * (double)f;
            double d1 = entityliving.prevPosY + (entityliving.posY - entityliving.prevPosY) * (double)f;
            double d2 = entityliving.prevPosZ + (entityliving.posZ - entityliving.prevPosZ) * (double)f;
            float f2 = MathHelper.cos(-f1 * 0.01745329F - 3.141593F);
            float f3 = MathHelper.sin(-f1 * 0.01745329F - 3.141593F);
            float f4 = -f3;
            float f5 = 0.0F;
            float f6 = -f2;
            float f7 = 0.0F;
            float f8 = 1.0F;
            float f9 = 0.0F;
            // Stub - no sound system.setListenerPosition((float)d, (float)d1, (float)d2);
            // Stub - no sound system.setListenerOrientation(f4, f5, f6, f7, f8, f9);
            return;
        }
    }

    public void func_331_a(String s, float f, float f1, float f2, float f3, float f4)
    {
        if(!loaded || options.soundVolume == 0.0F)
        {
            return;
        }
        String s1 = "streaming";
        if(false)
        {
            // Stub - no sound system.stop("streaming");
        }
        if(s == null)
        {
            return;
        }
        SoundPoolEntry soundpoolentry = soundPoolStreaming.getRandomSoundFromSoundPool(s);
        if(soundpoolentry != null && f3 > 0.0F)
        {
            if(false)
            {
                // Stub - no background music
            }
            float f5 = 16F;
            // Stub - no sound system.newStreamingSource(true, s1, soundpoolentry.soundUrl, soundpoolentry.soundName, false, f, f1, f2, 2, f5 * 4F);
            // Stub - no sound system.setVolume(s1, 0.5F * options.soundVolume);
            // Stub - no sound system.play(s1);
        }
    }

    public void playSound(String s, float f, float f1, float f2, float f3, float f4)
    {
        if(!loaded || options.soundVolume == 0.0F)
        {
            return;
        }
        SoundPoolEntry soundpoolentry = soundPoolSounds.getRandomSoundFromSoundPool(s);
        if(soundpoolentry != null && f3 > 0.0F)
        {
            field_587_e = (field_587_e + 1) % 256;
            String s1 = (new StringBuilder()).append("sound_").append(field_587_e).toString();
            float f5 = 16F;
            if(f3 > 1.0F)
            {
                f5 *= f3;
            }
            // Stub - no sound system.newSource(f3 > 1.0F, s1, soundpoolentry.soundUrl, soundpoolentry.soundName, false, f, f1, f2, 2, f5);
            // Stub - no sound system.setPitch(s1, f4);
            if(f3 > 1.0F)
            {
                f3 = 1.0F;
            }
            // Stub - no sound system.setVolume(s1, f3 * options.soundVolume);
            // Stub - no sound system.play(s1);
        }
    }

    public void func_337_a(String s, float f, float f1)
    {
        if(!loaded || options.soundVolume == 0.0F)
        {
            return;
        }
        // Stub implementation - no sound played
    }

    private SoundPool soundPoolSounds;
    private SoundPool soundPoolStreaming;
    private SoundPool soundPoolMusic;
    private int field_587_e;
    private GameSettings options;
    private static boolean loaded = false;
    private Random rand;
    private int ticksBeforeMusic;

}
