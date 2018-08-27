//
// Created by ori on 12/27/17.
//

#include "../include/GameControl.h"


GameControl::GameControl() {
    /*pthread_mutex_lock(&this->mutex);
     * if some data is added here in future mutex has to be used
    pthread_mutex_unlock(&this->mutex);*/
}

bool GameControl::AddGame(string gameName, int gameSocket) {
    pthread_mutex_lock(&this->mutex);
    if (this->nameToGameMap.find(gameName) != this->nameToGameMap.end()) {
        // found game with given name, don't add anything and return you didn't
        pthread_mutex_unlock(&this->mutex);
        return false;
    }
    // otherwise, add the game to NameToGame map, return you did
    this->nameToGameMap[gameName] = gameSocket;
    pthread_mutex_unlock(&this->mutex);
    return true;
}

int GameControl::GetGameSocket(string gameName)  {
    pthread_mutex_lock(&this->mutex);
    if (this->nameToGameMap.find(gameName) != this->nameToGameMap.end()) {
        // found the gameSocket - return it
        pthread_mutex_unlock(&this->mutex);
        return this->nameToGameMap[gameName];
    } else {
        // couldn't find the gameSocket
        throw "GetGameSocket failed, there is no such GameSocket, Are you sure you've added it first?";
    }
}

vector<string> GameControl::ListOfGamesNames()  {
    pthread_mutex_lock(&this->mutex);
    vector<string> listOfGamesNames;
    map<string, int>::const_iterator it;
    for (it = this->nameToGameMap.begin(); it != this->nameToGameMap.end(); it++) {
        listOfGamesNames.push_back(it->first);
    }
    pthread_mutex_unlock(&this->mutex);
    return listOfGamesNames;
}

bool GameControl::RemoveGame(string gameName) {
    pthread_mutex_lock(&this->mutex);
    if (this->nameToGameMap.find(gameName) != this->nameToGameMap.end()) {
        // found the gameSocket - close it and return true
        this->nameToGameMap.erase(gameName); // close the game
        pthread_mutex_unlock(&this->mutex);
        return true;
    }
    // couldn't find the game to close
    pthread_mutex_unlock(&this->mutex);
    return false;
}

void GameControl::AddPlayer(int player) {
    pthread_mutex_lock(&this->mutex);
    this->connectedPlayers.push_back(player);
    pthread_mutex_unlock(&this->mutex);
}

void GameControl::DeletePlayer(int player) {
    pthread_mutex_lock(&this->mutex);
    for(unsigned int i = 0; i < this->connectedPlayers.size(); i++) {
    	if (this->connectedPlayers.at(i) == player) {
    		this->connectedPlayers.erase(connectedPlayers.begin()+ i);
    	}
    }
    pthread_mutex_unlock(&this->mutex);
}

void GameControl::CloseAllPlayers() {
    pthread_mutex_lock(&this->mutex);
    for(unsigned int i = 0; i < this->connectedPlayers.size(); i++) {
		close(this->connectedPlayers[i]);
	}
    pthread_mutex_unlock(&this->mutex);
}
