package models.botstrategies;

import models.BotDifficultyLevel;

public class BotPlayingStrategyFactory {
    public static BotPlayingStrategy getStrategy(BotDifficultyLevel botDifficultyLevel){
        if(botDifficultyLevel == BotDifficultyLevel.EASY){
            return new EasyBotPlayingStrategy();
        }else return null;
    }
}
