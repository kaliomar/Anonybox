import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
public class SCApp {
    public SCApp() {
        JWindow window = new JWindow();
       window.add(new JLabel(new ImageIcon("loader.gif")));
        window.setBounds(200, 200, 500, 375);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        try {
                Thread.sleep(2000);
        } catch (InterruptedException e) {
                e.printStackTrace();
        }
        window.setVisible(false);
        JFrame frame = new main_frame();
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
        new SCApp();
    }
}