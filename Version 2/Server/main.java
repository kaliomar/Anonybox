import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;

public class main {
	 public static final String ANSI_RESET = "\u001B[0m";
	 public static final String ANSI_RED = "\u001B[31m";
	 public static final String ANSI_GREEN = "\u001B[32m";
	 public static final String ANSI_CYAN = "\u001B[36m";
	 public static String key = "";
	 public static String user = "";
	 public static String pass = "";
	 public static void main (String[] args) {
		 key = getSaltString();
		 user = getSaltString();
		 pass = getSaltString();
		 System.out.println(ANSI_CYAN+"Encryption key: "+ANSI_RESET+ANSI_GREEN+key+ANSI_RESET+ANSI_CYAN+"\nUsername: "+ANSI_RESET+ANSI_GREEN+user+ANSI_RESET+ANSI_CYAN+"\nPassword: "+ANSI_RESET+ANSI_GREEN+pass+ANSI_RESET);
		 run(key,user,pass);
	 }
	@SuppressWarnings("resource")
	public static void run(String keyy,String userr,String passs) {
	ServerSocket serverSocket = null;
	Socket s = null;
	  try {
		HashMap <String,String> data = new HashMap <String,String>();
		  String data_dump = "";
		  String clientSentence = "";
		  String nick = "";
		  serverSocket = new ServerSocket(1111);
		  while (true) {
		  s = serverSocket.accept();
		  DataInputStream dis = new DataInputStream(s.getInputStream());
		  DataOutputStream outToClient = new DataOutputStream(s.getOutputStream());
		  boolean isuser = false,ispassword = false;
		  int limit = 0;
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
		   clientSentence = AES.decrypt(clientSentence,keyy);
		   while (clientSentence.contains("SCPNULCHAR")||clientSentence.equals("")) {
			   if (limit==16) {
				   limit = 0;
				   outToClient.writeUTF(AES.encrypt("Packet limit Reached !",keyy));
				   break;
			   }else {
				   //DO NOTHING
			   }
		       outToClient.writeUTF(AES.encrypt("",keyy));
		       outToClient.flush();
		       clientSentence = clientSentence.replaceAll("SCPNULCHAR","");
		   	clientSentence += AES.decrypt(dis.readUTF(),keyy);
		   	limit = limit + 1;
		   }
		   if (clientSentence.equals(userr)) {
			   if (!isuser) {
			   outToClient.writeUTF(AES.encrypt("User is entered !",keyy));
			   isuser = true;
			   }else {
				   outToClient.writeUTF(AES.encrypt("User is already entered !", keyy));
			   }
		   }else if (clientSentence.equals(passs)) {
			   if (!ispassword) {
			   if (isuser) {
			   outToClient.writeUTF(AES.encrypt("Password is entered, Welcome !",keyy));
			   ispassword = true;
			   }else {
				   outToClient.writeUTF(AES.encrypt("No user entered !",keyy));
			   }
			   }else {
				   outToClient.writeUTF(AES.encrypt("You're already logged in !", keyy));
			   }
		   }else if (clientSentence.contains("nick")) {
			   nick = clientSentence.substring(6);
			   outToClient.writeUTF(AES.encrypt("Nickname is set !",keyy));
		   }
		   else if (clientSentence.equals("show")) {
			   if (ispassword) {
			    for (String key : data.keySet()) {
			        data_dump += key + "->" + data.get(key) + "\n";
			    }
			    String reply = AES.encrypt(data_dump, keyy);
			    if (reply.length()<=63980) {
			   outToClient.writeUTF(reply);
			    }else {
		             for (int oo = 0;oo<(reply.length()/63980)+1;oo++) {
		            	 String ooo = reply.substring(0,63980);
		            	 reply = reply.replace(ooo,"");
		            	 outToClient.writeUTF(AES.encrypt(AES.decrypt(reply,keyy)+"SCPNULCHAR",keyy));
		             }
			    }
			   }else {
				   outToClient.writeUTF(AES.encrypt("You can't show messages because you're not logged in !",keyy));
			   }
		   }else if (clientSentence.contains("msg")) {
			   if (ispassword) {
			   String msg = clientSentence.substring(5);
			   msg.replaceAll("msg","");
			   data.put(nick,msg);
			   outToClient.writeUTF(AES.encrypt("Message was sent !",keyy));
			   }else {
				   outToClient.writeUTF(AES.encrypt("You can't write a message because you're not logged in !",keyy));
			   }
		   }
		   else {
			   outToClient.writeUTF(AES.encrypt("SCPNULCHAR",keyy));
		   }
		   }
		   }
	} catch (Exception e) {
		try {
		serverSocket.close();
		s.close();
		run(key,user,pass);
		}catch (Exception ee) {
			ee.printStackTrace();
		}
	}
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
}
