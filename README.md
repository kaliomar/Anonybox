# Anonybox - Encrypted Mailbox [![GitHub stars](https://img.shields.io/github/stars/Praudyogikee/Anonybox)](https://github.com/Praudyogikee/Anonybox/stargazers) [![GitHub license](https://img.shields.io/github/license/Praudyogikee/Anonybox)](https://github.com/Praudyogikee/Anonybox/blob/master/LICENSE) ![GitHub repo size](https://img.shields.io/github/repo-size/Praudyogikee/Anonybox) ![Lines of code](https://img.shields.io/tokei/lines/github/Praudyogikee/Anonybox)
Anonybox is an open source encrypted mailbox made in java.
## Features
- Diffie-Hellman Key Exchange Algorithm is used to exchange encryption keys
- Specific Encryption Key for server files
- IMP - International Messaging Protocol; Allows you to communicate with other Anonybox servers
- Used AES-256 Encryption Algorithm "Quantum Resistant"
- No MITM attacks, since it uses AES encryption for every message sent/received
- Add your own features, make your own customized Anonybox Server
- Up-to-date features added every month to improve security, fixing bugs, and adding new features!
- You do not have to connect to another server. Run your own, and send to others!

## Donation
Buy us a cup of coffee! Donate at ko-fi.com/praudyogikee

## You're a server and want to be a part of the network?
You can:
1. Edit PublicServers.txt
2. Contact praudyogikee[dot]atdo[at]gmail[dot]com

## Build & Run
"You can view Test.mkv if you have any issues"<br>
###### For Server
`javac main.java && sudo java main [Arguments]`
###### For Client
`javac Client.java && sudo java Client`

## Server Arguments
- -h | Display Help Screen
- -v [1/0] | Verbose Mode "1: Enable 0: Disable"
- -U [String] | User Data Filename
- -d [String] | Data Directory "The directory that has The Server key, User data file, and the mailboxes"
- -mxc [Number] | Number of connections can be held at the same time

## Version
Anonybox 21 - Java Edition

## Encryption Details
- Used AES256/CBC/PKCS#5
- Produce Communication Encryption Key by looping Diffie-Hellman Algorithms 8 times to generate the final key
- All files "Including Mailboxes" are encrypted with the Server Key

## Files inside Data directory
- **Server.key** used to verify the server key. Encrypts itself and the server administrator has to enter the key to proceed, This key is used to encrypt ALL files.
- **User.txt** used to store the user credentials.
- **[Username].mail** is the mailbox of the user.

## IMP - International Messaging Protocol
This protocol allows different Anonybox clients to communicate with each other even if they are not on the same server. This allows people to communicate within closed/internal networks with limited access to the internet "like Russia, North Korea" and also avoids the ban of external servers.<br>
The client sends a "notify" command to his server notifing him that he'll send to X server. Then the client connects to the destination server and tells him "Hello, Server X. I would like to send the following content to the client on your server, my name is Y and I belong to Z server". The destination server send a "verify" command to Y's server "which is Z", once the verification is done, X server will send Y message to the destination. "Clients IP Addresses do not show up to anybody. instead, Servers IP Addresses only do show up to everybody"

## Java Version Required
openjdk version "11.0.10" 2021-01-19<br>
OpenJDK Runtime Environment (build 11.0.10+9-Ubuntu-0ubuntu1.20.04)<br>
OpenJDK 64-Bit Server VM (build 11.0.10+9-Ubuntu-0ubuntu1.20.04, mixed mode, sharing)

## Wanna try it?
"You can view Test.mkv if you have any issues"<br>
1. Execute `javac main.java && sudo java main`
2. Enter the Server Key, once you get "OK", Execute `sudo java main` and verify the Server Key, Once it says "Verified!" you are good to go!
3. Open a new tab, Execute `java Client.java && sudo java Client`
4. Type "6" and enter your server IP "Usually: 127.0.0.1".
5. Type "2" and enter a new username and password
6. Type "1" and enter previous credentials
7. Type "5" and enter your username twice, and the server IP "Usually: 127.0.0.1", and the subject, and the content. "Example: morad,morad,127.0.0.1,hi,hello"
8. Type "4" and you should see your message!

## Legal Disclaimer
PRAUDYOGIKEE IS NOT RESPONSIBLE FOR ANY ILLEGAL USES FOR ANONYBOX

## Am I allowed to modify Anonybox?
Yes, but under certain conditions:
- Mention the original developer "Praudyogikee" and show what you have modified
- Do not try to change the original code and algorithms, this may result in MANY unexpected bugs
- If you want to add a feature, you can open an issue and we will add it
- Do not republish before contacting Praudyogikee "You can fork and edit only"

## Team
- Morad Abdelrasheed Mokhtar Ali
- Omar Yehia
- Pavly O. Halim
- Mustafa Anwar

## Contributers
- Isla Mukheef
