## Anonybox - AES-256 Encrypted Mailbox
### To do:

1. Setup Python Version
2. Handle Client disconnect in server side
3. Make GUI for server application

### Build:
For server side:
> cd Server && chmod +x compile.sh && ./compile.sh

For client side:
> cd Client && chmod +x compile.sh && ./compile.sh

### Running:
For server side:
> cd Server && java main [Encryption key]

For client side:
> cd Client && java client

###  PYTHON3:
 Requirements #### pycrypto,socket,sys,shutil,os
 
> sudo pip3 install pycrypto

### Running:
For server side:
> sudo python3 main.py [HOST] [Night Key]

For client side:
> sudo python3 client.py [HOST]

### Encryption info:

*AES-256 CBC PKCS#5/7 with IV*

### Copyrights and Team information:

Creator: Morad Abdelrasheed Mokhtar Ali (Java Developer)<br>
Mustafa Anwar (Penetration Testing)<br>
Isla Mukheef (Python Developer UwU)<br>
Khaled Nassar (Flask Framework Developer)
