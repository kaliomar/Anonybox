import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Optional;
public class jsncp {
	public static void write(DataOutputStream s, String text, String key) {
		write(s,text,key,0);
	}
	public static String read(DataInputStream s, String key) {
		return read(s,key,0);
	}
	public static void write(DataOutputStream s,String text,String key,int debug) {
		if (text.isEmpty()) {
			System.out.println("Text var is empty");
		}
		if (key.isEmpty()) {
			System.out.println("Key var is empty");
		}
		System.out.println(text+" - "+key);
		String enc = "";
		int i = 0;
		String cut = "";
		enc = AES.encrypt(text,key);
		System.out.println(enc);
		if (enc.length()>63980) {
			while (i<(Math.round((double)enc.length()/63980))) {
           	    cut = enc.substring(0,63980);
           	    enc = enc.replace(cut,"");
				i++;
				try {
				s.writeUTF(AES.encrypt(AES.decrypt(cut,key)+"CONTIN",key));
				}catch(Exception e) {
					if (debug>0) {
					System.out.println("Error Writing at line 18");
					e.printStackTrace();
					}else {
						//DO NOTHING
					}
					//TODO: Handle writeUTF
				}
			}
		}else {
			try {
			s.writeUTF(enc);
			}catch(Exception e) {
				if (debug>0) {
				System.out.println("Error Writing at line 26");
				e.printStackTrace();
				}else {
					//DO NOTHING
				}
				//TODO: Handle writeUTF
			}
		}
		try {
			s.flush();
		} catch (IOException e) {
			if (debug>0) {
			System.out.println("Error Flushing at line 34");
			e.printStackTrace();
			}else {
				//DO NOTHING
			}
			//TODO: Handle s.flush();
		}
	}
	public static String read(DataInputStream s,String key,int debug) {
		if (key.isEmpty()) {
			System.out.println("Key var is empty");
		}
		String output = "";
		try {
		output = AES.decrypt(s.readUTF(),key);
		}catch(Exception e) {
			if (debug>0) {
			System.out.println("Error Reading at line 36");
			e.printStackTrace();
			}else {
				//DO NOTHING
			}
			//TODO: Handle readUTF
		}
		if (output.contains("CONTIN")) {
			while (output.contains("CONTIN")) {
				output = output.replaceAll("CONTIN","");
				try {
				output+=AES.decrypt(s.readUTF(),key);
				}catch(Exception e) {
					if (debug>0) {
					System.out.println("Error Writing at line 45");
					e.printStackTrace();
					}else {
						//DO NOTHING
					}
					//TODO: Handle readUTF
				}
				}
		}else {
			//DO NOTHING
		}
		System.out.println(output);
		return output;
		}
}
