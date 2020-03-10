import threading
from tkinter import *
from tkinter import ttk
from tkinter.ttk import Treeview, Progressbar
from Logic import GameLogic as gL

from View import start_menu

leaderboard = ""


def bar(progress, frame):
    global leaderboard
    import time
    i = 0
    flag = 0
    while True:
        if leaderboard == "":
            progress['value'] = i
            if i < 100 and flag == 0:
                i += 20
            else:
                flag = 1
                i -= 20
            if i == 0:
                flag = 0
            frame.update()
            time.sleep(1)
        else:
            break


def getleaderboard():
    global leaderboard
    leaderboard = gL.get_leaderboard()


def leaderboard_window(window, Gamer_name):
    global leaderboard
    for widget in window.winfo_children():
        widget.destroy()
    frame = Frame(window)
    frame.grid(row=1, column=1)

    frame.grid_columnconfigure(0, weight=1)
    frame.grid_rowconfigure(0, weight=1)

    t = threading.Thread(target=getleaderboard)
    t.start()
    message1 = Label(frame, text='Please Wait...', fg='black', font='Ariel 16 bold')
    message1.grid(row=0, column=0, pady=(5, 5))
    progress = Progressbar(frame, orient=HORIZONTAL, length=100, mode='indeterminate')
    progress.grid(row=1, column=0, pady=(5, 5))
    bar(progress, frame)
    listt = frame.grid_slaves()
    for l in listt:
        l.destroy()

    name = Label(frame, text='Leaderboard', fg='black', font='Ariel 16 bold')
    name.grid(row=0, column=0, pady=(10, 10))

    i = 1
    for key in leaderboard.keys():
        i += 1
        name1 = Label(frame, text=key, fg='black', font='Ariel 10 bold')
        name1.grid(row=i, column=0, pady=(5, 5))
        tv = Treeview(frame, height=len(leaderboard[key])-1)
        tv['columns'] = (leaderboard[key][0][1])
        tv.heading("#0", text=leaderboard[key][0][0])
        tv.column("#0", anchor="center", width=100)
        tv.heading(leaderboard[key][0][1], text=leaderboard[key][0][1])
        tv.column(leaderboard[key][0][1], anchor='center', width=100)
        tv.grid(sticky=(N, S))
        treeview = tv
        for j in range(len(leaderboard[key])-1):
            j += 1
            treeview.insert('', 'end', text=leaderboard[key][j][0], values=(leaderboard[key][j][1]))
        i += 1
        treeview.grid(row=i, column=0, pady=(10, 10))

    bottonEasy = Button(frame, text='Go back', bg="green", fg="white", font='Ariel 12 bold',
                        command=lambda: start(window, Gamer_name))
    bottonEasy.grid(row=i+1, column=0, pady=(5, 5))


def start(window, Gamer_name):
    start_menu.start_menu_window(window, Gamer_name)

