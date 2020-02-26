from flask import Flask, render_template, escape, session , redirect, request
from flask_socketio import SocketIO
from flask_wtf.csrf import CSRFProtect,CSRFError
from os import urandom , system
user = 'admin@gmail.com' # username
passwd = '123456789' # password
r"""
  __________________________
 */  Flask Chat AppWeb     \*
 |                          |
 | Coded By : @knassar702   |
 \*                        */

Tested on : {
	OS : Linux Mint 19
	Python : 3.6
}

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
app.config['SECRET_KEY'] = f'{urandom(24)}'
csrf = CSRFProtect(app)
socketio = SocketIO(app)
@app.errorhandler(CSRFError)
def handle_csrf_error(e):
    return render_template('csrf_error.html', reason=e.description), 400
@app.route('/',methods=['POST','GET'])
def login():
	if request.method == 'POST':
		r = request.form
		if r['email'] == user and passwd == r['password']:
			session['logged_in'] = True
			session['email'] = r['email']
			return sessions()
	if session.get('logged_in') == True:
		return sessions()
	return render_template('index.html')
@app.route('/logout')
def logout():
	session.pop('logged_in',None)
	session.pop('email',None)
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
    socketio.emit('my response', json, callback=messageReceived)
if __name__ == '__main__':
    socketio.run(app, debug=True)
