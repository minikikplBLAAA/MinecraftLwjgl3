package net.minecraft.src;
 


class ThreadRunIsoClient extends Thread
{

    ThreadRunIsoClient(CanvasIsomPreview canvasisompreview)
    {
        isoCanvas = canvasisompreview;
//        super();
    }

    public void run()
    {
        while(CanvasIsomPreview.isRunning(isoCanvas)) 
        {
            isoCanvas.showNextBuffer();
            try
            {
                Thread.sleep(1L);
            }
            catch(Exception exception) { }
        }
    }

    final CanvasIsomPreview isoCanvas; /* synthetic field */
}
