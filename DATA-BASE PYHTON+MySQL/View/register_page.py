from tkinter import *
from View import login_page, preferences_page
from Logic import GameLogic as gL


def register_window(window, fileBackground):
    for widget in window.winfo_children():
        widget.destroy()
    background_label = Label(window, image=fileBackground)
    background_label.place(x=0, y=0, relwidth=1, relheight=1)
    frame = Frame(window)
    frame.grid(row=1, column=1)

    frame.grid_columnconfigure(0, weight=1)
    frame.grid_rowconfigure(0, weight=1)

    name = Label(frame, text='Register', fg='black', font='Ariel 16 bold')
    name.grid(row=0, columnspan=2, pady=(10, 10))

    nameL = Label(frame, text='Username: ')  # More labels
    pwordL = Label(frame, text='Password: ')  # ^
    nameL.grid(row=1, column=0, padx=(10, 0))
    pwordL.grid(row=2, column=0, padx=(10, 0))

    username = StringVar()
    password = StringVar()
    nameEL = Entry(frame, textvariable=username)  # The entry input
    pwordEL = Entry(frame, show='*', textvariable=password)
    nameEL.grid(row=1, column=1, padx=(0, 10))
    pwordEL.grid(row=2, column=1, padx=(0, 10))

    bottonSend = Button(frame, text='Register', bg="green", fg="black", font='Ariel 8 bold',
                        command=lambda: validateRegister(window, frame, username, password))
    bottonSend.grid(row=3, columnspan=2, pady=(10, 5))

    bottonLogin = Button(frame, text='Login Page', fg="black", font='Ariel 8 bold',
                         command=lambda: login_page.login_window(window, fileBackground))
    bottonLogin.grid(row=4, columnspan=2, pady=(0, 10))


def validateRegister(window, frame, name, pword):
    print(name.get(), pword.get())
    validateR = gL.register(name.get(), pword.get())
    if validateR == 0:
        label = Label(frame, text='User Exists!', fg='red', font='Ariel 8 bold')
        label.grid(row=5, columnspan=2, pady=(10, 10))
    else:
        preferences_page.preference_window(window, name)
