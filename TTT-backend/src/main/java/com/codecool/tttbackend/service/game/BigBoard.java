package com.codecool.tttbackend.service.game;

public class BigBoard {
   private SmallBoard[][] smallBoards;

   public BigBoard() {
      smallBoards = new SmallBoard[3][3];
   }

   public void setSmallBoard(int row, int column, SmallBoard smallBoard) {
      smallBoards[row][column] = smallBoard;
   }

   public SmallBoard[][] getSmallBoards() {
      return smallBoards;
   }

   public void setSmallBoards(SmallBoard[][] smallBoards) {
      this.smallBoards = smallBoards;
   }

   public static BigBoard bigBoardFromString(String bigBoardString) {
      if (bigBoardString == null) {
         throw new NullPointerException("Big board cannot be created: input string is null!");
      }

      if (bigBoardString.length() != 81) {
         throw new IllegalArgumentException(
                 "Big board cannot be created: expected 81 characters but got " + bigBoardString.length() + "!"
         );
      }

      BigBoard newBigBoard = new BigBoard();

      for (int sr = 0; sr < 3; sr++) {
         for (int sc = 0; sc < 3; sc++) {
            StringBuilder sb = new StringBuilder(9);

            int bigRowStart = sr * 3;
            int bigColStart = sc * 3;

            for (int r = 0; r < 3; r++) {
               int bigRow = bigRowStart + r;
               int rowBase = bigRow * 9;
               for (int c = 0; c < 3; c++) {
                  int bigCol = bigColStart + c;
                  sb.append(bigBoardString.charAt(rowBase + bigCol));
               }
            }

            newBigBoard.setSmallBoard(sr, sc, SmallBoard.smallBoardFromString(sb.toString()));
         }
      }

      return newBigBoard;
   }
}
