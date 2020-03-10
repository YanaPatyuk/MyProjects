from tkinter import *
from View import game_manu, leaderboard_page


def start_menu_window(window, Gamer_name):
    for widget in window.winfo_children():
        widget.destroy()
    frame = Frame(window)
    frame.grid(row=1, column=1)

    frame.grid_columnconfigure(0, weight=1)
    frame.grid_rowconfigure(0, weight=1)

    name = Label(frame, text='The memory game of a funny name', fg='black', font='Ariel 16 bold')
    name.grid(row=0, column=0, pady=(10, 10))

    bottonEasy = Button(frame, text='Start Game', bg="blue", width= 20, fg="white", font='Ariel 12 bold',
                        command=lambda: start(window, Gamer_name))
    bottonEasy.grid(row=1, column=0, pady=(10, 5))

    bottonHard = Button(frame, text='Leaderboard', bg="blue", width= 20, fg="white", font='Ariel 12 bold',
                        command=lambda: leaderboard(window, Gamer_name))
    bottonHard.grid(row=2, column=0, pady=(10, 5))


def start(window, Gamer_name):
    game_manu.game_menu_window(window, Gamer_name)


def leaderboard(window, Gamer_name):
    leaderboard_page.leaderboard_window(window, Gamer_name)

