/**
 * # Ori Cohen
# Yana Patyuk
 */
#ifndef EX1_SLOT_H
#define EX1_SLOT_H

#include <string>
#include <sstream>

#include <iostream>
#include "EnumDeclration.h"
#include <vector>

using namespace std;

/**
 * Create a slot in the format (row,col), first row/col is 1! (NOT 0)
 */
class Slot {
public:
    /**
     * create Empty slot from string.
     */
    Slot(string slotValues);

    /**
     * Create a slot with E (Empty status)
     * @param row
     * @param col
     */
    Slot(int row, int col);

    /**
     * Create slot with given status
     * @param row
     * @param col
     * @param status
     */
    Slot(int row, int col, EnumDeclration::CellStatus status);

    ~Slot();

    /**
     * Print in the (row,col) format
     */
    void Print() const;

    /**
     * @return row of slot
     */
    int GetRow() const;

    /**
     * @param row to set
     */
    void SetRow(int row);

    /**
     * @return col of slot
     */
    int GetCol() const;

    /**
     * @param col to set
     */
    void SetCol(int col);

    /**
     * Prints the status of the slot (E, X, O)
     */
    void PrintStatus() const;

    /**
     * @return status of the slot
     */
    EnumDeclration::CellStatus GetCellStatus() const;

    /**
     * @param v vector
     * @return if the slot exist in the vector
     */
    bool ExistInVector(vector<Slot> v);

    /**
     * @param v vector
     * @return the index (location) of the slot in the given vector
     */
    int LocationInVector(vector<Slot> v);

    /**
     * @return deep copy for slot.
     */
    Slot *CopySlot();

    /**
     * @return slot values as string.
     */
    string GetString();

    /**
     * new operators to compare.
     */
    bool operator==(const Slot &s) const;

    bool operator!=(const Slot &s) const;

private:
    int row;
    int col;
    EnumDeclration::CellStatus status;
    string s;

};

#endif //EX1_SLOT_H
