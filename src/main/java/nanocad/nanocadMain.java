package nanocad;

import java.awt.*;
import javax.swing.JFrame;

import org.gridchem.client.common.Settings;

public class nanocadMain
{
    public static JFrame nano;
//    public static nanocadFrame2 nano;

    public static void main(String args[])
    {
    Settings settings = Settings.getInstance();
	nano = new nanocadFrame2();
	//nano = new nanocadFrame();
	nano.setBounds(10, 10, 700, 600);
	nano.setVisible(true);
    }
}


