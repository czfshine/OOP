from bottle import *

import CNN

import json
cnn = CNN.myCNN()
cnn.restore_save()

@route('/hello')
def hello():
    return "Hello World!"

@post('/login')
def do_login():
    global cnn
    
    
    
    data = request.forms.get('data')
    print(data)
    cnn_predict = cnn.predition(json.loads(data))


    _x = int((cnn_predict[0] - 25) / 30)
    _y = int((cnn_predict[1] - 25) / 30)
    return str(_x)+" "+str(_y)

run(host='localhost', port=8080, debug=True)