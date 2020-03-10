import threading
from tkinter import *
from tkinter.ttk import Progressbar

from View import start_menu
import Logic.GameLogic as gL

preferences_dict = ""


def bar(progress, frame):
    global preferences_dict
    import time
    i = 0
    flag = 0
    while True:
        if preferences_dict == "":
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


def getpre():
    global preferences_dict
    preferences_dict = gL.get_all_preferences()


def preference_window(window, name):
    global preferences_dict

    for widget in window.winfo_children():
        widget.destroy()
    frame = Frame(window)
    frame.grid(row=1, column=1)

    frame.grid_columnconfigure(0, weight=1)
    frame.grid_rowconfigure(0, weight=1)

    # get from alon the preference dictionary
    t = threading.Thread(target=getpre)
    t.start()

    message1 = Label(frame, text='Please Wait...', fg='black', font='Ariel 16 bold')
    message1.grid(row=0, column=0, pady=(5, 5))
    progress = Progressbar(frame, orient=HORIZONTAL, length=100, mode='indeterminate')
    progress.grid(row=1, column=0, pady=(5, 5))
    bar(progress, frame)

    listt = frame.grid_slaves()

    for l in listt:
        l.destroy()

    label = Label(frame, text='Preference manu', fg='black', font='Ariel 16 bold')
    label.grid(row=0, columnspan=7, pady=(10, 10))

    choice_dic = {}
    for preference in list(preferences_dict.keys()):
        choice_dic[preference] = []
        i = 0
        for choice in preferences_dict[preference]:
            var = IntVar()
            choice_dic[preference].append(var)
            i += 1
    rowindex = 1
    colindex = 0

    for preference in list(preferences_dict.keys()):
        pre_name = Label(frame, text='choose: ' + preference, fg='black', font='Ariel 16 bold')
        pre_name.grid(row=rowindex, columnspan=7, pady=(10, 10))
        rowindex += 1
        i = 0
        for text in preferences_dict[preference]:
            b = Checkbutton(frame, text=text,
                            variable=choice_dic[preference][i], offvalue="L")
            b.grid(row=rowindex, column=colindex, pady=(10, 10))
            if colindex < 6:
                colindex += 1
            else:
                colindex = 0
                rowindex += 1
            i += 1
        rowindex += 1
        colindex = 0

    bottonSend = Button(frame, text='Continue', bg="green", fg="black", font='Ariel 8 bold',
                        command=lambda: preference_button(window, preferences_dict, choice_dic, name))
    bottonSend.grid(row=rowindex, columnspan=7, pady=(10, 5))


def preference_button(window, pre_dictionary, choice_dic, name):
    return_dictionary = {}
    for preference in list(choice_dic.keys()):
        return_dictionary[preference] = []
        i = 0
        for choice in choice_dic[preference]:
            if choice.get() == 1:
                return_dictionary[preference].append(pre_dictionary[preference][i])
            i += 1
    gL.add_preferences_to_user(name.get(), return_dictionary)
    start_menu.start_menu_window(window, name)
