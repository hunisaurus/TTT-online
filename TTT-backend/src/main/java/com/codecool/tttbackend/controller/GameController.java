package com.codecool.tttbackend.controller;

import com.codecool.tttbackend.controller.dto.request.*;
import com.codecool.tttbackend.controller.dto.response.GameResponseDTO;
import com.codecool.tttbackend.controller.dto.response.GameStatusResponseDTO;
import com.codecool.tttbackend.dao.model.game.Move;
import com.codecool.tttbackend.dao.model.game.Position;
import com.codecool.tttbackend.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
      gameService.createGame(createGameRequestDTO);
      return ResponseEntity.status(HttpStatus.CREATED).build();
   }

   @GetMapping
   public ResponseEntity<List<GameResponseDTO>> getMyGames(Principal principal) {
      String userName = (principal != null) ? principal.getName() : null;

      if (userName == null || userName.isBlank()) {
         // Not authenticated and no username provided
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }
      System.out.println("Beérkező kérés username: " + userName); // LOG
      List<GameResponseDTO> gameResponseDTOS = gameService.getUserGameResponseDTOs(userName);
      System.out.println("Talált játékok száma: " + gameResponseDTOS.size()); // LOG
      return ResponseEntity.ok(gameResponseDTOS);
   }

   @GetMapping("/available")
   public ResponseEntity<List<GameResponseDTO>> getAvailableGames() {
      System.out.println("Beérkező kérés minden elérhetoo játékra"); // LOG
      List<GameResponseDTO> gameResponseDTOS = gameService.getAvailableGameResponseDTOs();
      System.out.println("Talált játékok száma: " + gameResponseDTOS.size()); // LOG
      return ResponseEntity.ok(gameResponseDTOS);
   }

   @PatchMapping("/{gameId}/join")
   public ResponseEntity<Void> joinGame(@PathVariable int gameId, @RequestBody JoinGameRequestDTO joinGameRequestDTO, Principal principal) {
      String userName = (principal != null) ? principal.getName() : null;

      if (userName == null || userName.isBlank()) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }
      gameService.joinGame(gameId, userName, joinGameRequestDTO.character());

      // TODO: broadcast notifications to players

      return ResponseEntity.ok().build();
   }

   @PatchMapping("/{gameId}/leave")
   public ResponseEntity<Void> leaveGame(@PathVariable int gameId, @RequestBody LeaveGameRequestDTO leaveGameRequestDTO) {
      gameService.leaveGame(gameId, leaveGameRequestDTO.userName());
      return ResponseEntity.ok().build();
   }

   @PatchMapping("/{gameId}/start")
   public ResponseEntity<Void> startGame(@PathVariable int gameId) {
      gameService.startGame(gameId);
      messagingTemplate.convertAndSend("/topic/games/" + gameId, gameService.getGameStatus(gameId));
      return ResponseEntity.ok().build();
   }

   @GetMapping("/{gameId}")
   public ResponseEntity<GameStatusResponseDTO> getGameStatus(@PathVariable int gameId) {
      return ResponseEntity.ok(gameService.getGameStatus(gameId));
   }

   @PatchMapping("/{gameId}/end")
   public ResponseEntity<Void> endGame(@PathVariable int gameId) {
      gameService.endGame(gameId);
      return ResponseEntity.ok().build();
   }

   @MessageMapping("/{gameId}/move")
   public ResponseEntity<GameStatusResponseDTO> makeMove(@DestinationVariable int gameId, @RequestBody MoveRequestDTO moveRequestDTO) {
      GameStatusResponseDTO response = gameService.makeMove(gameId, new Move(gameService.getPlayer(gameId, moveRequestDTO.userName()), new Position(moveRequestDTO.br(), moveRequestDTO.bc()), new Position(moveRequestDTO.sr(), moveRequestDTO.sc())));
      if (response == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

      messagingTemplate.convertAndSend("/topic/games/" + gameId, response);

      return ResponseEntity.ok(response);
   }

   @PatchMapping("/{gameId}/win")
   public ResponseEntity<Void> winGame(@PathVariable int gameId, @RequestBody WinGameRequestDTO winGameRequestDTO) {
      gameService.winGame(gameId, winGameRequestDTO.winnerName());
      gameService.endGame(gameId);
      return ResponseEntity.ok().build();
   }
}
