import java.io.*;
import java.security.*;
import java.util.*;
import javax.crypto.spec.*;
import javax.crypto.Cipher;
public class anonybox {
	public static void write(DataOutputStream s, String text, String key,byte[] iv) {
		write(s,text,key,0,iv);
	}
	public static String read(DataInputStream s, String key,byte[] iv) {
		return read(s,key,0,iv);
	}
	public static void write(DataOutputStream s,String text,String key,int debug,byte[] iv) {
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
		enc = AES.encrypt(text,key,iv);
		System.out.println(enc);
		if (enc.length()>63980) {
			while (i<(Math.round((double)enc.length()/63980))) {
           	    cut = enc.substring(0,63980);
           	    enc = enc.replace(cut,"");
				i++;
				try {
				s.writeUTF(AES.encrypt(AES.decrypt(cut,key,iv)+"CONTIN",key,iv));
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
	public static String read(DataInputStream s,String key,int debug,byte[] iv) {
		if (key.isEmpty()) {
			if (debug>0) System.out.println("Key var is empty");
		}
		String output = "";
		try {
		output = AES.decrypt(s.readUTF(),key,iv);
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
				output+=AES.decrypt(s.readUTF(),key,iv);
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
		if (debug>0) System.out.println(output);
		output = output.replaceAll("[-.\\+*?\\[^\\]$(){}=!<>|:\\\\]", "");
		return output;
		}
public static class AES {
	    private static SecretKeySpec secretKey;
	    private static byte[] key;
		public static byte[] generateIV() {
	        int ivSize = 16;
	        byte[] iv = new byte[ivSize];
	        SecureRandom random = new SecureRandom();
	        random.nextBytes(iv);
			return iv;
		}
	    public static void setKey(String myKey) {
	        MessageDigest sha = null;
	        try {
	            key = myKey.getBytes("UTF-8");
	            sha = MessageDigest.getInstance("SHA-1");
	            key = sha.digest(key);
	            key = Arrays.copyOf(key, 16); 
	            secretKey = new SecretKeySpec(key, "AES");
	        } 
	        catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        } 
	        catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        }
	    }
	    public static String encrypt(String strToEncrypt, String secret,byte[] iv) {
	        try {
	            IvParameterSpec ivspec = new IvParameterSpec(iv);
	            setKey(secret);
	            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
	            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
	        } 
	        catch (Exception e) {
	            System.out.println("Error while encrypting: " + e.toString());
	        }
	        return null;
	    }
	    public static String decrypt(String strToDecrypt, String secret,byte[] iv) {
	        try {
	            IvParameterSpec ivspec = new IvParameterSpec(iv);
	            setKey(secret);
	            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
	            return new String(cipher.doFinal(Base64.getDecoder().decode (strToDecrypt)));
	        } 
	        catch (Exception e) {
	            return "ENCERR"; // used to Detect Failed Handshake
	        }

	    }
	}
}
