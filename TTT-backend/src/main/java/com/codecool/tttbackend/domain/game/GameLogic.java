package com.codecool.tttbackend.domain.game;

import com.codecool.tttbackend.dao.model.game.Game;
import com.codecool.tttbackend.dao.model.game.Move;
import com.codecool.tttbackend.dao.model.game.Player;

import java.util.Comparator;

public class GameLogic {

   public static boolean validateMove(Game game, Move move) {
      boolean condition1 = game.getBoard().getCell(move.bigPosition().getRow(), move.bigPosition().getColumn(), move.smallPosition().getRow(), move.smallPosition().getColumn()) == '_';
      boolean condition2 = game.getCurrentPlayer().equals(move.player());
      boolean condition3 = game.getBoard().getActiveBoardPositions().contains(move.bigPosition());
      return condition1 && condition2 && condition3;
   }

   public static String applyMove(Game game, Move move) {
      game.getBoard().makeMove(move.player().getCharacter(), move.bigPosition(), move.smallPosition());
      return game.getBoard().toString();
   }

   public static Player getWinningPlayer(Game game){
      Character winningCharacter = game.getBoard().getWinningCharacter();
      if (winningCharacter == null) return null;
      if (winningCharacter == 'D'){
         return game.getPlayers().stream().max(Comparator.comparing(player -> game.getBoard().getNumberOfSmallWinsByChar(player.getCharacter()))).get();
      }
      return game.getPlayers().stream().filter(player -> player.getCharacter().equals(winningCharacter)).findFirst().get();
   }
}
