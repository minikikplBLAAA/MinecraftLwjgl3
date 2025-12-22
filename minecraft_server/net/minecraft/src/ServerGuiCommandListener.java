package net.minecraft.src;
 

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import net.minecraft.server.MinecraftServer;

class ServerGuiCommandListener
    implements ActionListener
{

    ServerGuiCommandListener(ServerGUI servergui, JTextField jtextfield)
    {
        mcServerGui = servergui;
        textField = jtextfield;
//        super();
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        String s = textField.getText().trim();
        if(s.length() > 0)
        {
            ServerGUI.getMinecraftServer(mcServerGui).addCommand(s, mcServerGui);
        }
        textField.setText("");
    }

    final JTextField textField; /* synthetic field */
    final ServerGUI mcServerGui; /* synthetic field */
}
