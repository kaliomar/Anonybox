import javax.crypto.*;
import javax.crypto.spec.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
public class frame extends JFrame {
    private JTextField jTextField1 = new JTextField();
    private JTextField jTextField2 = new JTextField();
    private JButton jButton1 = new JButton();
    private boolean status = false;
    private boolean loginstat = false;
    private JTextField jTextField3 = new JTextField();
    private JTextField jTextField4 = new JTextField();
    private JButton jButton2 = new JButton();
    private String enckey = "";
    private static SecretKeySpec secretKey;
    private static byte[] key;
    private DataInputStream dis;
    private DataOutputStream dos;
    private JTextArea jTextArea1 = new JTextArea();
    private JTextField jTextField5 = new JTextField();
    private JButton jButton3 = new JButton();
    public frame() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void jbInit() throws Exception {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation( ( screenSize.width - frameSize.width ) / 2, ( screenSize.height - frameSize.height ) / 2 );
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        this.setVisible(true);
        this.getContentPane().setLayout( null );
        this.setSize(new Dimension(568, 362));
        jTextField1.setBounds(new Rectangle(0, 0, 215, 25));
        jTextField2.setBounds(new Rectangle(215, 0, 215, 25));
        jButton1.setText("Connect");
        jTextField1.setText("IP Address:Port");
        jTextField2.setText("Encryption Key");
        jTextField3.setText("Username");
        jTextField4.setText("Password");
        jButton2.setText("Login");
jTextField1.setVisible(true);
jTextField2.setVisible(true);
jTextField3.setVisible(true);
jTextField4.setVisible(true);
jTextField5.setVisible(true);
jButton1.setVisible(true);
jButton2.setVisible(true);
jButton3.setVisible(true);
        jButton1.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               // CONNECTION
               try {
               String[] ip_port = jTextField1.getText().split(":");
               Socket s = new Socket(ip_port[0],Integer.parseInt(ip_port[1]));
               dis = new DataInputStream(s.getInputStream());
               dos = new DataOutputStream(s.getOutputStream());
               enckey = jTextField2.getText();
               status = true;
               JOptionPane.showMessageDialog(null,"Connection is initialized");
               }catch(Exception ee) {
                   // CONNECTION IS FAILED
                   status = false;
                   JOptionPane.showMessageDialog(null,"Connection cannot be initialized");
                   }
           }          
        });
        jButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // LOGIN
                try {
                    if (status) {
                    write(dos,jTextField3.getText(),enckey);
                    if (read(dis,enckey).equals("User is entered !")) {
                        write(dos,jTextField4.getText(),enckey);
                        if (read(dis,enckey).equals("Password is entered, Welcome!")) {
                            //SUCCESS
                            loginstat = true;
                            JOptionPane.showMessageDialog(null,"You logged in !");
                        }else {
                            JOptionPane.showMessageDialog(null,"Password isn't correct");
                            }
                    }else {
                        JOptionPane.showMessageDialog(null,"User isn't correct");
                        }
                    }else {
                        JOptionPane.showMessageDialog(null,"You didn't connect");
                        }
                }catch(Exception ee) {
                    
                    }
                }
            });
        jButton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (loginstat) {
                    write(dos,jTextField5.getText(),enckey);
                    jTextArea1.append(read(dis,enckey)+"\n");
                    }else {
                            JOptionPane.showMessageDialog(null,"You didn't log in !");
                        }
                }catch(Exception ee) {
                    
                    }
                }
            });
        jButton1.setBounds(new Rectangle(460, 5, 75, 21));
        jTextField3.setBounds(new Rectangle(40, 30, 120, 20));
        jTextField4.setBounds(new Rectangle(265, 30, 120, 20));
        jButton2.setBounds(new Rectangle(445, 35, 75, 21));
        jTextArea1.setBounds(new Rectangle(0, 110, 565, 230));
        jTextField5.setBounds(new Rectangle(0, 75, 450, 35));
        jButton3.setText("send");
        jButton3.setBounds(new Rectangle(470, 85, 75, 21));
        this.setTitle("Anonybox Client");
        this.getContentPane().add(jButton3, null);
        this.getContentPane().add(jTextField5, null);
        this.getContentPane().add(jTextArea1, null);
        this.getContentPane().add(jButton2, null);
        this.getContentPane().add(jTextField4, null);
        this.getContentPane().add(jTextField3, null);
        this.getContentPane().add(jButton1, null);
        this.getContentPane().add(jTextField2, null);
        this.getContentPane().add(jTextField1, null);
    }
    public static void setKey(String myKey) 
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); 
            secretKey = new SecretKeySpec(key, "AES");
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } 
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public static String encrypt(String strToEncrypt, String secret) 
    {
        try
        {
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
    public static String decrypt(String strToDecrypt, String secret) 
    {
        try
        {
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
        public static void write(DataOutputStream s, String text, String key) {
                write(s,text,key,0);
        }
        public static String read(DataInputStream s, String key) {
                return read(s,key,0);
        }
        public static void write(DataOutputStream s,String text,String key,int debug) {
                if (text.isEmpty()) {
                        System.out.println("Text var is empty");
                }
                if (key.isEmpty()) {
                        System.out.println("Key var is empty");
                }
                System.out.println(text+" - "+key);
                String enc = "";
                int i = 0;
                String cut = "";
                enc = encrypt(text,key);
                System.out.println(enc);
                if (enc.length()>63980) {
                        while (i<(Math.round((double)enc.length()/63980))) {
                    cut = enc.substring(0,63980);
                    enc = enc.replace(cut,"");
                                i++;
                                try {
                                s.writeUTF(encrypt(decrypt(cut,key)+"CONTIN",key));
                                }catch(Exception e) {
                                        if (debug>0) {
                                        System.out.println("Error Writing at line 18");
                                        e.printStackTrace();
                                        }else {
                                                //DO NOTHING
                                        }
                                        //TODO: Handle writeUTF
                                }
                        }
                }else {
                        try {
                        s.writeUTF(enc);
                        }catch(Exception e) {
                                if (debug>0) {
                                System.out.println("Error Writing at line 26");
                                e.printStackTrace();
                                }else {
                                        //DO NOTHING
                                }
                                //TODO: Handle writeUTF
                        }
                }
                try {
                        s.flush();
                } catch (IOException e) {
                        if (debug>0) {
                        System.out.println("Error Flushing at line 34");
                        e.printStackTrace();
                        }else {
                                //DO NOTHING
                        }
                        //TODO: Handle s.flush();
                }
        }
        public static String read(DataInputStream s,String key,int debug) {
                if (key.isEmpty()) {
                        System.out.println("Key var is empty");
                }
                String output = "";
                try {
                output = decrypt(s.readUTF(),key);
                }catch(Exception e) {
                        if (debug>0) {
                        System.out.println("Error Reading at line 36");
                        e.printStackTrace();
                        }else {
                                //DO NOTHING
                        }
                        //TODO: Handle readUTF
                }
                if (output.contains("CONTIN")) {
                        while (output.contains("CONTIN")) {
                                output = output.replaceAll("CONTIN","");
                                try {
                                output+=decrypt(s.readUTF(),key);
                                }catch(Exception e) {
                                        if (debug>0) {
                                        System.out.println("Error Writing at line 45");
                                        e.printStackTrace();
                                        }else {
                                                //DO NOTHING
                                        }
                                        //TODO: Handle readUTF
                                }
                                }
                }else {
                        //DO NOTHING
                }
                System.out.println(output);
                return output;
                }
}
