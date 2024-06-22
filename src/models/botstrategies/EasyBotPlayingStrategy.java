package models.botstrategies;

import models.*;

import java.util.List;

public class EasyBotPlayingStrategy implements BotPlayingStrategy{

    /**
     * @param board
     * @return
     */
    @Override
    public Move makeMove(Board board, Player player) {

        for(List<Cell> row: board.getBoard()){
            for(Cell cell : row){
                if(cell.getCellState().equals(CellState.EMPTY)){
                    return new Move(cell, player);
                }
            }
        }
        return null;

    }
}
