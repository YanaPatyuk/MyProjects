/**
 * # Ori Cohen
# Yana Patyuk
 */

#include "../include/RegularLogic.h"

RegularLogic::RegularLogic(Board *b) {
  this->board = b;
  this->forcedClose = false;
}
vector<Slot> RegularLogic::SlotsToPlace(EnumDeclration::CellStatus cellStatus) {
  Board *b = this->board;
  vector<Slot> overallSlotsToPlaceList;
    /*
     * iterate over every players in the board, find the PossibleSlots for him
     * add it to overall slots to place list only if it doesn't in it already
     * (overallSlotsToPlaceList is free of duplicates)
     */
    for (unsigned int i = 0; i < b->GetSlotsOfPlayer(cellStatus).size(); i++) {
      Slot s = b->GetSlotsOfPlayer(cellStatus)[i];
      vector<Slot> v = PossibleSlotsFor(s.GetCellStatus(), s.GetRow(), s.GetCol());
      for (unsigned int i = 0; i < v.size(); i++) {
        Slot slot = v[i];
        // verify that each Slot inserted only once (prevent duplicates)
        if (!slot.ExistInVector(overallSlotsToPlaceList)) {
          overallSlotsToPlaceList.push_back(slot);
        }
      }
    }
  return overallSlotsToPlaceList;
}

vector<Slot> RegularLogic::PossibleSlotsFor(EnumDeclration::CellStatus currentTag, int row, int col) {
  EnumDeclration::CellStatus tagsLookingFor;
  vector<Slot> possibleSlots;

  if (currentTag == EnumDeclration::X) {
    tagsLookingFor = EnumDeclration::O;
  } else if (currentTag == EnumDeclration::O){
    tagsLookingFor = EnumDeclration::X;
  }
  OneDierectionSlots(row - 1, col - 1, row - 2, col - 2, possibleSlots,
		  currentTag, tagsLookingFor, MINUS, MINUS, false);
  OneDierectionSlots(row - 1, col, row - 2, col, possibleSlots, currentTag,
		  tagsLookingFor, MINUS, NOTHING,false);
  OneDierectionSlots(row - 1, col + 1, row - 2, col + 2, possibleSlots,
		  currentTag, tagsLookingFor, MINUS, PLUS, false);
  OneDierectionSlots(row, col - 1, row, col - 2, possibleSlots, currentTag,
		  tagsLookingFor, NOTHING, MINUS, false);
  OneDierectionSlots(row, col + 1, row, col + 2, possibleSlots, currentTag,
		  tagsLookingFor, NOTHING, PLUS, false);
  OneDierectionSlots(row + 1, col - 1, row + 2, col - 2, possibleSlots,
		  currentTag, tagsLookingFor, PLUS, MINUS, false);
  OneDierectionSlots(row + 1, col, row + 2, col, possibleSlots, currentTag,
		  tagsLookingFor, PLUS, NOTHING, false);
  OneDierectionSlots(row + 1, col + 1, row + 2, col + 2, possibleSlots,
		  currentTag, tagsLookingFor, PLUS, PLUS, false);
  return possibleSlots;
}


RegularLogic::ExtraCellValidateResult RegularLogic::FlagCheckNextCell(int row,
                                                                      int col,
                                                                      EnumDeclration::CellStatus currentTag,
                                                                      EnumDeclration::CellStatus tagsLookingFor) {
  // if legal place & empty - it is possible place to place the current tag
  if (this->board->LegalPlaceInBoard(row, col) && this->board->GetCellStatus(row, col) == EnumDeclration::E) {
    return GOOD;
  }
  // if legal place & filled with currentTag - BAD place
  if (this->board->LegalPlaceInBoard(row, col) && this->board->GetCellStatus(row, col) == currentTag) {
    return BAD;
  }
  // if legal place & filled with the tag we look for we have to check again the next cell
  if (this->board->LegalPlaceInBoard(row, col) && this->board->GetCellStatus(row, col) == tagsLookingFor) {
    return AGAIN;
  }
  // default, if not legal place for example
  return BAD;
}

void RegularLogic::RecursiveCheckNextCell(int rowToCheck,
                                          int colToCheck,
                                          vector<Slot> &slotsVector,
                                          const EnumDeclration::CellStatus &tagsLookingFor,
                                          const EnumDeclration::CellStatus &currentTag,
                                          Operation rowOperation, Operation colOperation,
                                          const EnumDeclration::CellStatus &endTag) {
  // decide whether its checking next cell to place or checks cell to flip
  ExtraCellValidateResult condition = FlagCheckNextCell(rowToCheck, colToCheck, currentTag, tagsLookingFor);
  if (endTag != EnumDeclration::E) {
    condition = FlagCheckToFlipNextCell(rowToCheck, colToCheck, currentTag, tagsLookingFor);
  }
  switch (condition) {
    case GOOD:
      // if checks "next cell to place"
      if (endTag == EnumDeclration::E) {
        // add to "possible slot" vector
        slotsVector.push_back(Slot(rowToCheck, colToCheck));
      }
      // if checks "cells to flip", add the tempSlotsToFlip to final_slotsToFlip
      if (endTag != EnumDeclration::E) {
        for (unsigned int k = 0; k < slotsVector.size(); k++) {
          this->finalSlotsToFlip.push_back(slotsVector[k]);
        }
      }
      break;
    case AGAIN:
      // we have to check extra cell
      // if checks "cells to flip"
      if (endTag != EnumDeclration::E) {
        // add it "to_be_flipped" vector (don't mind the slot_vector name)
        // if the end tag is not E (end tag is the current tag...)
        slotsVector.push_back(Slot(rowToCheck, colToCheck, tagsLookingFor));
      }
      // optimize for the direction we check
      if (rowOperation == MINUS) {
        rowToCheck--;
      }
      if (rowOperation == PLUS) {
        rowToCheck++;
      }
      if (colOperation == MINUS) {
        colToCheck--;
      }
      if (colOperation == PLUS) {
        colToCheck++;
      }
      RecursiveCheckNextCell(rowToCheck, colToCheck, slotsVector,
                             tagsLookingFor, currentTag, rowOperation, colOperation, endTag);
      break;
      // default - for example BAD slot - do nothing
    default:break;
  }
}
void RegularLogic::OneDierectionSlots(int row, int col, int nestRow,
										int NextCol, vector<Slot> &slotsVector,
										EnumDeclration::CellStatus player,
										EnumDeclration::CellStatus enemy,
										Operation rowOperation,
										Operation colOperation, bool check) {
	if (this->board->LegalPlaceInBoard(row,col) &&
			this->board->GetCellStatus(row, col) == enemy) {
		  if (check == true) {
		  slotsVector.push_back(Slot(row, col, enemy));
		  RecursiveCheckNextCell(nestRow, NextCol, slotsVector,
				  enemy, player,rowOperation, colOperation,
		                              player);
		  } else {
			  RecursiveCheckNextCell(nestRow, NextCol, slotsVector, enemy,
					  player, rowOperation, colOperation);
		  }
	  }
}


vector<Slot> RegularLogic::SlotsToFlip(EnumDeclration::CellStatus currentTag,
																	int row, int col) {
  EnumDeclration::CellStatus tagsLookingFor;
  vector<Slot> tempSlotsToFlip;
  if (currentTag == EnumDeclration::X) {
    tagsLookingFor = EnumDeclration::O;
  } else {
    tagsLookingFor = EnumDeclration::X;
  }

  OneDierectionSlots(row - 1, col - 1, row - 2, col - 2, tempSlotsToFlip,
		  currentTag, tagsLookingFor, MINUS, MINUS, true);
  tempSlotsToFlip.clear();
  OneDierectionSlots(row - 1, col, row - 2, col, tempSlotsToFlip, currentTag,
		  tagsLookingFor, MINUS, NOTHING, true);
  tempSlotsToFlip.clear();
  OneDierectionSlots(row - 1, col + 1, row - 2, col + 2, tempSlotsToFlip,
		  currentTag, tagsLookingFor, MINUS, PLUS, true);
  tempSlotsToFlip.clear();
  OneDierectionSlots(row, col - 1, row, col - 2, tempSlotsToFlip, currentTag,
		  tagsLookingFor, NOTHING, MINUS, true);
  tempSlotsToFlip.clear();
  OneDierectionSlots(row, col + 1, row, col + 2, tempSlotsToFlip, currentTag,
		  tagsLookingFor, NOTHING, PLUS, true);
  tempSlotsToFlip.clear();
  OneDierectionSlots(row + 1, col - 1, row + 2, col - 2, tempSlotsToFlip,
		  currentTag, tagsLookingFor, PLUS, MINUS, true);
  tempSlotsToFlip.clear();
  OneDierectionSlots(row + 1, col, row + 2, col, tempSlotsToFlip, currentTag,
		  tagsLookingFor, PLUS, NOTHING, true);
  tempSlotsToFlip.clear();
  OneDierectionSlots(row + 1, col + 1, row + 2, col + 2, tempSlotsToFlip,
		  currentTag, tagsLookingFor, PLUS, PLUS, true);
  tempSlotsToFlip.clear();

  return this->finalSlotsToFlip;
}


RegularLogic::ExtraCellValidateResult RegularLogic::FlagCheckToFlipNextCell(int row,
                                                                            int col,
                                                                            EnumDeclration::CellStatus currentTag,
                                                                            EnumDeclration::CellStatus tagsLookingFor) {
  // if legal place & empty - BAD place
  if (this->board->LegalPlaceInBoard(row, col) && this->board->GetCellStatus(row, col) == EnumDeclration::E) {
    return BAD;
  }
  // if legal place & filled with currentTag - GOOD place
  if (this->board->LegalPlaceInBoard(row, col) && this->board->GetCellStatus(row, col) == currentTag) {
    return GOOD;
  }
  // if legal place & filled with the tag we look for we have to check again the next cell
  if (this->board->LegalPlaceInBoard(row, col) && this->board->GetCellStatus(row, col) == tagsLookingFor) {
    return AGAIN;
  }
  // default, if not legal place for example
  return BAD;
}

void RegularLogic::FlipSlots(int row, int col, EnumDeclration::CellStatus flipTo) {
  Board *b = this->board;
  // clear the slots to flip vector, in order to not flip an old slot unintentionally
  this->finalSlotsToFlip.clear();
  // receives all the slots we have to flip due to recent placement of the tag
  vector<Slot> slotsToFlip = SlotsToFlip(flipTo, row, col);
  EnumDeclration::CellStatus flipFrom;
  if (flipTo == EnumDeclration::X) {
    flipFrom = EnumDeclration::O;
  } else {
    flipFrom = EnumDeclration::X;
  }
  // for each slot in slot_to_flip vector
  for (unsigned int i = 0; i < slotsToFlip.size(); i++) {
      int locationToOmit = slotsToFlip[i].LocationInVector(b->GetSlotsOfPlayer(flipFrom));
      vector<Slot> newSlots;
      // if locationToOmit exist
      if (locationToOmit != -1) {
        // copy every players slot to a new vector without slot at [location to omit]
        for (unsigned int k = 0; k < b->GetSlotsOfPlayer(flipFrom).size(); k++) {
          if ((int)k != locationToOmit) {
            newSlots.push_back(b->GetSlotsOfPlayer(flipFrom)[k]);
          }
        }
      }
      // update the Board players slots to the new vector
    b->SetSlotsOfPlayer(flipFrom, newSlots);
    // flip it
    b->SetCellStatus(slotsToFlip[i].GetRow(), slotsToFlip[i].GetCol(), flipTo);
  }
}


LogicInterface* RegularLogic::CopyLogic(Board *b) {
  RegularLogic* copyOfRl = new RegularLogic(b);
  copyOfRl->board = b;
  return copyOfRl;
}
RegularLogic::~RegularLogic() {
}

