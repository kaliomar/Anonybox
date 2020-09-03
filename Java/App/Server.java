package App;
import java.awt.*;
import java.io.*;
import java.math.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
public class Server extends javax.swing.JFrame {
    private String user_path, admin_path, db_path;
    public Server() {
        try {
        this.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("./RES/ServerBkg.jfif")))));
        }catch(Exception e) {
            e.printStackTrace();
            }
        initComponents();
        inin();
    }
    private void inin() {
        this.setTitle("Anonybox R Server");
        requirement.setText("<html>All fields marked with \"*\" are required.</html>");
    }
    @SuppressWarnings("unchecked")
    private void initComponents() {
        enck = new javax.swing.JTextField();
        user = new javax.swing.JTextField();
        admin = new javax.swing.JTextField();
        enckey_label = new javax.swing.JLabel();
        user_label = new javax.swing.JLabel();
        admin_label = new javax.swing.JLabel();
        requirement = new javax.swing.JLabel();
        start = new javax.swing.JButton();
        admin_label1 = new javax.swing.JLabel();
        db_file = new javax.swing.JTextField();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Anonybox R Server");
        setResizable(false);
        enck.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        enck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enckActionPerformed(evt);
            }
        });
        user.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        admin.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        enckey_label.setFont(new java.awt.Font("Tahoma", 1, 11));
        enckey_label.setForeground(new java.awt.Color(255, 0, 0));
        enckey_label.setText("Encryption Key *");
        user_label.setFont(new java.awt.Font("Tahoma", 1, 11));
        user_label.setForeground(new java.awt.Color(255, 0, 0));
        user_label.setText("Users' DB File *");
        admin_label.setFont(new java.awt.Font("Tahoma", 1, 11));
        admin_label.setForeground(new java.awt.Color(255, 0, 0));
        admin_label.setText("Admins' DB File *");
        requirement.setFont(new java.awt.Font("Tahoma", 1, 11));
        requirement.setForeground(new java.awt.Color(255, 0, 0));
        requirement.setText("All fields marked with \"*\" are required.");
        start.setText("Start Server");
        start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startActionPerformed(evt);
            }
        });
        admin_label1.setFont(new java.awt.Font("Tahoma", 1, 11));
        admin_label1.setForeground(new java.awt.Color(255, 0, 0));
        admin_label1.setText("IDs' DB File *");
        db_file.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(enckey_label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(user_label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(admin_label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(admin_label1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(enck, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(user, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(admin, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(db_file, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(35, 35, 35))
                        .addComponent(requirement, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(97, 97, 97)
                        .addComponent(start)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(52, Short.MAX_VALUE)
                .addComponent(requirement)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(enck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(enckey_label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(user_label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(admin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(admin_label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(admin_label1)
                    .addComponent(db_file, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(start)
                .addGap(26, 26, 26))
        );

        pack();
    }
    String enckey = "";
    ServerSocket s = null;
    Accept a = new Accept(s, enckey);
    private void startActionPerformed(java.awt.event.ActionEvent evt) {
        enckey = enck.getText();
        boolean userIsEmpty = user.getText().isEmpty();
        boolean adminIsEmpty = admin.getText().isEmpty();
        boolean idIsEmpty = db_file.getText().isEmpty();
        if (!userIsEmpty && !adminIsEmpty && !idIsEmpty) {
            user_path = user.getText();
            admin_path = admin.getText();
            db_path = db_file.getText();
        } else {
            JOptionPane.showMessageDialog(this, "One of the arguments is missing");
        }
        try {
            s = new ServerSocket(1111);
            a = new Accept(s, enckey);
            a.start();
        } catch (IOException e) {
            e.getStackTrace();
            JOptionPane.showMessageDialog(this, "The server cannot be started. Please check terminal log !");
        }
    }
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing
                                                                   .UIManager
                                                                   .getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing
                         .UIManager
                         .setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util
                .logging
                .Logger
                .getLogger(App.Server.class.getName())
                .log(java.util
                         .logging
                         .Level
                         .SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util
                .logging
                .Logger
                .getLogger(App.Server.class.getName())
                .log(java.util
                         .logging
                         .Level
                         .SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util
                .logging
                .Logger
                .getLogger(App.Server.class.getName())
                .log(java.util
                         .logging
                         .Level
                         .SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util
                .logging
                .Logger
                .getLogger(App.Server.class.getName())
                .log(java.util
                         .logging
                         .Level
                         .SEVERE, null, ex);
        }
        java.awt
            .EventQueue
            .invokeLater(new Runnable() {
                public void run() {
                    new App.Server().setVisible(true);
                }
            });
    }
    private javax.swing.JTextField admin;
    private javax.swing.JLabel admin_label;
    private javax.swing.JLabel admin_label1;
    private javax.swing.JTextField db_file;
    private javax.swing.JTextField enck;
    private javax.swing.JLabel enckey_label;
    private javax.swing.JLabel requirement;
    private javax.swing.JButton start;
    private javax.swing.JTextField user;
    private javax.swing.JLabel user_label;
    class ServerThread extends Thread {
        private Socket s;
        private String enckey;

        ServerThread(Socket s, String enckey) {
            this.s = s;
            this.enckey = enckey;
        }

        @Override
        public void run() {
            HashMap<String, String> userDB = new HashMap<String, String>();
            HashMap<String, String> adminDB = new HashMap<String, String>();
            HashMap<String, String> userIDs = new HashMap<String, String>();
            boolean userRandom, adminRandom, userIsLogged, adminIsLogged, userIsEntered, adminIsEntered;
            userIsLogged = false;
            adminIsLogged = false;
            userIsEntered = false;
            adminIsEntered = false;
            try {
                String db_data = anonybox.readFile(db_path);
                System.out.println(db_data);
                userIDs = anonybox.TTHM(db_data, ",", ":");
                if (user_path.contains("_RANDOM")) {
                    userRandom = true;
                    admin_path = user_path.replaceAll("_RANDOM", "");
                } else {
                    userRandom = false;
                }
                if (admin_path.contains("_RANDOM")) {
                    adminRandom = false;
                    admin_path = admin_path.replaceAll("_RANDOM", "");
                } else {
                    adminRandom = true;
                }
                if (!userRandom) {
                    try {
                        String userCreds = anonybox.readFile(user_path);
                        userDB = anonybox.TTHM(userCreds, ",", ":");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    userDB.put(user_path, user_path);
                }
                if (adminRandom) {
                    try {
                        String adminCreds = anonybox.readFile(admin_path);
                        adminDB = anonybox.TTHM(adminCreds, ",", ":");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    adminDB.put(admin_path, admin_path);
                }
                DataInputStream DIS = new DataInputStream(s.getInputStream());
                DataOutputStream DOS = new DataOutputStream(s.getOutputStream());
                String username = "";
                for (Map.Entry<String, String> entry : userIDs.entrySet())
                    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                for (Map.Entry<String, String> entry : userDB.entrySet())
                    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                for (Map.Entry<String, String> entry : adminDB.entrySet())
                    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                byte[] iv = anonybox.EMPTY_IV;
                while (true) {
                    String i = anonybox.read(DIS, enckey, iv);
                    if (i.equals("IVGENERATE")) {
                        iv = anonybox.AES.generateIV();
                        DOS.writeInt(iv.length);
                        DOS.write(iv);
                    } else if (i.contains("mail")) {
                        if (userIsLogged) {
                            String ID = userIDs.get(username);
                            String o = anonybox.readFile(ID + "-mail.txt");
                            if (o.equals("")) {
                                o = "Nothing is here";
                                }
                            try {
                                anonybox.write(DOS, o, enckey, iv);
                            } catch (Exception e) {
                                e.printStackTrace();
                                anonybox.write(DOS, "No mail is created", enckey, iv);
                            }
                        } else {
                            anonybox.write(DOS, "You're not logged in", enckey, iv);
                        }
                    } else if (i.equals("TEST")) {
                        anonybox.write(DOS, "DONE", enckey, iv);
                    } else if (i.equals("ENCERR")) {
                        DOS.writeUTF("IEK"); // Invalid Encryption Key
                    } else if (i.equals("")) {
                        this.interrupt();
                    } else if (i.length() > 4) {
                        if (i.substring(0, 5).contains("user") && !userIsLogged && !adminIsLogged) {
                            username = i.substring(5);
                            if (userDB.containsKey(username) || adminDB.containsKey(username)) {
                                anonybox.write(DOS, "Username is entered", enckey, iv);
                                if (userDB.containsKey(username)) {
                                    userIsEntered = true;
                                } else {
                                    adminIsEntered = true;
                                }
                            } else if (!userDB.containsKey(username) || !adminDB.containsKey(username)) {
                                anonybox.write(DOS, "No username is found", enckey, iv);
                            }
                        } else if (i.substring(0, 5).contains("pass") && userIsEntered ^ adminIsEntered) {
                            String pass = i.substring(5);
                            if (userIsEntered && userDB.get(username).equals(pass)) {
                                anonybox.write(DOS, "Welcome " + username, enckey, iv);
                                File f = new File("./DATA/" + userIDs.get(username) + "-mail.txt");
                                if (f.exists()) {
                                } else {
                                    f.createNewFile();
                                }
                                userIsLogged = true;
                            } else if (adminIsEntered && adminDB.get(username).equals(pass)) {
                                anonybox.write(DOS, "Welcome Admin " + username, enckey, iv);
                                adminIsLogged = true;
                            } else if (!userDB.get(username).equals(pass) || !adminDB.get(username).equals(pass)) {
                                anonybox.write(DOS, "Wrong password", enckey, iv);
                            }
                        } else if (i.equals("logout")) {
                            if (userIsLogged) {
                                userIsLogged = false;
                                username = "";
                                anonybox.write(DOS, "Logged out", enckey, iv);
                            } else {
                                anonybox.write(DOS, "You're not logged in", enckey, iv);
                            }
                        } else if (i.contains("change password")) {
                            if (userIsLogged) {
                                try {
                                    String oldpass = i.substring(16, i.indexOf(","));
                                    String newpass = i.substring(i.indexOf(",") + 1);
                                    if (userDB.get(username).equals(oldpass)) {
                                        userDB.put(username, newpass);
                                        String tempdata = anonybox.readFile(user_path);
                                        tempdata =
                                            tempdata.replaceAll(username + ":" + oldpass, username + ":" + newpass);
                                        anonybox.writeFile(user_path, tempdata);
                                        userDB.put(username, newpass);
                                        anonybox.write(DOS, "Done", enckey, iv);
                                    } else {
                                        anonybox.write(DOS, "Wrong old password", enckey, iv);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    anonybox.write(DOS, "No delimiter ','", enckey, iv);
                                }
                            } else if (adminIsLogged) {
                                try {
                                    String oldpass = i.substring(16, i.indexOf(","));
                                    String newpass = i.substring(i.indexOf(",") + 1);
                                    if (adminDB.get(username).equals(oldpass)) {
                                        adminDB.put(username, newpass);
                                        String tempdata = anonybox.readFile(admin_path);
                                        tempdata =
                                            tempdata.replaceAll(username + ":" + oldpass, username + ":" + newpass);
                                        anonybox.writeFile(admin_path, tempdata);
                                        adminDB.put(username, newpass);
                                        anonybox.write(DOS, "Done", enckey, iv);
                                    } else {
                                        anonybox.write(DOS, "Wrong old password", enckey, iv);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    anonybox.write(DOS, "No delimiter ','", enckey, iv);
                                }
                            } else {
                                anonybox.write(DOS, "You're not logged in", enckey, iv);
                            }
                        } else if (i.contains("compose")) {
                            if (userIsLogged) {
                                String content = i.substring(8, i.indexOf(","));
                                String dest = i.substring(i.indexOf(",") + 1);
                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                                LocalDateTime now = LocalDateTime.now();
                                String line = "";
                                if (anonybox.readFile(userIDs.get(dest) + "-mail.txt").equals("")) {
                                    line = username + ":" + content + ":" + dtf.format(now);
                                } else {
                                    line = "," + username + ":" + content + ":" + dtf.format(now);
                                }
                                try {
                                    anonybox.writeData(userIDs.get(dest) + "-mail.txt",
                                                       anonybox.readFile(userIDs.get(dest) + "-mail.txt") + line, ",");
                                    anonybox.write(DOS, "Done !", enckey, iv);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    anonybox.write(DOS, "Failed to send", enckey, iv);
                                }
                            } else {
                                anonybox.write(DOS, "You're not logged in", enckey, iv);
                            }
                        } else if (i.contains("change username")) {
                            if (userIsLogged) {
                                String olduser = i.substring(16, i.indexOf(","));
                                String newuser = i.substring(i.indexOf(",") + 1);
                                if (olduser.equals(username)) {
                                    String PtbS = userDB.get(username);
                                    username = newuser;
                                    userDB.remove(olduser, PtbS);
                                    userDB.put(newuser, PtbS);
                                    anonybox.writeFile(user_path,
                                                       anonybox.readFile(user_path)
                                                       .replaceAll(olduser + ":" + PtbS, newuser + ":" + PtbS));
                                    anonybox.writeFile(db_path,
                                                       anonybox.readFile(db_path).replaceAll(olduser, newuser));
                                    anonybox.write(DOS, "Done", enckey, iv);
                                } else {
                                    anonybox.write(DOS, "Wrong old username", enckey, iv);
                                }
                            } else {
                                anonybox.write(DOS, "You're not logged in", enckey, iv);
                            }
                        } else if (i.contains("create user")) {
                            if (adminIsLogged) {
                                String user = i.substring(12, i.indexOf(","));
                                String pass = i.substring(i.indexOf(",") + 1);
                                userDB.put(user, pass);
                                anonybox.writeData(user_path, user + ":" + pass, ",");
                                anonybox.writeData(db_path, user + ":" + gR(), ",");
                                anonybox.write(DOS, "Done !", enckey, iv);
                            } else {
                                anonybox.write(DOS, "You're not an admin", enckey, iv);
                            }
                        } else if (i.contains("delete user")) {
                            if (adminIsLogged) {
                                String user = i.substring(12, i.indexOf(","));
                                String pass = i.substring(i.indexOf(",") + 1);
                                String yasss = "";
                                String y = "";
                                if (anonybox.readFile(user_path).equals("")) {
                                    yasss = user + ":" + pass;
                                } else {
                                    yasss = "," + user + ":" + pass;
                                }
                                if (anonybox.readFile(db_path).equals("")) {
                                    y = user + ":" + userIDs.get(user);
                                } else {
                                    y = "," + user + ":" + userIDs.get(user);
                                }
                                anonybox.writeFile(user_path, anonybox.readFile(user_path).replaceAll(yasss, ""));
                                anonybox.writeFile(db_path, anonybox.readFile(user_path).replaceAll(y, ""));
                                anonybox.write(DOS, "Done !", enckey, iv);
                            } else {
                                anonybox.write(DOS, "You're not an admin", enckey, iv);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("[CRITICAL] Error just happened");
                e.printStackTrace();
            }
        }

        public BigInteger gR() {
            BigInteger maxLimit = new BigInteger("99999999999");
            BigInteger minLimit = new BigInteger("00000000000");
            BigInteger bigInteger = maxLimit.subtract(minLimit);
            Random randNum = new Random();
            int len = maxLimit.bitLength();
            BigInteger res = new BigInteger(len, randNum);
            if (res.compareTo(minLimit) < 0)
                res = res.add(minLimit);
            if (res.compareTo(bigInteger) >= 0)
                res = res.mod(bigInteger).add(minLimit);
            return res;
        }
    }
    class Accept extends Thread {
        private ServerSocket s;
        private String enckey;
        Accept(ServerSocket s, String enckey) {
            this.s = s;
            this.enckey = enckey;
        }
        @Override
        public void run() {
            try {
                System.out.println("[INFO] Started the server");
                while (true) {
                    Socket c = s.accept();
                    System.out.println("[INFO] " + c.getInetAddress() + " is connected" + "\n");
                    ServerThread st = new ServerThread(c, enckey);
                    st.start();
                }
            } catch (Exception ee) {
                System.out.println("[CRITICAL] Error just happened");
                ee.printStackTrace();
            }
        }
    }
}