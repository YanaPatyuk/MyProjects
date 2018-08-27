/*
 * main.cpp
 */

#include "../include/Server.h"
#include <iostream>
#include <stdlib.h>
#include <sstream>
#include <fstream>
#include <limits>
#include <pthread.h>
#include "../include/CommandManager.h"
#include "../include/GameControl.h"


using namespace std;

int main() {

    // Read IP and port of remote Host from file
    string ip, line;
    int port;
    ifstream hostInfo("hostInfo.txt");
    if (hostInfo.is_open()) {
        // break the first line into IP
        // break the second line to Port No.
        string delimiter = ":";
        getline(hostInfo, line);
        string token = line.substr(line.find(delimiter) + 1, line.length());
        /* remove spaces from the ip/port
        std::remove(token.begin(), token.end()+1, ' '); */
        std::stringstream stream(token);
        stream >> token;
        ip = token;
        getline(hostInfo, line);
        token = line.substr(line.find(delimiter) + 1, line.length());
        std::istringstream(token) >> port; //convert to int
        hostInfo.close();
    } else {
        cout << "Unable to open file" << endl;
    }
    GameControl manager;
    CommandManager controller(&manager);
    Server server(port, &controller);
    try {
        server.start();
    } catch (const char *msg) {
        cout << "Cannot start server. Reason: " << msg << endl;
        exit(-1);
    }
}


