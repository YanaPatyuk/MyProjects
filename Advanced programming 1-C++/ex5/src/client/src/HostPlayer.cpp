/**
# Ori Cohen
# Yana Patyuk
 */

#include "../include/HostPlayer.h"
#define SUCCESS 0
#define FAILURE -1
#define PLAYER_X 1
#define PLAYER_O 2

HostPlayer::HostPlayer(const char *serverIP, int serverPort) : serverIP(serverIP), serverPort(serverPort),
                                                               clientSocket(0) {
    //cout << "Client" << endl;
}

void HostPlayer::ConnectToServer() {
    // Create a socket point
    clientSocket = socket(AF_INET, SOCK_STREAM, 0);
    if (clientSocket == -1) {
        throw "Error opening socket";
    }
    // Convert the ip string to a network address
    struct in_addr address;
    if (!inet_aton(serverIP, &address)) {
        throw "Can't parse IP address";
    }
    // Get a hostent structure for the given host address
    struct hostent *server;
    server = gethostbyaddr((const void *) &address, sizeof address, AF_INET);
    if (server == NULL) {
        throw "Host is unreachable";
    }
    // Create a structure for the server address
    struct sockaddr_in serverAddress;
    bzero((char *) &address, sizeof(address));
    serverAddress.sin_family = AF_INET;
    memcpy((char *) &serverAddress.sin_addr.s_addr, (char *) server->h_addr, server->h_length);
    // htons converts values between host and network byte orders
    serverAddress.sin_port = htons(serverPort);
    // Establish a connection with the TCP server
    if (connect(clientSocket, (struct sockaddr *) &serverAddress, sizeof(serverAddress)) == -1) {
        throw "Error connecting to server";
    }
    //cout << "Connected to server" << endl;
}

void HostPlayer::GetPlayerSymbolFromServer() {
    int player;
    int start;
    this->firstMove = true;
    // read which player this HostPlayer got, 1 stand for X 2 for O
    int n = read(clientSocket, &player, sizeof(player));
    if (n == -1) {
        throw "Error reading result from socket";
    } else if (n == 0) {
    	throw "Error: Server Disconnected";
    }
    //if player is 1 it means you are X.
    if (player == PLAYER_X) {
        this->player = EnumDeclration::X;
        //symbol type here references to enemy.
        this->symbol = 'O';
        //print to player who is he.
        cout << "you are: X" << endl;
    } else {//if player is 2-he is second "O".
        this->player = EnumDeclration::O;
        //symbol type here referees to enemy.
        this->symbol = 'X';
        //print to player who is he.
        cout << "you are: O" << endl;
    }
}

Slot HostPlayer::Play() { return Slot(-1, -1); }

char HostPlayer::GetSymbol() { return this->symbol; }

void HostPlayer::MakeAMove(Board *b, LogicInterface *logic) {
    // if we closed the game-just send Close and return.
    if (b->GetLastMove().compare("Close") == 0) {
    	try{
        SendMove("Close");
        return;
    	} catch(const char*mag) {
            logic->ForceCloseGame();
            return;
    	}
        //if its the first move-check if the player is X
        //if X return to get from TerminalPlayer a move.
    } else if (firstMove) {
        if (!(this->player == EnumDeclration::O)) {
            this->firstMove = false;
            return;
        }
        //if its not first move-send players move to server.
    } else if (!firstMove) {
    	try {
    		SendMove(b->GetLastMove());
    	} catch(const char* msg) {
            logic->ForceCloseGame();
    	}
    }
    this->firstMove = false;
    //initialize buffer. read from server array.
    char buffer[50] = {0};
    cout << "Waiting for other player's move..." << endl;
    int n = read(clientSocket, buffer, sizeof(buffer));
    if (n == -1) {
        throw "Error reading result from socket";
    } else if(n == 0) {
    	cout << "Error: Server Disconnected" << endl;
        logic->ForceCloseGame(); // force close the game
    	return;
    }
    //create a string from the array.
    string answer(buffer);
    //if we got Close-finish the game.
    if (answer.compare("Close") == 0) {
        //update boards last move.
        b->SetLastMove("Close");
        return;
        //if NoMove means other player have no moves
    } else if (answer.compare("NoMove") == 0) {
        b->SetLastMove("NoMove");
        cout << "Enemy has no moves-turn goes back to you." << endl;
        return;//to get new moves
    } else {
        //we assume that any other move is int, int
        ReceiveMove(b, logic, Slot(answer));
    }
}

void HostPlayer::ReceiveMove(Board *b, LogicInterface *logic, Slot move) {
    //place other player move on board and print updated board.
    b->SetCellStatus(move.GetRow(), move.GetCol(), EnumDeclration::OtherPlayer((this->player)));
    logic->FlipSlots(move.GetRow(), move.GetCol(), EnumDeclration::OtherPlayer((this->player)));
    //print updated board for player.
    cout << this->symbol << " played: ";
    move.Print();
    cout << endl;
    b->Print();
    //check if its the last move-if it is-update the board.
    if ((logic->SlotsToPlace(EnumDeclration::X).size() == 0
         && logic->SlotsToPlace(EnumDeclration::O).size() == 0)) {
        b->SetLastMove("Close");
    }
}

void HostPlayer::SendMove(string move) {
    //initialize the buffer array and copy the string.
    char buf[50] = {0};
    size_t length = move.copy(buf, move.size(), 0);
    buf[length] = '\0';
    //send the buffer.
    int n = write(clientSocket, buf, strlen(buf));
    if (n == -1) {
        throw "Error writing move to socket";
    }
    if( n == 0) {
    	throw "Error server disconnected";
    }
}

EnumDeclration::CellStatus HostPlayer::GetEnumSymbol() { return EnumDeclration::OtherPlayer((this->player)); }

bool HostPlayer::SendStart(string gameName) {
    // write/send start command to server
    string fullCommand = "start " + gameName;
    WriteCommand(fullCommand);
    // reads the server reply, 0 for success or -1 for failure
    int buf;
    int n = read(clientSocket, &buf, sizeof(buf));
    if (n == -1) {
        throw "Error writing command to socket";
    } else if (n == 0) {
    	throw "Error: Server disconnected";
    }
    // validate that server successfully preformed the command
    if(buf == SUCCESS) { // successfully opened the game
    	cout << "wait for other player to connect" << endl;
    	return true;
    } else if(buf == FAILURE) { // failed to open the game
    	cout << "their is such name in the list" << endl;
    	return false;
    }
}

bool HostPlayer::JoinGame(string gameName) {
    // write/send join command to server
    string fullCommand = "join " + gameName;
    WriteCommand(fullCommand);
    // reads the server reply, 0 for success or -1 for failure
    int buf;
    int n = read(clientSocket, &buf, sizeof(buf));
    if (n == -1) {
        throw "Error writing command to socket";
    } else if(n == 0) {
    	throw "Error: Server disconnected";
    }
    // validate that server successfully preformed the command
    if(buf == SUCCESS) { // successfully joined the game
    	return true;
    } else if(buf == FAILURE) { // failed to join the game
        cout << "their is no open game with the given name" << endl;
        return false;
    }
}

void HostPlayer::WriteCommand(const string &command) const {
    string fullCommand = command;
    //initialize the buffer array and copy the command.
    char buf[50] = {0};
    size_t length = fullCommand.copy(buf, fullCommand.size(), 0);
    buf[length] = '\0';
    //send the buffer.
    int n = write(clientSocket, buf, strlen(buf));
    if (n == -1) {
        throw "Error writing command to socket";
    } else if(n == 0) {
    	throw "Error: server disconnected";
    }
}

void HostPlayer::PrintGamesList() {
    cout << "Open Games:" << endl;
    try{
    // write/send Print Games List command to server
    WriteCommand("list_games");
    } catch (const char *msg) {
    	cout << "error sending: " <<  msg << endl;
    	return;
    }
    bool stop = true;
    while (stop) {
        //initialize buffer. read from server gameName.
        char buffer[50] = {0};
        int n = read(clientSocket, buffer, sizeof(buffer));
        if (n == -1) {
            throw "Error reading open games from socket";
        } else if (n == 0) { // finished reading games from server, disconnected from it
        	return;
        }
        //create a string from the array.
        string answer(buffer);
        //if we got end_open_games stop printing.
        if (answer.compare("end_open_games") == 0) {
            // no more open games to print, stop
            stop = false;
        }
        // prints the games
        cout << answer << endl;
    }
}

