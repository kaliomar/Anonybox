import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.time.format.*;  
import java.time.*;    
import java.net.*;
import java.util.*;
import javax.crypto.*;
import java.awt.event.*;
import java.security.*;
import javax.crypto.spec.*;
import javax.swing.*;
import javax.swing.JButton;

public class rcvclient extends JFrame {
    private JLabel title = new JLabel();
    private String ip = "";
    private String enckey = "";
    private String username = "";
    private String pass = "";
    private static SecretKeySpec secretKey;
    private static byte[] key;
    private JButton refresh = new JButton();
    private DataOutputStream dos;
    private DataInputStream dis; 
    private String rcvdmail;
    private String[] starrage;
    private JButton compose = new JButton();
	private String[] titles = new String[]{"Sender name","Message Content","Date"};
	private JTable table;
	private String[][] scage;
    private JButton change = new JButton();

    public rcvclient(String ip,String enckey,String user,String pass) {
        try {
            this.ip = ip;
            this.enckey = enckey;
            this.username = user;
            this.pass = pass;
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void jbInit() throws Exception {
        this.getContentPane().setLayout( null );
        this.setSize(new Dimension(500, 401));
        this.setResizable(false);
        title.setText("Welcome to Anonybox R");
        title.setBounds(new Rectangle(90, 10, 300, 35));
        title.setFont(new Font("Arial", Font.BOLD, 25));
		compose.setText("Compose");
		compose.setVisible(true);
        compose.setBounds(new Rectangle(165, 40, 80, 23));
        compose.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String dest = JOptionPane.showInputDialog("Enter the dest. username");
                        String content = JOptionPane.showInputDialog("Enter Message content");
                        write(dos,"compose "+dest+"@"+content,enckey);
							if(read(dis,enckey).equals("Sending is done")) {
								JOptionPane.showMessageDialog(null,"Sending done");
                            }else {
								JOptionPane.showMessageDialog(null,"Sending failed");
							}
                    }catch(Exception ee) {
                        ee.printStackTrace();
                        }
                }
            });
        change.setText("Change Password");
        change.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String newpass = JOptionPane.showInputDialog("Enter the new password");
                        write(dos,"change "+newpass,enckey);
                    }catch(Exception ee) {
                        ee.printStackTrace();
                        }
                }
            });
        change.setBounds(new Rectangle(255, 40, 120, 23));
        refresh.setText("Refresh");
        refresh.setBounds(new Rectangle(80, 40, 73, 23));
        this.setTitle( "Anonybox R - Mailbox" );
        Socket s = new Socket(ip, 1111);
        dos = new DataOutputStream(s.getOutputStream());
        dis = new DataInputStream(s.getInputStream());
        write(dos, username, enckey);
        read(dis, enckey);
        write(dos, pass, enckey);
        read(dis, enckey);
        write(dos, "show", enckey);
        rcvdmail = read(dis, enckey);
        rcvdmail = decrypt(rcvdmail,pass);
        System.out.println(rcvdmail);
        scage = new String[3][3];
        if (!rcvdmail.isEmpty()) {
            starrage = rcvdmail.split(":");
            int x = 0; // x is num of msgs. length of starrage/3
            int y = 0; // y is 3 increments. in one x increment
            while (x < (starrage.length / 3)) {
                scage[x][0] = starrage[y];
                System.out.println(starrage[y]);
                y++;
                scage[x][1] = starrage[y];
                System.out.println(starrage[y]);
                y++;
                scage[x][2] = starrage[y];
                System.out.println(starrage[y]);
                y++;
                x++;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Nothing in the mail, yet");
        }
        
        table = new JTable(scage, titles);
        table.setBounds(new Rectangle(15, 85, 455, 275));
        refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    write(dos, "show", enckey);
                    rcvdmail = read(dis, enckey);
                    rcvdmail = decrypt(rcvdmail,enckey);
	if (!rcvdmail.isEmpty()){
        starrage = rcvdmail.split(":");
        int x = 0; // x is num of msgs. length of starrage/3
        int y = 0; // y is 3 increments. in one x increment
        scage = new String[starrage.length / 3][3];
        while (x < (starrage.length / 3)) {
            scage[x][0] = starrage[y];
            y++;
            scage[x][1] = starrage[y];
            y++;
            scage[x][2] = starrage[y];
            y++;
            x++;
        }
		}else {
			JOptionPane.showMessageDialog(null,"Nothing in the mail, yet");
		}
                                        table.repaint();
                }
            });
        this.getContentPane().add(change, null);
        this.getContentPane().add(compose, null);
        this.getContentPane().add(refresh, null);
        this.add(title);
        this.add(table);
    }
    public static void setKey(String myKey) {
            MessageDigest sha = null;
            try {
                key = myKey.getBytes("UTF-8");
                sha = MessageDigest.getInstance("SHA-1");
                key = sha.digest(key);
                key = Arrays.copyOf(key, 16);
                secretKey = new SecretKeySpec(key, "AES");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        public static String encrypt(String strToEncrypt, String secret) {
            try {
                byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
                IvParameterSpec ivspec = new IvParameterSpec(iv);
                setKey(secret);
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
                return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
            } catch (Exception e) {
                System.out.println("Error while encrypting: " + e.toString());
            }
            return null;
        }
        public static String decrypt(String strToDecrypt, String secret) {
                        String retstat = "";
            try {
                byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
                IvParameterSpec ivspec = new IvParameterSpec(iv);
                setKey(secret);
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
                retstat = new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
            } catch (Exception e) {
                retstat = "Invalid Encryption Key / Bad Message";
            }
            return retstat;
        }
        public static void write(DataOutputStream s, String text, String key) {
            write(s, text, key, 0);
        }
        public static String read(DataInputStream s, String key) {
            return read(s, key, 0);
        }
        public static void write(DataOutputStream s, String text, String key, int debug) {
            if (text.isEmpty()) {
                System.out.println("Text var is empty");
            }
            if (key.isEmpty()) {
                System.out.println("Key var is empty");
            }
            String enc = "";
            int i = 0;
            String cut = "";
            enc = encrypt(text, key);
            if (enc.length() > 63980) {
                while (i < (Math.round((double)enc.length() / 63980))) {
                    cut = enc.substring(0, 63980);
                    enc = enc.replace(cut, "");
                    i++;
                    try {
                        s.writeUTF(encrypt(decrypt(cut, key) + "CONTIN", key));
                    } catch (Exception e) {
                        if (debug > 0) {
                            System.out.println("Error Writing at line 18");
                            e.printStackTrace();
                        } else {
                            //DO NOTHING
                        }
                        //TODO: Handle writeUTF
                    }
                }
            } else {
                try {
                    s.writeUTF(enc);
                } catch (Exception e) {
                    if (debug > 0) {
                        System.out.println("Error Writing at line 26");
                        e.printStackTrace();
                    } else {
                        //DO NOTHING
                    }
                    //TODO: Handle writeUTF
                }
            }
            try {
                s.flush();
            } catch (IOException e) {
                if (debug > 0) {
                    System.out.println("Error Flushing at line 34");
                    e.printStackTrace();
                } else {
                    //DO NOTHING
                }
                //TODO: Handle s.flush();
            }
        }
        public static String read(DataInputStream s, String key, int debug) {
            if (key.isEmpty()) {
                System.out.println("Key var is empty");
            }
            String output = "";
            try {
                output = decrypt(s.readUTF(), key);
            } catch (Exception e) {
                if (debug > 0) {
                    System.out.println("Error Reading at line 36");
                    e.printStackTrace();
                } else {
                    //DO NOTHING
                }
                //TODO: Handle readUTF
                return "Invalid Encryption Key / Bad Message";
            }
            if (output.contains("CONTIN")) {
                while (output.contains("CONTIN")) {
                    output = output.replaceAll("CONTIN", "");
                    try {
                        output += decrypt(s.readUTF(), key);
                    } catch (Exception e) {
                        if (debug > 0) {
                            System.out.println("Error Writing at line 45");
                            e.printStackTrace();
                        } else {
                            //DO NOTHING
                        }
                        //TODO: Handle readUTF
                    }
                }
            } else {
                //DO NOTHING
            }
            return output;
        }
}