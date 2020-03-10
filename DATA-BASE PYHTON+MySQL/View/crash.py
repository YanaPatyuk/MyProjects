from tkinter import *


def crash_window(textt = "crash"):
    window = Tk()
    window.title("crash")
    window.geometry("300x300")
    window.minsize(width=300, height=300)
    window.grid_columnconfigure(1, weight=1)
    window.grid_rowconfigure(1, weight=1)

    frame = Frame(window)
    frame.grid(row=1, column=1)

    frame.grid_columnconfigure(0, weight=1)
    frame.grid_rowconfigure(0, weight=1)

    name = Label(frame, text=textt, fg='red', font='Ariel 16 bold')
    name.grid(row=0, column=0, pady=(10, 10))
    window.mainloop()
