import os

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

            