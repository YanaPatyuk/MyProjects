/*
 * Server.h
 */

#ifndef SRC_SERVER_SERVER_H_
#define SRC_SERVER_SERVER_H_

#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <string.h>
#include <limits>
#include <iostream>
#include <stdio.h>
#include "CommandManager.h"
#include "CommandProtocol.h"
#include "CommandInfo.h"
#include "ThreadPool.h"
#include "Task.h"


using namespace std;
#define MAX_CONNECTED_CLIENTS 10
#define THREADS_NUM 4

class Server {

    /**
    * Holds all ClientArgs
    */
    struct ClientArgs {
        int clientSocket;
        //int *stop;
        int serverSocket;
        map<int, pthread_t> *threadArr;
        int indexAtThreadArr;
        CommandManager *controller;
        ThreadPool *pool;
    };

public:
    /**
     * Constructor.
     * @param port to connect to.
     */
    Server(int port, CommandManager *controller);

    /**
     * start the connection.
     */
    void start();

    /**
     * stop the connection.
     */
    void stop();

    /**
     * accepts new clients.
     */
    int ConnectNewClients();

    /**
     * close connection between client and server.
     * @param players socket number
     */
    void CloseClientSocket(int player);

    /**
     * @param players socket number
     * @param massage string to send.
     * send to client
     */
    void SendMessageToClient(int player, char *massage);

    /**
     * @param players socket number.
     * @return string massage
     * read massage from player.
     */
    char *GetMessageFromClient(int player);

    virtual ~Server();

private:
    int port;
    int serverSocket;
    CommandManager *controller;
    map<int, pthread_t> threads;
    int stopGame;
    ThreadPool pool;
    /**
     * Handles specific client, reading commands from the specific client
     * @param clientArgs
     * @return
     */
    static void *HandleClient(void *clientArgs);
    /**
     * Closes all the games (waiting for "exit" thread")
     * @param args
     * @return
     */
    static void *CloseAllGames(void *args);
    /**
     * Method to be used as thread for accepting new clients
     * @param clientArgs
     * @return
     */
    static void *ServerAcceptClients(void *clientArgs);
};

#endif /* SRC_SERVER_SERVER_H_ */
