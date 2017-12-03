'''
这一段用来训练神经网络，要训练是把注释去掉并运行这个文件就可以了
由于棋谱是以sgf格式保存的，所以这个程序主要实现了对sgf文件的处理，
转化问程序通用的棋谱形式。这个程序还包括了一些棋谱转sgf,以及一些
其他骚操作。
'''

import time
import numpy as np
import os

class SGFflie():
    def __init__(self):
        self.POS = 'abcdefghijklmno'
        self.savepath = '.'
        self.trainpath = '.'

    def openfile(self, filepath):
        f = open(filepath, 'r')
        data = f.read()
        f.close()

        effective_data = data.split(';')
        s = effective_data[2:-1]

        board = []
        step = 0
        for point in s:
            x = self.POS.index(point[2])
            y = self.POS.index(point[3])
            color = step % 2
            step += 1
            board.append([x, y, color, step])

        return board

    def savefile(self, board):
        data = self.createdata(board)

        filepath = self.savepath + data.split(';')[1] + ".sgf"
        f = open(filepath, 'w')
        f.write(data)
        f.close()
        return

    def createdata(self, board):
        now = time.localtime(time.time())
        _time = ''
        for index in range(6):
            _time = _time + str(now[index])
        data = '(;' + _time + ";"

        for it in board:
            if it[2] == 0:
                data = data + 'B[' + self.POS[it[0]] + self.POS[it[1]] + "];"
            else:
                data = data + 'W[' + self.POS[it[0]] + self.POS[it[1]] + "];"
        data = data + ')'
        return data

    def createTraindataFromqipu(self, path, color=0):
        qipu = self.openfile(path)

        bla = qipu[::2]
        whi = qipu[1::2]
        bla_step = len(bla)
        whi_step = len(whi)

        train_x = []
        train_y = []

        if color == 0:
            temp_x = [0.0 for i in range(225)]
            for index in range(bla_step):
                _x = [0.0 for i in range(225)]
                _y = [0.0 for i in range(225)]
                if index == 0:
                    lx = []
                    lx.append(_x)
                    train_x.append(_x)
                    _y[bla[index][0]*15 + bla[index][1]] = 2.0
                    ly = []
                    ly.append(_y)
                    train_y.append(_y)
                else:
                    _x = temp_x.copy()
                    lx = []
                    lx.append(_x)
                    train_x.append(_x)
                    _y[bla[index][0] * 15 + bla[index][1]] = 2.0
                    ly = []
                    ly.append(_y)
                    train_y.append(_y)

                temp_x[bla[index][0] * 15 + bla[index][1]] = 2.0
                if index < whi_step:
                    temp_x[whi[index][0] * 15 + whi[index][1]] = 1.0
        return train_x, train_y
        #print(train_y)

    def createTraindataFromqipu1(self, path, color=0):
        qipu = self.openfile(path)

        bla = qipu[::2]
        whi = qipu[1::2]
        bla_step = len(bla)
        whi_step = len(whi)

        train_x = []
        train_y = []

        if color == 0:
            temp_x = [[[0.0, 0.0, 0.0] for j in range(15)] for k in range(15)]
            for index in range(bla_step):
                _x = [[[0.0, 0.0, 0.0] for j in range(15)] for k in range(15)]
                _y = [0.0 for i in range(225)]
                if index == 0:
                    train_x.append(_x)
                    _y[bla[index][0]*15 + bla[index][1]] = 1.0
                    train_y.append(_y)
                else:
                    _x = temp_x.copy()
                    train_x.append(_x)
                    _y[bla[index][0] * 15 + bla[index][1]] = 1.0
                    train_y.append(_y)

                temp_x[bla[index][0]][bla[index][1]][1] = 1.0
                if index < whi_step:
                    temp_x[whi[index][0]][whi[index][1]][2] = 1.0
        for tmp in train_x:
            for x in tmp:
                for y in x:
                    if y[1] == 0 and y[2] == 0:
                        y[0] = 1
        return train_x, train_y
        #print(train_y)

    def createTraindata(self):
        filepath = self.allFileFromDir(self.savepath)
        train_x = []
        train_y = []
        for path in filepath:
            x, y = self.createTraindataFromqipu(path)
            train_x = train_x + x
            train_y = train_y + y
        #return np.array(train_x).astype(np.float32), \
               #np.array(train_y).astype(np.float32)
        return train_x, train_y

    def allFileFromDir(self, Dirpath):
        pathDir = os.listdir(Dirpath)
        pathfile = []
        for allDir in pathDir:
            child = os.path.join('%s%s' % (Dirpath, allDir))
            pathfile.append(child)
        return pathfile

    def createqijuFromqipu(self, path, color=0):
        qipu = self.openfile(path)

        bla = qipu[::2]
        whi = qipu[1::2]
        qiju = [[-1]*15 for i in range(15)]

        for tmp in bla:
            qiju[tmp[0]][tmp[1]] = -2
        for tmp in whi:
            qiju[tmp[0]][tmp[1]] = -7
        return qiju
