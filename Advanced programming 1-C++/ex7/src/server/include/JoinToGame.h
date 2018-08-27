/*
 * JoinToGame.h
 *
 *  Created on: 24 בדצמ׳ 2017
 *      Author: yanap
 */

#ifndef SERVER_INCLUDE_JOINTOGAME_H_
#define SERVER_INCLUDE_JOINTOGAME_H_

#include "CommandProtocol.h"
#include "GameControl.h"

#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <string.h>
#include <iostream>
#include <stdio.h>

class JoinToGame : public CommandProtocol {
public:
    JoinToGame(GameControl* gameControl);

    /**
     * Connects the given player (his info is stored at CommandInfo info struct)
     * to other player and starting the game, sending moves trough the server etc.
     * @param info
     */
    virtual void Execute(struct CommandInfo info);

    virtual ~JoinToGame();

private:
    GameControl *gameControl;

};

#endif /* SERVER_INCLUDE_JOINTOGAME_H_ */
