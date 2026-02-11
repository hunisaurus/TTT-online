package com.codecool.tttbackend.service.game;

public class SmallBoard {
   private Cell[][] cells;
   private

   public SmallBoard() {
      cells = new Cell[3][3];
      for (int row = 0; row < 3; row++) {
         for (int column = 0; column < 3; column++) {
            cells[row][column] = new Cell();
            cells[row][column].setCharacter('_');
         }
      }
   }

   public void setCell(int row, int column, char character) {
      cells[row][column].setCharacter(character);
   }

   public Cell[][] getCells() {
      return cells;
   }

   public void setCells(Cell[][] cells) {
      this.cells = cells;
   }

   public static SmallBoard smallBoardFromString(String smallBoardString) {
      if (smallBoardString == null) {
         throw new NullPointerException("Small board cannot be created: input string is null!");
      }
      if (smallBoardString.length() != 9) {
         throw new IllegalArgumentException(
                 "Small board cannot be created: expected 9 characters but got " + smallBoardString.length() + "!"
         );
      }

      SmallBoard newSmallBoard = new SmallBoard();

      for (int i = 0; i < 9; i++) {
         char ch = smallBoardString.charAt(i);
         if (ch == '_') continue;
         int row = i / 3;
         int column = i % 3;
         newSmallBoard.setCell(row, column, ch);
      }

      return newSmallBoard;
   }
}
