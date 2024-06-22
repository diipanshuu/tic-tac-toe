package controllers;

import exceptions.BotCountMoreThanOneException;
import exceptions.DuplicateSymbolException;
import exceptions.PlayerCountMisMatchException;
import models.Game;
import models.GameState;
import models.Player;
import models.winningstrategies.WinningStrategy;

import java.util.List;

public class GameController {
    public Game startGame(List<Player> players,
                          int boardDimension,
                          List<WinningStrategy> winningStrategies) throws BotCountMoreThanOneException, DuplicateSymbolException, PlayerCountMisMatchException {
        return Game.getBuilder().
                setPlayers(players).
                setDimension(boardDimension).
                setWinningStrategies(winningStrategies).
                build();
    }

    public void makeMove(Game game){
        game.makeMove();
    }
    public GameState checkState(Game game){
        return game.getGameState();
    }
    public Player getWinner(Game game){
        game.printBoard();
        return game.getWinner();
    }

    /**
     * Game can offload the task
     * to Board to print itself
     */
    public void printBoard(Game game){
        game.printBoard();
    }

    /**
     * To undo the last move and
     * update the winningStrategy maps
     */
    public void undo(Game game){
        game.undo();
    }
}
