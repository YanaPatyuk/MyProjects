/*
# Ori Cohen
# Yana Patyuk
 */

#ifndef AIPLAYER_H_
#define AIPLAYER_H_

#include "PlayerInterface.h"
#include "EnumDeclration.h"
#include "LogicInterface.h"
#include "Board.h"
#include "SlotWithRank.h"
#include <iostream>
#include <string>
#include <sstream>
#include <climits>

class AiPlayer : public PlayerInterface {
public:
    AiPlayer(EnumDeclration::CellStatus player);

    /**
     * this consrucor for gtest only.
     * @param b-board.
     * @param l logic for the game.
     * @param players symbol.
     */
    AiPlayer(EnumDeclration::CellStatus player, Board *b, LogicInterface *l);

    ~AiPlayer();

    /**
     * @return the Slot AI player chose to locate his tag at
     */
    Slot Play();

    /**
     * @return symbol (X/O) as char
     */
    char GetSymbol();

    /**
     * @return symbol (X/O) as CellStatus enum
     */
    EnumDeclration::CellStatus GetEnumSymbol();

    /**
     * input: pointer to board and gameLogic.
     * output:non
     * the function make players move-depended what kind of player it is.
     */
    void MakeAMove(Board *b, LogicInterface *logic);

private:
    EnumDeclration::CellStatus player;
    Board *board;
    LogicInterface *logic;
    char symbol;

/**
 * calculates the max points difference in the given move
 * if AI is X: calc m = oSlots - xSlots
 * if AI is O: calc m = xSlots - oSlots
 * calculates the other player points - AI points.
 * @param aiSlot the current aiSlot (the slot we check now)
 * @param bToCalculateWith board to findMaxM of
 * @param m value
 * @param maxM the current maxM
 * @param maxMSlot the current maxMSlot
 */
    void FindMaxM(const Slot &aiSlot, const Board *bToCalculateWith, int m, int &maxM, Slot &maxMSlot) const;

/**
 * find the slot that AI should choose in order to make other player have the min "MaxM" value
 * in shortly - find the min rank of slotsWithMaxM and return the slot
 * @param min rank so far
 * @param slotsWithMaxM pointer to array of vector of slots with Max m
 * @return the slot which has the min rank
 */
    Slot FindMinSlotRank(int min, vector<SlotWithRank> &slotsWithMaxM);
};

#endif /* AIPLAYER_H_ */
