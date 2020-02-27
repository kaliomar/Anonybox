#!/usr/bin/env python3
from flask import Flask, render_template, escape, session , redirect, request, make_response, abort
from flask_socketio import SocketIO
from flask_wtf.csrf import CSRFProtect,CSRFError
from optparse import OptionParser
from passlib.hash import sha256_crypt
import secrets,string,random
def encrypt(my_word,my_hash=None,hasher=None):
	if hasher:
		return sha256_crypt.encrypt(my_word)
	if my_hash != None:
		return sha256_crypt.verify(my_word, my_hash)
def random_char(y):
       return ''.join(random.choice(string.ascii_letters) for x in range(y))
optp = OptionParser(add_help_option=False)
optp.add_option("-h","--help",dest="help",action='store_true')
optp.add_option("-s","--server",dest='server')
optp.add_option("-p","--port",dest='port',type='int')
optp.add_option('--username',dest='username')
optp.add_option("--user-agent",dest='uagent')
opts, args = optp.parse_args()
helper= ("""
Options:
	-h, --help      | Show help message and exit
	-s, --server    | The IP of your server (default: localhost)
	-p, --port   	| The PORT of your server (default: 5000)
	--user-agent 	| add custom user-agent to access the web interface
	--username   	| The username of login page (default: Anonybox)
	""")
if opts.help:
	print(helper)
	exit()
if opts.username:
	user = opts.username
else:
	user = 'Anonybox'
if opts.server:
	host = opts.server
else:
	host = 'localhost'
if opts.port:
	port = opts.port
else:
	port = 5000
if opts.uagent:
	user_agent = opts.uagent
else:
	user_agent = None
passwd = random_char(50)
print(f'\n\n\n\n Your Password : {passwd}\n\n\n\n')

r"""
some options:

$ CSRF :
--------
# @csrf.exempt # if you need stop csrf token check ok ?
example :

@app.route('/hi')
def hi():
	return 'welcome'

* Add this *

@app.route('/hi')
@csrf.exempt
def hi():
	return 'welcome'
"""
app = Flask(__name__)
app.config['SECRET_KEY'] = secrets.token_urlsafe(200) # SECRET_KEY Long
csrf = CSRFProtect(app)
socketio = SocketIO(app)
@app.errorhandler(CSRFError)
def handle_csrf_error(e):
    return render_template('csrf_error.html', reason=e.description), 400 # the response after CSRF_TOKEN Error .. you can change it /* khaled *\
@app.route('/password')
def show_password():
	if user_agent:
		r = request.headers.get('User-agent')
		if r != user_agent:
			return abort(403)
	print(f'\n\n\n\n Your Password : {passwd}\n\n\n\n')
	return abort(404)
@app.route('/',methods=['POST','GET'])
def login():
	if user_agent:
		r = request.headers.get('User-agent')
		if r != user_agent:
			return abort(403)
	if request.method == 'POST':
		r = request.form
		if r['username'] == user and passwd == r['password']:
			session['logged_in'] = True
			session['user'] = r['username']
			res = make_response(sessions())
			res.headers['Strict-Transport-Security'] = 'max-age=31536000; includeSubDomains'
			res.headers['X-Content-Type-Options'] = 'nosniff'
			res.headers['X-Frame-Options'] = 'SAMEORIGIN'
			res.headers['X-XSS-Protection'] = '1; mode=block'
			return res
		else:
			return redirect('/logout')
	if session.get('logged_in') == True:
		res = make_response(sessions())
		res.headers['Strict-Transport-Security'] = 'max-age=31536000; includeSubDomains'
		res.headers['X-Content-Type-Options'] = 'nosniff'
		res.headers['X-Frame-Options'] = 'SAMEORIGIN'
		res.headers['X-XSS-Protection'] = '1; mode=block'
		return res
	res = make_response(render_template('index.html'))
	res.headers['Strict-Transport-Security'] = 'max-age=31536000; includeSubDomains'
	res.headers['X-Content-Type-Options'] = 'nosniff'
	res.headers['X-Frame-Options'] = 'SAMEORIGIN'
	res.headers['X-XSS-Protection'] = '1; mode=block'
	return res
@app.route('/logout')
def logout():
	session.pop('logged_in',None)
	session.pop('user',None)
	return redirect('/')
def sessions():
    return render_template('session.html')

def messageReceived(methods=['GET', 'POST']):
    print('message was received!!!')

@socketio.on('my event')
def handle_my_custom_event(json, methods=['GET', 'POST']):
    print('received my event: ' + str(json))
    for i,c in json.items():
        json[i] = escape(c)
        if len(json[i]) < 100:
        	json[i] = json[i]
    socketio.emit('my response', json, callback=messageReceived)
if __name__ == '__main__':
    socketio.run(app,host=host,port=port, debug=False)
