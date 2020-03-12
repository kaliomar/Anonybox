import java.net.*;
import javax.swing.*;
import java.io.*;
public class client {
	public static void main(String[] args) {
		try {
			String ip = JOptionPane.showInputDialog("Enter the IP Address");
			Socket s = new Socket(ip,2468);
			DataInputStream i = new DataInputStream(s.getInputStream());
			DataOutputStream o = new DataOutputStream(s.getOutputStream());
			String test = "Empty",key = null;
			while (test.equals("IEK")||test.equals("Empty")) {
				key = JOptionPane.showInputDialog("Enter the Encryption Key");
				o.writeUTF(AES.encrypt("Encryption Test",key));
				o.flush();
				test = i.readUTF();
			}
			while (true) {
				String cmd = JOptionPane.showInputDialog("Enter the command");
				o.writeUTF(AES.encrypt(cmd,key));
				String decrypted = i.readUTF();
			    decrypted = AES.decrypt(decrypted,key);
				JOptionPane.showMessageDialog(null,decrypted);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
