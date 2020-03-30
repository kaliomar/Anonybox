import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
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
	int msgCnt = 1;
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	try {
		Map <String,String> data = new HashMap <String,String>();
		  String data_dump = "";
		  String reply = "";
		  String msg = "";
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
			   clientSentence = jsncp.read(dis, keyy);
			   }catch (Exception f) {
				   s = serverSocket.accept();
				   dis = new DataInputStream(s.getInputStream());
				   outToClient = new DataOutputStream(s.getOutputStream());
				   clientSentence = null;
				   clientSentence = jsncp.read(dis, keyy);
			   }
		   if (clientSentence.equals(userr)) {
			   if (!isuser) {
			   jsncp.write(outToClient,"User is entered !", keyy);
			   isuser = true;
			   }else {
				   jsncp.write(outToClient,"User is already entered !", keyy);
			   }
		   }else if (clientSentence.equals(passs)) {
			   if (!ispassword) {
			   if (isuser) {
			   jsncp.write(outToClient,"Password is entered, Welcome !", keyy);
			   ispassword = true;
			   }else {
				   jsncp.write(outToClient,"No user entered !", keyy);
			   }
			   }else {
				   jsncp.write(outToClient,"You're already logged in !", keyy);
			   }
		   }else if (clientSentence.contains("nick")) {
			   nick = clientSentence.substring(5);
			   jsncp.write(outToClient,"Nickname is set !", keyy);
		   }
		   else if (clientSentence.equals("show")) {
			   if (ispassword) {
			    for (Map.Entry<String, String> entry : data.entrySet()) {
			        data_dump += entry.getKey() + "->" + entry.getValue() + "\n";
			    }
				   jsncp.write(outToClient,data_dump, keyy);
			   }else {
				   jsncp.write(outToClient,"You can't show messages because you're not logged in !", keyy);
			   }
			   reply = "";
			   data_dump = "";
		   }else if (clientSentence.contains("msg")) {
			   if (ispassword) {
				   if (!(nick.equals("")||nick.equals(null))) {
			   msg = clientSentence.substring(4);
			   msg = msg.replaceAll("msg","");
			   LocalDateTime now = LocalDateTime.now(); 
			   msg = msg+" || "+dtf.format(now);
			   data.put(nick+"|Message no. "+msgCnt,msg);
			   jsncp.write(outToClient,"Message was sent !", keyy);
			   msgCnt++;
				   }else {
					   jsncp.write(outToClient,"Please add a nickname", keyy);
				   }
			   }else {
				   jsncp.write(outToClient,"You can't write a message because you're not logged in !", keyy);
			   }
			   msg = "";
		   }
		   else {
			    if (clientSentence.isEmpty()) {
			    	int xxxx = 4/0;
			    }else {
			   	jsncp.write(outToClient,"Invalid Command", keyy);
			    }
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
