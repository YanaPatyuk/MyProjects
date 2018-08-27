/**
 * # Ori Cohen
# Yana Patyuk
 */
#ifndef EX1_ENUMDECLRATION_H
#define EX1_ENUMDECLRATION_H

class EnumDeclration {
 public:
  enum CellStatus {
    E,
    O,
    X,
  };
  /**
   * Static (class) method. simply utility method.
   * @param player
   * @return the other Player (if player is X return O etc.)
   * caution: if the player is E returns E!
   */
  static EnumDeclration::CellStatus OtherPlayer(EnumDeclration::CellStatus player){
    if(player == EnumDeclration::X) {
      return EnumDeclration::O;
    }
    if (player == EnumDeclration::O) {
      return EnumDeclration::X;
    }
    // default - return E (there is no "other player" for empty player therefore return empty)
    return EnumDeclration:: E;
  }
};

#endif //EX1_ENUMDECLRATION_H
