/**
# Ori Cohen
# Yana Patyuk
 */
#ifndef EX1_GAMEFLOW_H
#define EX1_GAMEFLOW_H
#include "LogicInterface.h"
#include "Board.h"
#include "EnumDeclration.h"
#include "PlayerInterface.h"

class GameFlow {
 public:
	/**
	 * Class that holds all the information in order to run the game
	 * @param logic - logic the game run with
	 * @param board - game board
	 * @param player1
	 * @param player2
	 * @param server if its a game using server or not
	 */
  GameFlow(LogicInterface* logic, Board* board, PlayerInterface* player1, PlayerInterface* player2, bool server);
  /**
   * Run the game
   */
  void Run();
 private:
  LogicInterface* logic;
  Board* board;
  PlayerInterface* player[2]; // holds both players
  EnumDeclration::CellStatus currentTurn;
  bool clentServer;
  /**
   * @return if Game is Over
   */
  bool GameOver();
  /**
   * prints why the game end and who is the winner
   */
  void EndGame();
};

#endif //EX1_GAMEFLOW_H
