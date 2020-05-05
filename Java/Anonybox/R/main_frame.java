import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class main_frame extends JFrame {
    private JLabel title = new JLabel();
    private JButton server = new JButton();
    private JButton client = new JButton();
    main_frame() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void jbInit() throws Exception {
        this.getContentPane().setLayout(null);
        this.setSize(new Dimension(508, 303));
        this.setTitle("Anonybox R");
        this.setResizable(false);
        this.getContentPane().setBackground(Color.CYAN);
        title.setText("Welcome to Anonybox R");
        title.setBounds(new Rectangle(105, 55, 300, 35));
        title.setFont(new Font("Arial", Font.BOLD,25));
        server.setFont(new Font("Arial", Font.BOLD,15));
        client.setFont(new Font("Arial", Font.BOLD,15));
        server.setText("I'm a server");
        server.setBounds(new Rectangle(50, 140, 125, 40));
        server.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    server_action(e);
                }
            });
        client.setText("I'm a client");
        client.setBounds(new Rectangle(320, 140, 125, 40));
        client.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    client_action(e);
                }
            });
        this.getContentPane().add(client, null);
        this.getContentPane().add(server, null);
        this.add(title);
    }
    private void server_action(ActionEvent e) {
        servermain.main(new String[]{(null)});
    }
    private void client_action(ActionEvent e) {
        clientmain.main(new String[]{(null)});
    }
}
