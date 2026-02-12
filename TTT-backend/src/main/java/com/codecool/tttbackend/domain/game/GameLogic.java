package com.codecool.tttbackend.domain.game;

import com.codecool.tttbackend.dao.model.game.Move;

public class GameLogic {

   public boolean validateMove(String gameStatus, Move move){
      BigBoard bigBoard = BigBoard.bigBoardFromString(gameStatus);
      return bigBoard.getCell(move.br(), move.bc(), move.sr(), move.sc()) !=
   }
}
