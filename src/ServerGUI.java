import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.Action;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.DropMode;
import javax.swing.JTextArea;

public class ServerGUI {
	static int                     port = 1000; // Default Port Number
	static int                     MAX_CONS = 100; // Max Connections
	static int                     c = 0; // Current Connections
	public static final String     RE = ""; // Reset Terminal Color
	public static final String     R = ""; // Red Terminal Color
	static HashMap<String, String> UD = new HashMap<String,String>(); // User Credentials K: Username V: Password
	static HashMap<String, String> N = new HashMap<String, String>(); // Send External Mail
	static ArrayList<String>       BIP = new ArrayList<String>(); // Banned IP Addresses
	static ArrayList<String>       CIP = new ArrayList<String>(); // Connected IP Addresses
	static String                  SKv = ""; // Server Key
	static String                  UserFilePath = "User.txt"; // User credentials path
	static String                  DataDir = "data/"; // Data folder
	static String                  SKf = "Server.key"; // Server Key filename
	static boolean                 v = false; // Verbose Mode
	private JFrame                 frmAnonyboxServer;
	private JTextField             textField;
	private JLabel                 State = new JLabel("State: Stopped");
	private JTextArea              Log = new JTextArea();
	private JTextArea              Terminal = new JTextArea();
	public class CustomOutputStream extends OutputStream {
	    private JTextArea textArea;    
	    public CustomOutputStream(JTextArea textArea) {
	        this.textArea = textArea;
	    }
	     
	    @Override
	    public void write(int b) throws IOException {
	        textArea.append(String.valueOf((char)b));
	        textArea.setCaretPosition(textArea.getDocument().getLength());
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
	public static boolean ALF(ArrayList<String> bIP2, Object toFind) {
		boolean state = false;
		for(int ii = 0;ii<BIP.size();ii++) {
			if(bIP2.get(ii).equals(toFind)) {
				state = true;
				break;
			}
		}
		return state;
	}
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		if(args.length%2>0 &&args[0].equals("-h")) {
			System.out.println(R+"Help Screen\n"
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
				s.close();
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							ServerGUI window = new ServerGUI();
							window.frmAnonyboxServer.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}else {
				System.out.println(R+"Incorrect Server Key"+RE);
			}
			}
	}
	public ServerGUI() {
		initialize();
	}
	public  class T extends Thread {
		public void run() {
 			try {
 			PrintStream printStream = new PrintStream(new CustomOutputStream(Log));
 			System.setOut(printStream);
 			ServerSocket ss = new ServerSocket(port);
 			System.out.println("Server has started");
 			while(true) {
 				if(c<MAX_CONS) {
 				Socket p = ss.accept();
 				if(v) System.out.println(R+p.getInetAddress().getHostAddress().replace("/","")+" is connected");
 				if(!ALF(BIP,p.getInetAddress().getHostAddress().replace("/",""))) {
 					if(!ALF(CIP,p.getInetAddress().getHostAddress().replace("/",""))) {
 						t thread = new t(p);
 						thread.start();
 						c++;
 						CIP.add(p.getInetAddress().getHostAddress().replace("/",""));
 					}else {
 						new DataOutputStream(p.getOutputStream()).writeUTF("Already Connected");
 					}
 				}else {
 					new DataOutputStream(p.getOutputStream()).writeUTF("Banned");
 				}
 				}else {
 					// Do nothing
 				}
 			}
 			}catch(Exception ee) {
 				ee.printStackTrace();
 			}
		}
	}
	private void initialize() {
		frmAnonyboxServer = new JFrame();
		frmAnonyboxServer.setTitle("Anonybox Server");
		frmAnonyboxServer.setBounds(100, 100, 550, 300);
		frmAnonyboxServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAnonyboxServer.getContentPane().setLayout(null);
		textField = new JTextField();
		textField.setBackground(Color.DARK_GRAY);
		textField.setForeground(Color.GREEN);
		textField.setBounds(12, 232, 159, 19);
		textField.addKeyListener(new KeyAdapter() {
	        @Override
	        public void keyPressed(KeyEvent e) {
	            if(e.getKeyCode() == KeyEvent.VK_ENTER){
			        String in = textField.getText();
			        if(in.equals("show connected")) {
			        	Terminal.setText(CIP.toString());
			        }
			        else if(in.equals("help")) {
			        	Terminal.setText("Anonybox Terminal\n"
								+ "show connected | Show Connected Users\n"
								+ "show banned | Show Banned Users\n"
								+ "show ram | Show RAM Usage\n"
								+ "show users | show users and creds\n");	
			        }
			        else if(in.equals("show banned")) {
			        	Terminal.setText(BIP.toString());
			        }
			        else if(in.equals("show ram")) {
			        	Terminal.setText((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000000+" MB");
			        }
			        else if(in.equals("show users")) {
			        	Terminal.setText(UD.toString());
			        }
			        textField.setText("");
			        textField.grabFocus();
			        textField.requestFocus();
	            }
	        }

	    });		
		frmAnonyboxServer.getContentPane().add(textField);
		textField.setColumns(10);
		JButton B = new JButton("Start Server");
		ActionListener actionListener = new ActionListener() {
	         public void actionPerformed(ActionEvent event) {
	        	 State.setText("State: Running");
	        	 T o = new T();
	        	 o.start();
	         }
	      };
		B.addActionListener(actionListener);
		B.setBounds(235, 0, 159, 25);
		frmAnonyboxServer.getContentPane().add(B);
		State.setBounds(12, 5, 205, 15);
		frmAnonyboxServer.getContentPane().add(State);
		Terminal.setLineWrap(true);
		Terminal.setEditable(false);
		Terminal.setText("Anonybox Terminal\n"
				+ "show connected | Show Connected Users\n"
				+ "show banned | Show Banned Users\n"
				+ "show ram | Show RAM Usage\n"
				+ "show users | show users and creds\n");
		Terminal.setBackground(Color.BLACK);
		Terminal.setForeground(Color.GREEN);
		Terminal.setBounds(12, 32, 159, 219);
		frmAnonyboxServer.getContentPane().add(Terminal);
		Log.setEditable(false);
		Log.setBackground(Color.BLACK);
		Log.setForeground(Color.GREEN);
		Log.setBounds(183, 32, 355, 219);
		frmAnonyboxServer.getContentPane().add(Log);
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
			int INPC = 0;
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
							if(INPC<3) {
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
									INPC = INPC + 1; 
								}
								if(!UD.containsKey(un)) {
									U.net.write(DOS, "IL", key); // Invalid Login
									INPC = INPC + 1;
								}
							}else {
								U.net.write(DOS,"Banned from the server", key);
								String ip=(((InetSocketAddress) tt.getRemoteSocketAddress()).getAddress()).toString().replace("/","");
								BIP.add(ip);
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
						}else {
							if(v) System.out.println(R+"Secure Connection isn't established"+RE);
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
				c--;
				CIP.remove(tt.getInetAddress().getHostAddress().replace("/",""));
			}
			}catch(Exception e) {
				if(v) e.printStackTrace();
				c--;
				CIP.remove(tt.getInetAddress().getHostAddress().replace("/",""));
			}
		}
	}

}
