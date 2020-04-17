import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.security.*;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.*;
public class main {
    private static SecretKeySpec secretKey;
    private static byte[] key;
    static class t extends Thread{
    	private Socket s;
    	private String enckey,u,p;
    	t(Socket s,String k,String u,String p) {
    		this.s = s;
    		this.enckey = k;
    		this.u = u;
    		this.p = p;
    	}
    	@Override
    	public void run() {
    		try {
			DataInputStream is = new DataInputStream(s.getInputStream());
			DataOutputStream o = new DataOutputStream(s.getOutputStream());
			String i = "";
			boolean uie = false;
			boolean logged = false;
		    while(true) {
				i = is.readUTF();
				i = decrypt(i, enckey);
				if (i.equals(u)&&!logged) {
					uie = true;
					write(o,"User is entered",enckey);
				}else if(i.contentEquals(p)&&uie&&!logged) {
					logged = true;
					write(o,"Password is entered,Welcome !",enckey);
				}else if (logged) {
					if (i.contains("visit")) {
						String site = i.substring(6);
						String content = readsite(site).replaceAll("href=\"/","href=\""+site+"/").replaceAll("src=\"/","src=\""+site+"/");
						write(o,content,enckey);
					}else {
						write(o,"Wrong Command",enckey);
					}
				}
				else {
					write(o,"Wrong Command",enckey);
				}
		    }
    		}catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
    }
    public static void main(String[] args) {
    	String key = getSaltString();
    	String user = getSaltString();
    	String pass = getSaltString();
    	System.out.println(key+" "+user+" "+pass);
    	try {
    	ServerSocket s = new ServerSocket(1111);
    	while (true) {
    		Socket c = s.accept();
    		Thread t = new main.t(c,key,user,pass);
    		t.start();
    	}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    public static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890qwertyuiopasdfghjklzxcvbnm";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 20) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
    public static void setKey(String myKey) 
    {
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
    public static String encrypt(String strToEncrypt, String secret) 
    {
        try
        {
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
    public static String decrypt(String strToDecrypt, String secret) 
    {
        try
        {
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            return new String(cipher.doFinal(Base64.getDecoder().decode (strToDecrypt)));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
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
		enc = encrypt(text,key);
		System.out.println(enc);
		if (enc.length()>63980) {
			while (i<(Math.round((double)enc.length()/63980))) {
           	    cut = enc.substring(0,63980);
           	    enc = enc.replace(cut,"");
				i++;
				try {
				s.writeUTF(encrypt(decrypt(cut,key)+"CONTIN",key));
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
		output = decrypt(s.readUTF(),key);
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
				output+=decrypt(s.readUTF(),key);
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
	public static String readsite(String requestURL) throws IOException
	{
	    try (Scanner scanner = new Scanner(new URL(requestURL).openStream(),
	            StandardCharsets.UTF_8.toString()))
	    {
	        scanner.useDelimiter("\\A");
	        return scanner.hasNext() ? scanner.next() : "";
	    }
	}	
}
