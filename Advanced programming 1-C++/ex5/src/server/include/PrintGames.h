/*
 * PrintGames.h
 *
 *  Created on: 27 בדצמ׳ 2017
 *      Author: yanap
 */

#ifndef SERVER_INCLUDE_PRINTGAMES_H_
#define SERVER_INCLUDE_PRINTGAMES_H_
#include <map>

#include "CommandProtocol.h"
#include "GameControl.h"

class PrintGames: public CommandProtocol {
public:
	PrintGames(GameControl* gameControl);
	/**
	 * @param args will be list of open games.
	 * first vector is number of player's socket.
	 * send to player the list.
	 */
	/**
	 * prints a list of the open games to given player his info is stored at CommandInfo info struct)
	 * @param info
	 */
	virtual void Execute(struct CommandInfo info);
	virtual ~PrintGames();
private:
    GameControl* gameControl;
};

#endif /* SERVER_INCLUDE_PRINTGAMES_H_ */
