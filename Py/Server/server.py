import sys
import socket
from Utils.AES import *
from Utils.authentication import authentication
from Utils.isAdministrator import Administrator
from Utils.isNormalUser import normal_users
from Utils.nightSec import night_sec
import os
import shutil


key = 0
ANSI_CYAN = "\u001B[36m"
ANSI_GREEN = "\u001B[32m"
ANSI_RED = "\u001B[31m"
ANSI_RESET = "\u001B[0m"
def main(): 
    print(ANSI_CYAN+"BETA [!]"+ANSI_RESET)
    port = 2468
    try:
        host = str(sys.argv[1])
        key = (sys.argv[2])
    except:
        print('example : main.py 127.0.0.1 godisdead\n [python file name] [HOST][Night Key]')
        sys.exit()
    print(ANSI_CYAN+"Using Key:{} Using :{} Port".format(key,port)+ANSI_RESET)
    s =  socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind((host,port))
    s.listen(1) # it will listen to only for one connection
    conn, addr = s.accept()
#this is a beta it can handle only one connection and if the connection got closed the server will be down too
    with conn:
        print('Connection by {}'.format(addr))
        try: 
            while True:
                ### note dont forget to send your public key so you can uncode the data
                #public_key = conn.recv(1024) # first ill recive a key from the client
                conn.send(b'Hello Guest, The connection is not encrypt yet...\nAlso you should have the Secret ;)\nEverything after this message will be encrypt...')
                user = conn.recv(1024).decode() # ill recive the user
                if len(user) > 0:
                    conn.send(b'OK')
                    night_secret = conn.recv(1024).decode() # ill recive the night secret here
                    check,Nsec = night_sec(user,night_secret,key)
                    if check == 1:
                        secure_flag = True # the user sent a valid night secret to do a secure connection with
                        msg = encrypt('OK',Nsec)
                        conn.send(msg)
                        secure_passd = conn.recv(1024).decode()
                        passd = decrypt(secure_passd,Nsec)
                        passd = passd.decode()
                        try:
                            info = user,passd
                            auth,admin_stat = authentication(info)
                            if admin_stat == True:
                                print("{} Loged in as Administrator ".format(auth))
                                msg = ('Welcome Administrator {}'.format(auth))
                                mesg = encrypt(msg,key)
                                conn.send(mesg)
                                while True:
                                    stuff = conn.recv(1024)
                                        ##stuff to do for admins
                                    ord = decrypt(stuff,key)
                                    ord = ord.decode()
                                    if ord.find(":") != -1:
                                        ord = ord.split(':')
                                    elif ord.find(":") == -1:
                                        pass
                                    if type(ord) == list:
                                        try:
                                            if ord[0] == 'create account':
                                                if admin_stat == True:
                                                    
                                                    done = Administrator(user,ord,key)
                                                    msg = encrypt(done,key)
                                                    conn.send(msg)
                                                else:
                                                    msg = ('You should have an Administrator privileges for that')
                                                    mesg = encrypt(msg,key)
                                                    conn.send(mesg)
                                            elif ord[0] == 'send mail':
                                                try:
                                                    done = normal_users(user,ord)#there is no need for the Administrator 
                                                                                    #privileges so normal_users will handle it"
                                                    msg = encrypt(msg,key)
                                                    conn.send(msg)
                                                except:
                                                    msg = ('Unable to {}'.format(ord[0]))
                                                    mesg = encrypt(msg,key)
                                                    conn.send(mesg)
                                            elif ord[0] == 'delete account':

                                                if admin_stat == True:
                                                    done = Administrator(user,ord,key)
                                                    msg = encrypt(done,key)
                                                    conn.send(mesg)
                                            
                                                else:

                                                    try:
                                                        info = ord[1],ord[2]
                                                        checking_user = authentication(info,key)
                                                        if checking_user == ord[1]:
                                                            
                                                            done = normal_users(user,ord)
                                                            conn.close() #there is no need for telling the user we done just close the connection
                                                        else:
                                                            pass
                                                    except:
                                                        msg = ('Unable to {}'.format(ord))
                                                        mesg = encrypt(msg,key)
                                                        conn.send(mesg)
                                        except:
                                            print('error while handling the order')
                                    elif type(ord) != list:
                                        if ord == "inbox":
                                            done = normal_users(user,ord)
                                            msg = encrypt(done,key)
                                            conn.send(msg)
                                    else:
                                        msg =('server could not understand your order {}'.format(ord))
                                        mesg = encrypt(msg,key)
                                        conn.send(mesg)
                            elif (admin_stat == False) and (auth != 0) :
                                msg = ('Welcome {}'.format(auth))
                                mesg = encrypt(msg,key)
                                conn.send(mesg)
                                while True:
                                    stuff = conn.recv(1024)
                                    #stuff to do for users
                                    ord = decrypt(stuff,key)
                                    ord = ord.decode()
                                    if ord.find(":") != -1:
                                        ord = ord.split(':')
                                    elif ord.find(":") == -1:
                                        pass
                                    if type(ord) == list:
                                        try:
                                            if ord[0] == 'create account':
                                                msg = ('You should have an Administrator privileges for that')
                                                mesg = encrypt(msg,key)
                                                conn.send(mesg)
                                            elif ord[0] == 'send mail':
                                                try:
                                                    done = normal_users(user,ord)#there is no need for the Administrator 
                                                                                    #privileges so normal_users will handle it"
                                                    msg = encrypt(msg,key)
                                                    conn.send(msg)
                                                except:
                                                    msg = ('Unable to {}'.format(ord[0]))
                                                    mesg = encrypt(msg,key)
                                                    conn.send(mesg)
                                            elif ord[0] == 'delete account':
                                                try:
                                                    info = ord[1],ord[2]
                                                    checking_user = authentication(info,key)
                                                    if checking_user == ord[1]:  
                                                        done = normal_users(user,ord)
                                                        conn.close() #there is no need for telling the user we done just close the connection
                                                    else:
                                                        pass
                                                except:
                                                    msg = ('Unable to {}'.format(ord))
                                                    mesg = encrypt(msg,key)
                                                    conn.send(mesg)
                                        except:
                                            print('error while handling the order')
                                    elif type(ord) != list:
                                        if ord == "inbox":
                                            done = normal_users(user,ord)
                                            msg = encrypt(done,key)
                                            conn.send(msg)
                            else:
                                break ## TODO: add a blocking ips
                        except:
                            print('there was an error while checking someone login {}'.format(user))
                    elif check == 3:
                        print('Could not open [{}] night secret'.format(user))
                        conn.send(b"we don't like you")
                        s.close()
                    else: #CONNECTION IS NOT SECURE AT ALL ADMIN SHOULD TAKE ACTION HERE
                        secure_flag = False
                        conn.send('NOT found').encode()
                        print('Uh no fuck you im out, there was no secure connection so i dropped it')
                        s.close()
                        #sys.exit()
                else:
                    user = ''
                    night_secret = ''
                    print('user was not vaild')
                    s.close()

        except: 
            print("User disconnected")
           
main()
