import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
public class U {
	public static final String RE = "\u001B[0m"; // Reset Terminal Color
	public static final String R = "\u001B[31m"; // Red Terminal Color
	public static byte[] iv = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	public static class IO {
		public static  byte[] read(String filename) {
			byte[] out = null;
			try {
				filename = "../"+filename;
				File f = new File(filename);
				FileInputStream m = new FileInputStream(f);
				try {
					out = m.readAllBytes();
				} catch (Exception ee) {
					out = "404FILENOTFOUND".getBytes();
				}
				m.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
			return out;
		}
		public static  void write (String filename,String content,StandardOpenOption t) {
			try {
				Files.write(Paths.get("../"+filename), content.getBytes(), t);
				//System.out.println(R+"Wrote to "+Paths.get("../"+filename)+RE);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static class util {
		public static int getPRM(long p) {
			return GFG.findPrimitive(p);
		}
	}
	public static class crypto {
			public static byte[] ivgenerate() {
				byte[] iv = null;
				try {
					SecureRandom randomSecureRandom = SecureRandom.getInstance("SHA1PRNG");
					Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				    iv = new byte[cipher.getBlockSize()];
					randomSecureRandom.nextBytes(iv);
				}catch(Exception e) {
					e.printStackTrace();
				}
				return iv;
			}
	}
	public static class net {
		public static void write(DataOutputStream s,String text,String key) {
			try {
				double buf = 2D;
				if(text.length() > 2) {
					int iter = (int) Math.ceil((text.length()/buf));
					int LA = 0;
					for(int i = 1;i<iter+1;i++) {
						if(i!=iter) {
							String encF = AES.encrypt(text.substring(LA,LA+2)+"CONTIN", key, U.iv);
							s.writeUTF(encF);
						}else {
							String encF = AES.encrypt(text.substring(LA,text.length()), key, U.iv);
							s.writeUTF(encF);
						}
						LA = LA+2;
					}
				}else {
					s.writeUTF(AES.encrypt(text, key, iv));
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		public static String replaceLast(String texto, String substituir, String substituto) {
		    int pos = texto.lastIndexOf(substituir); 
		    if (pos > -1) { 
		       return texto.substring(0, pos) + substituto + texto.substring(pos + substituir.length(), texto.length()); 
		    } else 
		       return texto;
		}
		public static String read(DataInputStream s,String key) throws Exception {
			Thread.sleep(100);
			String out = "";
			int trig = 1;
			while(trig == 1) {
				String I = AES.decrypt(s.readUTF(), key, iv);
				if(I.contains("CONTIN")) {
						out = out + replaceLast(I,"CONTIN","");
						trig = 1;
				}else {
					out = out + I;
					trig = 0;
				}
			}
				//System.out.println("RCVD: "+out);
			return out;
		}
		public static String dh(DataInputStream DIS,DataOutputStream DOS) {
			String secret = "";
			try {
				for(int i = 0;i<8;i++) {
					long P = p((int) (ThreadLocalRandom.current().nextInt(2,5)));
					//System.out.println(R+"P value is "+P+RE);
					DOS.writeUTF("P."+P);
					if(DIS.readUTF().contains("OK")) {
						long G = GFG.findPrimitive(P);
						//System.out.println(R+"G value is "+G+RE);
						DOS.writeUTF("G."+G);
						if(DIS.readUTF().contains("OK")) {
							long a =(long) (ThreadLocalRandom.current().nextInt(1,4));
							//System.out.println(R+"Secret key is "+a+RE);
							double A = Math.pow(G, a)%P;
							//System.out.println(R+"A value is "+A+RE);
							DOS.writeUTF(String.valueOf(A));
							long B = Long.parseLong(DIS.readUTF());
							//System.out.println(R+"B value is "+B+RE);
							secret += String.valueOf(Math.pow(B,a)%P);
							//System.out.println(R+"Shared key is "+secret+RE);
						}
				}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			return secret;
		}
		public static long p(int n) {
			  BigInteger ret;
			  BigInteger maxLong = BigInteger.valueOf(Long.MAX_VALUE);
			  int numBits = BigInteger.valueOf(n).bitLength() + 1;
			  do {
			    ret = BigInteger.probablePrime(numBits, new Random());
			  } while (ret.compareTo(maxLong) > 1);
			  return ret.longValue();
			}
	}
	public static class GFG {
		static boolean isPrime(long p) { 
			if (p <= 1) { 
				return false; 
			} 
			if (p <= 3) { 
				return true; 
			}
			if (p % 2 == 0 || p % 3 == 0) { 
				return false; 
			} 

			for (int i = 5; i * i <= p; i = i + 6) { 
				if (p % i == 0 || p % (i + 2) == 0) { 
					return false; 
				} 
			}
			return true; 
		}
		static long power(long x, long l, long p) { 
			long res = 1;
			x = x % p;
			while (l > 0) { 
				if (l % 2 == 1) { 
					res = (res * x) % p; 
				} 
				l = l >> 1;
				x = (x * x) % p; 
			} 
			return res; 
		}  
		static void findPrimefactors(HashSet<Long> s, long phi) { 
			while (phi % 2 == 0) { 
				s.add((long) 2); 
				phi = phi / 2; 
			}
			for (int i = 3; i <= Math.sqrt(phi); i = i + 2) { 
				while (phi % i == 0) { 
					s.add((long) i); 
					phi = phi / i; 
				} 
			}
			if (phi > 2) { 
				s.add(phi); 
			} 
		}
		static int findPrimitive(long p) { 
			HashSet<Long> s = new HashSet<Long>();
			if (isPrime(p) == false) { 
				return -1; 
			} 
			long phi = p - 1; 
			findPrimefactors(s, phi); 
			for (int r = 2; r <= phi; r++) { 
				boolean flag = false; 
				for (Long a : s) { 
					if (power(r, phi / (a), p) == 1) { 
						flag = true; 
						break; 
					} 
				} 
				if (flag == false) { 
					return r; 
				} 
			}  
			return -1; 
		} 
	} 
	public static class AES {
	    private static SecretKeySpec secretKey;
	    private static byte[] key;
	    public static void setKey(String myKey) {
	        MessageDigest sha = null;
	        try {
	            key = myKey.getBytes("UTF-8");
	            sha = MessageDigest.getInstance("SHA-512");
	            key = sha.digest(key);
	            key = Arrays.copyOf(key, 32); 
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
	        try{
	            setKey(secret);
	            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	            cipher.init(Cipher.ENCRYPT_MODE, secretKey,new IvParameterSpec(iv));
	            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
	        } 
	        catch (Exception e) {
	            System.out.println("Error while encrypting: " + strToEncrypt);
	        }
	        return null;
	    }
	 
	    public static String decrypt(String strToDecrypt, String secret,byte[] iv) {
	        try {
	            setKey(secret);
	            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	            cipher.init(Cipher.DECRYPT_MODE, secretKey,new IvParameterSpec(iv));
	            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
	        } 
	        catch (Exception e) {
	            System.out.println("Error while decrypting: " + strToDecrypt+" | "+secret+"\n"+e.getMessage());
	        }
	        return "";
	    }
	}
	}
