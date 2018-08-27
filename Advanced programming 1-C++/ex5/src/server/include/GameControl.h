//
// Created by ori on 12/27/17.
//

#ifndef ADVANCEDPROGRAMMING1_COMMANDMANAGER_H
#define ADVANCEDPROGRAMMING1_COMMANDMANAGER_H
#include <vector>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <string>
#include <iostream>
#include <stdio.h>
#include <map>
using namespace std;


class GameControl {
public:
    GameControl();

    /**
     * @param gameName
     * @param gameSocket
     * @return true if added new game, false if didn't add new game (there's already
     * game with this name)
     */
    bool AddGame(string gameName, int gameSocket);
    /**
     * @param gameName
     * @return the socket of the given game
     */
    int GetGameSocket(string gameName);
    /**
     * @return vector holds the gameNames
     */
    vector<string> ListOfGamesNames();
    /**
     * @param player adds the given player to connectedPlayers vector
     */
    void AddPlayer(int player);
    /**
     * @param player removes the given player to connectedPlayers vector
     */
    void DeletePlayer(int player);
    /**
     * Disconnects (Close) all the player in connectedPlayers vector
     */
    void CloseAllPlayers();
    /**
     * @param gameName
     * @return true if successfully closed the game, false otherwise
     */
    bool RemoveGame(string gameName);

private:
    vector<int> connectedPlayers;
    map<string, int> nameToGameMap;
    pthread_mutex_t mutex;

};


#endif //ADVANCEDPROGRAMMING1_COMMANDMANAGER_H
