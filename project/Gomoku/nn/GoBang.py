#coding=utf-8
'''
这是主程序，主要定义了Gobang和Robot两个对象：
1.Gobang主要是图像界面交互的实现。
2.Robot是基于五子棋的一些基本规则写出来的一个简单智能程序，不包含
神经网络的搭建。
'''

from tkinter import *
from tkinter.ttk import *
import math
from tkinter.filedialog import askopenfilename
from tkinter.filedialog import asksaveasfile
from SGFfile import SGFflie
import os
from CNN import myCNN

class GoBang():

    def __init__(self):
        self.someoneWin = False
        self.humanChessed = False
        self.IsStart = False
        self.player = 0
        self.playmethod = 0
        self.whi_chessed = []
        self.bla_chessed = []
        self.board = self.init_board()
        self.window = Tk()
        self.var = IntVar()
        self.var.set(0)
        self.var1 = IntVar()
        self.var1.set(0)
        self.window.title("myGoBang")
        self.window.geometry("600x470+80+80")
        self.window.resizable(0, 0)
        self.can = Canvas(self.window, bg="#EEE8AC", width=470, height=470)
        self.draw_Board()
        self.can.grid(row=0, column=0)
        self.net_board = self.get_net_board()
        self.robot = Robot(self.board)
        self.sgf = SGFflie()
        self.cnn = myCNN()
        self.cnn.restore_save()

    def init_board(self):
        list1 = [[-1]*15 for i in range(15)]
        return list1

    def draw_Board(self):
        for row in range(15):
            if row == 0 or row == 14:
                self.can.create_line((25, 25 + row * 30), (445, 25 + row * 30), width=2)
            else:
                self.can.create_line((25, 25 + row * 30), (445, 25 + row * 30), width=1)
        for col in range(15):
            if col == 0 or col == 14:
                self.can.create_line((25 + col * 30, 25), (25 + col * 30, 445), width=2)
            else:
                self.can.create_line((25 + col * 30, 25), (25 + col * 30, 445), width=1)
        self.can.create_oval(112, 112, 118, 118, fill="black")
        self.can.create_oval(352, 112, 358, 118, fill="black")
        self.can.create_oval(112, 352, 118, 358, fill="black")
        self.can.create_oval(232, 232, 238, 238, fill="black")
        self.can.create_oval(352, 352, 358, 358, fill="black")

    def get_nearest_po(self, x, y):
        flag = 600
        position = ()
        for point in self.net_board:
            distance = math.sqrt((x - point[0]) ** 2 + (y - point[1]) ** 2)
            if distance < flag:
                flag = distance
                position = point
        return position

    def check_chessed(self, point, chessed):
        if len(chessed) == 0:
            return False
        flag = 0
        for p in chessed:
            if point[0] == p[0] and point[1] == p[1]:
                flag = 1
        if flag == 1:
            return True
        else:
            return False

    def have_five(self, chessed):
        if len(chessed) == 0:
            return False
        for row in range(15):
            for col in range(15):
                x = 25 + row * 30
                y = 25 + col * 30
                if self.check_chessed((x, y), chessed) == True and \
                                self.check_chessed((x, y + 30), chessed) == True and \
                                self.check_chessed((x, y + 60), chessed) == True and \
                                self.check_chessed((x, y + 90), chessed) == True and \
                                self.check_chessed((x, y + 120), chessed) == True:
                    return True
                elif self.check_chessed((x, y), chessed) == True and \
                                self.check_chessed((x + 30, y), chessed) == True and \
                                self.check_chessed((x + 60, y), chessed) == True and \
                                self.check_chessed((x + 90, y), chessed) == True and \
                                self.check_chessed((x + 120, y), chessed) == True:
                    return True
                if self.check_chessed((x, y), chessed) == True and \
                                self.check_chessed((x + 30, y + 30), chessed) == True and \
                                self.check_chessed((x + 60, y + 60), chessed) == True and \
                                self.check_chessed((x + 90, y + 90), chessed) == True and \
                                self.check_chessed((x + 120, y + 120), chessed) == True:
                    return True
                if self.check_chessed((x, y), chessed) == True and \
                                self.check_chessed((x + 30, y - 30), chessed) == True and \
                                self.check_chessed((x + 60, y - 60), chessed) == True and \
                                self.check_chessed((x + 90, y - 90), chessed) == True and \
                                self.check_chessed((x + 120, y - 120), chessed) == True:
                    return True
        return False

    def check_win(self):
        if self.have_five(self.whi_chessed) == True:
            label = Label(self.window, text="White Win!", background='#FFF8DC', font=("宋体", 15, "bold"))
            label.place(relx=0, rely=0, x=480, y=40)
            return True
        elif self.have_five(self.bla_chessed) == True:
            label = Label(self.window, text="Black Win!", background='#FFF8DC', font=("宋体", 15, "bold"))
            label.place(relx=0, rely=0, x=480, y=40)
            return True
        return False

    def draw_chess(self):
        if len(self.whi_chessed) != 0:
            for tmp in self.whi_chessed:
                self.can.create_oval(tmp[0] - 11, tmp[1] - 11, tmp[0] + 11, tmp[1] + 11, fill="white")
        if len(self.bla_chessed) != 0:
            for tmp in self.bla_chessed:
                self.can.create_oval(tmp[0] - 11, tmp[1] - 11, tmp[0] + 11, tmp[1] + 11, fill="black")

    def AIrobotChess(self):
        cnn_predict = self.cnn.predition(self.board)
        pre_x = cnn_predict[0]
        pre_y = cnn_predict[1]

        if self.player % 2 == 0:
            if len(self.bla_chessed) == 0 and len(self.whi_chessed) == 0:
                self.bla_chessed.append([25 + 30 * 7, 25 + 30 * 7, self.player % 2])
                self.can.create_oval(25 + 30 * 7 - 11, 25 + 30 * 7 - 11, 25 + 30 * 7 + 11, 25 + 30 * 7 + 11,
                                     fill="black")
                self.board[7][7] = 1
            else:
                _x, _y, _ = self.robot.MaxValue_po(1, 0)
                newPoint = [_x * 30 + 25, _y * 30 + 25]
                if self.check_chessed(cnn_predict, self.whi_chessed) == False and \
                                self.check_chessed(cnn_predict, self.bla_chessed) == False\
                                and _ < 5000:
                    self.can.create_oval(pre_x - 11, pre_y - 11, pre_x + 11, pre_y + 11,
                                         fill="black")
                    self.bla_chessed.append([pre_x, pre_y, self.player % 2])
                    _x = int((cnn_predict[0] - 25) / 30)
                    _y = int((cnn_predict[1] - 25) / 30)
                    # print(_x, _y)
                    self.board[_x][_y] = 0
                    # print("AI")
                else:
                    self.can.create_oval(newPoint[0] - 11, newPoint[1] - 11, newPoint[0] + 11, newPoint[1] + 11,
                                         fill="black")
                    self.bla_chessed.append([newPoint[0], newPoint[1], self.player % 2])
                    self.board[_x][_y] = 0
        else:
            if self.check_chessed(cnn_predict, self.whi_chessed) == False and self.check_chessed(cnn_predict,
                                                                                                 self.bla_chessed) == False:

                self.can.create_oval(pre_x - 11, pre_y - 11, pre_x + 11,pre_y + 11,
                                     fill="white")
                self.bla_chessed.append([pre_x, pre_y, self.player % 2])
                _x = int((pre_x - 25) / 30)
                _y = int((pre_y - 25) / 30)
                self.board[_x][_y] = 1
            else:
                _x, _y, _ = self.robot.MaxValue_po(0, 1)
                newPoint = [_x * 30 + 25, _y * 30 + 25]
                self.can.create_oval(newPoint[0] - 11, newPoint[1] - 11, newPoint[0] + 11, newPoint[1] + 11,
                                     fill="white")
                self.bla_chessed.append([newPoint[0], newPoint[1], self.player % 2])
                self.board[_x][_y] = 1

    def robotChess(self):
        if self.player == 0:
            if len(self.bla_chessed) == 0 and len(self.whi_chessed) == 0:
                self.bla_chessed.append([25 + 30 * 7, 25 + 30 * 7, self.player % 2])
                self.can.create_oval(25 + 30 * 7 - 11, 25 + 30 * 7 - 11, 25 + 30 * 7 + 11, 25 + 30 * 7 + 11,
                                     fill="black")
                self.board[7][7] = 1
                return
            else:
                _x, _y, _ = self.robot.MaxValue_po(1, 0)
                #print([_x, _y], [x, y])
                newPoint = [_x * 30 + 25, _y * 30 + 25]
                self.can.create_oval(newPoint[0] - 11, newPoint[1] - 11, newPoint[0] + 11, newPoint[1] + 11,
                                     fill="black")
                self.bla_chessed.append([newPoint[0], newPoint[1], self.player % 2])
                self.board[_x][_y] = 0
        else:
            _x, _y, _ = self.robot.MaxValue_po(0, 1)
            newPoint = [_x * 30 + 25, _y * 30 + 25]
            self.can.create_oval(newPoint[0] - 11, newPoint[1] - 11, newPoint[0] + 11, newPoint[1] + 11,
                                 fill="white")
            self.whi_chessed.append([newPoint[0], newPoint[1], self.player % 2])
            self.board[_x][_y] = 1

    def chess(self, event):
        if self.someoneWin == True or self.IsStart == False: return
        ex = event.x
        ey = event.y
        if ex > 10 and ex < 460 and ey > 10 and ey < 460:
            neibor_po = self.get_nearest_po(ex, ey)
            if self.check_chessed(neibor_po, self.whi_chessed) == False \
                    and self.check_chessed(neibor_po, self.bla_chessed) == False:
                _x = neibor_po[0]
                _y = neibor_po[1]
                if self.player == 0:
                    self.can.create_oval(_x - 11, _y - 11, _x + 11, _y + 11, fill="white")
                    self.whi_chessed.append([_x, _y, self.player % 2])
                    _x = int((_x - 25) / 30)
                    _y = int((_y - 25) / 30)
                    self.board[_x][_y] = 1
                else:
                    self.can.create_oval(_x - 11, _y - 11, _x + 11, _y + 11, fill="black")
                    self.bla_chessed.append([_x, _y, self.player % 2])
                    _x = int((_x - 25) / 30)
                    _y = int((_y - 25) / 30)
                    self.board[_x][_y] = 0
                self.someoneWin = self.check_win()
                if self.playmethod == 0:
                    self.AIrobotChess()
                elif self.playmethod == 1:
                    self.robotChess()
                self.someoneWin = self.check_win()

    def get_net_board(self):
        net_list = []
        for row in range(15):
            for col in range(15):
                point = (25 + row * 30, 25 + col * 30)
                net_list.append(point)
        return net_list

    def resetButton(self):
        self.someoneWin = False
        self.IsStart = False
        self.whi_chessed.clear()
        self.bla_chessed.clear()
        self.board = self.init_board()
        self.robot = Robot(self.board)
        label = Label(self.window, text="          ", background="#F0F0F0", font=("宋体", 15, "bold"))
        label.place(relx=0, rely=0, x=480, y=40)
        self.can.delete("all")
        self.draw_Board()
        self.can.grid(row=0, column=0)

    def BakcAChess(self):
        if self.someoneWin == False:
            if len(self.whi_chessed) != 0:
                p = self.whi_chessed.pop()
                self.board[int((p[0] - 25) / 30)][int((p[1] - 25) / 30)] = -1
            if self.player == 0 and len(self.bla_chessed) != 1:
                p = self.bla_chessed.pop()
                self.board[int((p[0] - 25) / 30)][int((p[1] - 25) / 30)] = -1
            elif self.player == 1 and len(self.bla_chessed) != 0:
                p = self.bla_chessed.pop()
                self.board[int((p[0] - 25) / 30)][int((p[1] - 25) / 30)] = -1
            self.can.delete("all")
            self.draw_Board()
            self.draw_chess()

    def startButton(self):
        if self.IsStart == False:
            self.IsStart = True
            if self.player % 2 == 0:
                if self.playmethod == 0:
                    self.AIrobotChess()
                elif self.playmethod == 1:
                    self.robotChess()
                self.draw_chess()

    def selectColor(self):
        if self.IsStart == False:
            if self.var.get() == 0:
                self.player = 0
            elif self.var.get() == 1:
                self.player = 1

    def selectMathod(self):
        if self.IsStart == False:
            if self.var1.get() == 0:
                self.playmethod = 0
            elif self.var1.get() == 1:
                self.playmethod = 1

    def createqipu(self):
        qipu = []
        step = 0
        totalstep = len(self.whi_chessed) + len(self.bla_chessed)
        while step < totalstep:
            if totalstep == 0:
                break
            flag = int(step / 2)
            if step % 2 == 0:
                x = int((self.bla_chessed[flag][0] - 25) / 30)
                y = int((self.bla_chessed[flag][1] - 25) / 30)
                qipu.append([x, y, 0, step + 1])
            else:
                x = int((self.whi_chessed[flag][0] - 25) / 30)
                y = int((self.whi_chessed[flag][1] - 25) / 30)
                qipu.append([x, y, 1, step + 1])
            step += 1
        return qipu

    def OpenFile(self):
        file_path = askopenfilename(filetypes=(('sgf file', '*.sgf'),
                                                    ('All File', '*.*')))
        if len(file_path) == 0:
            return

        qipu = self.sgf.openfile(file_path)

        self.whi_chessed.clear()
        self.bla_chessed.clear()

        for point in qipu:
            if point[2] == 0:
                self.bla_chessed.append([25 + point[0]*30,
                             25 + point[1]*30, point[2]])
            else:
                self.whi_chessed.append([25 + point[0] * 30,
                             25 + point[1] * 30, point[2]])

        self.can.delete("all")
        self.draw_Board()
        self.draw_chess()

    def SaveFile(self, method=1):
        '''while method is 0,client should manually chioce file or add new file to save data
        while method is 1,the data will save with name of random number all by computer'''

        qipu = self.createqipu()

        if method == 0:
            try:
                file = asksaveasfile(filetypes=(('sgf file', '*.sgf'),
                                                ('All File', '*.*')))
                file.close()
            except AttributeError:
                return

            pathName = file.name
            newName = pathName + '.sgf'
            os.rename(pathName, newName)

            f = open(newName, 'w')
            data = self.sgf.createdata(qipu)
            f.write(data)
            f.close()

        elif method == 1:
            self.sgf.savefile(qipu)

    def start(self):
        b3 = Button(self.window, text="开始", command=self.startButton)
        b3.place(relx=0, rely=0, x=495, y=100)

        b1 = Button(self.window, text="重置", command=self.resetButton)
        b1.place(relx=0, rely=0, x=495, y=150)

        b2 = Button(self.window, text="悔棋", command=self.BakcAChess)
        b2.place(relx=0, rely=0, x=495, y=200)

        b4 = Radiobutton(self.window, text="电脑执黑棋", variable=self.var, value=0, command=self.selectColor)
        b4.place(relx=0, rely=0, x=495, y=250)

        b5 = Radiobutton(self.window, text="电脑执白棋", variable=self.var, value=1, command=self.selectColor)
        b5.place(relx=0, rely=0, x=495, y=280)

        b6 = Button(self.window, text="打开棋谱", command=self.OpenFile)
        b6.place(relx=0, rely=0, x=495, y=400)

        b7 = Button(self.window, text="保存棋谱", command=self.SaveFile)
        b7.place(relx=0, rely=0, x=495, y=430)

        b8 = Radiobutton(self.window, text="用神经网络走", variable=self.var1, value=0, command=self.selectMathod)
        b8.place(relx=0, rely=0, x=490, y=320)

        b9 = Radiobutton(self.window, text="用普通规则走", variable=self.var1, value=1, command=self.selectMathod)
        b9.place(relx=0, rely=0, x=490, y=350)

        '''self.robotChess()
        self.draw_chess()'''

        self.can.bind("<Button-1>", lambda x: self.chess(x))
        self.window.mainloop()

class Robot():

    def __init__(self, _board):
        self.board = _board

    def haveValuePoints(self, player, enemy, board):
        points = []

        for x in range(15):
            for y in range(15):
                list1 = []
                list2 = []
                list3 = []
                list4 = []
                if self.board[x][y] == -1:
                    for tmp in range(9):
                        i = x + tmp - 4
                        j = y + tmp - 4
                        if i < 0 or i > 14:
                            list1.append(-2)
                        else:
                            list1.append(board[i][y])
                        if j < 0 or j > 14:
                            list2.append(-2)
                        else:
                            list2.append(board[x][j])
                        if i < 0 or j < 0 or i > 14 or j > 14:
                            list3.append(-2)
                        else:
                            list3.append(board[i][j])
                        k = y - tmp + 4
                        if i < 0 or k < 0 or i > 14 or k > 14:
                            list4.append(-2)
                        else:
                            list4.append(board[i][k])


                    playerValue = self.value_point(player, enemy, list1, list2, list3, list4)
                    enemyValue = self.value_point(enemy, player, list1, list2, list3, list4)
                    if enemyValue >= 10000:
                        enemyValue -= 500
                    elif enemyValue >= 5000:
                        enemyValue -= 300
                    elif enemyValue >= 2000:
                        enemyValue -= 250
                    elif enemyValue >= 1500:
                        enemyValue -= 200
                    elif enemyValue >= 99:
                        enemyValue -= 10
                    elif enemyValue >= 5:
                        enemyValue -= 1
                    value = playerValue + enemyValue
                    if value > 0:
                        points.append([x, y, value])
        return points

    def MaxValue_po(self, player, enemy):
        points = self.haveValuePoints(player, enemy, self.board)
        flag = 0
        _point = []
        for p in points:
            if p[2] > flag:
                _point = p
                flag = p[2]
        return _point[0], _point[1], _point[2]

    def value_point(self, player, enemy, list1, list2, list3, list4):
        flag = 0
        flag += self.willbefive(player, list1)
        flag += self.willbefive(player, list2)
        flag += self.willbefive(player, list3)
        flag += self.willbefive(player, list4)
        flag += self.willbealive4(player, list1)
        flag += self.willbealive4(player, list2)
        flag += self.willbealive4(player, list3)
        flag += self.willbealive4(player, list4)
        flag += self.willbesleep4(player, enemy, list1)
        flag += self.willbesleep4(player, enemy, list2)
        flag += self.willbesleep4(player, enemy, list3)
        flag += self.willbesleep4(player, enemy, list4)
        flag += self.willbealive3(player, list1)
        flag += self.willbealive3(player, list2)
        flag += self.willbealive3(player, list3)
        flag += self.willbealive3(player, list4)
        flag += self.willbesleep3(player, enemy, list1)
        flag += self.willbesleep3(player, enemy, list2)
        flag += self.willbesleep3(player, enemy, list3)
        flag += self.willbesleep3(player, enemy, list4)
        flag += self.willbealive2(player, enemy, list1)
        flag += self.willbealive2(player, enemy, list2)
        flag += self.willbealive2(player, enemy, list3)
        flag += self.willbealive2(player, enemy, list4)
        flag += self.willbesleep2(player, enemy, list1)
        flag += self.willbesleep2(player, enemy, list2)
        flag += self.willbesleep2(player, enemy, list3)
        flag += self.willbesleep2(player, enemy, list4)
        return flag

    def willbefive(self, player, checklist):
        if checklist[0] == player and checklist[1] == player and \
                checklist[2] == player and checklist[3] == player:
            return 10000
        elif checklist[5] == player and checklist[6] == player and \
                checklist[7] == player and checklist[8] == player:
            return 10000
        elif checklist[2] == player and checklist[3] == player and \
                checklist[5] == player and checklist[6] == player:
            return 10000
        elif checklist[1] == player and checklist[2] == player and \
                checklist[3] == player and checklist[5] == player:
            return 10000
        elif checklist[3] == player and checklist[5] == player and \
                checklist[6] == player and checklist[7] == player:
            return 10000
        else:
            return 0

    def willbealive4(self, player, checklist):
        if checklist[0] == -1 and checklist[1] == player and \
                checklist[2] == player and checklist[3] == player \
                and checklist[5] == -1:
            return 5000
        elif checklist[3] == -1 and checklist[5] == player and \
                checklist[6] == player and checklist[7] == player \
                and checklist[8] == -1:
            return 5000
        elif checklist[1] == -1 and checklist[2] == player and \
                checklist[3] == player and checklist[5] == player \
                and checklist[6] == -1:
            return 5000
        elif checklist[2] == -1 and checklist[3] == player and \
                checklist[5] == player and checklist[6] == player \
                and checklist[7] == -1:
            return 5000
        else:
            return 0

    def willbesleep4(self, player, enemy, checklist):
        if checklist[0] == enemy and checklist[1] == player and \
                checklist[2] == player and checklist[3] == player \
                and checklist[5] == -1:
            return 1700
        elif checklist[1] == enemy and checklist[2] == player and \
                checklist[3] == player and checklist[5] == player \
                and checklist[6] == -1:
            return 1700
        elif checklist[2] == enemy and checklist[3] == player and \
                checklist[5] == player and checklist[6] == player \
                and checklist[7] == -1:
            return 1700
        elif checklist[3] == enemy and checklist[5] == player and \
                checklist[6] == player and checklist[7] == player \
                and checklist[8] == -1:
            return 1700
        elif checklist[0] == -1 and checklist[1] == player and \
                checklist[2] == player and checklist[3] == player \
                and checklist[5] == enemy:
            return 1700
        elif checklist[1] == -1 and checklist[2] == player and \
                checklist[3] == player and checklist[5] == player \
                and checklist[6] == enemy:
            return 1700
        elif checklist[2] == -1 and checklist[3] == player and \
                checklist[5] == player and checklist[6] == player \
                and checklist[7] == enemy:
            return 1700
        elif checklist[3] == -1 and checklist[5] == player and \
                checklist[6] == player and checklist[7] == player \
                and checklist[8] == enemy:
            return 1700
        else:
            return 0

    def willbealive3(self, player, checklist):
        if checklist[0] == -1 and checklist[1] == -1 and \
                checklist[2] == player and checklist[3] == player \
                and checklist[5] == -1:
            return 1900
        elif checklist[1] == -1 and checklist[2] == -1 and \
                checklist[3] == player and checklist[5] == player \
                and checklist[6] == -1:
            return 1900
        elif checklist[2] == -1 and checklist[3] == -1 and \
                checklist[5] == player and checklist[6] == player \
                and checklist[7] == -1:
            return 1900
        elif checklist[1] == -1 and checklist[2] == player and \
                checklist[3] == player and checklist[5] == -1 \
                and checklist[6] == -1:
            return 1900
        elif checklist[2] == -1 and checklist[3] == player and \
                checklist[5] == player and checklist[6] == -1 \
                and checklist[7] == -1:
            return 1900
        elif checklist[3] == -1 and checklist[5] == player and \
                checklist[6] == player and checklist[7] == -1 \
                and checklist[8] == -1:
            return 1900
        elif checklist[0] == -1 and checklist[1] == player and \
                checklist[2] == player and checklist[3] == -1 \
                and checklist[5] == -1:
            return 1600
        elif checklist[2] == -1 and checklist[3] == player and \
                checklist[6] == player and checklist[5] == -1 \
                and checklist[7] == -1:
            return 1600
        elif checklist[3] == -1 and checklist[5] == player and \
                checklist[7] == player and checklist[6] == -1 \
                and checklist[8] == -1:
            return 1600
        elif checklist[3] == -1 and checklist[5] == -1 and \
                checklist[7] == player and checklist[6] == player \
                and checklist[8] == -1:
            return 1600
        elif checklist[0] == -1 and checklist[1] == player and \
                checklist[2] == player and checklist[3] == -1 \
                and checklist[6] == -1:
            return 1600
        elif checklist[0] == -1 and checklist[1] == player and \
                checklist[2] == player and checklist[3] == -1 \
                and checklist[6] == -1:
            return 1600
        else:
            return 0

    def willbesleep3(self, player, enemy, checklist):
        if checklist[1] == enemy and checklist[2] == player and \
                checklist[3] == player and checklist[5] == -1 \
                and checklist[6] == -1:
            return 350
        elif checklist[2] == enemy and checklist[3] == player and \
                checklist[5] == player and checklist[6] == -1 \
                and checklist[7] == -1:
            return 350
        elif checklist[3] == enemy and checklist[5] == player and \
                checklist[6] == player and checklist[7] == -1 \
                and checklist[8] == -1:
            return 350
        elif checklist[0] == -1 and checklist[1] == -1 and \
                checklist[2] == player and checklist[3] == player \
                and checklist[5] == enemy:
            return 350
        elif checklist[1] == -1 and checklist[2] == -1 and \
                checklist[3] == player and checklist[5] == player \
                and checklist[6] == enemy:
            return 350
        elif checklist[2] == -1 and checklist[3] == -1 and \
                checklist[5] == player and checklist[6] == player \
                and checklist[7] == enemy:
            return 350
        elif checklist[0] == enemy and checklist[1] == -1 and \
                checklist[2] == player and checklist[3] == player \
                and checklist[5] == -1 and checklist[6] == enemy:
            return 300
        elif checklist[1] == enemy and checklist[2] == -1 and \
                checklist[3] == player and checklist[5] == player \
                and checklist[6] == -1 and checklist[7] == enemy:
            return 300
        elif checklist[2] == enemy and checklist[3] == -1 and \
                checklist[5] == player and checklist[6] == player \
                and checklist[7] == -1 and checklist[8] == enemy:
            return 300
        elif checklist[0] == enemy and checklist[1] == player and \
                checklist[2] == -1 and checklist[3] == player \
                and checklist[5] == -1 and checklist[6] == enemy:
            return 300
        elif checklist[1] == enemy and checklist[2] == player and \
                checklist[3] == -1 and checklist[5] == player \
                and checklist[6] == -1 and checklist[7] == enemy:
            return 300
        elif checklist[2] == enemy and checklist[3] == player and \
                checklist[5] == -1 and checklist[6] == player \
                and checklist[7] == -1 and checklist[8] == enemy:
            return 300
        elif checklist[0] == enemy and checklist[1] == player and \
                checklist[2] == -1 and checklist[3] == player \
                and checklist[5] == -1 and checklist[6] == enemy:
            return 300
        elif checklist[1] == enemy and checklist[2] == player and \
                checklist[3] == -1 and checklist[5] == player \
                and checklist[6] == -1 and checklist[7] == enemy:
            return 300
        elif checklist[3] == enemy and checklist[5] == -1 and \
                checklist[6] == player and checklist[7] == player \
                and checklist[8] == -1 :
            return 300
        elif checklist[0] == enemy and checklist[1] == player and \
                checklist[2] == player and checklist[3] == -1 \
                and checklist[5] == -1 :
            return 300
        elif checklist[2] == enemy and checklist[3] == player and \
                checklist[5] == -1 and checklist[6] == player \
                and checklist[7] == -1 :
            return 300
        elif checklist[3] == enemy and checklist[5] == player and \
                checklist[6] == -1 and checklist[7] == player \
                and checklist[8] == -1 :
            return 300
        elif checklist[0] == player and checklist[1] == player and \
                checklist[2] == -1 and checklist[3] == -1 \
                and checklist[5] == enemy :
            return 300
        elif checklist[2] == enemy and checklist[3] == player and \
                checklist[5] == -1 and checklist[6] == -1 \
                and checklist[7] == player :
            return 300
        elif checklist[3] == enemy and checklist[5] == player and \
                checklist[6] == -1 and checklist[7] == -1 \
                and checklist[8] == player :
            return 300
        elif checklist[0] == player and checklist[1] == -1 and \
                checklist[2] == -1 and checklist[3] == player \
                and checklist[5] == enemy :
            return 300
        elif checklist[1] == player and checklist[2] == -1 and \
                checklist[3] == -1 and checklist[5] == player \
                and checklist[6] == enemy :
            return 300
        elif checklist[3] == enemy and checklist[5] == -1 and \
                checklist[6] == -1 and checklist[7] == player \
                and checklist[8] == player :
            return 300
        elif checklist[0] == -1 and checklist[1] == player and \
                checklist[2] == player and checklist[3] == -1 \
                and checklist[5] == enemy :
            return 300
        elif checklist[2] == -1 and checklist[3]== player and \
                checklist[5] == -1 and checklist[6] == player \
                and checklist[7] == enemy :
            return 300
        elif checklist[3] == -1 and checklist[5] == player and \
                checklist[6] == -1 and checklist[7] == player \
                and checklist[8] == enemy :
            return 300
        elif checklist[0] == -1 and checklist[1] == player and \
                checklist[2] == -1 and checklist[3] == player \
                and checklist[5] == enemy :
            return 300
        elif checklist[1] == -1 and checklist[2] == player and \
                checklist[3] == -1 and checklist[5] == player \
                and checklist[6] == enemy :
            return 300
        elif checklist[3] == -1 and checklist[5] == -1 and \
                checklist[6] == player and checklist[7] == player \
                and checklist[8] == enemy :
            return 300
        elif checklist[0] == player and checklist[1] == -1 and \
                checklist[2] == player and checklist[3] == -1 \
                and checklist[5] == enemy :
            return 300
        elif checklist[1] == enemy and checklist[2] == player and \
                checklist[3] == -1 and checklist[5] == -1 \
                and checklist[6] == player :
            return 300
        elif checklist[2] == player and checklist[3] == -1 and \
                checklist[5]== -1 and checklist[6] == player \
                and checklist[7] == enemy:
            return 300
        elif checklist[3] == enemy and checklist[5] == -1 and \
                checklist[6] == player and checklist[7] == -1 \
                and checklist[8] == player :
            return 300
        else:return 0

    def willbealive2(self, player, enemy, checklist):
        if checklist[1] == -1 and checklist[2] == -1 and \
                checklist[3] == player and checklist[5] == -1 \
                and checklist[6] == -1:
            return 99
        elif checklist[2] == -1 and checklist[3] == -1 and \
                checklist[5] == player and checklist[6] == -1 \
                and checklist[7] == -1:
            return 99
        elif checklist[0] == -1 and checklist[1] == -1 and \
                checklist[2] == -1 and checklist[3] == player \
                and checklist[5] == -1 and checklist[6] == enemy:
            return 99
        elif checklist[1] == -1 and checklist[2] == -1 and \
                checklist[3] == -1 and checklist[5] == player \
                and checklist[6] == -1 and checklist[7] == enemy:
            return 99
        elif checklist[1] == enemy and checklist[2] == -1 and \
                checklist[3] == player and checklist[5] == -1 \
                and checklist[6] == -1 and checklist[7] == -1:
            return 99
        elif checklist[2] == enemy and checklist[3] == -1 and \
                checklist[5] == player and checklist[6] == -1 \
                and checklist[7] == -1 and checklist[8] == -1:
            return 99
        else:return 0

    def willbesleep2(self, player, enemy, checklist):
        if checklist[2] == enemy and checklist[3] == player and \
                checklist[5] == -1 and checklist[6] == -1 \
                and checklist[7] == -1:
            return 5
        elif checklist[3] == enemy and checklist[5] == player and \
                checklist[6] == -1 and checklist[7] == -1 \
                and checklist[8] == -1:
            return 5
        elif checklist[0] == -1 and checklist[1] == -1 and \
                checklist[2] == -1 and checklist[3] == player \
                and checklist[5] == enemy:
            return 5
        elif checklist[1] == -1 and checklist[2] == -1 and \
                checklist[3] == -1 and checklist[5] == player \
                and checklist[6] == enemy:
            return 5
        elif checklist[1] == enemy and checklist[2] == -1 and \
                checklist[3] == player and checklist[5] == -1 \
                and checklist[6] == -1 and checklist[7] == enemy:
            return 5
        elif checklist[2] == enemy and checklist[3] == -1 and \
                checklist[5] == player and checklist[6] == -1 \
                and checklist[7] == -1 and checklist[8] == enemy:
            return 5
        elif checklist[0] == enemy and checklist[1] == -1 and \
                checklist[2] == player and checklist[3] == -1 \
                and checklist[5] == -1 and checklist[6] == enemy:
            return 5
        elif checklist[2] == enemy and checklist[3] == -1 and \
                checklist[5] == -1 and checklist[6] == player \
                and checklist[7] == -1 and checklist[8] == enemy:
            return 5
        elif checklist[0] == enemy and checklist[1] == -1 and \
                checklist[2] == -1 and checklist[3] == player \
                and checklist[5] == -1 and checklist[6] == enemy:
            return 5
        elif checklist[1] == enemy and checklist[2] == -1 and \
                checklist[3] == -1 and checklist[5] == player \
                and checklist[6] == -1 and checklist[7] == enemy:
            return 5
        elif checklist[0] == -1 and checklist[1] == player and \
                checklist[2] == -1 and checklist[3] == -1 \
                and checklist[5] == enemy:
            return 5
        elif checklist[3] == -1 and checklist[5] == -1 and \
                checklist[6] == -1 and checklist[7] == player \
                and checklist[8] == enemy:
            return 5
        elif checklist[0] == -1 and checklist[1] == -1 and \
                checklist[2] == player and checklist[3] == -1 \
                and checklist[5] == enemy:
            return 5
        elif checklist[2] == -1 and checklist[3] == -1 and \
                checklist[5] == -1 and checklist[6] == player \
                and checklist[7] == enemy:
            return 5
        elif checklist[1] == enemy and checklist[2] == player and \
                checklist[3] == -1 and checklist[5] == -1 \
                and checklist[6] == -1:
            return 5
        elif checklist[3] == enemy and checklist[5] == -1 and \
                checklist[6] == player and checklist[7] == -1 \
                and checklist[8] == -1:
            return 5
        elif checklist[0] == enemy and checklist[1] == player and \
                checklist[2] == -1 and checklist[3] == -1 \
                and checklist[5] == -1:
            return 5
        elif checklist[3] == enemy and checklist[5] == -1 and \
                checklist[6] == -1 and checklist[7] == player \
                and checklist[8] == -1:
            return 5
        else:
            return 0

if __name__ == '__main__':
    game = GoBang()
    game.start()
    del game
