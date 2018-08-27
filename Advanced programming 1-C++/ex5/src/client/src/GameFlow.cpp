/**
# Ori Cohen
# Yana Patyuk
 */

#include "../include/GameFlow.h"

GameFlow::GameFlow(LogicInterface *logic, Board *board, PlayerInterface *player1, PlayerInterface *player2,
                   bool server) {
    this->logic = logic;
    this->board = board;
    this->currentTurn = EnumDeclration::X;
    this->player[0] = player1;
    this->player[1] = player2;
    this->clentServer = server;
    // some initialization
    board->SetCellStatus(board->NumOfRows() / 2, board->NumOfCols() / 2, EnumDeclration::O);
    board->SetCellStatus(board->NumOfRows() / 2 + 1, board->NumOfCols() / 2 + 1, EnumDeclration::O);
    board->SetCellStatus(board->NumOfRows() / 2 + 1, board->NumOfCols() / 2, EnumDeclration::X);
    board->SetCellStatus(board->NumOfRows() / 2, board->NumOfCols() / 2 + 1, EnumDeclration::X);
}

void GameFlow::Run() {
    Board *b = this->board;
    int turnCounter = 0;
    // run the game while it is still not over
    // prints the board
    b->Print();
    while (!GameOver()) {
        // if player has possible slots to place.
        if (this->logic->SlotsToPlace(this->player[turnCounter % 2]->GetEnumSymbol()).size() != 0) {
            // player makes a move.
            this->player[turnCounter % 2]->MakeAMove(this->board, this->logic);
            if (!(clentServer && turnCounter % 2 == 0)) {
                cout << endl;
                cout << "current board:" << endl;
                b->Print();
            }
        } else {
            //in server-client mode: we have to send lastmove and receive nomove if
            //we know other player cant move.
            if (clentServer && (turnCounter % 2 == 0)) {
                this->player[turnCounter % 2]->MakeAMove(this->board, this->logic);
                turnCounter++;
                continue;
            }
            // it doesn't have possible slots to place tag at
            // the turn passes over
            cout << player[turnCounter % 2]->GetSymbol()
                 << " I'ts your move. but unfortunately you don't have anything to do," <<
                 "therefore it's only fair that the play passes back to other player " <<
                 player[(turnCounter + 1) % 2]->GetSymbol() << endl;
            //update board to noMove
            this->board->SetLastMove("NoMove");
        }
        turnCounter++;
    }
    //print end game screen.
    EndGame();
}

bool GameFlow::GameOver() {
    if (this->logic->ForcedCloseStatus()) return true;
    if (this->logic->SlotsToPlace(EnumDeclration::X).size() != 0
        || this->logic->SlotsToPlace(EnumDeclration::O).size() != 0) {
        return false;
    }//if its the end of board and we made last move- send to server end.
    if (this->board->GetLastMove().compare("Close") != 0) {
        //send to other player your last move.
        if (this->clentServer) this->player[0]->MakeAMove(this->board, this->logic);
        this->board->SetLastMove("Close");
    } else {//send to other player end.
        if (this->clentServer) this->player[0]->MakeAMove(this->board, this->logic);
    }
    return true;
}

void GameFlow::EndGame() {
    Board *b = this->board;
    // if the game was forced closed
    if (this->logic->ForcedCloseStatus()) {
        return; // skip the score screen (prints who won..)
    }
    // finds the reason why game is over, and print score screen - who won.
    int numOfSlotsInBoard = b->NumOfRows() * b->NumOfCols();
    int numOfUsedSlots = b->GetXSlots().size() + b->GetOSlots().size();
    if (numOfUsedSlots >= numOfSlotsInBoard) {
        cout << "Game is over, board is completely full" << endl;
    } else {
        cout << "Game is over, both sides can't make any more moves" << endl;
    }
    // declare result
    if (b->GetXSlots().size() > b->GetOSlots().size()) {
        cout << "X is the WINNER!";
    } else if (b->GetXSlots().size() < b->GetOSlots().size()) {
        cout << "O is the WINNER!";
    } else {
        cout << "It's a tie";
    }
    cout << endl;
}
