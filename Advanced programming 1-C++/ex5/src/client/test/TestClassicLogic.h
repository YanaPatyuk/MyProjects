/*
# Ori Cohen
# Yana Patyuk
 */

#ifndef TEST_TESTCLASSICLOGIC_H_
#define TEST_TESTCLASSICLOGIC_H_


#include "gtest/gtest.h"
#include "../include/EnumDeclration.h"
#include "../include/AiPlayer.h"
#include "../include/Board.h"
#include "../include/RegularLogic.h"
#include "../include/Slot.h"

#include <iostream>

using namespace std;
class TestClassicLogic: public testing::Test {
public:
	TestClassicLogic() {}
	/*
	 * setUp new Board and a classic logic game.
	 */
	virtual void SetUp() {
		cout << "Setting Up Board and Logic" << endl;
		b = new Board();
		rl = new RegularLogic(b);
		//place first slots on Board.
		b->SetCellStatus(4, 4, EnumDeclration::O);
		b->SetCellStatus(5, 5, EnumDeclration::O);
		b->SetCellStatus(4, 5, EnumDeclration::X);
		b->SetCellStatus(5, 4, EnumDeclration::X);
	}
	/**
	 * free memory.
	 */
	virtual void TearDown() {
		cout << "Setting Down" << endl;
		delete rl;
		delete b;

	}
	/**
	 * creates another board to compare our board after putting and flipping slots.
	 * the check is in this function.
	 */
	void ComperBord() {
		Board compered =  Board();
		rl->FlipSlots(3, 4,EnumDeclration::X);
		b->SetCellStatus(3, 4, EnumDeclration::X);
		compered.SetCellStatus(4, 4, EnumDeclration::X);
		compered.SetCellStatus(5, 5, EnumDeclration::O);
		compered.SetCellStatus(4, 5, EnumDeclration::X);
		compered.SetCellStatus(5, 4, EnumDeclration::X);
		compered.SetCellStatus(3, 4, EnumDeclration::X);
		EXPECT_EQ(*b, compered);
	}


protected:
	Board * b;
	RegularLogic *rl;

};





#endif /* TEST_TESTCLASSICLOGIC_H_ */
