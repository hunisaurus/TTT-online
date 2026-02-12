package com.codecool.tttbackend.domain.game;

import com.codecool.tttbackend.dao.model.game.Game;
import com.codecool.tttbackend.dao.model.game.Move;

public class GameLogic {

   public boolean validateMove(Game game, Move move) {
      boolean condition1 = game.getBoard().getCell(move.bigPosition().getRow(), move.bigPosition().getColumn(), move.smallPosition().getRow(), move.smallPosition().getColumn()) == '_';
      boolean condition2 = game.getCurrentPlayer().equals(move.player());
      boolean condition3 = game.getBoard().getActiveBoardPositions().contains(move.bigPosition());
      return condition1 && condition2 && condition3;
   }

   public String applyMove(Game game, Move move) {
      game.getBoard().makeMove(move.player().getCharacter(), move.bigPosition(), move.smallPosition());
      return game.getBoard().toString();
   }
}
