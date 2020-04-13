package com.morad.anonyboxv20;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {
    EditText ip;
    EditText key;
    EditText message;
    TextView status;
    TextView log;
    Socket s;
    Button send;
    DataInputStream dis;
    DataOutputStream dos;
    String keyi;
    private static SecretKeySpec secretKey;
    private static byte[] key0;
    Cipher cipher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ip = (EditText) (findViewById(R.id.ip));
        key = (EditText) (findViewById(R.id.key));
        message = (EditText) (findViewById(R.id.message));
        status = (TextView) (findViewById(R.id.status));
        log = (TextView) (findViewById(R.id.log));
        send = (Button) (findViewById(R.id.send));
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        log.setMovementMethod(new ScrollingMovementMethod());
    }
    public void connect_onclick(View v) {
        String ipi = ip.getText().toString();
        keyi = key.getText().toString();
        try {
            s = new Socket(ipi, 1111);
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
            status.setText("Connection is successful");
            System.out.println("SC");
        }catch (Exception e) {
            status.setText("Connection is NOT successful");
            System.out.println("NSC");
            e.printStackTrace();
        }
    }
    public void send_onclick(View v) {
        String msg = message.getText().toString();
        if (msg.equals(null)||msg.equals("")) {
            log.setText("Enter a proper message");
        }else {
            write(dos, msg, keyi);
            try {
                dos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.append("[CLIENT]:"+msg+"\n");
            log.append("[SERVER]:"+read(dis, keyi) + "\n");
        }
    }
    public void write(DataOutputStream s, String text, String key) {
        write(s,text,key,0);
    }
    public String read(DataInputStream s, String key) {
        return read(s,key,0);
    }
    public void write(DataOutputStream s,String text,String key,int debug) {
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
        enc = encrypt(text,keyi);
        System.out.println(enc);
        if (enc.length()>63980) {
            while (i<(Math.round((double)enc.length()/63980))) {
                cut = enc.substring(0,63980);
                enc = enc.replace(cut,"");
                i++;
                try {
                    s.writeUTF(encrypt(decrypt(cut,keyi)+"CONTIN",keyi));
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
    public String read(DataInputStream s,String key,int debug) {
        if (key.isEmpty()) {
            System.out.println("Key var is empty");
        }
        String output = "";
        try {
            output = decrypt(s.readUTF(),keyi);
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
                    output+=decrypt(s.readUTF(),keyi);
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
    public void setKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            key0 = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key0 = sha.digest(key0);
            key0 = Arrays.copyOf(key0, 16);
            secretKey = new SecretKeySpec(key0, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String strToEncrypt, String secret)
    {
        try {
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            setKey(secret);
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        }
        catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
            return "";
        }

    }

    public String decrypt(String strToDecrypt, String secret)
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
            return "ENCERR";
        }
    }
}
