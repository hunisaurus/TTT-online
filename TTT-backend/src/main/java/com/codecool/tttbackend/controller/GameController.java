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
   public ResponseEntity<Void> createGame(@RequestBody CreateGameRequest request) {
      gameService.createGame(request.userName(), request.gameName(), request.maxPlayerCount());
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
   public ResponseEntity<Void> joinGame(@PathVariable int id, @RequestBody JoinGameRequest joinGameRequest) {
      gameService.joinGame(id, joinGameRequest.userName(), joinGameRequest.character());
      return ResponseEntity.ok().build();
   }

   @PatchMapping("/{id}/leave")
   public ResponseEntity<Void> leaveGame(@PathVariable int id, @RequestBody LeaveGameRequest leaveGameRequest) {
      gameService.leaveGame(id, leaveGameRequest.userName());
      return ResponseEntity.ok().build();
   }

   @PatchMapping("/{id}/start")
   public ResponseEntity<Void> startGame(@PathVariable int id) {
      gameService.startGame(id);
      return ResponseEntity.ok().build();
   }

   @GetMapping("/{id}")
   public ResponseEntity<GameStatusResponseDTO> getGameStatus(@PathVariable int id){
      return ResponseEntity.ok(gameService.getGameStatus(id));
   }

   @PatchMapping("/{id}/end")
   public ResponseEntity<Void> endGame(@PathVariable int id) {
      gameService.endGame(id);
      return ResponseEntity.ok().build();
   }

   @PatchMapping("/{id}/move")
   public ResponseEntity<MoveResponseDTO> makeMove(@PathVariable int id, @RequestBody MoveRequest moveRequest) {
      MoveResponseDTO response = gameService.makeMove(id, new Move(gameService.getPlayer(id, moveRequest.userName()), new Position(moveRequest.br(), moveRequest.bc()), new Position(moveRequest.sr(), moveRequest.sc())));

      // Broadcast to everyone watching/playing this game. (Huni)
      messagingTemplate.convertAndSend("/topic/games/" + id, response);

      return ResponseEntity.ok(response);
   }

   @PatchMapping("/{id}/win")
   public ResponseEntity<Void> winGame(@PathVariable int id, @RequestBody WinGameRequest winGameRequest) {
      gameService.winGame(id, winGameRequest.winnerName());
      gameService.endGame(id);
      return ResponseEntity.ok().build();
   }
}
