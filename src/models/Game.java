package models;

import exceptions.BotCountMoreThanOneException;
import exceptions.DuplicateSymbolException;
import exceptions.PlayerCountMisMatchException;
import models.winningstrategies.WinningStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private Board board;
    private List<Player> players;
    private List<Move> moves;
    private Player winner;
    private GameState gameState;
    private int nextMovePlayerIndex;
    private List<WinningStrategy> winningStrategies;

    private Game(List<Player> players, int dimension, List<WinningStrategy> winningStrategies){
        this.board = new Board(dimension);
        this.players = players;
        moves = new ArrayList<>();
        gameState = GameState.IN_PROGRESS;
        this.winningStrategies = winningStrategies;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Player getWinner() {
        return winner;
    }

    public GameState getGameState() {
        return gameState;
    }

    public static class Builder{
        private List<Player> players;
        private int dimension;
        private List<WinningStrategy> winningStrategies;

        private Builder(){
            this.players = new ArrayList<>();
            this.winningStrategies = new ArrayList<>();
        }

        public Builder setPlayers(List<Player> players){
            this.players = players;
            return this;
        }

        public Builder addPlayer(Player player){
            this.players.add(player);
            return this;
        }

        public Builder setDimension(int dimensions){
            this.dimension = dimensions;
            return this;
        }

        public Builder setWinningStrategies(List<WinningStrategy> winningStrategies){
            this.winningStrategies = winningStrategies;
            return this;
        }

        public Builder addWinningStrategy(WinningStrategy winningStrategy){
            this.winningStrategies.add(winningStrategy);
            return this;
        }

        // TODO: Move the validation logic to another class
        public void validatePlayersCount() throws PlayerCountMisMatchException{
            if(players.size() != dimension - 1){
                throw new PlayerCountMisMatchException();
            }
        }

        public void validateUniqueSymbolForPlayer() throws DuplicateSymbolException {
            Map<Character, Integer> count = new HashMap<>();
            for (Player player: players
                 ) {
                Character symbol = player.getSymbol().getaChar();
                count.put(symbol, count.getOrDefault(symbol, 0) + 1);
                if(count.get(symbol) > 1){
                    throw new DuplicateSymbolException();
                }
            }
        }

        public void validateBotCount() throws BotCountMoreThanOneException {
            int botCount = 0;
            for(Player player : players){
                if(player.getPlayerType() == PlayerType.BOT){
                    botCount++;
                }
            }
            if(botCount > 1) throw new BotCountMoreThanOneException();
        }

        // TODO: Add more exceptions to check nulls

        public void validate() throws PlayerCountMisMatchException, DuplicateSymbolException, BotCountMoreThanOneException {
            validatePlayersCount();
            validateUniqueSymbolForPlayer();
            validateBotCount();
        }

        public Game build() throws PlayerCountMisMatchException, BotCountMoreThanOneException, DuplicateSymbolException {
            validate();
            return new Game(players, dimension, winningStrategies);
        }

    }
    public static Builder getBuilder(){
        return new Builder();
    }

    /*
    * logic to make a move and validate
    * and offloading the work of making
    * the move to the player
    * */
    public void makeMove(){
        Player nextPlayerToMakeAMove = players.get(nextMovePlayerIndex);

        System.out.println("It is " + nextPlayerToMakeAMove.getName() +
                "'s turn to make a move. Please make a move!");

        /*
        * Here because makeMove will get called on
        * diff types of Players without using If Else
        * */
        Move move = nextPlayerToMakeAMove.makeMove(board);
        if(!validateMove(move)){
            System.out.println("Invalid move. Please try again!");
            return;
        }

        int row = move.getCell().getRow();
        int col = move.getCell().getCol();

        Cell cellToUpdate = board.getBoard()
                .get(row).get(col);
        cellToUpdate.setCellState(CellState.FILLED);
        cellToUpdate.setPlayer(nextPlayerToMakeAMove);

        Move finalMove = new Move(cellToUpdate, nextPlayerToMakeAMove);
        moves.add(finalMove);

        nextMovePlayerIndex += 1;
        nextMovePlayerIndex %= players.size();

        if(checkWinner(finalMove)){
            gameState = GameState.WIN;
            winner = nextPlayerToMakeAMove;
        } else if (moves.size() == board.getSize() * board.getSize()) {
            gameState = GameState.DRAW;
        }
    }

    private boolean checkWinner(Move move){
        for(WinningStrategy winningStrategy : winningStrategies){
            if(winningStrategy.checkWinner(move, board)){
                return true;
            }
        }

        return false;
    }

    private boolean validateMove(Move move){
        int row = move.getCell().getRow();
        int col = move.getCell().getCol();

        // Check whether the cell is out of bounds or not
        if (row >= board.getSize() || col >= board.getSize()){
            return false;
        }

        // Check the game status
        if (gameState != GameState.IN_PROGRESS){
            return false;
        }

        // Check the cell status
        if (board.getBoard().get(row).get(col).getCellState() == CellState.EMPTY){
            return true;
        }

        return false;
    }
    public void printBoard(){
        board.printBoard();
    }

    public void undo(){
        if(moves.size() == 0){
            System.out.println("Board is empty can not undo!");
            return;
        }

        Move lastMove = moves.get(moves.size() - 1);
        /*
        * Call to handleUndo to update
        * the relevant winningStrategy
        * */
        for(WinningStrategy winningStrategy : winningStrategies){
            winningStrategy.handleUndo(lastMove, board);
        }
        moves.remove(lastMove);

        Cell cell = lastMove.getCell();
        cell.setCellState(CellState.EMPTY);
        cell.setPlayer(null);

        nextMovePlayerIndex -= 1;
        nextMovePlayerIndex = (nextMovePlayerIndex + players.size()) % players.size();
    }
}
