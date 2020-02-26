import os
from .AES import *

def night_sec(user,key,mykey):
    path = os.getcwd() +'/'+user+"/night.txt" # if error happen it may be cuz the path was wrong
    try:
        with open(path,'r+') as check:
            night = check.read()
            hmm = decrypt(night,mykey).decode()
            user_night = decrypt(key,hmm).decode() 
            if user_night == hmm :
                Nsec = hmm
                return 1 , Nsec
            else:
                return 2 
            
    except:
        return 3  
