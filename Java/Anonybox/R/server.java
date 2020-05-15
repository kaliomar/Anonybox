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

public class server extends JFrame {
    private JLabel title = new JLabel();
    private JTextField enckey = new JTextField();
    private JButton connectbtn = new JButton();
    private JLabel enckeyiden = new JLabel();
    private JTextArea log = new JTextArea();
    private static SecretKeySpec secretKey;
    private static byte[] key;

    public server() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(null);
        this.setSize(new Dimension(500, 500));
        this.setTitle("Anonybox R - Server");
        this.setResizable(false);
        log.setAutoscrolls(true);
        title.setText("Welcome to Anonybox R");
        title.setBounds(new Rectangle(90, 10, 300, 35));
        title.setFont(new Font("Arial", Font.BOLD, 25));
        enckey.setBounds(new Rectangle(140, 50, 160, 25));
        connectbtn.setText("Start Server");
        connectbtn.setBounds(new Rectangle(310, 50, 95, 25));
        connectbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connection(e);
            }
        });
        enckeyiden.setText("Encryption Key:");
        enckeyiden.setFont(new Font("Arial", Font.BOLD, 15));
        enckeyiden.setBounds(new Rectangle(10, 50, 115, 25));
        log.setBounds(new Rectangle(0, 95, 485, 360));
        log.setBackground(Color.black);
        log.setForeground(Color.green);
        log.setEditable(false);
        log.setAutoscrolls(true);
        this.getContentPane().add(log, null);
        this.getContentPane().add(enckeyiden, null);
        this.getContentPane().add(connectbtn, null);
        this.getContentPane().add(enckey, null);
        this.add(title);
    }

    private void connection(ActionEvent e) {
        try {
            //Open a port
            ServerSocket s = new ServerSocket(1111);
            acceptor a = new acceptor(s);
            a.start();
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    class acceptor extends Thread {
        public ServerSocket s;

        acceptor(ServerSocket s) {
            this.s = s;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    //Accept User Connection
                    Socket c = s.accept();
                    log.append(c.getInetAddress() + " is connected" + "\n");
                    //Thread init - ???? ?????? ?? ???? ???? ???? ??? ???? ?? ??? ?????
                    ServerThread st = new ServerThread(c, enckey.getText());
                    st.start();
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }
    //THREAD - START BLOCK

    class ServerThread extends Thread {
        public Socket s;
        public String enckey;
        public String adminCreds = "";
        public String userCreds = "";

        ServerThread(Socket s, String enckey) {
            //Constructor Variable to public
            this.s = s;
            this.enckey = enckey;
        }

        @Override
        public void run() {
            try {
                //Data I/O init
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                DataInputStream dis = new DataInputStream(s.getInputStream());
                //Variables init
                String recieve = "";
                String read = "";
                boolean useru, userl, adminu, adminl, neww;
                useru = false;
                userl = false;
                neww = false;
                String finall = "";
                adminu = false;
                adminl = false;
                String username = "";
                String tmpass = "";
                File tmpfile = new File("user.txt");
                boolean exists = tmpfile.exists();
                if (exists) {
                    // DO NOTHING
                } else {
                    tmpfile.createNewFile();
                }
                tmpfile = new File("admin.txt");
                exists = tmpfile.exists();
                if (exists) {
                    //DO NOTHING
                } else {
                    tmpfile.createNewFile();
                }
                //Read and reply BLOCK
                while (true) {
                    //Read User Input
                    //Decrypt it
                    recieve = read(dis,enckey);
                    log.append(s.getInetAddress() + " sent " + recieve + "\n");
                    //Check Encryption Key
                    if (recieve.equals("ERR" + read)) {
                        //INVALID Encryption Key Status
                        dos.writeUTF("Invalid Encryption Key / Bad Message");
                    } else {
                        userCreds = new String(Files.readAllBytes(Paths.get("user.txt")));
                        adminCreds = new String(Files.readAllBytes(Paths.get("admin.txt")));
                        String[] userdb = userCreds.split(",");
                        String[] admindb = adminCreds.split(",");
                        HashMap<String, String> usermap = new HashMap<String, String>();
                        HashMap<String, String> adminmap = new HashMap<String, String>();
                        for (int x = 0; x < userdb.length; x++) {
                            String usercred = userdb[x];
                            String[] pair = usercred.split(":");
                            usermap.put(pair[0], pair[1]);
                        }
                        for (int xx = 0; xx < admindb.length; xx++) {
                            String admincred = admindb[xx];
                            String[] pairr = admincred.split(":");
                            adminmap.put(pairr[0], pairr[1]);
                        }
                        //Commands init
                        if (usermap.containsKey(recieve) && !useru && !userl) {
                            //If user entered username. not entered before and not logged in
                            write(dos, "Username is entered", enckey);
                            username = recieve;
                            useru = true;
                        } else if (recieve.equals(usermap.get(username)) && useru && !userl) {
                            //If password is entered. username is entered before and not logged in
                            write(dos, "Welcome !", enckey);
                            tmpfile = new File("user-" + username + ".txt");
                            exists = tmpfile.exists();
                            if (exists) {
                                //DO NOTHING
                            } else {
                                tmpfile.createNewFile();
                            }
                            userl = true;
                            tmpass = recieve;
                        } else if (adminmap.containsKey(recieve) && !adminu && !adminl) {
                            //If admin entered username. not entered before and not logged in
                            write(dos, "Admin is entered", enckey);
                            username = recieve;
                            adminu = true;
                        } else if (recieve.equals(adminmap.get(username)) && adminu && !adminl) {
                            //If password is entered. username is entered before and not logged in
                            write(dos, "Welcome !", enckey);
                            adminl = true;
                        } else if (userl) {
                            if (recieve.equals("show")) {
                                String data = new String(Files.readAllBytes(Paths.get("user-" + username + ".txt")));
                                write(dos, data, enckey);
                            } else if (recieve.contains("compose")) {
                                String dest = recieve.substring(8, recieve.indexOf("@"));
                                String content = recieve.substring(recieve.indexOf("@") + 1);
                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH-mm-ss");
                                LocalDateTime now = LocalDateTime.now();
                                String date = dtf.format(now);
                                String dataaa = new String(Files.readAllBytes(Paths.get("user-" + dest + ".txt")));
                                if (neww || dataaa.isEmpty()) {
                                    finall = username + ":" + content + ":" + date;
                                } else {
                                    finall = ":" + username + ":" + content + ":" + date;
                                }
                                tmpfile = new File("user-" + dest + ".txt");
                                exists = tmpfile.exists();
                                if (exists) {
                                    //DO NOTHING
                                } else {
                                    tmpfile.createNewFile();
                                    neww = true;
                                }
                                tmpfile = new File("user-" + dest + ".txt");
                                if (tmpfile.exists()) {
                                    Files.write(Paths.get("user-" + dest + ".txt"),
                                                encrypt(decrypt(dataaa, usermap.get(dest)) + finall,
                                                        usermap.get(dest)).getBytes());
                                } else {
                                    write(dos, "Username email isn't found", enckey);
                                }
                                write(dos, "Sending is done", enckey);
                            } else if (recieve.contains("change")) {
                                String newpass = recieve.substring(7);
                                String dataaa0 = new String(Files.readAllBytes(Paths.get("user.txt")));
                                dataaa0 = dataaa0.replace(username + ":" + tmpass, username + ":" + newpass);
                                Files.write(Paths.get("user.txt"), dataaa0.getBytes());
                                Files.write(Paths.get("user-" + username + ".txt"),
                                            encrypt(decrypt(new String(Files.readAllBytes(Paths.get("user-" +
                                                                                                    username +
                                                                                                    ".txt"))),
                                                            usermap.get(username)), newpass).getBytes());
                                write(dos, "Password is changed", enckey);
                            }
                        } else {
                            write(dos, "Invalid Command", enckey);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //THREAD - END BLOCK

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
            retstat = "ERR" + strToDecrypt;
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
            return "ERR";
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
        output = output.replaceAll("[-.\\+*?\\[^\\]$(){}=!<>|:\\\\]", "");
        return output;
    }
}
