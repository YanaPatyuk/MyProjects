/**
# Ori Cohen
# Yana Patyuk
 */

#ifndef EX1_TERMINALPLAYER_H
#define EX1_TERMINALPLAYER_H

#include "PlayerInterface.h"
#include "EnumDeclration.h"
#include "LogicInterface.h"
#include "Board.h"
#include <iostream>
#include <string>
#include <sstream>

using namespace std;

/**
 * Define a player, player has status (X, O)
 * and play method which return a Slot
 * Terminal player actually reads the slot from terminal and converts it
 * to Slot object to return
 */
class TerminalPlayer : public PlayerInterface {
public:
    /**
     * @param player the side/status of the player X or O
     */
    TerminalPlayer(EnumDeclration::CellStatus player);

    /**
     * reads from terminal "row, col" & converts it to slot
     * @return the slot the player chose to locate his tag at
     */
    Slot Play();

    /**
     * @return the symbol (X/O) of the player
     */
    char GetSymbol();

    /**
     * Makes a move (play) over the board
     * receives "play" from the terminal and placing it
     * @param b board to make the move on
     * @param logic logic to boards using in order to place tags
     */

    void MakeAMove(Board *b, LogicInterface *logic);

    /**
     * @return symbol (X/O) as CellStatus enum
     */
    EnumDeclration::CellStatus GetEnumSymbol();

private:
    EnumDeclration::CellStatus player;
    char symbol;
};

#endif //EX1_TERMINALPLAYER_H
