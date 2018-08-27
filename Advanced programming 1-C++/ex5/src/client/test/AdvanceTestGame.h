
/*
* # Ori Cohen
# Yana Patyuk
 */
#include "gtest/gtest.h"
#include "../include/EnumDeclration.h"
#include "../include/AiPlayer.h"
#include "../include/Board.h"
#include "../include/RegularLogic.h"
#include "../include/Slot.h"
#include "../include/GameFlow.h"

#include <iostream>

using namespace std;
class AdvanceTestGame: public testing::Test {
public:
	AdvanceTestGame	() {
	}
	virtual void SetUp() {
		cout << "Setting Up" << endl;
		b = new Board();
		rl = new RegularLogic(b);
		p[1] = new AiPlayer(EnumDeclration::O, b, rl);
		p[0] = new AiPlayer(EnumDeclration::X, b, rl);
		b->SetCellStatus(4,4,EnumDeclration::O);
		b->SetCellStatus(5,5,EnumDeclration::O);
		b->SetCellStatus(4,5,EnumDeclration::X);
		b->SetCellStatus(5,4,EnumDeclration::X);
	}

	virtual void TearDown() {
		cout << "Setting Down" << endl;
   	delete b;
	  delete p[0];
	  delete p[1];
		delete rl;
		delete compered;
	}
	/**
	 * each player choose the best move and place in slot.
	 * at the end-check if coppy works.
	 */
	void MenualGameMoves() {
		Slot placeSlot = p[0]->Play();
		b->SetCellStatus(placeSlot.GetRow(), placeSlot.GetCol(),EnumDeclration::X);
		rl->FlipSlots(placeSlot.GetRow(), placeSlot.GetCol(),EnumDeclration::X);
	  placeSlot = p[1]->Play();
		b->SetCellStatus(placeSlot.GetRow(), placeSlot.GetCol(), EnumDeclration::O);
		rl->FlipSlots(placeSlot.GetRow(), placeSlot.GetCol(),EnumDeclration::O);
		placeSlot = p[0]->Play();
		b->SetCellStatus(placeSlot.GetRow(), placeSlot.GetCol(), EnumDeclration::X);
		rl->FlipSlots(placeSlot.GetRow(), placeSlot.GetCol(),EnumDeclration::X);
		placeSlot = p[1]->Play();
		b->SetCellStatus(placeSlot.GetRow(), placeSlot.GetCol(), EnumDeclration::O);
		rl->FlipSlots(placeSlot.GetRow(), placeSlot.GetCol(),EnumDeclration::O);
		compered =  b->CopyBoard();
		EXPECT_EQ(*b, *compered);
	}
/**
 * check if each slot is or not on the board.
 */
	void CheckSlotsPlace() {
		Slot placeSlot = Slot(1,1);
		EXPECT_FALSE(placeSlot.ExistInVector(b->GetOSlots()));
		EXPECT_FALSE(placeSlot.ExistInVector(b->GetXSlots()));
		placeSlot = Slot(2,2);
		EXPECT_FALSE(placeSlot.ExistInVector(b->GetOSlots()));
		EXPECT_FALSE(placeSlot.ExistInVector(b->GetXSlots()));
		placeSlot = Slot(8,8);
		EXPECT_FALSE(placeSlot.ExistInVector(b->GetOSlots()));
		EXPECT_FALSE(placeSlot.ExistInVector(b->GetXSlots()));
		placeSlot = Slot(6,5);
		EXPECT_FALSE(placeSlot.ExistInVector(b->GetXSlots()));

	}

protected:
	AiPlayer *p[2];
	Board * b;
	Board * compered;
	RegularLogic *rl;

};
