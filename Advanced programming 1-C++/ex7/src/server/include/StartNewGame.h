/*
 * StartNewGame.h
 *
 *  Created on: 24 בדצמ׳ 2017
 *      Author: yanap
 */

#ifndef SERVER_INCLUDE_STARTNEWGAME_H_
#define SERVER_INCLUDE_STARTNEWGAME_H_

#include "CommandProtocol.h"
#include "GameControl.h"


class StartNewGame : public CommandProtocol {
public:
    StartNewGame(GameControl *gameControl);
    /**
     * Start (open) new game with given name and player (info needed (playerSocket and name is stored at CommandInfo)
     * @param info
     */
    virtual void Execute(struct CommandInfo info);

    virtual ~StartNewGame();

private:
    GameControl *gameControl;

};

#endif /* SERVER_INCLUDE_STARTNEWGAME_H_ */
