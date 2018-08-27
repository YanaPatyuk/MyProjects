//
// Created by ori on 12/27/17.
//

#ifndef ADVANCEDPROGRAMMING1_NEWCLIENTCHECKER_H
#define ADVANCEDPROGRAMMING1_NEWCLIENTCHECKER_H


class CheckNewClient {
public:
    CheckNewClient();
    void NewClientConnected(int clientSocket);
    bool CheckIfNewClientConnected() const;
    int ReturnClientSocket() const;
    void ClientHasBeenRead();

private:
    bool newClient;
    int clientSocket;
};


#endif //ADVANCEDPROGRAMMING1_NEWCLIENTCHECKER_H
