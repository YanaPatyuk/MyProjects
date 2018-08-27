/**
 * # Ori Cohen
# Yana Patyuk
 */

#ifndef EX1_BOARD_H
#define EX1_BOARD_H

#include <string>
#include <vector>
#include "EnumDeclration.h"
#include "Slot.h"

using namespace std;

class Board {
public:
    /**
     * Constructor for board, which initialize the board to E (Empty)
     * cells.
     * Note that default board size is set to 8
     */
    Board(unsigned long numOfRows = 8, unsigned long numOfCols = 8);

    ~Board();

    /**
     * Print method which prints the board as requested.
     */
    void Print() const;

    /**
     * @param xLocation to get
     * @param yLocation to get
     * @return the status of the given cell (Empty, O, X)
     */
    int GetCellStatus(int xLocation, int yLocation) const;

    /**
     * Set the given cell new status.
     * (if given (4,4) location set new status to board[3][3] location
     * since we print from 1,1 and not 0,0).
     * @param xLocation of the cell
     * @param yLocation of the cell
     * @param status - the new status to set (E, O, X)
     */
    void SetCellStatus(int xLocation, int yLocation, EnumDeclration::CellStatus status);

    /**
     * @return number of rows
     */
    int NumOfRows() const;

    /**
     * @return number of cols
     */
    int NumOfCols() const;

    /**
     * @reutrn vector slots for o
     */
    vector<Slot> GetOSlots() const;

    /**
     * @reutrn vector slots for x
     */
    vector<Slot> GetXSlots() const;

    /**
     * @reutrn vector slots for x
     */
    vector<Slot> GetSlotsOfPlayer(EnumDeclration::CellStatus player) const;

    /**
     * Update the slots of X/O (given player) vector to new one
     * @param player X/O
     * @param slots the new vector to set
     */
    void SetSlotsOfPlayer(EnumDeclration::CellStatus player, vector<Slot> slots);

    void SetOSlots(vector<Slot> oSlots);

    void SetXSlots(vector<Slot> xSlots);

    /**
     * @param row
     * @param col
     * @return if its legal place in board
     */
    bool LegalPlaceInBoard(int row, int col);

    /**
     * @return deep copy of the board.
     */
    Board *CopyBoard();
    /**
     * @param lastMove as String (you may convert Slot to string using GetString method)
     */
    void SetLastMove(string lastMove);
    /**
     * @return LastMove
     */
    string GetLastMove();
    /**
     * @param slot
     * @return true if slot is legal slot in board otherwise false
     */
    bool LegalSlotInBoard(Slot slot);

    /**
     * operator compare between boards.
     */
    bool operator==(const Board &b) const;

private:
    vector<vector<int> > board;
    vector<Slot> oSlots;
    vector<Slot> xSlots;
    string lastMove;
};

#endif //EX1_BOARD_H
