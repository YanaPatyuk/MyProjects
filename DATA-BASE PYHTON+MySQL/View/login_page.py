from tkinter import *
from View import start_menu, register_page, preferences_page
from Logic import GameLogic as gL


def login_window(window, fileBackground):
    background_label = Label(window, image=fileBackground)
    background_label.place(x=0, y=0, relwidth=1, relheight=1)
    frame = Frame(window)
    frame.grid(row=1, column=1)

    frame.grid_columnconfigure(0, weight=1)
    frame.grid_rowconfigure(0, weight=1)

    name = Label(frame, text='Login', fg='black', font='Ariel 16 bold')
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

    bottonSend = Button(frame, text='login', bg="green", fg="black", font='Ariel 8 bold',
                        command=lambda: validateLogin(window, frame, username, password))
    bottonSend.grid(row=3, columnspan=2, pady=(10, 5))

    bottonRegister = Button(frame, text='Register Page', fg="black", font='Ariel 8 bold',
                            command=lambda: register_page.register_window(window, fileBackground))
    bottonRegister.grid(row=4, columnspan=2, pady=(0, 10))


def validateLogin(window, frame, name, pword):
    print(name.get(), pword.get())
    status = gL.login(name.get(), pword.get())
    if status == -1:
        Need = Label(frame, text='User does not exist', fg='red', font='Ariel 8 bold')
        Need.grid(row=5, columnspan=2, pady=(10, 10))
    elif status == 0:
        preferences_page.preference_window(window, name)
    else:
        start_menu.start_menu_window(window, name)
