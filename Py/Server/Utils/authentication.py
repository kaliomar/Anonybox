from .AES import decrypt

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