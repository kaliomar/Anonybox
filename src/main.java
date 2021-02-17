import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
public class main {
	static int                     port = 1000; // Default Port Number
	static int                     MAX_CONS = 100; // Max Connections
	public static final String     RE = "\u001B[0m"; // Reset Terminal Color
	public static final String     R = "\u001B[31m"; // Red Terminal Color
	static HashMap<String, String> UD = new HashMap<String,String>(); // User Credentials K: Username V: Password
	static HashMap<String, String> N = new HashMap<String, String>(); // Send External Mail
	static String                  SKv = ""; // Server Key
	static String                  UserFilePath = "User.txt"; // User credentials path
	static String                  DataDir = "data/"; // Data folder
	static String                  SKf = "Server.key"; // Server Key filename
	static boolean                 v = false; // Verbose Mode
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		if(args.length%2>0 &&args[0].equals("-h")) {
			System.out.println(R+"Help Screen\n"
					+ "-p [Number]   Port number \"Default is 1000\"\n"
					+ "-U [String]   User Data File \"Default is User.txt\"\n"
					+ "-d [String]   Data Directory \"Default is data/\"\n"
					+ "-v [1/0]      Enable/Disable Verbose \"Default is disabled\""
					+ "-mxc [Number] Max. Connections that will be held at the same time \"Default is 100\""
					+ "\n"+RE);
			System.exit(1);
		}else {
		CMDArgSet(args);
		}
		File f = new File("../"+DataDir);
		if(!f.exists()) {
			f.mkdir();
		}
		File SKF = new File("../"+DataDir+SKf);
		if(!SKF.exists()){
			try {
				SKF.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(R+"Server Key file doesn't exist"+RE);
			System.out.print(R+"Enter a new Server Key: "+RE);
			String SK = s.nextLine();
			U.IO.write(DataDir+SKf,U.AES.encrypt(SK, SK, U.iv), StandardOpenOption.WRITE);
			// Verify
			if(U.AES.decrypt(new String(U.IO.read(DataDir+SKf)),SK, U.iv).equals(SK)) {
				System.out.println(R+"OK"+RE);
				SKv = SK;
			}else {
				System.out.println(R+"Error Happened with writing the Server Key"+RE);
			}
		}else {
			System.out.print(R+"Verify your Server Key: "+RE);
			String SSK = s.nextLine();
			if(U.AES.decrypt(new String(U.IO.read(DataDir+SKf)),SSK, U.iv).equals(SSK)) {
				System.out.println(R+"Verified!"+RE);
				SKv = SSK;
				if(new File("../"+DataDir+UserFilePath).exists()) {
					UD = TTH.Convert(U.AES.decrypt(new String(U.IO.read(DataDir+UserFilePath)), SKv, U.iv),",",":");
				}else {
					try {
						new File("../"+DataDir+UserFilePath).createNewFile();
					} catch (IOException e) {
						if(v) System.out.println(R+"Something is wrong with creating User file, check permissions"+RE);
						e.printStackTrace();
					}
				}
				// Start the server
				s.close();
				try {
				ServerSocket ss = new ServerSocket(port,MAX_CONS);
				while(true) {
					Socket p = ss.accept();
					if(v) System.out.println(R+p.getInetAddress().getHostAddress().replace("/","")+" is connected");
					t thread = new t(p);
					thread.start();
				}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}else {
				System.out.println(R+"Incorrect Server Key"+RE);
			}
		}
	}
	public static class t extends Thread {
		public Socket tt;
		t(Socket o) {
			tt = o;
		}
		public void run() {
			try {
				// SKv is server key
				// key is encryption key for netIO
			DataInputStream        DIS = new DataInputStream(tt.getInputStream());
			DataOutputStream       DOS = new DataOutputStream(tt.getOutputStream());
			String                 key = String.valueOf(U.net.dh(DIS, DOS));
			if(v) System.out.println(R+"Secret Shared key is "+key+RE);
			DOS.writeUTF(U.AES.encrypt("test", key, U.iv));
			if(U.AES.decrypt(DIS.readUTF(), key, U.iv).equals("ok")) {
				if(v)System.out.println(R+"Secure Connection is established"+RE);
				boolean            LM = false; // Login Mode
				boolean            LI = false; // Logged In?
				boolean            RM = false; // Register Mode
				String             un = ""; // Username
				String             p = ""; // Password
				if(v)System.out.println(R+"User Data HashMap content "+UD+RE);
				while(true) {
					DOS.flush();
					String         cmd = "";
					cmd = U.net.read(DIS, key);
					if(v) System.out.println("Recieved "+cmd);
					if(cmd.equals("login")) {
						RM = false;
						if(v) System.out.println(R+"Login Mode Activated"+RE);
						LM = true;
						U.net.write(DOS, "LMA", key); // Login Mode Activated
					}else if(cmd.equals("register")) {
						RM = true;
						if(v)System.out.println(R+"Register Mode Activated"+RE);
						U.net.write(DOS, "RMA", key); // Register Mode Activated
					}else if(cmd.startsWith("user")) {
						// User Input Req: user [username] Res: OK
						un = cmd.split(" ")[1];
						if(v)System.out.println(R+"username is "+un+RE);
						U.net.write(DOS, "OK", key);
					}else if(cmd.startsWith("pass")) {
						// Password Input | Login/Register Req: pass [password] Res: OK/UAT/IL/IP
						p = cmd.split(" ")[1];
						if(v)System.out.println(R+"Password is "+p+RE);
						if(v)System.out.println(R+"Login? "+LM+RE);
						if(v)System.out.println(R+"Register? "+RM+RE);
						if(RM) {
							if(!UD.containsKey(un)) {
							UD.put(un, p);
							if(v)System.out.println(R+"User Data HashMap content "+UD+RE);
							String cont = U.AES.decrypt(new String(U.IO.read(DataDir+UserFilePath)), SKv, U.iv);
							if(v)System.out.println(R+"User Data File Content"+cont+RE);
							if(cont.isBlank()||cont.isEmpty()||cont.equals("")||cont.equals(null)) {
								U.IO.write(DataDir+UserFilePath, U.AES.encrypt((un+":"+p), SKv, U.iv), StandardOpenOption.WRITE);
							}else {
								U.IO.write(DataDir+UserFilePath, U.AES.encrypt(cont+","+(un+":"+p), SKv, U.iv), StandardOpenOption.WRITE);
							}
							U.net.write(DOS, "OK", key);
							}else {
								U.net.write(DOS, "UAT", key); // Username Already Taken	
							}
							}
						if(LM) {
							String pwd = UD.get(un);
							if(pwd.equals(p)) {
								U.net.write(DOS, "OK", key);
								File f = new File("../"+DataDir+un+".mail");
								if(!f.exists()) {
									f.createNewFile();
									U.IO.write(DataDir+un+".mail","0",StandardOpenOption.WRITE);
								}
								LI = true;
								if(v)System.out.println(R+"Logged in"+RE);
							}else {
								U.net.write(DOS, "IP", key); // Incorrect Password
							}
							if(!UD.containsKey(un)) {
								U.net.write(DOS, "IL", key); // Invalid Login
							}
						}
						// LOGIN SECTION:END
					}else if(cmd.startsWith("verify")) { // Verify SRCU,DESTU,SUBJ
						String[] arg = cmd.replaceAll("verify ", "").split(",");
						String ip=(((InetSocketAddress) tt.getRemoteSocketAddress()).getAddress()).toString().replace("/","");
						if(N.containsKey(ip)) {
							String[] v = N.get(ip).split(":");
							if(v[2].equals(arg[0])) {
								if(v[0].equals(arg[1])) {
									if(v[1].equals(arg[2])) {
										U.net.write(DOS, "OK", key);
									}else {
										U.net.write(DOS, "ERR", key);
									}
								}else {
									U.net.write(DOS, "ERR", key);
								}
							}else {
								U.net.write(DOS, "ERR", key);
						}
					}else {
						U.net.write(DOS, "ERR", key);
					}
						}else if(cmd.startsWith("send")) {
							try {
						String[]          arg = cmd.replaceAll("send ", "").split(",");// send SRCU,DESTU,SRCIP,SUBJ,CONTENT
						String            IP = arg[2];
						String            secret = "";
						Socket            s = new Socket(IP,1000);
						DataInputStream   DiS = new DataInputStream(s.getInputStream());
						DataOutputStream  DoS = new DataOutputStream(s.getOutputStream());
						for(int i = 0;i<8;i++) {
							long P = Long.parseLong(DiS.readUTF().replace("P.", "")); DoS.writeUTF("OK");
							if(v)System.out.println(R+"P value is "+P+RE);
							long G = Long.parseLong(DiS.readUTF().replace("G.", "")); DoS.writeUTF("OK");
							if(v)System.out.println(R+"G value is "+G+RE);
							double A = Double.parseDouble(DiS.readUTF());
							if(v)System.out.println(R+"A value is "+A+RE);
							long b = (long) (ThreadLocalRandom.current().nextInt(1,4));
							if(v)System.out.println(R+"b value is "+b+RE);
							long B = (long) (Math.pow(G, b)%P);
							if(v)System.out.println(R+"B value is "+B+RE);
							DoS.writeUTF(String.valueOf(B));
							secret += String.valueOf(Math.pow(A, b)%P);
							if(v)System.out.println(R+"Shared key is "+secret+RE);
						}
						if(U.AES.decrypt(DiS.readUTF(), secret, U.iv).equals("test")) {
							DoS.writeUTF(U.AES.encrypt("ok", secret, U.iv));
							if(v) System.out.println(R+"Secure Connection is established"+RE);
						}
						U.net.write(DoS, "verify "+arg[0]+","+arg[1]+","+arg[3], secret);
						String res = U.net.read(DiS, secret);
						if(res.equals("OK")) {
							String cont = new String(U.IO.read(DataDir+arg[1]+".mail"));
							if (!cont.equals("0")) {
								cont = U.AES.decrypt(cont, SKv, U.iv);
								U.IO.write(DataDir+arg[0]+".mail", U.AES.encrypt(cont+","+arg[0]+"@"+arg[2]+":"+arg[3]+":"+arg[4], SKv, U.iv), StandardOpenOption.WRITE);
								U.net.write(DOS, "OK", key);
							}else {
								U.IO.write(DataDir+arg[0]+".mail", U.AES.encrypt(arg[0]+"@"+arg[2]+":"+arg[3]+":"+arg[4], SKv, U.iv), StandardOpenOption.WRITE);
								U.net.write(DOS, "OK", key);
							}
						}else {
							U.net.write(DOS, "FAIL", key);
							if(v)System.out.println(R+"Fail"+RE);
						}
						s.close();
							}catch(Exception e) {
								U.net.write(DOS, "ERR", key);
							}
					}
					else  {
						if(LI) {
							if(v) System.out.println("LI enter Stage for "+cmd);
							// Get Mailbox Res: From:Subj:Content, ...
							if(cmd.equals("mail")) {
								File f = new File("../"+DataDir+un+".mail");
								if(f.exists()) {
									String c = new String(U.IO.read(DataDir+un+".mail"));
									if(c.equals("0")) {
										U.net.write(DOS, "EMPTY", key); // Empty Mail
									}else {
										c = U.AES.decrypt(c,SKv, U.iv);
										U.net.write(DOS, c, key);
									}
								}else {
									f.createNewFile();
									U.IO.write(DataDir+un+".mail","0",StandardOpenOption.WRITE);
									U.net.write(DOS, "EMPTY", key);// Empty Mail
								}
							}
							// chg pwd
							else if(cmd.startsWith("chg pwd")) {
								if(v) System.out.println("CHG PWD Enter Stage");
								String[] on = cmd.replace("chg pwd ", "").split(",");// chg pwd oldpwd,newpwd
								if(on.length != 2) {
									U.net.write(DOS, "MLFR", key); // Malformed Request
								}else {
								if(v)System.out.println(R+"OldPwd:"+on[0]+" | NewPwd:"+on[1]+RE);
								if(on[0].equals(UD.get(un))) {
									UD.remove(un);
									UD.put(un, on[1]);
									String cont = U.AES.decrypt(new String(U.IO.read(DataDir+UserFilePath)), SKv, U.iv).replace(un+":"+on[0],un+":"+on[1]);
									U.IO.write(DataDir+UserFilePath, U.AES.encrypt(cont, SKv, U.iv), StandardOpenOption.WRITE);
									U.net.write(DOS, "OK", key);
								}else {
									U.net.write(DOS, "INC", key); // Old password is incorrect
								}
								}
							}
							// IMS Notify destUsername,destIP,subject
							else if(cmd.startsWith("notify")) {
								String[] arg = cmd.replaceAll("notify ", "").split(",");
								if(arg.length != 3) {
									U.net.write(DOS, "MLFR", key);
								}else {
								N.put(arg[1], arg[0]+":"+arg[2]+":"+un); // K: IP, V: DESTUSER:SUBJ:SENDER
								U.net.write(DOS, "OK", key);
								}
							}
						}
					}
					
					
				}
				
				
			}else {
				DOS.writeUTF("ERR");
				this.interrupt();
				System.gc();
			}
			}catch(Exception e) {
				if(v) e.printStackTrace();
			}
		}
	}
	static void CMDArgSet(String[] args) {
		try {
			HashMap<String,String> Args = new HashMap<String,String>();
			for(int i = 0;i<args.length;i+=2) {
				Args.put(args[i], args[i+1]);
			}
			if(Args.containsKey("-U")) {
				UserFilePath = Args.get("-U");
			}
			if(Args.containsKey("-d")) {
				DataDir = Args.get("-d");
			}
			if(Args.containsKey("-v")) {
				int vMode = Integer.parseInt(Args.get("-v"));
				if(vMode<=0) {
					v = false;
				}else {
					v = true;
				}
			}
			if(Args.containsKey("-mxc")) {
				int Mxc = Integer.parseInt(Args.get("-mxc"));
				MAX_CONS = Mxc;
			}
		}catch(Exception e) {
			System.out.println("Something wrong happened with the CMD Arguments.");
			e.printStackTrace();
		}
	}
}
