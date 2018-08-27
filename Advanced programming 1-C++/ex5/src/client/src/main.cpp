/**
# Ori Cohen
# Yana Patyuk
 */

#define BOARD_ROWS 8
#define BOARD_COLS 8

#include <iostream>
#include <limits>
#include <fstream>
#include <algorithm>
#include <string>
#include "../include/Board.h"
#include <sstream>

#include "../include/RegularLogic.h"
#include "../include/GameFlow.h"
#include "../include/TerminalPlayer.h"
#include "../include/AiPlayer.h"
#include "../include/PlayerInterface.h"
#include "../include/HostPlayer.h"

void ReadIPAndPortFromFile(string file, int *port, string *ip);

using namespace std;

int main() {
    // initialize new board, create the objects required for
    // game flow to run and run it.
    Board *b = new Board(BOARD_ROWS, BOARD_COLS);
    RegularLogic rl = RegularLogic(b);
    PlayerInterface *p1 = NULL;
    PlayerInterface *p2 = NULL;
    HostPlayer *p3 = NULL;
    string ip;
    int port;
    string name;
    short userChoice = 0;
    bool correct = false;
    bool server = false;

    cout << "Let The Game Begin!" << endl;
    cout << "May the odds be ever in your favor\n" << endl;
    do {
        //ask user against who he wants to play.
        cout << "Main Menu:" << endl;
        cout << "Choose against who to play\n1-Human local player" << endl;
        cout << "2-AI player" << endl;
        cout << "3-Remote player" << endl;
        cin >> userChoice;
        //check which option the user choose.
        switch (userChoice) {
            case -1: //secret choice-exit.
                cout << "This is a secret ending game. Goodbye :)" << endl;
                delete b;
                return 0;

            case 1://play against human local player.
                correct = true;
                server = false;
                p1 = new TerminalPlayer(EnumDeclration::X);
                p2 = new TerminalPlayer(EnumDeclration::O);
                break;

            case 2://play against AI
                correct = true;
                server = false;
                p1 = new TerminalPlayer(EnumDeclration::X);
                p2 = new AiPlayer(EnumDeclration::O);
                break;

            case 3://play against server.-open connection with server first;
                //get ip and port from file.
                ReadIPAndPortFromFile("hostInfo.txt", &port, &ip);
                //create host player.
                p3 = new HostPlayer(ip.c_str(), port);
                /**
              * this try-catch used for any disconnections with server.
              * if something went wrong we go back to menu.
                 */
                try {
                    p3->ConnectToServer();
                    p1 = p3;
                    correct = true;
                    server = true;
                    //we exit loop only after correct choice.
                    while (true) {
                        try {//if server disconnected at this part we go back to menu and
                            //print user the error.

                            //ask user what he wants to do.
                            //note: in this part users choice is a number for convince.
                            //when sending command to server we manually add the "command <gameName>".
                            cout << "What would you like to do?\n1-Start new game" << endl;
                            cout << "2-Get a list of all open games" << endl;
                            cout << "3-Join specific game" << endl;
                            cout << "0-Back to Main Menu" << endl;
                            int command = 0;
                            cin >> command;
                            //check users input. after each operation go back to main loop.
                            switch (command) {
                                case 1: //start a new game-ask for game name.
                                    cout << "Name your game:\n";
                                    cin.clear();
                                    std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
                                    cin >> name;
                                    //send server start command.
                                    correct = p3->SendStart(name);
                                    break;

                                case 2://print list of games
                                    //send server list command.
                                    p3->PrintGamesList();
                                    correct = false;
                                    server = false;
                                    break;

                                case 3://connect to a game
                                    cout << "Enter game's name you would like to connect:" << endl;
                                    cin.clear();
                                    std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
                                    cin >> name;
                                    correct = p3->JoinGame(name);
                                    break;

                                case 0: //back to Main Menu
                                    correct = false;
                                    break;

                                default://for any other input.
                                    cout << "Illegal input. integers only!" << endl;
                                    cin.clear();
                                    std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
                                    continue;
                            }
                            //if their was a problem with sending request go back to main menu;
                            if (!correct) break;
                            //get symbols of player. and create a human player to work with.
                            p3->GetPlayerSymbolFromServer();
                            p2 = new TerminalPlayer(EnumDeclration::OtherPlayer(p3->GetEnumSymbol()));
                            break;
                        } catch (const char *msg) { //if problem occurred for some reason
                            cout << msg << endl;
                            correct = false;
                            server = false;
                            break;
                        }
                    }
                } catch (const char *msg) {
                    cout << "Failed to connect to server. Reason:" << msg << endl;
                    server = false;
                    continue;
                }
                break;
            default:
                cout << "Illegal input entered or there isn't such option" <<
                     "(not in rage 1-3). try again" << endl;
                cin.clear();
                std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
        }
    } while (!correct); // run until users input is correct.
    cin.ignore();
    GameFlow gameFlow(&rl, b, p1, p2, server);
    gameFlow.Run();

    //delete members-free memory.
    delete b;
    delete p2;
    delete p1;
    return 0;
}

void ReadIPAndPortFromFile(string file, int *port, string *ip) {
    ifstream hostInfo(file.c_str());
    string line;
    if (hostInfo.is_open()) {
        // break the first line into IP
        // break the second line to Port No.
        string delimiter = ":";
        getline(hostInfo, line);
        string token = line.substr(line.find(delimiter) + 1, line.length());
        std::stringstream stream(token);
        stream >> token;
        *ip = token; // save as ip
        getline(hostInfo, line);
        token = line.substr(line.find(delimiter) + 1, line.length());
        std::istringstream(token) >> *port; //convert to int and save as port
        hostInfo.close();
    } else {
        cout << "Unable to open file" << endl;
    }
}
