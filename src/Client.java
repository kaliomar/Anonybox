import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
public class Client {
	public static void main(String[] args) {
		Client c = new Client("",0);
		while(true) {
		System.out.println("Menu: \n"
				+ "1. Login\n"
				+ "2. Register\n"
				+ "3. Change Password\n"
				+ "4. Mail\n"
				+ "5. Send External Mail\n"
				+ "6. Connect\n"
				+ "7. Exit\n");
		Scanner s = new Scanner(System.in);
		int o = Integer.parseInt(s.nextLine());
		if(o == 7) {
			System.exit(1);
		}
		else if(o == 1) {
			System.out.print("Enter the Username and the Password Ex. Tyler,123456\n");
			String[] UP = s.nextLine().split(",");
			System.out.println(R+c.Login(UP[0],UP[1])+RE);
		}
		else if(o == 2) {
			System.out.print("Enter the Username and the Password Ex. Tyler,123456\n");
			String[] UP = s.nextLine().split(",");
			System.out.println(R+c.registerUser(UP[0],UP[1])+RE);
		}
		else if(o == 3) {
			System.out.print("Enter the OldPassword and the NewPassword Ex. 123456,654321\n");
			String[] UP = s.nextLine().split(",");
			System.out.println(R+c.chgpwd(UP[0],UP[1])+RE);
		}
		else if(o == 4) {
			System.out.println(R+c.mail()+RE);
		}
		else if(o == 5) {
			System.out.print("Enter the Your Username and the Destination Username and the Destination Server IP and the Subject and the Content Ex. Tyler,Ahmed,156.56.56.56,Hi,Test\n");
			String[] UP = s.nextLine().split(",");
			System.out.println(R+c.sendEmail(UP[0], UP[1], UP[2], UP[3], UP[4])+RE);
		}
		else if(o == 6) {
			System.out.print("Enter the IP Address Ex. 192.168.1.6\n");
			String[] UP = s.nextLine().split(",");
			c.setIP(UP[0]);
			c.setPort(1000);
			c.init();
		}
		}
	}
	public Socket s;
	public static final String RE = "\u001B[0m";
	public static final String R = "\u001B[31m";
	public DataInputStream DIS;
	public String secret = "";
	public DataOutputStream DOS;
	public String ip = "";
	public int port = 0;
	public Client(String ip,int port) {
		this.ip = ip;
		this.port = port;
	}
	public String getIP() {
		return ip;
	}
	public void flush() {
		try {
		DOS.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public int getPort() {
		return port;
	}
	public void setIP(String ip) {
		this.ip = ip;
	}
	public void setPort(int p) {
		this.port = p;
	}
	public 	String registerUser(String username,String password) {
		String succ = "";
		try {
			U.net.write(DOS, "register", secret);
			if(U.net.read(DIS, secret).equals("RMA")) {
				U.net.write(DOS, "user "+username, secret);
				if(U.net.read(DIS, secret).equals("OK")) {
					U.net.write(DOS, "pass "+password, secret);
					String res = U.net.read(DIS, secret);
					if(res.equals("OK")) {
						succ = "Done Registeration";
					}else if(res.equals("UAT")) {
						succ = "Username Already Taken";
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return succ;
	}
	public String sendEmail(String SrcUser,String destUser,String destIP, String Subj, String Cont) {
		String succ = "";
		try {
			U.net.write(DOS, "notify "+destUser+","+destIP+","+Subj, secret);
			if(U.net.read(DIS, secret).equals("OK")) {
				System.out.println("P");
				Socket s = new Socket(destIP,1000);
				System.out.println("P");
				DataInputStream DiS = new DataInputStream(s.getInputStream());
				DataOutputStream DoS = new DataOutputStream(s.getOutputStream());
				System.out.println("P");
				String key = DH(DiS,DoS);
				if(U.AES.decrypt(DiS.readUTF(), key, U.iv).equals("test")) {
					DoS.writeUTF(U.AES.encrypt("ok", key, U.iv));
				}
				U.net.write(DoS, "send "+SrcUser+","+destUser+","+destIP+","+Subj+","+Cont, key);
				String rep = U.net.read(DiS, key);
				if(rep.equals("OK")) {
					succ = "Sending is done";
				}else if(rep.equals("FAIL") || rep.equals("ERR")) {
					succ = "Failed Sending";
				}
				s.close();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return succ;
	}
	public String mail() {
		String f = "";
		String succ = "";
		try {
		U.net.write(DOS,"mail" , secret);
		succ = U.net.read(DIS, secret);
		String[] p = succ.split(",");
		for(int i = 0; i < p.length; i++) {
			String[] args = p[i].split(":"); // i0: From | i1: Subject | i2: Content
			f += "Sender: "+args[0]+"\nSubject: "+args[1]+"\nContent: "+args[2]+"\n";
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return f;
	}
	public String chgpwd(String op, String np) {
		String succ = "";
		try {
			U.net.write(DOS, "chg pwd "+op+","+np, secret);
			String res = U.net.read(DIS, secret);
			if(res.equals("OK")) {
				succ = "Changing Password is done";
			}else if(res.equals("MLFR")) {
				succ = "Malformed Request";
			}else if(res.equals("INC")) {
				succ = "Old password is Incorrect";
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return succ;
	}
	public String Login(String username, String password) {
		String succ = "";
		try {
			U.net.write(DOS, "login", secret);
			if(U.net.read(DIS, secret).equals("LMA")) {
				U.net.write(DOS, "user "+username, secret);
				if(U.net.read(DIS, secret).equals("OK")) {
					U.net.write(DOS, "pass "+password, secret);
					String res = U.net.read(DIS, secret);
					if(res.equals("OK")) {
						succ = "Logged in";
					}else if(res.equals("IL")) {
						succ = "Invalid Login";
					}else if(res.equals("IP")) {
						succ = "Incorrect Password";
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return succ;
	}
	public void init() {
		try {
			s = new Socket(ip,port);
			DIS = new DataInputStream(s.getInputStream());
			DOS = new DataOutputStream(s.getOutputStream());
			secret = DH(DIS,DOS);
			if(U.AES.decrypt(DIS.readUTF(), secret, U.iv).equals("test")) {
				DOS.writeUTF(U.AES.encrypt("ok", secret, U.iv));
				System.out.println(R+"Secure Connection is established"+RE);
			}else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String DH(DataInputStream DIS, DataOutputStream DOS) {
		String out = "";
		try {
			for(int i = 0;i<8;i++) {
				long P = Long.parseLong(DIS.readUTF().replace("P.", "")); DOS.writeUTF("OK");
				long G = Long.parseLong(DIS.readUTF().replace("G.", "")); DOS.writeUTF("OK");
				double A = Double.parseDouble(DIS.readUTF());
				long b = (long) (ThreadLocalRandom.current().nextInt(1,4));
				long B = (long) (Math.pow(G, b)%P);
				DOS.writeUTF(String.valueOf(B));
				out += String.valueOf(Math.pow(A, b)%P);
			}
		}catch(Exception e) {
			System.out.println("Error while exchanging keys");
			e.printStackTrace();
		}
		return out;
	}
}
