import java.io.*;
import java.awt.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import javax.crypto.*;
import java.time.*;
import java.awt.event.*;
import java.security.*;
import javax.crypto.spec.*;
import javax.swing.*;
public class client extends JFrame {
    private JLabel title = new JLabel();
    private JTextField ip = new JTextField();
    private JTextField enckey = new JTextField();
    private JLabel iplabel = new JLabel();
    private JLabel keylabel = new JLabel();
    private JButton connectbtn = new JButton();
    private static SecretKeySpec secretKey;
    private static byte[] key;
    private JLabel userlabel = new JLabel();
    private JLabel passlabel = new JLabel();
    private JTextField usernm = new JTextField();
    private JTextField pass = new JTextField();
    public client() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void jbInit() throws Exception {
        this.getContentPane().setLayout( null );
        this.setSize(new Dimension(500, 325));
        this.setTitle("Anonybox R - Client");
        this.setResizable(false);
        title.setText("Welcome to Anonybox R");
        title.setBounds(new Rectangle(90, 10, 300, 35));
        title.setFont(new Font("Arial", Font.BOLD, 25));
        ip.setBounds(new Rectangle(225, 55, 140, 25));
        enckey.setBounds(new Rectangle(225, 85, 140, 25));
        iplabel.setText("IP Address");
        iplabel.setFont(new Font("Arial", Font.BOLD, 15));
        iplabel.setBounds(new Rectangle(95, 55, 80, 25));
        keylabel.setText("Encryption Key");
        keylabel.setFont(new Font("Arial", Font.BOLD, 15));
        keylabel.setBounds(new Rectangle(95, 85, 125, 25));
        connectbtn.setText("Connect & Login");
        connectbtn.setBounds(new Rectangle(195, 250, 90, 30));
        connectbtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    connectbtn_action(e);
                }
            });
        userlabel.setText("Username");
        userlabel.setFont(new Font("Arial", Font.BOLD, 15));
        userlabel.setBounds(new Rectangle(95, 140, 125, 25));
        passlabel.setText("Password");
        passlabel.setFont(new Font("Arial", Font.BOLD, 15));
        passlabel.setBounds(new Rectangle(95, 170, 125, 25));
        usernm.setBounds(new Rectangle(225, 140, 140, 25));
        pass.setBounds(new Rectangle(225, 170, 140, 25));
        this.getContentPane().add(pass, null);
        this.getContentPane().add(usernm, null);
        this.getContentPane().add(passlabel, null);
        this.getContentPane().add(userlabel, null);
        this.getContentPane().add(connectbtn, null);
        this.getContentPane().add(keylabel, null);
        this.getContentPane().add(iplabel, null);
        this.getContentPane().add(enckey, null);
        this.getContentPane().add(ip, null);
        this.add(title);
    }

    private void connectbtn_action(ActionEvent e) {
        try {
        Socket s = new Socket(ip.getText(),1111);
        String enckey = this.enckey.getText();
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        DataInputStream dis = new DataInputStream(s.getInputStream());
        write(dos,"TEST",enckey);
        String TEST = read(dis,enckey);
            if (TEST.equals("Invalid Encryption Key / Bad Message")) {
                JOptionPane.showMessageDialog(null,"Invalid Encryption Key / Bad Message");
            }else {
                write(dos,usernm.getText(),enckey);
                if (read(dis,enckey).equals("Username is entered")) {
                write(dos,pass.getText(),enckey);
                if (read(dis,enckey).equals("Welcome !")) {
                Recieve.main(new String[]{(ip.getText()),(enckey),(usernm.getText()),(pass.getText())});
                }else {
                    JOptionPane.showMessageDialog(null,"Invalid Password");
                    }
                }else {
                    JOptionPane.showMessageDialog(null,"Invalid Username");
                    }
                }
        }catch(Exception ee) {
            ee.printStackTrace();
            }
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
            System.out.println(text + " - " + key);
            String enc = "";
            int i = 0;
            String cut = "";
            enc = encrypt(text, key);
            System.out.println(enc);
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
            System.out.println(output);
            return output;
        }
}