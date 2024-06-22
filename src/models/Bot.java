package models;

import models.botstrategies.BotPlayingStrategy;
import models.botstrategies.BotPlayingStrategyFactory;

public class Bot extends Player {
    private BotDifficultyLevel botDifficultyLevel;
    private BotPlayingStrategy botPlayingStrategy;

    public Bot(Long id, String name, Symbol symbol, BotDifficultyLevel botDifficultyLevel) {
        super(id, name, symbol, PlayerType.BOT);
        this.botDifficultyLevel = botDifficultyLevel;
        this.botPlayingStrategy = BotPlayingStrategyFactory.
                getStrategy(botDifficultyLevel);
    }

    public void setBotDifficultyLevel(BotDifficultyLevel botDifficultyLevel) {
        this.botDifficultyLevel = botDifficultyLevel;
    }

    public BotDifficultyLevel getBotDifficultyLevel() {
        return botDifficultyLevel;
    }

    /**
     * @param board
     * @return Move
     * Diff implementation from Human Player
     */
    @Override
    public Move makeMove(Board board) {
        return botPlayingStrategy.makeMove(board, this);
    }
}
