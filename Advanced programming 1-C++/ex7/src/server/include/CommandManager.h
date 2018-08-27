/*
 * GameControll.h
 *
 *  Created on: 26 בדצמ׳ 2017
 *      Author: yanap
 */

#ifndef SERVER_INCLUDE_GAMECONTROL_H_
#define SERVER_INCLUDE_GAMECONTROL_H_

#include <iostream>
#include <map>
#include <string>
#include <sstream>
#include "CommandProtocol.h"
#include "StartNewGame.h"
#include "JoinToGame.h"
#include "PrintGames.h"

using namespace std;

class CommandManager {
public:
    /**
     * constructor.
     * @param server which will connect between clients.
     */
    CommandManager(GameControl *game);

    virtual ~CommandManager();

    /**
     * start a command, return true if found one otherwise false
     */
    bool ExecuteCommand(string command, CommandInfo args);

    /**
     * add the socket to gameControl
     * @param socketNumber
     */
    void AddPlayerSocket(int socketNumber);

    /**
     * stop all players and close them.
     */
    void End();

private:
    map<string, int> listGamesAndSockets;
    GameControl *gameControl;
    map<string, CommandProtocol *> commandsMap;
};


#endif /* SERVER_INCLUDE_GAMECONTROL_H_ */
