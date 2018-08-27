/*
 * StartNewGame.cpp
 *
 *  Created on: 24 בדצמ׳ 2017
 *      Author: yanap
 */

#include "../include/StartNewGame.h"
#define SUCCESS 0
#define FAILURE -1

StartNewGame::StartNewGame(GameControl* gameControl) {
    this->gameControl = gameControl;
}

StartNewGame::~StartNewGame() {
}

void StartNewGame::Execute(struct CommandInfo info) {
    // if successfully added the game
	int addedTheGameStatus;
    if(this->gameControl->AddGame(info.gameName, info.clientSocket)){
    	addedTheGameStatus = SUCCESS;
        int n = write(info.clientSocket, &addedTheGameStatus, sizeof(addedTheGameStatus));
        if (n == -1) {
            throw "Error writing to socket";
        }
        return;
    }
    // failed to add the game, delete and disconnect client
    addedTheGameStatus = FAILURE;
    int n = write(info.clientSocket, &addedTheGameStatus, sizeof(addedTheGameStatus));
    gameControl->DeletePlayer(info.clientSocket);
    close(info.clientSocket);
    if (n == -1) {
        throw "Error writing to socket";
    }
}

