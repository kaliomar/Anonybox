import java.io.*;
import java.net.*;
public class main {
	public static void main(String[] args) {
		try {
			int port = 10; // Port number
            String enckey = ""; // Encryption Key
            ServerSocket s = new ServerSocket(port); // Start the Server
            Accept a = new Accept(s,enckey); // Acceptor Class
            a.start(); // Start the Acceptor
		}catch(Exception e) {
			e.getStackTrace();
		}
	}
	static class ServerThread extends Thread {
		public Socket s;
		public String enckey;
        ServerThread(Socket s, String enckey) {
            //Constructor
            this.s = s;
            this.enckey = enckey;
        }
        @Override
        public void run() {
        	// Anonybox init
        }
	}
    static class Accept extends Thread {
        public ServerSocket s;
        public String enckey;
        Accept(ServerSocket s,String enckey) {
        	// Constructor
            this.s = s;
            this.enckey = enckey;
        }
        @Override
        public void run() {
            try {
                while (true) {
                    Socket c = s.accept(); //Accept User Connection
                    System.out.println(c.getInetAddress() + " is connected" + "\n"); // Log Client IP
                    ServerThread st = new ServerThread(c,enckey); // ServerThread Class
                    st.start(); // Start the ServerThread
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }
}
