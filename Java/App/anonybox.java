package App;

import java.io.*;
import java.security.*;
import java.util.*;
import javax.crypto.spec.*;
import javax.crypto.Cipher;

public class anonybox {
	static int DATA_BUFFER_CONST = 65531; // IMMA BEAT UR ASS IF YOU SENT MORE THAN THIS
	static byte[] EMPTY_IV = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }; // Security stonks

	public static HashMap<String, String> TTHM(String text, String EDelimiter, String KVDelimiter) { // DB Alternative,
																										// Go away SQL
																										// Boomers
		HashMap<String, String> out = new HashMap<String, String>();
		String[] AText = text.split(EDelimiter);
		for (int x = 0; x < AText.length; x++) {
			String Element = AText[x];
			String[] pair = Element.split(KVDelimiter);
			out.put(pair[0], pair[1]);
		}
		return out;
	}

	public static String getSaltString() { // Put your ass on the keyboard and generate 20 chars
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

	public static void writeFile(String path, String text) throws Exception { // Write on file.. Probably
		FileWriter myWriter = new FileWriter("./DATA/" + path, false);
		myWriter.write(text);
		myWriter.close();
	}

	public static void writeData(String path, String text, String d) throws Exception { // Ù…Ù†Ø§ Ù�Ø§Ø¶ÙŠ Ø¨Ù‚Ø§
		FileWriter myWriter = null;
		if (anonybox.readFile(path).equals("")) {
			myWriter = new FileWriter("./DATA/" + path, false);
		} else {
			text = d + text;
			myWriter = new FileWriter("./DATA/" + path, true);
		}
		myWriter.write(text);
		myWriter.close();
	}

	public static String readFile(String filename) throws FileNotFoundException { // Sorry not sorry
		String out = "";
		File file = new File("./DATA/" + filename);
		Scanner sc = new Scanner(file);
		while (sc.hasNextLine()) {
			out += sc.nextLine();
		}
		if (out.isEmpty()) {
			out = "";
		}
		System.out.println(out);
		return out;
	}

	public static void write(DataOutputStream s, String text, String key, byte[] iv) { // Mr. No Debug
		write(s, text, key, 0, iv);
	}

	public static String read(DataInputStream s, String key, byte[] iv) { // Miss. No Debug
		return read(s, key, 0, iv);
	}

	public static void write(DataOutputStream s, String text, String key, int debug, byte[] iv) { // LEMME WRITE DIS
																									// DOWN
		if (text.isEmpty()) { // Something is wrong
			System.out.println("Text var is empty");
		}
		if (key.isEmpty()) { // Something is wrong
			System.out.println("Key var is empty");
		}
		String enc = "";
		int i = 0;
		String cut = "";
		firewall FirewallWriteCheck = new firewall(text);
		text = FirewallWriteCheck.run(); // 'Filtering the packet because I've trust issues' checkkk
		enc = AES.encrypt(text, key, iv);
		if (enc.length() > DATA_BUFFER_CONST) { // Now, THIS IS EPIC !
			while (i < (Math.round((double) enc.length() / DATA_BUFFER_CONST))) { // MAFS
				cut = enc.substring(0, DATA_BUFFER_CONST);
				enc = enc.replace(cut, "");
				i++;
				try {
					s.writeUTF(AES.encrypt(AES.decrypt(cut, key, iv) + "CONTIN", key, iv));
				} catch (Exception e) {
					if (debug > 0) {
						System.out.println("Error Writing at line 18");
						e.printStackTrace();
					} else {
						// DO NOTHING
					}
					// TODO: Handle writeUTF
				}
			}
		} else {
			try {
				s.writeUTF(enc);
			} catch (Exception e) {
				if (debug > 0) {
					System.out.println("Error Writing at line 26");
					e.printStackTrace();
				} else {
					// DO NOTHING
				}
				// TODO: Handle writeUTF
			}
		}
		System.out.println("[INFO] Outgoing: " + text);
		try {
			s.flush();
		} catch (IOException e) {
			if (debug > 0) {
				System.out.println("Error Flushing at line 34");
				e.printStackTrace();
			} else {
				// DO NOTHING
			}
			// TODO: Handle s.flush();
		}
	}

	public static String read(DataInputStream s, String key, int debug, byte[] iv) { // Hold on, Lemme try to read this
		if (key.isEmpty()) { // Tany ?
			if (debug > 0)
				System.out.println("Key var is empty");
		}
		String output = "";
		try {
			output = AES.decrypt(s.readUTF(), key, iv);
			firewall FirewallReadCheck = new firewall(output); // Trust Issues..
			output = FirewallReadCheck.run();
			System.out.println("[INFO] Incoming: " + output);
		} catch (Exception e) {
			if (debug > 0) {
				System.out.println("Error Reading at line 36");
				e.printStackTrace();
			} else {
				// DO NOTHING
			}
			// TODO: Handle readUTF
		}
		if (output.contains("CONTIN")) { // Just continue reading xD
			while (output.contains("CONTIN")) {
				output = output.replaceAll("CONTIN", "");
				try {
					output += AES.decrypt(s.readUTF(), key, iv);
				} catch (Exception e) {
					if (debug > 0) {
						System.out.println("Error Writing at line 45");
						e.printStackTrace();
					} else {
						// DO NOTHING
					}
					// TODO: Handle readUTF
				}
			}
		} else {
			// DO NOTHING
		}
		return output;
	}

	public static class AES { // I'm tired so I'm not gonna try comment this one..
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
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		public static String encrypt(String strToEncrypt, String secret, byte[] iv) {
			try {
				IvParameterSpec ivspec = new IvParameterSpec(iv);
				setKey(secret);
				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
				return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
			} catch (Exception e) {
				System.out.println("Error while encrypting: " + e.toString());
			}
			return null;
		}

		public static String decrypt(String strToDecrypt, String secret, byte[] iv) {
			try {
				IvParameterSpec ivspec = new IvParameterSpec(iv);
				setKey(secret);
				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
				return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
			} catch (Exception e) {
				return "ENCERR"; // used to Detect Failed Handshake
			}

		}
	}
}