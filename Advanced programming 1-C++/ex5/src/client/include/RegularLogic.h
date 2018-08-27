/**
 * # Ori Cohen
# Yana Patyuk
 */

#ifndef EX1_REGULARLOGIC_H
#define EX1_REGULARLOGIC_H

#include "LogicInterface.h"
#include "Board.h"
#include <string>
#include <iostream>
#include <vector>

using namespace std;

class RegularLogic : public LogicInterface {
 public:
	/**
	 * @param b create logic using a pointer to board
	 */
  RegularLogic(Board *b);
  virtual ~RegularLogic();
  /**
   * @param cell_status tag
   * @return vector with all the slots available to place the given tag
   */
  vector<Slot> SlotsToPlace(EnumDeclration::CellStatus cellStatus);
  /**
   * Flips the slots which should be flipped due to the recent placement
   * of the given tag
   * @param row of the recently placed tag
   * @param col of the recently placed tag
   * @param flip_to the status of the recently placed tag
   */
  void FlipSlots(int row, int col, EnumDeclration::CellStatus flipTo);
 private:
  /**
  * when checking another cell we get the result, bad place (can't place there tag)
  * good place, or we have to check the next cell
  */
  enum ExtraCellValidateResult {
    BAD,
    GOOD,
    AGAIN,
  };
  /**
   * receives an operation to do to a row/col
   */
  enum Operation {
    NOTHING,
    PLUS,
    MINUS,
  };

  Board *board;
  vector<Slot> finalSlotsToFlip;

  /**
   * @param currentTag
   * @param row
   * @param col
   * @return vector with all the possible slots to another tag (same status) unlocked because of
   * the given tag
   */
  vector<Slot> PossibleSlotsFor(EnumDeclration::CellStatus currentTag, int row, int col);

/**
 * this function calls recirsuve function to check one direction if slots can flipped.
 * it also can detemine if slots should be flipped in process or not.
 * @param row place of slot to check if enemy or empty.
 * @param col place of slot to check if enemy or empty.
 * @param nextRow place of slot near the row val
 * @param nextCol place of slot near the col val
 * @param slotsVector will contain list of slots.
 * @param player enum symbol
 * @param enemy enum symbol
 * @param rowOperation minus plus or nothing
 * @param colOperation minus plus or nothing
 * @param check bool if we just serach for liggal slots but not flipping-true.
 *
 */
  void OneDierectionSlots(int row, int col, int nextRow, int nextCol, vector<Slot> &slotsVector,
		  	  	  EnumDeclration::CellStatus player, EnumDeclration::CellStatus enemy, Operation rowOperation, Operation colOperation, bool check);
  /**
   * @param currentTag
   * @param row
   * @param col
   * @return vector with slots we have to flip because of the placement
   * of the given tag
   */
  vector<Slot> SlotsToFlip(EnumDeclration::CellStatus currentTag, int row, int col);
  /**
   * Return if given cell is good, bad or we have to check the also the next cell (again)
   * to place certain tag - returns a flag.
   * if the next cell filled with the tag we look for, we should check another cell (AGAIN)
   * if the next cell filled with the same tag as given tag (XOX) - nothing to do (BAD place)
   * if the next cell is empty, it is a GOOD place
   * @param row
   * @param col
   * @param currentTag
   * @param tagsLookingFor
   * @return flag, cell is GOOD, BAD, or we have to check AGAIN
   */
  ExtraCellValidateResult FlagCheckNextCell(int row, int col, EnumDeclration::CellStatus currentTag,
                                            EnumDeclration::CellStatus tagsLookingFor);


  /**
   * Return if given cell is good, bad or we have to check the also the next cell (again)
   * so we have to flip all the slots between the start to end slots - returns a flag
   * "end slot is: GOOD, BAD, AGAIN" if it is indeed GOOD we have to flip slots...
   * if the next cell filled with the tag we look for, we should check another cell (AGAIN)
   * if the next cell filled with the same tag as given tag (XOX) - GOOD place
   * if the next cell is empty, it is a BAD place
   * @param row
   * @param col
   * @param currentTag
   * @param tagsLookingFor
   * @return flag, cell is GOOD, BAD, or we have to check AGAIN
   */
  ExtraCellValidateResult FlagCheckToFlipNextCell(int row, int col, EnumDeclration::CellStatus currentTag,
                                                  EnumDeclration::CellStatus tagsLookingFor);
  /**
   * Checks if a given cell is good to place tag and add it to the possible slot vector
   * @param rowToCheck
   * @param colToCheck
   * @param slotsVector
   * @param tagsLookingFor
   * @param currentTag
   * @param rowOperation
   * @param colOperation
   */

  void RecursiveCheckNextCell(int rowToCheck,
                              int colToCheck,
                              vector<Slot> &slotsVector,
                              const EnumDeclration::CellStatus &tagsLookingFor,
                              const EnumDeclration::CellStatus &currentTag,
                              Operation rowOperation,
                              Operation colOperation, const EnumDeclration::CellStatus &endTag = EnumDeclration::E);
  /**
   * @param b - pointer to board which the new copy of logic (deep copy) should run on.
   * @return RegularLogic object running on the given board
   */
  LogicInterface* CopyLogic(Board *b);

};

#endif //EX1_REGULARLOGIC_H
