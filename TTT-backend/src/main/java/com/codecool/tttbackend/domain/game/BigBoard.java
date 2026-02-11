package com.codecool.tttbackend.domain.game;

public class BigBoard implements Board{
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

   public Character getWinningCharacter() {
      for (int i = 0; i < 3; i++) {
         if (smallBoards[i][0].getWinningCharacter() != null && smallBoards[i][0].getWinningCharacter() != 'D' && smallBoards[i][0].getWinningCharacter() == smallBoards[i][1].getWinningCharacter() && smallBoards[i][1].getWinningCharacter() == smallBoards[i][2].getWinningCharacter())
            return smallBoards[i][0].getWinningCharacter();
         if (smallBoards[0][i].getWinningCharacter() != null && smallBoards[0][i].getWinningCharacter() != 'D' && smallBoards[0][i].getWinningCharacter() == smallBoards[1][i].getWinningCharacter() && smallBoards[1][i].getWinningCharacter() == smallBoards[2][i].getWinningCharacter())
            return smallBoards[0][i].getWinningCharacter();
      }
      if (smallBoards[0][0].getWinningCharacter() != null && smallBoards[0][0].getWinningCharacter() != 'D' && smallBoards[0][0].getWinningCharacter() == smallBoards[1][1].getWinningCharacter() && smallBoards[1][1].getWinningCharacter() == smallBoards[2][2].getWinningCharacter())
         return smallBoards[0][0].getWinningCharacter();
      if (smallBoards[0][2].getWinningCharacter() != null && smallBoards[0][2].getWinningCharacter() != 'D' && smallBoards[0][2].getWinningCharacter() == smallBoards[1][1].getWinningCharacter() && smallBoards[1][1].getWinningCharacter() == smallBoards[2][0].getWinningCharacter())
         return smallBoards[0][2].getWinningCharacter();
      if (isFull()) return 'D';
      return null;
   }

   public boolean isFull() {
      for (int r = 0; r < 3; r++) {
         for (int c = 0; c < 3; c++) {
            if (smallBoards[r][c].getWinningCharacter() == null) return false;
         }
      }
      return true;
   }

   public int getNumberOfSmallWinsByChar(char character){
      int numberOfSmallWins = 0;
      for (int r = 0; r < 3; r++) {
         for (int c = 0; c < 3; c++) {
            if (smallBoards[r][c].getWinningCharacter() == character) numberOfSmallWins++;
         }
      }
      return numberOfSmallWins;
   }
}
