def authentication(info):
    user,password = info
    auth = open('Utils/admin.txt','r+')
    admins =(auth.read()).split(':')
    while('' in admins):
        admins.remove('')
    try:
        for i in range(0,len(admins),2):
            admin = admins[i]
            passwd = admins[1+i]
            if admin == user and passwd.replace('\n','') == password:
                admin_stat = True
                flag = 1
                auth.close()
                return user , admin_stat
            else:
                pass         
        user_auth = open('Utils/user.txt','r+')
        users = (auth.read()).split(':')
        while('' in users ):
            users.remove('')
    except:
        return 0 ,0
    try:
        for i in range(0,len(users),2):
            name = users[i]
            passwd = users[1+i]
            if name == user and passwd.replace('\n','') == password:
                    admin_stat = False
                    flag = 1
                    user_auth.close()
                    return user , admin_stat
            else:
                admin_stat = False
                try:
                    user_auth.close()
                    auth.close()
                except:
                    pass
                return 0 , admin_stat
    except:
        return 0 , 0 
