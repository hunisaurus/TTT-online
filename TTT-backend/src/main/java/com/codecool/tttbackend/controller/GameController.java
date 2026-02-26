package com.codecool.tttbackend.controller;

import com.codecool.tttbackend.controller.dto.request.*;
import com.codecool.tttbackend.controller.dto.response.GameStatusResponseDTO;
import com.codecool.tttbackend.controller.dto.response.MoveResponseDTO;
import com.codecool.tttbackend.dao.model.game.Game;
import com.codecool.tttbackend.dao.model.game.Move;
import com.codecool.tttbackend.dao.model.game.Position;
import com.codecool.tttbackend.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/games")
public class GameController {

   private final GameService gameService;
   private final SimpMessagingTemplate messagingTemplate;

   public GameController(GameService gameService, SimpMessagingTemplate messagingTemplate) {
      this.gameService = gameService;
      this.messagingTemplate = messagingTemplate;
   }

   @PostMapping("/create")
   public ResponseEntity<Void> createGame(@RequestBody CreateGameRequestDTO createGameRequestDTO) {
      gameService.createGame(createGameRequestDTO.userName(), createGameRequestDTO.gameName(), createGameRequestDTO.maxPlayerCount());
      return ResponseEntity.status(HttpStatus.CREATED).build();
   }

   @GetMapping
   public ResponseEntity<List<Game>> getMyGames(@RequestParam(required = false) String username) {
      System.out.println("Beérkező kérés username: " + username); // LOG
      List<Game> games = gameService.listUserGames(username);
      System.out.println("Talált játékok száma: " + games.size()); // LOG
      return ResponseEntity.ok(games);
   }

   @PatchMapping("/{id}/join")
   public ResponseEntity<Void> joinGame(@PathVariable int id, @RequestBody JoinGameRequestDTO joinGameRequestDTO) {
      gameService.joinGame(id, joinGameRequestDTO.userName(), joinGameRequestDTO.character());
      return ResponseEntity.ok().build();
   }

   @PatchMapping("/{id}/leave")
   public ResponseEntity<Void> leaveGame(@PathVariable int id, @RequestBody LeaveGameRequestDTO leaveGameRequestDTO) {
      gameService.leaveGame(id, leaveGameRequestDTO.userName());
      return ResponseEntity.ok().build();
   }

   @PatchMapping("/{id}/start")
   public ResponseEntity<Void> startGame(@PathVariable int id) {
      gameService.startGame(id);
      return ResponseEntity.ok().build();
   }

   @GetMapping("/{id}")
   public ResponseEntity<GameStatusResponseDTO> getGameStatus(@PathVariable int id) {
      return ResponseEntity.ok(gameService.getGameStatus(id));
   }

   @PatchMapping("/{id}/end")
   public ResponseEntity<Void> endGame(@PathVariable int id) {
      gameService.endGame(id);
      return ResponseEntity.ok().build();
   }

   @MessageMapping("/{gameId}/move")
   public ResponseEntity<GameStatusResponseDTO> makeMove(@DestinationVariable int gameId, @RequestBody MoveRequestDTO moveRequestDTO) {
      GameStatusResponseDTO response = gameService.makeMove(gameId, new Move(gameService.getPlayer(gameId, moveRequestDTO.userName()), new Position(moveRequestDTO.br(), moveRequestDTO.bc()), new Position(moveRequestDTO.sr(), moveRequestDTO.sc())));
      if (response == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

      messagingTemplate.convertAndSend("/topic/games/" + gameId, response);

      return ResponseEntity.ok(response);
   }

   @PatchMapping("/{id}/win")
   public ResponseEntity<Void> winGame(@PathVariable int id, @RequestBody WinGameRequestDTO winGameRequestDTO) {
      gameService.winGame(id, winGameRequestDTO.winnerName());
      gameService.endGame(id);
      return ResponseEntity.ok().build();
   }
}
