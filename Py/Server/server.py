import sys
import socket
from AES import encrypt, decrypt
import os
import shutil


key = 0
ANSI_CYAN = "\u001B[36m"
ANSI_GREEN = "\u001B[32m"
ANSI_RED = "\u001B[31m"
ANSI_RESET = "\u001B[0m"
global Nsec
global admin_stat
admin_stat = False
Nsec = ''
def main(): 
    print(ANSI_CYAN+"BETA [!]"+ANSI_RESET)
    global admin_stat
    global Nsec
    secure_flag =False 
    #key = 'godisdead'
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
        while True:
            ### note dont forget to send your public key so you can uncode the data
            #public_key = conn.recv(1024) # first ill recive a key from the client
            conn.send(b'Hello Guest, The connection is not encrypt yet...\nAlso you should have the Secret ;)\nEverything after this message will be encrypt...')
            user = conn.recv(1024).decode() # ill recive the user
            if len(user) > 0:
                conn.send(b'OK')
                night_secret = conn.recv(1024).decode() # ill recive the night secret here
                check = night_sec(user,night_secret,key)
                if check == 1:
                    secure_flag = True # the user sent a valid night secret to do a secure connection with
                    msg = encrypt('OK',Nsec)
                    conn.send(msg)
                    secure_passd = conn.recv(1024).decode()
                    passd = decrypt(secure_passd,Nsec)
                    passd = passd.decode()
                    try:
                        info = user,passd
                        auth = authentication(info,key)
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
           
def normal_users(user,ord):
    if ord[0] == 'send mail':
        to = ord[1]
        if type(ord[2]) == str:
            msg = ord[2]
        else:
            msg = str(ord[2])
        try:
            path = os.getcwd() +'/'+to
            if not os.path.exists(path):
                msg =("Could'n send an Email to the user please make sure to write an exists user !")
                return msg
            elif os.path.exists(path):
                try:
                    mail_path = (path+'/inbox.txt') 
                    with open(mail_path,'a') as mail:
                        mail.write('\t[!]====================[EMAIL]==================[!]\n{}\nBy : {}\nDATE : 1900/08/16'.format(msg,user)) ## 'TODO': SET THE DATE
                        mesg ='Message has been sent'
                        return mesg
                except:
                    mesg ='Message has [NOT] been sent'
                    return mesg
            else:
                mesg ='There was a problim during sending a message'
                return mesg
        except:
            mesg ='There was a problim during finding the user please report the issue to the AnonyBox TM'
            return mesg


    elif ord[0] == 'delete account':
        if user == ord[1]:
            try:
                file = user
                path = os.getcwd() +'/'+file
                if os.path.exists(path):
                    shutil.rmtree(path, ignore_errors=True)
                    with open('admins_report.txt','a') as report:
                        report.write("A user {} was deleted at [to use time here]\n".format(user))
                        msg =("A user {} was deleted at [to use time here]".format(user)) #use time
                        return msg
                else:
                    msg = ("User may not be exists !")
                    return msg
            except:
                msg = ("Couldn't Delete User please report the issue to the AnonyBox TM")
                return msg
        else:
            msg = ("You only can delete your account !")
            return msg
    elif ord == 'inbox':
        path = os.getcwd() +'/'+user+"/inbox.txt"
        if os.path.exists(path):
            with open(path,'r+') as mails:
                data = mails.read() 
                return data
        else:
            msg = "NO MAILS"
            return msg
def Administrator(admin,ord,mykey):
    if ord[0] == 'create account':
        file = ord[1]
        path = os.getcwd() +'/'+file
        if not os.path.exists(path):
            os.makedirs(file)
            msg = ord [2]
            night = (encrypt(msg,mykey)).decode()
            sec = open(file+'/night.txt','w')
            sec.write(night)
            sec.close
            with open ('user.txt','r') as data:
                new_users = (data.read()).strip()
                new_users = (new_users+file+":"+ord[3]+":")
                print(new_users , file=open('user.txt','w'))
            try:
                with open('admins_report.txt','a') as report:
                    report.write('New user was created {}: by {}:\n'.format(file,admin))
                    msg = ("User {} was created by {} ".format(file,admin))
                    return msg
            except:
                msg = ("Could not write a report for creating new account for but user was created{}".format(admin))
                return msg
        else:
            msg = ("Couldn't create a new user ! [issue] Name of the user may be already taken !")
            return msg
    elif ord[0] == 'delete account':
        try:
            file = ord[1]
            path = os.getcwd() +'/'+file
            if os.path.exists(path):
                shutil.rmtree(path, ignore_errors=True)
                with open('admins_report.txt','a') as report:
                    report.write("A user was deleted by {} at [to use time here]\n".format(admin))
                    msg =("A user was deleted by {} at [to use time here]".format(admin)) #use time
                    return msg
            else:
                msg = ("User may not be exists !")
                return msg
        except:
            msg = ("Couldn't Delete User please report the issue to the AnonyBox TM")
            return msg

            #user,night_secret,key
def night_sec(user,key,mykey):
    global Nsec
    path = os.getcwd() +'/'+user+"/night.txt" # if error happen it may be cuz the path was wrong
    try:
        with open(path,'r+') as check:
            night = check.read()
            hmm = decrypt(night,mykey).decode()
            user_night = decrypt(key,hmm).decode() 
            if user_night == hmm :
                Nsec = hmm
                return 1
            else:
                return 2 
            
    except:
        return 3  
def authentication(info,key):
    flag = 0
    global admin_stat
    user,password = info
    auth = open('admin.txt','r+')
    auth = decrypt(auth.read(),key)
    auth = auth.decode()
   # for admins in auth: #for admin
    admins =auth.split(':')
    for i in range(0,len(admins),2):
        admin = admins[i]
        passwd = admins[1+i]
        if admin == user and passwd == password:
            admin_stat = True
            flag = 1
            return user
        else:
            pass
    user_auth = open('user.txt','r+')
    for users in user_auth: #for users
        users = users.split(':')
        while('' in users ):
            users.remove('')
        #users = user.remove('')
        name,passwd = users

        if name == user and passwd.replace('\n','') == password:
            admin_stat = False
            flag = 1
            return user
        else:
            pass

    try:
        if flag == 0:
            msg = 'User or password did not found'
            return 0
        else:
            pass
            print('a pass worked')
    except:
        print('there was an error while checking the flag for the auth')

main()
