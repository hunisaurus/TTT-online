package com.codecool.tttbackend.dao.model.game;

import com.codecool.tttbackend.dao.model.User;
import com.codecool.tttbackend.domain.game.board.BigBoard;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "games")
public class Game {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;

   @Column(name = "creation_date", nullable = false)
   private LocalDateTime timeCreated;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "creator_id")
   private User creator;

   @Column(name = "name", nullable = false)
   private String name;

   @Enumerated(EnumType.STRING)
   @Column(name = "game_state", nullable = false)
   private GameState gameState;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "winner_id")
   private User winner;

   @Column(name = "max_players")
   private Integer maxPlayers;

   @Column(name = "board_state")
   private String boardState;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "current_player")
   private User currentPlayer;

   @Column(name = "active_board")
   private String activeBoard;

   @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<Player> players = new ArrayList<>();

   @Transient
   private BigBoard board = new BigBoard();

   public Game() {
   }

   public void addPlayer(Player player) {
      if (player == null) {
         return;
      }
      player.setGame(this);
      players.add(player);
   }

   public void removePlayer(Player player) {
      if (player == null || player.getUser() == null) {
         return;
      }

      players.removeIf(p ->
              p.getUser() != null &&
                      p.getUser().getId().equals(player.getUser().getId())
      );
   }

   public Player getCurrentPlayerAsPlayer() {
      if (currentPlayer == null || players == null) {
         return null;
      }

      return players.stream()
              .filter(player ->
                      player.getUser() != null &&
                              player.getUser().getId().equals(currentPlayer.getId()))
              .findFirst()
              .orElse(null);
   }

   public List<Character> getRotation() {
      return players.stream().map(Player::getCharacter).toList();
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public LocalDateTime getTimeCreated() {
      return timeCreated;
   }

   public void setTimeCreated(LocalDateTime timeCreated) {
      this.timeCreated = timeCreated;
   }

   public User getCreator() {
      return creator;
   }

   public void setCreator(User creator) {
      this.creator = creator;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public GameState getGameState() {
      return gameState;
   }

   public void setGameState(GameState gameState) {
      this.gameState = gameState;
   }

   public User getWinner() {
      return winner;
   }

   public void setWinner(User winner) {
      this.winner = winner;
   }

   public Integer getMaxPlayers() {
      return maxPlayers;
   }

   public void setMaxPlayers(Integer maxPlayers) {
      this.maxPlayers = maxPlayers;
   }

   public String getBoardState() {
      return boardState;
   }

   public void setBoardState(String boardState) {
      this.boardState = boardState;
   }

   public User getCurrentPlayer() {
      return currentPlayer;
   }

   public void setCurrentPlayer(User currentPlayer) {
      this.currentPlayer = currentPlayer;
   }

   public String getActiveBoard() {
      return activeBoard;
   }

   public void setActiveBoard(String activeBoard) {
      this.activeBoard = activeBoard;
   }

   public List<Player> getPlayers() {
      return players;
   }

   public void setPlayers(List<Player> players) {
      this.players.clear();
      if (players != null) {
         for (Player player : players) {
            addPlayer(player);
         }
      }
   }

   public BigBoard getBoard() {
      return board;
   }

   public void setBoard(BigBoard board) {
      this.board = board;
   }
}