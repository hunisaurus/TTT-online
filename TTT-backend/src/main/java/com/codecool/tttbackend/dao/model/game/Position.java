package com.codecool.tttbackend.dao.model.game;

public class Position {
   private final int row;
   private final int column;

   public Position(int row, int column) {
      this.row = row;
      this.column = column;
   }

   public int getRow() {
      return row;
   }

   public int getColumn() {
      return column;
   }

   public static Position positionFromString(String position){
      String[] splitPos = position.split(",");
      return new Position(Integer.parseInt(splitPos[0]), Integer.parseInt(splitPos[1]));
   }

   @Override
   public String toString(){
      return row + "," + column;
   }
}
