
import socket


key = 0
ANSI_CYAN = "\u001B[36m"
ANSI_GREEN = "\u001B[32m"
ANSI_RED = "\u001B[31m"
ANSI_RESET = "\u001B[0m"
global Nsec
global admin_stat
admin_stat = False
Nsec = ''

import time
def main():


    key = 'godisdead'
    port = 3000
    host = ''
    #key = sys.argv[1]
    #port = int(sys.argv[2])
    print(ANSI_CYAN+"Using Key:{} Using :{} Port".format(key,port)+ANSI_RESET)
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind((host,port))
    s.listen(1) # it will listen to only for one connection
    conn,addr = s.accept()
    while True:
        print('Connection by {}'.format(addr))
        time.sleep(2)
        user = conn.recv(2048)
        print(user)
        data = b'Hello Guest'
        conn.send(data)
         # ill recive the user
        #print(user)

    s.close()


main()
