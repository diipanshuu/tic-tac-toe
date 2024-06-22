import controllers.GameController;
import exceptions.BotCountMoreThanOneException;
import exceptions.DuplicateSymbolException;
import exceptions.PlayerCountMisMatchException;
import models.*;
import models.winningstrategies.ColWinningStrategy;
import models.winningstrategies.DiagonalWinningStrategy;
import models.winningstrategies.RowWinningStrategy;
import models.winningstrategies.WinningStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws BotCountMoreThanOneException, DuplicateSymbolException, PlayerCountMisMatchException {
        GameController gameController = new GameController();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter size for the grid: ");
        int dimensions = scanner.nextInt();
        List<Player> players = new ArrayList<>();
        players.add(
                new Player(1L,
                        "Player1",
                        new Symbol('X'),
                        PlayerType.HUMAN));
        players.add(
                new Bot(2L,
                        "Bot1",
                        new Symbol('O'),
                        BotDifficultyLevel.EASY));

        List<WinningStrategy> winningStrategies =
                new ArrayList<>();
        winningStrategies.add(
                new RowWinningStrategy());
        winningStrategies.add(
                new ColWinningStrategy());
        winningStrategies.add(
                new DiagonalWinningStrategy());

        Game game = gameController.
                startGame(players, dimensions, winningStrategies);

        while(game.getGameState() == GameState.IN_PROGRESS){
            gameController.printBoard(game);

            System.out.println("Does any one wants to undo? y/n");
            String ch = scanner.next();
            if(ch.equalsIgnoreCase("y")) {
                gameController.undo(game);
                continue;
            }

            gameController.makeMove(game);
        }

        System.out.println("Game is finished!");
        if(game.getGameState() == GameState.WIN) {
            System.out.println("The winner is " + gameController.getWinner(game).getName());
        }else System.out.println("The game has drawn!");
    }
}
