/**
# Ori Cohen
# Yana Patyuk
 */

#include "../include/TerminalPlayer.h"

TerminalPlayer::TerminalPlayer(EnumDeclration::CellStatus player) {
    this->player = player;
    if (player == EnumDeclration::X) {
        this->symbol = 'X';
    } else {
        this->symbol = 'O';
    }
}

Slot TerminalPlayer::Play() {
    string str;
    vector<int> vect;
    cout << "\n\n" << "Please enter your row,col: ";
    getline(cin, str);
    try {
        return Slot(str);
    }
    catch (exception exception) {
        cout << "Are you serious? enter something in the row, col format!";
        return Play();
    }
}

char TerminalPlayer::GetSymbol() { return this->symbol; }

EnumDeclration::CellStatus TerminalPlayer::GetEnumSymbol() { return this->player; }

void TerminalPlayer::MakeAMove(Board *b, LogicInterface *logic) {
    cout << this->GetSymbol() << " I'ts your move.\n" << "Your possible moves: ";
    vector<Slot> v = logic->SlotsToPlace(this->player);
    for (unsigned int i = 0; i < v.size(); i++) {
        v[i].Print();
    }
    // get the chosen slot from the player, confirm its legal slot and add it to the board_.
    Slot chosen_slot = Play();
    if (chosen_slot.ExistInVector(logic->SlotsToPlace(this->player))) {
        b->SetCellStatus(chosen_slot.GetRow(), chosen_slot.GetCol(), this->player);
        logic->FlipSlots(chosen_slot.GetRow(), chosen_slot.GetCol(), this->player);
        b->SetLastMove(chosen_slot.GetString());
    } else {
        cout << "ILLEGAL PLACE FOR TAG " << GetSymbol() << " try again" << endl;
        MakeAMove(b, logic);
    }
}
