//
/**
 * # Ori Cohen
# Yana Patyuk
 */
//

#include "../include/SlotWithRank.h"
SlotWithRank::SlotWithRank(Slot slot, int rank) : slot(slot) {
  this->rank = rank;
}

SlotWithRank::~SlotWithRank() {
}

int SlotWithRank::GetRank() const {
  return rank;
}
Slot SlotWithRank::GetSlot(){
  return this->slot;
}


