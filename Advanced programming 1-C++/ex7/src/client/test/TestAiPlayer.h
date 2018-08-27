/*
# Ori Cohen
# Yana Patyuk
 */


#include "gtest/gtest.h"
#include "../include/EnumDeclration.h"
#include "../include/AiPlayer.h"
#include "../include/Board.h"
#include "../include/RegularLogic.h"
#include "../include/Slot.h"

#include <iostream>

using namespace std;
class TestAiPlayer: public testing::Test {
public:
	//first choice manualy. next one should be diffrent.
	TestAiPlayer():choosenCell(1,1) {}
	/**
	 * set new and empty board.
	 */
	virtual void SetUp() {
		cout << "Setting Up" << endl;
		//create Board, logic of the game and player.
		b = new Board();
		rl = new RegularLogic(b);
		p1 = new AiPlayer(EnumDeclration::O, b, rl);
		//place for first cell should not work.
		choosenCell = p1->Play();
	}
	virtual void TearDown() {
		cout << "Setting Down" << endl;
		delete p1;
		delete rl;
		delete b;

	}
	//place first cells and get computers choice.
	void PlaceAnotherCells() {
	  PlaceFirstCells();
		choosenCell = p1->Play();
	}
	//place more cells and get computers choice.
	void PlaceAnotherCells1() {
		rl->FlipSlots(3, 4,EnumDeclration::X );
		b->SetCellStatus(3,4,EnumDeclration::X);
		choosenCell = p1->Play();
	}
	//place more cells and get computers choice.
	void PlaceAnotherCells2() {
		rl->FlipSlots(3, 3, EnumDeclration::O );
		b->SetCellStatus(3, 3, EnumDeclration::O);
		choosenCell = p1->Play();
	}
	//place all cell on board get computers choice.
	void PlaceAnotherCells3() {
		for(int i = 1; i <= b->NumOfRows(); i++) {
			for(int j = 1; j <= b->NumOfCols(); j++) {
				if((b->GetCellStatus(i, j) != EnumDeclration::X) &&
						(b->GetCellStatus(i, j) != EnumDeclration::O))
				b->SetCellStatus(i, j, EnumDeclration::X);
			}
		}
		choosenCell = p1->Play();
	}

protected:
	AiPlayer *p1;
	Board * b;
	Slot choosenCell;
	RegularLogic *rl;
private:
	void PlaceFirstCells(){
		b->SetCellStatus(4, 4, EnumDeclration::O);
		b->SetCellStatus(5, 5, EnumDeclration::O);
		b->SetCellStatus(4, 5, EnumDeclration::X);
		b->SetCellStatus(5, 4, EnumDeclration::X);
	}

};





