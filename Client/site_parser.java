import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;
import java.net.URLEncoder;
import java.util.Base64;
public class site_parser {
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
        String username = JOptionPane.showInputDialog("Enter username:");
        if (username.equals(null)||username.equals("")) {
        	String cmd = JOptionPane.showInputDialog("No cusername entered, enter it !");
        }
        dos.writeUTF(AES.encrypt("user "+username,key));
        dos.flush();
        out = AES.decrypt(dis.readUTF(),key);
         while (out.contains("SCPNULCHAR")) {
            dos.writeUTF(AES.encrypt("",key));
            dos.flush();
            out = out.replaceAll("SCPNULCHAR","");
        	out += AES.decrypt(dis.readUTF(),key);
        }
         if (out.contains("USER OK")) {
        	 String password = JOptionPane.showInputDialog("Enter your password:");
             dos.writeUTF(AES.encrypt("pass "+password,key));
             dos.flush();
             out = AES.decrypt(dis.readUTF(),key);
              while (out.contains("SCPNULCHAR")) {
                 dos.writeUTF(AES.encrypt("",key));
                 dos.flush();
                 out = out.replaceAll("SCPNULCHAR","");
             	out += AES.decrypt(dis.readUTF(),key);
              }
              if (out.contains("PASS OK")||out.contains("NO")) {
            	  while (true) {
            	  String cmd = JOptionPane.showInputDialog("Enter the URL:");
                  String encCmd = AES.encrypt("site "+cmd,key);
                  while (cmd.equals(null)||cmd.equals("")) {
                  	cmd = JOptionPane.showInputDialog("No URL entered, enter the URL:");
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
                   Process process = Runtime.getRuntime().exec("firefox data:text/html;base64,"+Base64.getEncoder().encodeToString(out.replaceAll("src=\"","src=\""+cmd).getBytes()));
                   out = "";
                   encCmd = "";
                   cmd = "";
            	  }
              }else {
            	  JOptionPane.showMessageDialog(null,"Password is wrong, Exiting..");
            	  System.exit(1);
              }
         }else {
        	 JOptionPane.showMessageDialog(null,"User isn't found, exiting..");
        	 System.exit(1);
         }
        }
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
