package com.codecool.tttbackend.controller;

import com.codecool.tttbackend.controller.dto.CreateGameRequest;
import com.codecool.tttbackend.controller.dto.JoinGameRequest;
import com.codecool.tttbackend.dao.model.Game;
import com.codecool.tttbackend.service.game.GameService;
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

    @PostMapping
    public ResponseEntity<Void> createGame(@RequestBody CreateGameRequest request) {
        gameService.createGame(request.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Game>> getAllGames() {
        return ResponseEntity.ok(gameService.listAllGames());
    }

    @PatchMapping("/{id}/join")
    public ResponseEntity<Void> joinGame(@PathVariable int id, @RequestBody JoinGameRequest request) {
        gameService.joinGame(id, request.getUser());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/start")
    public ResponseEntity<Void> startGame(@PathVariable int id){
        gameService.
        return ResponseEntity.ok().build();
    }
}
