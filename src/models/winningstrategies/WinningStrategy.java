package models.winningstrategies;

import models.Board;
import models.Move;

public interface WinningStrategy {
     boolean checkWinner(Move move, Board board);
     void handleUndo(Move move, Board board);
}
