import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;
public class client {
	public static void main(String[] args) {
		try {
		String out = "";
		String ip = JOptionPane.showInputDialog("Server IP:");
		if (ip.contentEquals("")) {
			JOptionPane.showMessageDialog(null,"No IP entered, exiting...");
			System.exit(1);
		}else {
			//Nothing
		}
		String key = JOptionPane.showInputDialog("Encryption Key:");
		if (key.contentEquals("")) {
			JOptionPane.showMessageDialog(null,"No Key entered, exiting...");
			System.exit(1);
		}else {
			//Nothing
		}
        Socket s = new Socket(ip,2468);
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        while (true) {
        String cmd = JOptionPane.showInputDialog("Enter the command:");
        String encCmd = AES.encrypt(cmd,key);
        if (encCmd.equals(null)||encCmd.equals("")) {
        	cmd = JOptionPane.showInputDialog("No command entered, Enter the command:");
        }
        dos.writeUTF(encCmd);
        dos.flush();
        out = AES.decrypt(dis.readUTF(),key);
         while (out.contains("SCPNULCHAR")) {
            dos.writeUTF(AES.encrypt("",key));
            dos.flush();
            out = out.replaceAll("SCPNULCHAR","");
        	out += AES.decrypt(dis.readUTF(),key);
        }
        JOptionPane.showMessageDialog(null, out);
        }
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
