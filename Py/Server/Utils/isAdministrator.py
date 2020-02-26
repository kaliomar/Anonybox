import os
from .AES import *


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