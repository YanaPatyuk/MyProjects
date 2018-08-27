/**
 * # Ori Cohen
# Yana Patyuk
 */

#include "../include/Board.h"

Board::Board(unsigned long numOfRows, unsigned long numOfCols) {
// num of rows
    this->board.resize(numOfRows);
    // num of cols
    for (unsigned long i = 0; i < numOfRows; ++i) {
        this->board[i].resize(numOfCols);
    }
    this->lastMove = "No Move"; // default
}

Board::~Board() {
    // if in future changing the members to dynamically allocated members,
    // I should run on each object in the vector, delete him, empty the vector and only then delete it.
    //delete this->board;
    //delete this->xSlots;
    //delete this->oSlots;
}

void Board::Print() const {
    cout << " |";
    for (int i = 1; i <= NumOfCols(); i++) {
        cout << " " << i << " |";
    }
    cout << endl;
    cout << "--";
    for (int i = 0; i < NumOfCols(); i++) {
        cout << "----";
    }
    cout << endl;
    for (int i = 0; i < NumOfCols(); i++) {
        cout << "" << i + 1 << "|";
        for (int j = 0; j < NumOfRows(); j++) {
            cout << " ";
            switch (board[i][j]) {
                case EnumDeclration::E:
                    cout << " ";
                    break;
                case EnumDeclration::O:
                    cout << "O";
                    break;
                case EnumDeclration::X:
                    cout << "X";
                    break;
            }
            cout << " |";
        }
        cout << endl;
        cout << "--";
        for (int i = 0; i < NumOfCols(); i++) {
            cout << "----";
        }
        cout << endl;
    }
}


int Board::GetCellStatus(int xLocation, int yLocation) const {
    if (xLocation >= 1 && xLocation <= this->NumOfRows()
        && yLocation >= 1 && yLocation <= this->NumOfCols()) {
        // -1 because array starting from 0
        return this->board[xLocation - 1][yLocation - 1];
    }
    cout << "Illegal row or col" << endl;
    return -1;
}

void Board::SetCellStatus(int xLocation, int yLocation, EnumDeclration::CellStatus status) {
    // if X or O add it to the vectors which holds list of X/O Slots
    if (status == EnumDeclration::X) {
        this->xSlots.push_back(Slot(xLocation, yLocation, status));
    } else if (status == EnumDeclration::O) {
        this->oSlots.push_back(Slot(xLocation, yLocation, status));
    }
    // -1 because array starting from 0
    this->board[xLocation - 1][yLocation - 1] = status;
}

int Board::NumOfRows() const {
    return this->board.size();
}

int Board::NumOfCols() const {
    return this->board[0].size();
}

vector<Slot> Board::GetOSlots() const {
    return this->oSlots;
}

vector<Slot> Board::GetXSlots() const {
    return this->xSlots;
}

bool Board::LegalPlaceInBoard(int row, int col) {
    return (row <= NumOfRows() && row > 0
            && col <= NumOfCols() && col > 0);
}

void Board::SetOSlots(vector<Slot> oSlots) {
    this->oSlots = oSlots;
}

void Board::SetXSlots(vector<Slot> xSlots) {
    this->xSlots = xSlots;
}

vector<Slot> Board::GetSlotsOfPlayer(EnumDeclration::CellStatus player) const {
    if (player == EnumDeclration::X) {
        return this->xSlots;
    } else {
        return this->oSlots;
    }
}

void Board::SetSlotsOfPlayer(EnumDeclration::CellStatus player, vector<Slot> slots) {
    if (player == EnumDeclration::X) {
        this->xSlots = slots;
    } else {
        this->oSlots = slots;
    }
}

Board *Board::CopyBoard() {
    Board *copyOfBoard = new Board();
    copyOfBoard->board = std::vector<vector<int> >(this->board);
    copyOfBoard->xSlots = std::vector<Slot>(this->xSlots);
    copyOfBoard->oSlots = std::vector<Slot>(this->oSlots);
    return copyOfBoard;
}

bool Board::operator==(const Board &b) const {
    //if the sizes of the board are different.
    if ((b.NumOfCols() != this->NumOfCols()) || (
            b.NumOfRows() != this->NumOfRows())) {
        return false;
    }
    //check boards cells.
    for (int i = 0; i < this->NumOfRows(); i++) {
        for (int j = 0; j < NumOfCols(); j++) {
            if (!(this->board[i][j] == b.board[i][j])) {
                return false;
            }
        }
    }
    return true;
}

bool Board::LegalSlotInBoard(Slot slot) {
    return (slot.GetRow() <= NumOfRows() && slot.GetRow() > 0
            && slot.GetCol() <= NumOfCols() && slot.GetCol() > 0);
}

void Board::SetLastMove(string lastMove) {
    this->lastMove = lastMove;
}

string Board::GetLastMove() {
    return this->lastMove;
}

