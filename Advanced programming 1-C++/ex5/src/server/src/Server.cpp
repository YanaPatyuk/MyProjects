/*
 * Server.cpp
 *
 *  Created on: 2 בדצמ׳ 2017
 *      Author: yanap
 */

#include "../include/Server.h"

#define THREADS_NUM 10

Server::Server(int port, CommandManager *controller) : port(port), serverSocket(0) {
    this->controller = controller;
    cout << "Server" << endl;
    this->stopGame = 0;
}

void Server::start() {
    // Create a socket point
    serverSocket = socket(AF_INET, SOCK_STREAM, 0);
    if (serverSocket == -1) {
        throw "Error opening socket";
    }
    // Assign a local address to the socket
    struct sockaddr_in serverAddress;
    bzero((void *) &serverAddress, sizeof(serverAddress));
    serverAddress.sin_family = AF_INET;
    serverAddress.sin_addr.s_addr = INADDR_ANY;
    serverAddress.sin_port = htons(port);
    if (bind(serverSocket, (struct sockaddr *) &serverAddress, sizeof(serverAddress)) == -1) {
        throw "Error on binding";
    }

    // Start listening to incoming connections
    listen(serverSocket, MAX_CONNECTED_CLIENTS);

    pthread_t end;
    pthread_t serverOperation;


    ClientArgs arg;
    arg.serverSocket = serverSocket;
    //no clients for now.
    arg.clientSocket = 0;
    //this argument used only for exit thread.
    arg.stop = &this->stopGame;
    arg.indexAtThreadArr = 0;
    //gameControl
    arg.controller = this->controller;
    //map of threads.
    arg.threadArr = &this->threads;


    //this thread in charge to regularly check "exit" input on the server in order to stop the server
    int rc = pthread_create(&end, NULL, CloseAllGames, (void *) &arg);
    if (rc) {
        cout << "Error: unable to create thread, " << rc << endl;
        return;
    }

    //this thread accept new clients and handles them.
    //first insert thread to list
    threads.insert(pair<int, pthread_t>(0, serverOperation));
    rc = pthread_create(&serverOperation, NULL, ServerAcceptClients, (void *) &arg);
    if (rc) {
        cout << "Error: unable to create thread, " << rc << endl;
        return;
    }

    //wait the exit to stop
    void *status;
    pthread_join(end, &status);
    ///////cout << "stop the server" << endl;
    //stop the server-close servers socket.
    stop();
}

void Server::stop() {
    close(serverSocket);
}

Server::~Server() {

}

int Server::ConnectNewClients() {
    struct sockaddr_in clientAddress;
    socklen_t clientAddressLen;
    int player = accept(serverSocket, (struct sockaddr *) &clientAddress, &clientAddressLen);
    cout << "Client 1 connected" << endl;
    if (player == -1)
        throw "Error on accept";
    return player;
}

void Server::CloseClientSocket(int player) {
    //close clients socket.
    close(player);
}

void Server::SendMessageToClient(int player, char *massage) {
    int n = write(player, massage, sizeof(massage));
    if (n == -1) {
        cout << "Error writing to socket" << endl;
        return;
    } else if (n == 0) {
        cout << "Client disconnected" << endl;
        return;
    }
}

char *Server::GetMessageFromClient(int player) {
    char massage[50];
    char *buffer = massage;
    //read from client massage.
    int n = read(player, buffer, sizeof(buffer));
    //if reading didnt work-return null/
    if (n == -1) {
        cout << "Error reading point" << endl;
        return NULL;
        //if client disconnected-return null
    } else if (n == 0) {
        cout << "Client disconnected" << endl;
        return NULL;
    }
    //return massage from client.
    return buffer;
}


void *Server::HandleClient(void *clientArgs) {
    // break the struct back to parameters
    // cast back from void* to struct
    struct ClientArgs *playersInfo = (struct ClientArgs *) clientArgs;
    int client = playersInfo->clientSocket;
    map<int, pthread_t> *refToThreadArr = playersInfo->threadArr;
    int indexAtThreadArr = playersInfo->indexAtThreadArr;
    CommandManager *control = playersInfo->controller;

    // Read massage from player.
    char msg[50];
    char *buffer = msg;
    memset(msg, 0, 50);
    bool endReading = false;
    do {
        int n = read(client, msg, sizeof(msg));
        if (n == -1) {
            cout << "Error reading point" << endl;
            pthread_exit(NULL);
        } else if (n == 0) {
            cout << "Client disconnected" << endl;
            pthread_exit(NULL);
        }
        //concert array to string.
        string massage(buffer);
        //split the massage to the command and values clients send.
        //value will be command and <gameName> if there's two of them or just command.
        string i;
        vector<string> vect;
        stringstream ss(massage);
        bool twoWords = false;
        while (ss >> i) {
            vect.push_back(i);
            if (ss.peek() == ' ') {
                ss.ignore();
                twoWords = true;
            }
        }
        //create struct of arguments command will get.
        CommandInfo args;
        string command = vect.at(0).c_str();
        if (twoWords) {
            string val = vect.at(1).c_str();
            args.gameName = val;
        }
        args.clientSocket = client;

        // Execute the Command
        if (control->ExecuteCommand(command, args)) {
            endReading = true;
            refToThreadArr->erase(indexAtThreadArr);
        } else {
            cout << "no such command" << endl;
        }
    } while (!endReading);
}


void *Server::ServerAcceptClients(void *clientArgs) {
    struct sockaddr_in clientAddress;
    socklen_t clientAddressLen;
    int threadCounter = 1;
    struct ClientArgs *playersInfo = (struct ClientArgs *) clientArgs;
    int serverSocket = playersInfo->serverSocket;
    map<int, pthread_t> *threads = playersInfo->threadArr;

    while (true) {
        cout << "Waiting for client connections..." << endl;
        // Accept a new client connection
        int playerNumber = accept(serverSocket, (struct sockaddr *) &clientAddress, &clientAddressLen);
        cout << "Client " << playerNumber << " connected" << endl;
        playersInfo->controller->AddPlayerSocket(playerNumber);
        pthread_t newOne;
        threads->insert(pair<int, pthread_t>(threadCounter, newOne));


        struct ClientArgs cArgs;
        cArgs.clientSocket = playerNumber;
        cArgs.controller = playersInfo->controller;
        cArgs.indexAtThreadArr = threadCounter;
        cArgs.threadArr = threads;

        int rc = pthread_create(&newOne, NULL, HandleClient, (void *) &cArgs);
        if (rc) {
            cout << "Error: unable to create thread, " << rc << endl;
        }
        threadCounter++;
    }
}

void *Server::CloseAllGames(void *args) {
    cout << "In order to force close type: exit" << endl;
    struct ClientArgs *playersInfo = (struct ClientArgs *) args;
    CommandManager *control = playersInfo->controller;
    map<int, pthread_t> *threadMap = playersInfo->threadArr;
    string checkToClose;
    bool stop = false;
    while (!stop) {
        cin >> checkToClose;
        if (checkToClose.compare("exit") == 0) {
            stop = true;
            control->End();
            map<int, pthread_t>::const_iterator it;
            for (it = threadMap->begin(); it != threadMap->end(); it++) {
                pthread_cancel(it->second);
            }
            *playersInfo->stop = 1;
        } else {
            cout << "you typed something, but it wasn't exit \n";
            cin.clear();
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
        }
    }
}
