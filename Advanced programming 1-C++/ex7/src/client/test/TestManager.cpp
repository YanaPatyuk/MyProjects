/*
# Ori Cohen
# Yana Patyuk
 */

#include <iostream>
#include "gtest/gtest.h"
#include "../include/Slot.h"
#include "../include/EnumDeclration.h"
#include "AdvanceTestGame.h"
#include "TestAiPlayer.h"
#include "TestClassicLogic.h"
TEST_F(AdvanceTestGame, testBoardCells) {
	//play some moves of two computers and check the board.
	MenualGameMoves();
	//Board boundaries.
	EXPECT_FALSE(b->LegalPlaceInBoard(-1, -1));
	EXPECT_FALSE(b->LegalPlaceInBoard(b->NumOfRows() + 1,b->NumOfCols() + 1));
	//check if slots are already on the board.
	CheckSlotsPlace();

}
//check first time Ai choice
TEST_F(TestAiPlayer, testCorrectChoice) {
	//Get Slot if board is empty.
	EXPECT_NE(choosenCell, Slot(0, 0));
	//Place starting slots and check again.
  PlaceAnotherCells();
	EXPECT_EQ(choosenCell, Slot(4, 6));
	//place more slots on board.
	PlaceAnotherCells1();
	EXPECT_EQ(choosenCell, Slot(3, 3));
	//place anoyher slots and check for difference.
	PlaceAnotherCells2();
	EXPECT_NE(choosenCell, Slot(2, 3));
	//if the boards is full
	PlaceAnotherCells3();
	EXPECT_NE(choosenCell, Slot(0, 0));
}

//check of flipping cells works by compere boards.
TEST_F(TestClassicLogic, flippingTest) {
	ComperBord();
}
//Test for Enum Class
TEST(EnumDeclration, checkWhoIsEnemy) {
	EXPECT_EQ(EnumDeclration::OtherPlayer(EnumDeclration::O),
																		EnumDeclration::X);
	EXPECT_EQ(EnumDeclration::OtherPlayer(EnumDeclration::X),
																	  EnumDeclration::O);
	EXPECT_NE(EnumDeclration::OtherPlayer(EnumDeclration::O),
																		EnumDeclration::O);
	EXPECT_NE(EnumDeclration::OtherPlayer(EnumDeclration::X),
																		EnumDeclration::X);
	EXPECT_NE(EnumDeclration::OtherPlayer(EnumDeclration::X),
																		EnumDeclration::E);
	EXPECT_NE(EnumDeclration::OtherPlayer(EnumDeclration::O),
																		EnumDeclration::E);
	EXPECT_EQ(EnumDeclration::OtherPlayer(EnumDeclration::E),
																		EnumDeclration::E);
}

