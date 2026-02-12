package com.codecool.tttbackend.domain.game;

import com.codecool.tttbackend.dao.model.game.Position;

import java.util.ArrayList;
import java.util.List;

public class BigBoard implements Board {
   private SmallBoard[][] smallBoards;

   public BigBoard() {
      smallBoards = new SmallBoard[3][3];
       for (int i = 0; i < 3; i++) {
           for (int j = 0; j < 3; j++) {
               smallBoards[i][j] = new SmallBoard();
               smallBoards[i][j].setPosition(new com.codecool.tttbackend.dao.model.game.Position(i, j));
               smallBoards[i][j].setActive(true);
           }
       }
   }

   public SmallBoard getSmallBoard(Position position) {
      return smallBoards[position.getRow()][position.getColumn()];
   }

   public void setSmallBoard(Position position, SmallBoard smallBoard) {
      smallBoard.setPosition(position);
      smallBoards[position.getRow()][position.getColumn()] = smallBoard;
   }

   public SmallBoard[][] getSmallBoards() {
      return smallBoards;
   }

   public void setSmallBoards(SmallBoard[][] smallBoards) {
      this.smallBoards = smallBoards;
   }

   public static BigBoard createBigBoard(String boardState, Position activeBoard) {
      if (boardState == null) {
         throw new NullPointerException("Big board cannot be created: input string is null!");
      }

      if (boardState.length() != 81) {
         throw new IllegalArgumentException(
             "Big board cannot be created: expected 81 characters but got " + boardState.length() + "!"
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
                  sb.append(boardState.charAt(rowBase + bigCol));
               }
            }
            SmallBoard smallBoard = SmallBoard.smallBoardFromString(sb.toString());
            Position smallBoardPosition = new Position(sr, sc);
            if (activeBoard == null && !smallBoard.isFull() || activeBoard.equals(smallBoardPosition))
               smallBoard.setActive(true);
            newBigBoard.setSmallBoard(smallBoardPosition, smallBoard);
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

   public int getNumberOfSmallWinsByChar(char character) {
      int numberOfSmallWins = 0;
      for (int r = 0; r < 3; r++) {
         for (int c = 0; c < 3; c++) {
            if (smallBoards[r][c].getWinningCharacter() == character) numberOfSmallWins++;
         }
      }
      return numberOfSmallWins;
   }

   public char getCell(int br, int bc, int sr, int sc) {
      return smallBoards[br][bc].getCell(sr, sc);
   }

   public void makeMove(char character, Position bigPosition, Position smallPosition) {
      smallBoards[bigPosition.getRow()][bigPosition.getColumn()].setCell(smallPosition.getRow(), smallPosition.getColumn(), character);
   }

   public List<Position> getActiveBoardPositions() {
      List<Position> activeBoards = new ArrayList<>();
      for (SmallBoard[] row : smallBoards) {
         for (SmallBoard smallBoard : row) {
            if (smallBoard.isActive()) activeBoards.add(smallBoard.getPosition());
         }
      }
      return activeBoards;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder(81);

      for (int bigRow = 0; bigRow < 9; bigRow++) {
         int sr = bigRow / 3;      // which small-board row
         int innerRow = bigRow % 3; // row within that small board

         for (int bigCol = 0; bigCol < 9; bigCol++) {
            int sc = bigCol / 3;       // which small-board column
            int innerCol = bigCol % 3; // col within that small board

            SmallBoard small = smallBoards[sr][sc];
            if (small == null || small.getCells() == null) {
               sb.append('_');
               continue;
            }

            Character ch = small.getCells()[innerRow][innerCol];
            sb.append(ch == null ? '_' : ch);
         }
      }

      return sb.toString();
   }

   public void setActiveBoards(Position position) {
      if (position != null) {
         setActiveAllNonFullBoards(false);
         smallBoards[position.getRow()][position.getColumn()].setActive(true);
      } else {
         setActiveAllNonFullBoards(true);
      }
   }

   private void setActiveAllNonFullBoards(boolean isActive) {
      for (SmallBoard[] row : smallBoards) {
         for (SmallBoard smallBoard : row) {
            if (smallBoard.isFull() && smallBoard.isActive()) {
               smallBoard.setActive(false);
            } else {
               smallBoard.setActive(isActive);
            }
         }
      }
   }
}
