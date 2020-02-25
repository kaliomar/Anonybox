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
   System.out.println(ANSI_CYAN+"[Info] Using Key: "+key+ANSI_RESET);
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
		   System.out.println(ANSI_RED+"[Error] Help file cannot be accessed. Exiting.."+ANSI_RESET);
		   System.exit(0);
	   }else if (stat == 1) {
		   System.out.println(ANSI_RED+"[Error] Admin data file cannot be accessed. Exiting.."+ANSI_RESET);
		   System.exit(0);
	   }else if (stat == 2) {
		   System.out.println(ANSI_RED+"[Error] User data file cannot be accessed. Exiting.."+ANSI_RESET);
		   System.exit(0);
	   }else {
		   e.printStackTrace();
	   }
   }
   String Sentencee = "";
   String response = ""; 
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
   String dataaa = "";
   String dc = "";
   String keyu = "";
   String kk = "";
   String dc0 = "";
   String usermail = "";
   String clientSentence = null;
   String ppaass = "";
   String neww = "";
   String to = "";
   String message = "";
   String wew = "";
   String in = "";
   String content = "";
   String deleted = "";
   String username = "";
   String new000 = "";
   BufferedWriter writeer = null;
   BufferedWriter ewe = null;
   BufferedReader urd = null;
   BufferedWriter writerr = null;
   Date date = null;
   SimpleDateFormat formatter = null;
   PrintWriter writer = null;
   int limit = 0;
   URL uurl = null;
   HttpURLConnection uconn = null;
   String[] p = data.split(":");
   String[] pp = null;
   String[] admindb = admin.split(":");
   DataOutputStream outToClient = null;
   DataInputStream dis = null;
   HashMap <String,String> adminhm = new HashMap <String,String>();
   HashMap <String,String> hm = new HashMap <String,String>();
   File fileee = new File("user.txt");
   File filee = null;
   boolean adminlog = false;
   for (int i = 1,o = 2; i < admindb.length; i += 2) {
	    adminhm.put(admindb[i - 1], admindb[o - 1]);
	    o += 2;
	   }
   for (int i = 1,o = 2; i < p.length; i += 2) {
    hm.put(p[i - 1], p[o - 1]);
    o += 2;
   }
   System.out.println(ANSI_GREEN+"[Success] All data read. Starting Server.."+ANSI_RESET);
   serverSocket = new ServerSocket(2468,100);
   System.out.println(ANSI_GREEN+"[Success] Started"+ANSI_RESET);
while (true) {
   s = serverSocket.accept();
   dis = new DataInputStream(s.getInputStream());
   outToClient = new DataOutputStream(s.getOutputStream());
   while (true) {
	   try {
	   clientSentence = dis.readUTF();
	   }catch (Exception f) {
		   s = serverSocket.accept();
		   dis = new DataInputStream(s.getInputStream());
		   outToClient = new DataOutputStream(s.getOutputStream());
		   clientSentence = null;
		   clientSentence = dis.readUTF();
	   }
	   System.out.println(ANSI_CYAN+"[Info] Recieved Message from: "+s.getInetAddress()+" : "+clientSentence+ANSI_RESET);
	   clientSentence = AES.decrypt(clientSentence,key);
       while (clientSentence.contains("SCPNULCHAR")) {
    	   if (limit==16) {
    		   limit = 0;
    		   break;
    	   }else {
    		   //DO NOTHING
    	   }
           outToClient.writeUTF(AES.encrypt("",key));
           outToClient.flush();
           clientSentence = clientSentence.replaceAll("SCPNULCHAR","");
       	clientSentence += AES.decrypt(dis.readUTF(),key);
       	limit = limit + 1;
       }
	   dc0 = AES.encrypt(help,key);
	   if (clientSentence.equals("help")) {
		   outToClient.writeUTF(dc0);
	   }
    if (clientSentence.contains("user") && !(state.equals("true"))) {
    	if (hm.containsKey(clientSentence.substring(5))) {
     keyu = clientSentence.substring(5);
     keyp = hm.get(keyu);
      response = "USER OK";
      dc = AES.encrypt(response,key);
      usermail = keyu;
      outToClient.writeUTF(dc);
      user = "usertrue";
     }else {
    	 outToClient.writeUTF(AES.encrypt("No user found",key));
     }
    	}
    if (clientSentence.contains("pass") && !(state.equals("true"))) {
    	if (user.equals("usertrue")) {
     kk = clientSentence.substring(5);
     if (kk.equals(keyp)) {
    	 usernm = keyu;
   	  if (Files.exists(Paths.get(keyu+"-mail.txt"))) {
   		response = "PASS OK !,Welcome "+keyu; 
   		dc = AES.encrypt(response,key);
        outToClient.writeUTF(dc);
	    	}else {
	    		response = "PASS OK !,Welcome "+keyu+"SUPNULCHAR";
	    		writer = new PrintWriter(usernm+"-mail.txt", "UTF-8");
	        	writer.append(null);
	        	response = "No Mailbox Owned ! Created New One !";
	        	outToClient.writeUTF(AES.encrypt(response,key));
	    	}
      dc = AES.encrypt(response,key);
      outToClient.writeUTF(dc);
      state = "true";
	  } else {
      response = "PASSWORD IS WRONG";
      dc = AES.encrypt(response,key);
      outToClient.writeUTF(dc);
	}}else{
		outToClient.writeUTF(AES.encrypt("You didn't enter the username, use user [user]",key));
	}}
    if (state.equals("true") && clientSentence.equals("mail")) {
    	if (Files.exists(Paths.get(keyu+"-mail.txt"))) {
    	dataaa = new String(Files.readAllBytes(Paths.get(keyu+"-mail.txt")));
    	dc = AES.encrypt(dataaa,key);
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
    	ppaass = clientSentence.substring(16);
    	neww = data.replace(keyp,ppaass);
        fileee.delete();
        writeer = new BufferedWriter(new FileWriter("user.txt", true));
        writeer.write(neww);
        writeer.close();
        outToClient.writeUTF(AES.encrypt("The password changed successfully !",key));
    }
    if (state.equals("true") && clientSentence.contains("send mail")) {
    	to = clientSentence.substring(13,clientSentence.indexOf("@"));
    	message = clientSentence.substring((clientSentence.indexOf("@")+1));
        ewe = new BufferedWriter(new FileWriter(to+"-mail.txt", true));
        wew = new String(Files.readAllBytes(Paths.get(to+"-mail.txt")));
        if ((wew.equals(null) || wew.equals("")) == false) {
        	ewe.newLine();
        }else {}
    	formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	date = new Date();
        ewe.append(usernm+" -> "+message+" - "+formatter.format(date));
        ewe.close();
	    dc = AES.encrypt("SENDING DONE !",key);
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
     dc = AES.encrypt(response,key);
     outToClient.writeUTF(dc);
    } else if (clientSentence.equals(adminhm.get(userm))) {
     adminlog = true;
     response = "Admin Password OK.Welcome Administrator.";
	dc = AES.encrypt(response,key);
     outToClient.writeUTF(dc);
     if (adminlog) {
         login = "true";
        }
    }
    if (clientSentence.contains("createuser") && login.equals("true")) {
     outToClient.writeUTF(AES.encrypt("DONE !",key));
     in = clientSentence.substring(11);
     writer.write(in);
     pp = in .split(":");
     hm.put(pp[0], pp[1]);
     writer.close();
    }
    if (clientSentence.contains("list users") && login.equals("true")) {
    for (String kkey:hm.keySet()) {
    Sentencee = Sentencee.concat(kkey);
}
    content = AES.encrypt(Sentencee,key);
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
    	deleted = clientSentence.substring(11);
    	username = clientSentence.substring(13,clientSentence.indexOf(":"));
    	new000 = data.replace(deleted,"");
    	filee = new File(username+"-mail.txt");
    	filee.delete();
    	fileee.delete();
    	fileee = new File("user.txt");
    	writerr = new BufferedWriter(new FileWriter("user.txt", true));
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
         content = AES.encrypt(uresult,key);
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
