//
// Created by ori on 12/27/17.
//

#include "../include/CheckNewClient.h"

CheckNewClient::CheckNewClient() {
    this->newClient = false;
    this->clientSocket = -1; // default value = Error
}

void CheckNewClient::NewClientConnected(int clientSocket) {
    this->newClient = true;
    this->clientSocket = clientSocket;
}

int CheckNewClient::ReturnClientSocket() const {
    return this->clientSocket;
}

void CheckNewClient::ClientHasBeenRead() {
    this->newClient = false;
}

bool CheckNewClient::CheckIfNewClientConnected() const {
    return this->newClient;
}
