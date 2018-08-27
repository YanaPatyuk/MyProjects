/*
 * JoinToGame.cpp
 *
 *  Created on: 24 בדצמ׳ 2017
 *      Author: yanap
 */

#include "../include/JoinToGame.h"

#define SUCCESS 0
#define FAILURE -1
#define PLAYER_X 1
#define PLAYER_O 2

JoinToGame::JoinToGame(GameControl *gameControl) {
    this->gameControl = gameControl;
}

JoinToGame::~JoinToGame() {
}

void JoinToGame::Execute(struct CommandInfo info) {
    int otherPlayerSocket;
    int getGameSocketStatus;
    /* try to GetGameSocket(info.gameName) - try to join the game,
     * if successfully did that, write our client SUCCESS "0" as getGameSocketStatus
     * if failed write our client "-1" = failure */
    try {
        getGameSocketStatus = SUCCESS;
        otherPlayerSocket = this->gameControl->GetGameSocket(info.gameName);
        int n = write(info.clientSocket, &getGameSocketStatus, sizeof(getGameSocketStatus));
        if (n == -1) {
            cout << "Error writing to socket" << endl;
            return;
        } else if (n == 0) {
            cout << "Client disconnected" << endl;
            return;
        }
    }
    catch (char const *e) {
        getGameSocketStatus = FAILURE;
        int n = write(info.clientSocket, &getGameSocketStatus, sizeof(getGameSocketStatus));
        if (n == -1) {
            cout << "Error writing to socket" << endl;
            return;
        } else if (n == 0) {
            cout << "Client disconnected" << endl;
            return;
        }
        // disconnect the client from the server
        gameControl->DeletePlayer(otherPlayerSocket);
        close(info.clientSocket);
        return;
    }

    // Write 1/2 to the player in order to later determine which one is the first and second (X/O)
    int playerMode = PLAYER_X;
    int n = write(otherPlayerSocket, &playerMode, sizeof(playerMode));
    if (n == -1 || n == 0) {
        cout << "Error writing to socket Or Client disconnected" << endl;
        return;
    }
    playerMode = PLAYER_O;
    n = write(info.clientSocket, &playerMode, sizeof(playerMode));
    if (n == -1 || n == 0) {
        cout << "Error writing to socket Or Client disconnected" << endl;
        return;
    }

    // Remove the game from the NameToGameMap
    if (this->gameControl->RemoveGame(info.gameName)) {
        // successfully removed the game - so there is no way 3 clients will try to connect to the same game
        // because the 3rd one was an idiot
    } else {
        throw "Couldn't Remove the game because couldn't find such game";
    }

    // Read the plays from each client until game is over
    int player[] = {otherPlayerSocket, info.clientSocket};
    int turnCounter = 0;
    char buffer[50];
    char *massage = buffer;
    while (true) {
        memset(buffer, 0, 50);//empty the values.
        // Read massage from player.
        n = read(player[turnCounter % 2], massage, sizeof(massage));
        if (n == -1) {
            cout << "Error reading point" << endl;
            return;
        } else if (n == 0) {
            cout << "Client disconnected" << endl;
            return;
        }
        // Write the massage back to the other player
        n = write(player[(turnCounter + 1) % 2], massage, sizeof(massage));
        if (n == -1) {
            cout << "Error writing to socket" << endl;
            return;
        } else if (n == 0) {
            cout << "Client disconnected" << endl;
            return;
        }
        turnCounter++;
        ////////cout << "this is the move " << massage << endl;
        //if the message is Close- delete & close connections with players.
        if (strcmp(massage, "Close") == 0) {
            gameControl->DeletePlayer(otherPlayerSocket);
            gameControl->DeletePlayer(info.clientSocket);
            close(player[0]);
            close(player[1]);
            return;
        }
    }
}
