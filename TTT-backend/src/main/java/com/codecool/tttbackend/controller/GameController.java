package com.codecool.tttbackend.controller;

import com.codecool.tttbackend.controller.dto.request.CreateGameRequest;
import com.codecool.tttbackend.controller.dto.request.JoinGameRequest;
import com.codecool.tttbackend.controller.dto.request.MoveRequest;
import com.codecool.tttbackend.controller.dto.request.LeaveGameRequest;
import com.codecool.tttbackend.controller.dto.response.GameStatusResponse;
import com.codecool.tttbackend.dao.model.game.Game;
import com.codecool.tttbackend.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createGame(@RequestBody CreateGameRequest request) {
        gameService.createGame(request.userName(), request.gameName(), request.maxPlayerCount());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Game>> getAllGames() {
        return ResponseEntity.ok(gameService.listAllGames());
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
    public ResponseEntity<Void> startGame(@PathVariable int id){
        gameService.startGame(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/end")
    public ResponseEntity<Void> endGame(@PathVariable int id){
        gameService.endGame(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/move")
    public ResponseEntity<GameStatusResponse> makeMove(@PathVariable int id, @RequestBody MoveRequest moveRequest){
        gameService.makeMove(int gameId, new Move);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/win")
    public ResponseEntity<Void> winGame(@PathVariable int id, @RequestBody WinGameRequest winGameRequest){
        gameService.winGame(id, winGameRequest.winnerName());
        gameService.endGame(id);
        return ResponseEntity.ok().build();
    }
}
