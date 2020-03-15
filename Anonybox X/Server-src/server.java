import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import javax.crypto.BadPaddingException;
import java.time.*;
public class server {
	public static String enckey = "";
	public static String adminCreds = "";
	public static String userCreds = "";
	public static void main (String[] args) {
		int status = 0;
		try {
			adminCreds = new String(Files.readAllBytes(Paths.get("admin.txt")));
			status+=1;
			userCreds = new String(Files.readAllBytes(Paths.get("user.txt")));
		}catch(Exception i) {
			if (status==0) {
				System.out.println("Admin file not found");
			}else {
				System.out.println("Users file not found");
			}
			status = 0;
			i.getStackTrace();
			main(new String[]{null});
		}
		try {
			System.out.print("Enter the Encyption Key:");
			Scanner keyinput = new Scanner(System.in);
			enckey = keyinput.nextLine();
			run(enckey);
		}catch(Exception e) {
			e.getStackTrace();
		}
	}
	public static void run(String key) {
		String[] userdb = userCreds.split(",");
		String[] admindb = adminCreds.split(",");
		HashMap<String, String> usermap = new HashMap<String, String>();
		HashMap<String, String> adminmap = new HashMap<String, String>();
		for (int x = 0;x<userdb.length;x++) {
			String usercred = userdb[x];
			String[] pair = usercred.split(":");
			System.out.println(pair[0]+" "+pair[1]);
			usermap.put(pair[0],pair[1]);
		}
		for (int xx = 0;xx<admindb.length;xx++) {
			String admincred = admindb[xx];
			String[] pairr = admincred.split(":");
			System.out.println(pairr[0]+" "+pairr[1]);
			adminmap.put(pairr[0],pairr[1]);
		}
		try {
			ServerSocket socket = new ServerSocket(2468);
			Socket s = null;
			while (true) {
				try {
				s = socket.accept();
				DataInputStream i = new DataInputStream(s.getInputStream());
				DataOutputStream o = new DataOutputStream(s.getOutputStream());
				String incoming = "";
				String reply = "";
				File usermail = null;
				String data = "";
				boolean isuser = false;
				boolean logged = false;
				boolean isadmin = false;
				boolean yes = false;
				boolean adminstat = false;
				String currentuser = "";
				int packetlimit = 0;
				try {
					incoming = i.readUTF();
				}catch (Exception z) {
					   s = socket.accept();
					   i = new DataInputStream(s.getInputStream());
					   o = new DataOutputStream(s.getOutputStream());
					   incoming = null;
					   incoming = i.readUTF();
					   z.getStackTrace();
				}
				System.out.println("User Sent: "+incoming);
				incoming = AES.decrypt(incoming,key);
				System.out.println("User Sent (Decrypted): "+incoming);
				if (incoming.equals("ENCERR")) {
					System.out.println("Entered if");
					reply = "IEK";
					o.writeUTF(reply);
					System.out.println("We sent: "+reply);
				}else {
					yes = true;
					o.writeUTF("OKOKOK");					
					}
					System.out.println("iscorrect");
				while (true) {
					try {
						incoming = i.readUTF();
					}catch (Exception z) {
						   s = socket.accept();
						   i = new DataInputStream(s.getInputStream());
						   o = new DataOutputStream(s.getOutputStream());
						   incoming = null;
						   incoming = i.readUTF();
						   z.getStackTrace();
					}
					System.out.println("User Sent: "+incoming);
					incoming = AES.decrypt(incoming,key);
					System.out.println("User Sent (Decrypted): "+incoming);
				    if (incoming.contains("ENCERR")) {
						o.writeUTF("IEK");
						System.out.println("We sent: "+reply);
					}else {
						yes = true;
					}
				    if (yes) {
					if (usermap.containsKey(incoming)&&yes) {
						reply = AES.encrypt("User is entered",key);
						currentuser = incoming;
						isuser = true;
						System.out.println("We sent: "+reply);
						o.writeUTF(reply);
					}else if (incoming.equals(usermap.get(currentuser))&&isuser&&yes) {
						reply = AES.encrypt("Welcome "+currentuser+" !",key);
						File tmp0 = new File(currentuser+"-mail.txt");
                                                boolean exists = tmp0.exists();
                                                if (exists) {System.out.println("exists");}else{tmp0.createNewFile();}
						logged = true;
						System.out.println("We sent: "+reply);
						o.writeUTF(reply);
					}else if (adminmap.containsKey(incoming)&&yes) {
						reply = AES.encrypt("Admin is entered",key);
						System.out.println("We sent: "+reply);
						o.writeUTF(reply);
					}else if (incoming.equals(adminmap.get(currentuser))&&isadmin&&yes) {
						reply = AES.encrypt("Welcome Admin",key);
						System.out.println("We sent: "+reply);
						adminstat = true;
						o.writeUTF(reply);
					}
					else if (logged&&yes) {
						if (incoming.contains("password")) {
							// password oldpassword:newpassword
							String oldpass = incoming.substring(9,incoming.indexOf(":"));
							String newpass = incoming.substring(incoming.indexOf(":")+1);
							if (oldpass.equals(usermap.get(currentuser))) {
							usermap.put(currentuser,newpass);
							FileWriter userwrite = new FileWriter("user.txt");
							userwrite.write(userCreds.replaceAll(currentuser+":"+oldpass,currentuser+":"+newpass));
							userwrite.close();
							reply = AES.encrypt("Password is changed !", key);
							o.writeUTF(reply);
							}else {
							reply = AES.encrypt("Old password is wrong !", key);
							o.writeUTF(reply);
							}
						}
						else if (incoming.equals("show")) {
							data = new String(Files.readAllBytes(Paths.get(currentuser+"-mail.txt")));
							reply = AES.encrypt(data,key);
							System.out.println("We sent: "+reply);
							o.writeUTF(reply);
						}else if (incoming.substring(0,4).equals("send")) {
							// send toUser:Msg
							String dest = incoming.substring(5,incoming.indexOf(":"));
							String msg = incoming.substring(incoming.indexOf(":")+1);
							data = new String(Files.readAllBytes(Paths.get(currentuser+"-mail.txt")));
							FileWriter destmail = new FileWriter(dest+"-mail.txt");
							LocalDateTime time = LocalDateTime.now();
							destmail.write(data+currentuser+" | "+msg+" | at:"+time+"\n");
							destmail.close();
							reply = AES.encrypt("Sent !",key);
							System.out.println("We sent: "+reply);
							o.writeUTF(reply);
						}else {
							o.writeUTF(AES.encrypt("invalid command",key));
						}
					}else {
						o.writeUTF(AES.encrypt("invalid command",key));
						System.out.println("We sent: Inn");
					}
				    }
				}
				}catch (Exception e0) {
					try {
						socket.close();
						s.close();
						e0.getStackTrace();
						run(enckey);
					}catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		}catch(Exception e1) {
			run(enckey);
		}
	}
}
