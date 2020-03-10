import threading
from tkinter import *
from tkinter.ttk import Progressbar

from View import login_page
import Queries

serverRun = 0


def bar(progress, frame):
    global serverRun
    import time
    i = 0
    flag = 0
    while True:
        if serverRun == 0:
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

def run():
    window = Tk()
    window.title("The memory game of a funny name")
    window.geometry("800x600")
    window.minsize(width=800, height=600)
    window.grid_columnconfigure(1, weight=1)
    window.grid_rowconfigure(1, weight=1)

    backgroundName = sys.path[0] + '\\background.png'
    fileBackground = PhotoImage(file=backgroundName)

    frame = Frame(window)
    frame.grid(row=1, column=1)
    frame.grid_columnconfigure(0, weight=1)
    frame.grid_rowconfigure(0, weight=1)
    message1 = Label(frame, text='Please Wait...', fg='black', font='Ariel 16 bold')
    message1.grid(row=0, column=0, pady=(5, 5))
    progress = Progressbar(frame, orient=HORIZONTAL, length=100, mode='indeterminate')
    progress.grid(row=1, column=0, pady=(5, 5))
    bar(progress, frame)

    login_page.login_window(window, fileBackground)
    window.mainloop()


def getQueries():
    global serverRun
    Queries.run()
    serverRun = 1


if __name__ == "__main__":
    t = threading.Thread(target=getQueries)
    t.start()
    run()


