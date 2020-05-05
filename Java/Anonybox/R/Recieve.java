import java.time.format.*;   
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import javax.crypto.*;
import java.time.*;
import java.awt.*;
import java.awt.event.*;
import java.security.*;
import javax.crypto.spec.*;
import javax.swing.*;
public class Recieve {
    public Recieve(String ip,String enckey,String user,String pass) {
        JFrame frame = new rcvclient(ip,enckey,user,pass);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation( ( screenSize.width - frameSize.width ) / 2, ( screenSize.height - frameSize.height ) / 2 );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Recieve(args[0],args[1],args[2],args[3]);
    }
}
