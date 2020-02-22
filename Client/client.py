import sys
import socket
from AES import encrypt, decrypt
import os
import shutil
import sys


def main():
    port = 2468
    host = str(sys.argv[1])
    key = input('key :')
    while(key =='help'):
        help()
        key = input('key :')
    key = key.encode()
    s = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
    s.connect((host,port))
    try:
        wlc_msg = s.recv(1024).decode()
        print(wlc_msg)
        user = input('User :').encode() 
        s.send(user)
        respon = s.recv(1024).decode()
        if respon == 'OK':
            secret= input('Your secret :')
            night_secret = encrypt(secret,secret)
            s.send(night_secret)
            respon = s.recv(1024).decode()
            try:
                respon = decrypt(respon,secret).decode() 
                if respon == 'OK':
                    password = input('Password:')
                    passd = encrypt(password,secret)
                    s.send(passd)
                    respon = s.recv(1024).decode()
                    msg = decrypt(respon,secret)
                    while True:
                        cmd = input(':>')
                        try:
                            if len(cmd) < 1 :
                                cmd = input('Type something :>')
                        except:
                            pass
                        if cmd == 'help':
                            help()
                        elif cmd == 'create account':
                            username = input('account user:')
                            user_password = input('Password for the user:')
                            user_night = input ('Night secret for the user:')
                            cmd =('create account'+':'+username+':'+user_night+':'+user_password)
                            command = encrypt(cmd,secret)
                            s.send(command)
                            s.settimeout(3.5)
                            server_resposn = s.recv(1024).decode()
                            server_resposn = decrypt(server_resposn,secret)
                            print(server_resposn.decode())
                        elif cmd == 'delete account':
                            username = input('account user:')
                            user_password = input('Password:')
                            cmd = ('delete account'+':'+username+':'+user_password)
                            command = encrypt(cmd,secret)
                            s.send(command)
                            s.settimeout(3.5)
                            server_resposn = s.recv(1024).decode()
                            server_resposn = decrypt(server_resposn,secret)
                            print(server_resposn)
                        elif cmd == 'inbox':
                            cmd = encrypt(cmd,secret)
                            s.send(cmd)
                            s.settimeout(3.5)
                            server_resposn = s.recv(1024).decode()
                            server_resposn = decrypt(server_resposn,secret)
                            print(server_resposn)
                        elif cmd == 'send mail':
                            username = input('User to send to :')
                            my_mail = input('Your mail:')
                            cmd = ('send mail'+':'+username+':'+my_mail)
                            command = encrypt(cmd,secret)
                            s.settimeout(3.5)
                            server_resposn = s.recv(1024).decode()
                            server_resposn = decrypt(server_resposn,secret)
                            print(server_resposn)
                        
                    
                        
            except:
                print('Server cloesd the connection')
        else:
            print('server respond with an error')
        
    except:
        print('Could not be able to make connection with the server !')
def help():
     print ('''
    
    **Authentication Section**
User <- Enter User Credentialt
Secret <- Enter the night secret *IF YOU DON'T HAVE ONE GET ONE FROM THE ADMINS*
Password <- Enter Password Credential

----------------------------------------

**User Commands Section**
inbox <- Get all your mails
reset mail <- Delete all Messages from the Mail of the Authenticated Host
delete account <- Delete a user account
send mail to <- Send Message to Person
help <- Show this Help Message

-------------------------------
**Administrator Section**
create account <- Create a new user with a password
delete user <- Delete a Username With his/her/thier Mailbox
    ''')
     pass
main()
