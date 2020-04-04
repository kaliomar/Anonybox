## Anonybox - AES-256 Encrypted Mailbox
### To do:

1. Make GUI for server application

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

###  Python 3:

 #### Requirements 
 ~~~~
 pycrypto,socket,sys,shutil,os 
 ~~~~
> sudo pip3 install pycryptodome

### Running:
For server side:
> sudo python3 main.py [HOST] [Night Key]

For client side:
> sudo python3 client.py [HOST]

 ### Web Interface Requirements
 ```
  $ sudo pip3 install flask
  $ sudo pip3 install flask_wtf
  $ sudo pip3 install flask_socketio
  ```
### Encryption info:

*AES-256 CBC PKCS#5/7 with IV*

### Copyrights and Team information:

Creator: Morad Abdelrasheed Mokhtar Ali (Java Developer)<br>
Isla Mukheef (Python Developer UwU) Also, She's the creator of python version !<br>
Khaled Nassar (Flask Framework Developer)

<p align="center">
  <img width="205" height="210" src="https://i.ya-webdesign.com/images/badge-transparent-communist-1.png">
 <br>
  <b>Programmers of the world, unite!</b>
</p>
