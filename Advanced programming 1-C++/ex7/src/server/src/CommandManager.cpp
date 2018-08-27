


#include "../include/CommandManager.h"

CommandManager::CommandManager(GameControl* game) {
	this->gameControl = game;
	this->commandsMap["start"] = new StartNewGame(gameControl);
	this->commandsMap["list_games"] = new PrintGames(gameControl);
	this->commandsMap["join"] = new JoinToGame(gameControl);
}

CommandManager::~CommandManager() {
	map<string, CommandProtocol *>::iterator it;
	 for (it = commandsMap.begin(); it != commandsMap.end(); it++) {
		 	 delete it->second;
	 }
}


bool CommandManager::ExecuteCommand(string command, CommandInfo args) {
	if (this->commandsMap.find(command) != this->commandsMap.end() )  {
		CommandProtocol* cmdPtr = this->commandsMap[command];
		cmdPtr->Execute(args);
		return true;
	}
	return false;
}

void CommandManager::End() {
	this->gameControl->CloseAllPlayers();
}

void CommandManager::AddPlayerSocket(int socketNumber) {
	this->gameControl->AddPlayer(socketNumber);
}



