package com.codecool.tttbackend.domain.game;

public class SmallBoard implements Board{
   private Character[][] cells;

   public SmallBoard() {
      cells = new Character[3][3];
   }

   public void setCell(int row, int column, char character) {
      cells[row][column] = character;
   }

   public Character[][] getCells() {
      return cells;
   }

   public void setCells(Character[][] cells) {
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

   public Character getWinningCharacter() {
      for (int i = 0; i < 3; i++) {
         if (cells[i][0] != null && cells[i][0] != '_' && cells[i][0] == cells[i][1] && cells[i][1] == cells[i][2])
            return cells[i][0];
         if (cells[0][i] != null && cells[0][i] != '_' && cells[0][i] == cells[1][i] && cells[1][i] == cells[2][i])
            return cells[0][i];
      }
      if (cells[0][0] != null && cells[0][0] != '_' && cells[0][0] == cells[1][1] && cells[1][1] == cells[2][2])
         return cells[0][0];
      if (cells[0][2] != null && cells[0][2] != '_' && cells[0][2] == cells[1][1] && cells[1][1] == cells[2][0])
         return cells[0][2];
      if (isFull()) return 'D';
      return null;
   }

   private boolean isFull() {
      for (int r = 0; r < 3; r++) {
         for (int c = 0; c < 3; c++) {
            if (cells[r][c] == null || cells[r][c] == '_') return false;
         }
      }
      return true;
   }
}
