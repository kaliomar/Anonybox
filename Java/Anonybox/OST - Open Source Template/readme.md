## Anonybox Documentation

**Getting Started:**

- Download the Anonybox OST "Open Source Template"
- If you downloaded JDK, Skip this step..
  Download Java SE Development Kit 8 [From Here](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)

**First: anonybox.java :**

* **public static void write(DataOutputStream s, String text, String key,byte[] iv)**
  Takes DataOutputStream. The plaintext. The Encryption Key. The IV in form of byte[]. Then sends the ciphertext to the DataOutputStream Using Encrypting String text with String Key and byte[] IV.
  * USAGE: `anonybox.write(s,text,key,iv);`

* **public static String read(DataInputStream s, String key,byte[] iv)**
  Takes DataInputStream. The Encryption Key. The IV in form of byte[]. Then reads the DataOutputStream. Decrypts it with String key and byte[] iv. Returns Plaintext in form of String. Can return "ENCERR" in failed decryption cases.
  * USAGE: `String output = anonybox.read(s,key,iv);`

* **public static void write(DataOutputStream s,String text,String key,int debug,byte[] iv)**
  Takes DataOutputStream. The plaintext. The Encryption Key. The IV in form of byte[]. Then sends the ciphertext to the DataOutputStream Using Encrypting String text with String Key and byte[] IV.
  if debug>0 , It will print error messages and outputs.
  * USAGE: `anonybox.write(s,text,key,1,iv);`

* **public static String read(DataInputStream s,String key,int debug,byte[] iv)**
  Takes DataInputStream. The Encryption Key. The IV in form of byte[]. Then reads the DataOutputStream. Decrypts it with String key and byte[] iv. Returns Plaintext in form of String. Can return "ENCERR" in failed decryption cases.
  if debug>0 , It will print error messages and outputs.
  * USAGE: `String output = anonybox.read(s,key,1,iv);`

* **public static byte[] generateIV()**
  Generates random IV. returns byte[] iv
  * USAGE: `byte[] iv = anonybox.AES.generateIV();`

* **public static void setKey(String myKey)**
  Sets the Encryption Key in current session.
  * USAGE: `anonybox.AES.setKey(enckey);`

* **public static String encrypt(String strToEncrypt, String secret,byte[] iv)**
  Takes Plaintext. The Encryption Key. The IV in form of byte[]. Then returns the ciphertext.
  * USAGE: `String ciphertxt = anonybox.AES.encrypt(plaintxt,enckey,iv);`

* **public static String decrypt(String strToDecrypt, String secret,byte[] iv)**
  Takes Ciphertext. The Encryption Key. The IV in form of byte[]. Then returns the plaintext.
  * USAGE: `String plaintxt = anonybox.AES.decrypt(ciphertxt,enckey,iv);`

**Second: main.java :**

- **public static void main(String[] args)**
  The First Stage of Anonybox

  * ```java 
    int port = 10;
    ``` 
    Sets The Server port

  * ```java 
    String enckey = "";
    ``` 
    Sets The Encryption Key

  * ```java 
    ServerSocket s = new ServerSocket(port);
    ``` 
    Initialize The Server with specified port

  * ```java 
    Accept a = new Accept(s,enckey);
    ``` 
   Initialize Accept Class with ServerSocket and Encryption Key

  * ```java 
    a.start();
    ``` 
   Start the Accept Class Thread

* **static class Accept extends Thread**
  The Second Stage of Anonybox

  * ```java 
    Accept(ServerSocket s,String enckey)
    ``` 
    Constructor for class. sets Inputs to public variables

  * ```java 
    public void run()
    ``` 
    Run method. It's role is to accept User connection and redirects Socket to ServerThread to process the user input
    * ```java 
      Socket c = s.accept();
      ``` 
     Accept user connection
    * ```java 
      System.out.println(c.getInetAddress() + " is connected" + "\n");
      ``` 
     Log who is connected to the server. can be deleted.
    * ```java 
     ServerThread st = new ServerThread(c,enckey);
      ``` 
     Initialize ServerThread Class with the user connection and the Encryption Key
    * ```java 
     st.start();
      ``` 
     Start ServerThread Thread

* **static class ServerThread extends Thread**
  The Third Stage of Anonybox

  * ```java 
    ServerThread(Socket s, String enckey)
    ```
   Constructor for class. sets Inputs to public variables
  * ```java
    public void run()
    ```
   Anonybox Core.
    **Recommended Stages:** 

    * Set the DataInputStream and DataOutputStream.

      ```java
      DataOutputStream dos = new DataOutputStream(s.getOutputStream());
      DataInputStream dis = new DataInputStream(s.getInputStream());
      ```

    * Set the User and Admin User Creds files.

      ```java
      File tmpfile = new File("user.txt");
      boolean exists = tmpfile.exists();
      if (exists) {
       // DO NOTHING
      } else {
       tmpfile.createNewFile();
      }
      tmpfile = new File("admin.txt");
      exists = tmpfile.exists();
      if (exists) {
       //DO NOTHING
      } else {
       tmpfile.createNewFile();
      }
      ```

    * Convert user and admin files to Hashmap.
      Form: `usernm:imapotato,user2nm:morad`

      ```java
      userCreds = new String(Files.readAllBytes(Paths.get("user.txt")));
      adminCreds = new String(Files.readAllBytes(Paths.get("admin.txt")));
      String[] userdb = userCreds.split(",");
      String[] admindb = adminCreds.split(",");
      HashMap < String, String > usermap = new HashMap < String, String > ();
      HashMap < String, String > adminmap = new HashMap < String, String > ();
      for (int x = 0; x < userdb.length; x++) {
       String usercred = userdb[x];
       String[] pair = usercred.split(":");
       usermap.put(pair[0], pair[1]);
      }
      for (int xx = 0; xx < admindb.length; xx++) {
       String admincred = admindb[xx];
       String[] pairr = admincred.split(":");
       adminmap.put(pairr[0], pairr[1]);
      }
      ```

    * Check the Client Encryption Key. The client sends a test message and the server will try to decrypt it. if Encryption Keys don't match, The plaintext will be *ENCERR*. At this stage, The server can send to the client that the Encryption Key is wrong. if Encryption Keys match, The Server will proceed

    * Generate random IV using `anonybox.AES.generateIV()`. Sends it to client under AES Encryption with a Prefix/Suffix identifies that this message is IV Setting. Client detects the Prefix/Suffix then sets the IV. Sends a test message, if IV is correct, The Server will proceed

    * Check for Username and password from the Hashmap.
      `usermap.containsKey(recieve)` Check user if exists in Hashmap
      `username = recieve;` We will need it later
      `recieve.equals(usermap.get(username))` Check Password of the Password. if success, The user can move to the *Logged in* level

    * You can Read the user input. make if..else statements. and do operations

