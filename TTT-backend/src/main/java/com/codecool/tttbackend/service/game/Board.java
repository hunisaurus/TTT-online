package com.codecool.tttbackend.service.game;

public abstract class Board {
   private Cell[][] cells;

   public Board(){
      cells = new Cell[3][3];
   }

   public void setCell(int row, int column, char character){
      if (cells[row][column].getCharacter() == null) return;
   }

   public Cell[][] getCells() {
      return cells;
   }

   public void setCells(Cell[][] cells) {
      this.cells = cells;
   }
}
