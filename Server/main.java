import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
public class main {
 public static final String ANSI_RESET = "\u001B[0m";
 public static final String ANSI_RED = "\u001B[31m";
 public static final String ANSI_GREEN = "\u001B[32m";
 public static final String ANSI_CYAN = "\u001B[36m";
 @SuppressWarnings("resource")
 public static void main (String[] args) {
  String key = null;
  Socket s = null;
  ServerSocket serverSocket = null;
  key = args[0];
     try {
   char[] sm = key.toCharArray();
   System.out.println(ANSI_CYAN+"Using Key: "+key+ANSI_RESET);
   String help = "";
   String admin = "";
   String data = "";
   String ooo = "";
   int stat = -1;
   try {
   stat = 0;
   help = new String(Files.readAllBytes(Paths.get("help.txt"))); 
   stat = 1;
   admin = new String(Files.readAllBytes(Paths.get("admin.txt")));
   stat = 2;
   data = new String(Files.readAllBytes(Paths.get("user.txt")));
   }catch(Exception e) {
	   if (stat == 0) {
		   System.out.println(ANSI_RED+"Help file cannot be accessed. Exiting.."+ANSI_RESET);
		   System.exit(0);
	   }else if (stat == 1) {
		   System.out.println(ANSI_RED+"Admin data file cannot be accessed. Exiting.."+ANSI_RESET);
		   System.exit(0);
	   }else if (stat == 2) {
		   System.out.println(ANSI_RED+"User data file cannot be accessed. Exiting.."+ANSI_RESET);
		   System.exit(0);
	   }else {
		   e.printStackTrace();
	   }
   }
   String Sentencee = "";
   String response = ""; 
   PrintWriter writer = null;
   URL uurl = null;
   HttpURLConnection uconn = null;
   BufferedReader urd = null;
   String uline = "";
   String uresult = "";
   String state = ""; 
   String user = ""; 
   String token = ""; 
   String login = ""; 
   String userm = ""; 
   String usernm = ""; 
   String line = ""; 
   String keyp = ""; 
   String keyu = "";
   String[] p = data.split(":");
   String[] admindb = admin.split(":");
   HashMap <String,String> adminhm = new HashMap <String,String>();
   HashMap <String,String> hm = new HashMap <String,String>();
   File fileee = new File("user.txt");
   boolean adminlog = false;
   for (int i = 1,o = 2; i < admindb.length; i += 2) {
	    adminhm.put(admindb[i - 1], admindb[o - 1]);
	    o += 2;
	   }
   for (int i = 1,o = 2; i < p.length; i += 2) {
    hm.put(p[i - 1], p[o - 1]);
    o += 2;
   }
   System.out.println(ANSI_GREEN+"All data read. Starting Server.."+ANSI_RESET);
   serverSocket = new ServerSocket(2468,100);
   System.out.println(ANSI_GREEN+"Started"+ANSI_RESET);
while (true) {
   s = serverSocket.accept();
   DataInputStream dis = new DataInputStream(s.getInputStream());
   DataOutputStream outToClient = new DataOutputStream(s.getOutputStream());
   String clientSentence = null;
   while (true) {
	   clientSentence = dis.readUTF();
	   System.out.println(ANSI_CYAN+"Recieved Message from: "+s.getInetAddress()+" : "+clientSentence+ANSI_RESET);
	   clientSentence = AES.decrypt(clientSentence,key);
       while (clientSentence.contains("SCPNULCHAR")) {
           outToClient.writeUTF(AES.encrypt("",key));
           outToClient.flush();
           clientSentence = clientSentence.replaceAll("SCPNULCHAR","");
       	clientSentence += AES.decrypt(dis.readUTF(),key);
       }
	   String dc0 = AES.encrypt(help,key);
	   if (clientSentence.equals("help")) {
		   outToClient.writeUTF(dc0);
	   }
    if (clientSentence.contains("user") && !(state.equals("true"))) {
    	if (hm.containsKey(clientSentence.substring(5))) {
     keyu = clientSentence.substring(5);
     keyp = hm.get(keyu);
      response = "USER OK";
      String dc = AES.encrypt(response,key);
      String usermail = keyu;
      outToClient.writeUTF(dc);
      user = "usertrue";
     }else {
    	 outToClient.writeUTF(AES.encrypt("No user found",key));
     }
    	}
    if (clientSentence.contains("pass") && !(state.equals("true"))) {
    	if (user.equals("usertrue")) {
     String kk = clientSentence.substring(5);
     if (kk.equals(keyp)) {
    	 usernm = keyu;
   	  if (Files.exists(Paths.get(keyu+"-mail.txt"))) {
   		response = "PASS OK !,Welcome "+keyu; 
   		String dc = AES.encrypt(response,key);
        outToClient.writeUTF(dc);
	    	}else {
	    		response = "PASS OK !,Welcome "+keyu+"SUPNULCHAR";
	    		writer = new PrintWriter(usernm+"-mail.txt", "UTF-8");
	        	writer.append(null);
	        	response = "No Mailbox Owned ! Created New One !";
	        	outToClient.writeUTF(AES.encrypt(response,key));
	    	}
      String dc = AES.encrypt(response,key);
      outToClient.writeUTF(dc);
      state = "true";
	  } else {
      response = "PASSWORD IS WRONG";
      String dc = AES.encrypt(response,key);
      outToClient.writeUTF(dc);
	}}else{
		outToClient.writeUTF(AES.encrypt("You didn't enter the username, use user [user]",key));
	}}
    if (state.equals("true") && clientSentence.equals("mail")) {
    	if (Files.exists(Paths.get(keyu+"-mail.txt"))) {
    	String dataaa = new String(Files.readAllBytes(Paths.get(keyu+"-mail.txt")));
    	String dc = AES.encrypt(dataaa,key);
        if (dc.length()<=63980) {
       	 outToClient.writeUTF(dc);
       	 urd.close();
        }else {
            for (int oo = 0;oo<(dc.length()/63980)+1;oo++) {
           	 ooo = dc.substring(0,63980);
           	 dc = dc.replace(ooo,"");
           	 outToClient.writeUTF(AES.encrypt(AES.decrypt(dc,key)+"SCPNULCHAR",key));
            }
    	}
        }else {
        	writer.append(null);
        	response = "No Mailbox Owned ! Created New One !";
        	outToClient.writeUTF(AES.encrypt(response,key));
    	}
    }
    if (state.equals("true") && clientSentence.contains("change password")) {
    	String ppaass = clientSentence.substring(16);
    	String neww = data.replace(keyp,ppaass);
        fileee.delete();
        BufferedWriter writeer = new BufferedWriter(new FileWriter("user.txt", true));
        writeer.write(neww);
        writeer.close();
        outToClient.writeUTF(AES.encrypt("The password changed successfully !",key));
    }
    if (state.equals("true") && clientSentence.contains("send mail")) {
    	String to = clientSentence.substring(13,clientSentence.indexOf("@"));
    	String message = clientSentence.substring((clientSentence.indexOf("@")+1));
        BufferedWriter ewe = new BufferedWriter(new FileWriter(to+"-mail.txt", true));
        String wew = new String(Files.readAllBytes(Paths.get(to+"-mail.txt")));
        if ((wew.equals(null) || wew.equals("")) == false) {
        	ewe.newLine();
        }else {}
    	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	Date date = new Date();
        ewe.append(usernm+" -> "+message+" - "+formatter.format(date));
        ewe.close();
	    String dc = AES.encrypt("SENDING DONE !",key);
        outToClient.writeUTF(dc);
    }
    if (state.equals("true") && (clientSentence.equals("create mail") || clientSentence.equals("reset mail"))) {
    	writer.append(null);
    	response = "New Mail box created !";
    	outToClient.writeUTF(AES.encrypt(response,key));
    }
    if (adminhm.containsKey(clientSentence) && !(login.equals("true"))) {
     userm = clientSentence;
	response = "Admin User OK";
     String dc = AES.encrypt(response,key);
     outToClient.writeUTF(dc);
    } else if (clientSentence.equals(adminhm.get(userm))) {
     adminlog = true;
     response = "Admin Password OK.Welcome Administrator.";
	String dc = AES.encrypt(response,key);
     outToClient.writeUTF(dc);
     if (adminlog) {
         login = "true";
        }
    }
    if (clientSentence.contains("createuser") && login.equals("true")) {
     outToClient.writeUTF(AES.encrypt("DONE !",key));
     String in = clientSentence.substring(11);
     writer.write(in);
     String[] pp = in .split(":");
     hm.put(pp[0], pp[1]);
     writer.close();
    }
    if (clientSentence.contains("list users") && login.equals("true")) {
    for (String kkey:hm.keySet()) {
    Sentencee = Sentencee.concat(kkey);
}
    String content = AES.encrypt(Sentencee,key);
    if (content.length()<=63980) {
   	 outToClient.writeUTF(content);
    }else {
        for (int oo = 0;oo<(content.length()/63980)+1;oo++) {
       	 ooo = content.substring(0,63980);
       	 content = content.replace(ooo,"");
       	 outToClient.writeUTF(AES.encrypt(AES.decrypt(content,key)+"SCPNULCHAR",key));
        }
    }
}
    if (clientSentence.contains("deleteuser") && login.equals("true")) {
    	data = new String(Files.readAllBytes(Paths.get("user.txt")));
    	String deleted = clientSentence.substring(11);
    	String username = clientSentence.substring(13,clientSentence.indexOf(":"));
    	String new000 = data.replace(deleted,"");
    	File filee = new File(username+"-mail.txt");
    	filee.delete();
    	fileee.delete();
    	fileee = new File("user.txt");
    	BufferedWriter writerr = new BufferedWriter(new FileWriter("user.txt", true));
    	writerr.write(new000);
    	writerr.close();
    	outToClient.writeUTF(AES.encrypt(username+" is deleted !",key));
    }
    if (clientSentence.contains("site") && (state.equals("true") || login.equals("true"))) {
      if (clientSentence.contains("http")) {
         uurl = new URL(clientSentence.substring(5));
      }else {
    	  uurl = new URL("http://"+clientSentence.substring(5)); 
      }
         uconn = (HttpURLConnection) uurl.openConnection();
         uconn.setRequestMethod("GET");
         urd = null;
         try {
         urd = new BufferedReader(new InputStreamReader(uconn.getInputStream()));
         }catch(Exception e) {
        	 outToClient.writeUTF(AES.encrypt("Site cannot be reached !",key));
         }
         while ((uline = urd.readLine()) != null) {
            uresult += uline;
         }
         String content = AES.encrypt(uresult,key);
         if (content.length()<=63980) {
        	 outToClient.writeUTF(content);
        	 urd.close();
         }else {
             for (int oo = 0;oo<(content.length()/63980)+1;oo++) {
            	 ooo = content.substring(0,63980);
            	 content = content.replace(ooo,"");
            	 outToClient.writeUTF(AES.encrypt(AES.decrypt(content,key)+"SCPNULCHAR",key));
             }
             urd.close();
         }
    }
    else {
    	outToClient.writeUTF(AES.encrypt("SCPNULCHAR",key));
    }
    }
   }
    	 }catch (Exception gg) {
	System.out.println(ANSI_RED+"RunTime Error!"+ANSI_RESET);
	gg.printStackTrace();
	try {
	serverSocket.close();
	s.close();
	}catch (Exception h) {
		System.out.println(ANSI_RED+"RunTime Error!"+ANSI_RESET);
		h.printStackTrace();
	}
	main(new String[] {key,"b","c"});
    }
 }
 public static boolean portav(int portNr) {
	  boolean portFree;
	  try (ServerSocket ignored = new ServerSocket(portNr)) {
	      portFree = true;
	  } catch (IOException e) {
	      portFree = false;
	  }
	  return portFree;
	}
 }
