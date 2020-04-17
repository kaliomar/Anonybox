import java.awt.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.security.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
public class Frame extends JFrame {
    private JTextField enckey = new JTextField();
    private JButton start = new JButton();
    public JTextArea log = new JTextArea();
    private static SecretKeySpec secretKey;
    private static byte[] key;
        private static Map<String,String> data = new HashMap<String,String>();
        class t extends Thread {
                private Socket ss;
                private String k,u,p;
                t(Socket s,String key,String u,String p) {
                        this.ss = s;
                        this.u = u;
                        this.p = p;
                        this.k = key;
                }
                @Override
                public void run() {
                        try {
                        String keyy = k;
                        String userr = u;
                        String passs = p;
                        String data_dump = "";
                        String reply = "";
                        String msg = "";
                        String clientSentence = "";
                        String nick = "";
                        int msgCnt = 1;
                        String ncknms = "";
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        DataInputStream dis = new DataInputStream(ss.getInputStream());
                        DataOutputStream outToClient = new DataOutputStream(ss.getOutputStream());
                        boolean isuser = false,ispassword = false;
                        while (true) {
                                clientSentence = read(dis,keyy);
                               log.append("User "+ss.getInetAddress()+" just sent ["+clientSentence+"]");
                                if (clientSentence.equals(userr)) {
                                        if (!isuser) {
                                                reply = "User is entered !";
                                                write(outToClient,reply,keyy);
                                                isuser = true;
                                        } else {
                                                reply = "User is already entered !";
                                                write(outToClient,reply,keyy);
                                        }
                                } else if (clientSentence.equals(passs)) {
                                        if (!ispassword) {
                                                if (isuser) {
                                                        reply = "Password is entered, Welcome!";
                                                        write(outToClient,reply,keyy);
                                                        ispassword = true;
                                                } else {
                                                        reply = "NO User is entered !";
                                                        write(outToClient,reply,keyy);
                                                }
                                        } else {
                                                reply = "Already logged in";
                                                write(outToClient,reply,keyy);
                                        }
                                } else if (clientSentence.contains("nick")) {
                                        if (!ncknms.contains(clientSentence.substring(5))) {
                                        nick = clientSentence.substring(5);
                                        ncknms+=nick;
                                        reply = "Nickname is set!";
                                        write(outToClient,reply,keyy);
                                        }else {
                                                reply = "Nickname already exists !";
                                                write(outToClient,reply,keyy);
                                        }
                                } else if (clientSentence.equals("show")) {
                                        if (ispassword) {
                                                for (Map.Entry<String, String> entry : data.entrySet()) {
                                                        data_dump += entry.getKey() + "->" + entry.getValue() + "\n";
                                                }
                                                write(outToClient,data_dump,keyy);
                                        } else {
                                                write(outToClient,"You cannot view messages",keyy);
                                        }
                                        reply = "";
                                        data_dump = "";
                                        }
                 else if (clientSentence.contains("msg")) {
                                        if (ispassword) {
                                                if (!(nick.equals("") || nick.equals(null))) {
                                                        msg = clientSentence.substring(4);
                                                        msg = msg.replaceAll("msg", "");
                                                        LocalDateTime now = LocalDateTime.now();
                                                        msg = msg + " || " + dtf.format(now);
                                                        data.put(nick + "|Message no. " + msgCnt, msg);
                                                        write(outToClient,"Message is sent",keyy);
                                                        msgCnt++;
                                                } else {
                                                        write(outToClient,"No nickname is set",keyy);                                           
                                                        }
                                        } else {
                                                write(outToClient,"You cannot write messages",keyy);                                    
                                                }
                                        msg = "";
                                } else {
while (!clientSentence.equals("ERR")){
                                        write(outToClient,"Invalid Command",keyy);
}else {
System.out.println("ERR");
break;
}
                                } 
                        }
                        }catch(Exception e) {
                                e.printStackTrace();
                        }
                }
                
        }
    public Frame() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void jbInit() throws Exception {
        this.getContentPane().setLayout( null );
        this.setSize(new Dimension(427, 312));
        enckey.setBounds(new Rectangle(0, 0, 155, 25));
        start.setText("Start");
        enckey.setText("Encryption Key");
        start.setBounds(new Rectangle(270, 0, 73, 23));
        log.setBounds(new Rectangle(5, 30, 400, 245));
        this.getContentPane().add(log, null);
        this.getContentPane().add(start, null);
        this.getContentPane().add(enckey, null);
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String keyy = enckey.getText();
                    String userr = getSaltString();
                    String passs = getSaltString();
log.append(userr+" : "+passs);
                    ServerSocket server = new ServerSocket(1111);
                    JOptionPane.showMessageDialog(null,"Server started at port 1111, The random Username: "+userr+"\n The random Password: "+passs);
                    while(true) {
                            Socket c=server.accept();
                            log.append("User "+c.getInetAddress()+" is connected !");
                            Thread t = new t(c,keyy,userr,passs); 
                            t.start();
                }
                }catch(Exception ee){ee.printStackTrace();}}});
    }
    public static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890qwertyuiopasdfghjklzxcvbnm";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 20) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

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
            return new String(cipher.doFinal(Base64.getDecoder().decode (strToDecrypt)));
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
			return "ERR";
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
