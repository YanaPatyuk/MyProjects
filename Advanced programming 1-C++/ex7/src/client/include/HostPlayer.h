/**
# Ori Cohen
# Yana Patyuk
 */
#ifndef SRC_CLIENT_INCLUDE_HOSTPLAYER_H_
#define SRC_CLIENT_INCLUDE_HOSTPLAYER_H_

#include "PlayerInterface.h"
#include <iostream>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <string.h>
#include <unistd.h>

using namespace std;

class HostPlayer : public PlayerInterface {
public:
    HostPlayer(const char *serverIP, int serverPort);

    /**
     * connect to the sever by the ip and port.
     */
    void ConnectToServer();

    /**
     * read from the server for the first time.
     * if the number is 1 the player is X and wait to other player.
     * else player is O.
     */
    void GetPlayerSymbolFromServer();

    /**
     * do nothing.
     * @return -1 -1 for error check.
     */
    virtual Slot Play();

    /**
     * @return the symbol (X/O) of the player
     */
    virtual char GetSymbol();

    /**
     * Makes a move (play) over the board
     * receives "play" from remote server and placing it
     * @param b board to make the move on
     * @param logic logic to boards using in order to place tags
     */
    virtual void MakeAMove(Board *b, LogicInterface *logic);

    /**
     * @return symbol (X/O) as CellStatus enum
     */
    EnumDeclration::CellStatus GetEnumSymbol();

    /**
     * Starts (opens) new game in the given name at the server
     * @param gameName
     * @return if successfully started the new game or failed, prints the reason for failure
     */
    bool SendStart(string gameName);
    /**
     * Joins the given game
     * @param gameName
     * @return if successfully joined the game, prints the reason for failure
     */
    bool JoinGame(string gameName);
    /**
     * Prints the open (started) games list
     */
    void PrintGamesList();

private:
    EnumDeclration::CellStatus player;
    char symbol;
    const char *serverIP;
    int serverPort;
    int clientSocket;
    bool firstMove;

    /**
     * place enemy's move on the board and flip the slots.
     * @param b pointer for board.
     * @param logic - for the game rules.
     * @param slot to place.
     */
    void ReceiveMove(Board *b, LogicInterface *logic, Slot move);

    /**
     * sends to the server last move on the board.
     */
    void SendMove(string move);

    /**
     * Writes/sends the given command (start/join etc.) and gameName to server
     * @param command start/join/list_games etc.
     * @param gameName gameName..
     */
    void WriteCommand(const string &command) const;
};

#endif /* SRC_CLIENT_INCLUDE_HOSTPLAYER_H_ */
