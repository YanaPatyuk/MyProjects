from tkinter import *
from View import easy_game_page, challenging_game_page, hard_game_page
from View import start_menu


def game_menu_window(window, Gamer_name):
    for widget in window.winfo_children():
        widget.destroy()
    frame = Frame(window)
    frame.grid(row=1, column=1)

    frame.grid_columnconfigure(0, weight=1)
    frame.grid_rowconfigure(0, weight=1)

    name = Label(frame, text='The memory game of a funny name', fg='black', font='Ariel 16 bold')
    name.grid(row=0, column=0, pady=(10, 10))

    bottonEasy = Button(frame, text='Easy Game', bg="blue", width= 20, fg="white", font='Ariel 12 bold',
                        command=lambda: easy(window, Gamer_name))
    bottonEasy.grid(row=1, column=0, pady=(10, 5))

    bottonHard = Button(frame, text='Hard Game', bg="blue", width= 20, fg="white", font='Ariel 12 bold',
                        command=lambda: hard(window, Gamer_name))
    bottonHard.grid(row=2, column=0, pady=(10, 5))

    bottonChallenging = Button(frame, text='Challenging Game', width= 20, bg="blue", fg="white", font='Ariel 12 bold',
                        command=lambda: challenging(window, Gamer_name))
    bottonChallenging.grid(row=3, column=0, pady=(10, 5))

    bottonEasy = Button(frame, text='Go back', bg="green", width= 20, fg="white", font='Ariel 12 bold',
                        command=lambda: start(window, Gamer_name))
    bottonEasy.grid(row=4, column=0, pady=(5, 5))


def start(window, Gamer_name):
    start_menu.start_menu_window(window, Gamer_name)


def easy(window, Gamer_name):
    easy_game_page.easy_game_window(window, Gamer_name)


def hard(window, Gamer_name):
    hard_game_page.hard_game_window(window, Gamer_name)


def challenging(window, Gamer_name):
    challenging_game_page.challenging_game_window(window, Gamer_name)
