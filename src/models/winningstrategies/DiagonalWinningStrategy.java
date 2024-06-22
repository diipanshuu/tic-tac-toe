package models.winningstrategies;

import models.Board;
import models.Move;
import models.Symbol;

import java.util.HashMap;
import java.util.Map;

public class DiagonalWinningStrategy implements WinningStrategy{
    Map<Symbol, Integer> letfDiagMap = new HashMap<>();
    Map<Symbol, Integer> rightDiagMap = new HashMap<>();

    @Override
    public boolean checkWinner(Move move, Board board) {
        int row = move.getCell().getRow();
        int col = move.getCell().getCol();
        Symbol symbol = move.getPlayer().getSymbol();

        if(row == col) {
            letfDiagMap.put(symbol,
                    letfDiagMap.getOrDefault(symbol, 0) + 1);
        }
        if(row + col == board.getSize() - 1) {
            rightDiagMap.put(symbol,
                    rightDiagMap.getOrDefault(symbol, 0) + 1);
        }
        if(row == col){
            if (letfDiagMap.get(symbol).equals(board.getSize())){
                return true;
            }
        }
        if(row + col == board.getSize() - 1){
            if(rightDiagMap.get(symbol).equals(board.getSize())){
                return true;
            }
        }
        return false;
    }

    @Override
    public void handleUndo(Move move, Board board) {
        int row = move.getCell().getRow();
        int col = move.getCell().getCol();
        Symbol symbol = move.getPlayer().getSymbol();

        if(row == col && letfDiagMap.containsKey(symbol)){
            letfDiagMap.put(symbol,
                    letfDiagMap.get(symbol) - 1);
        }

        if(row + col == board.getSize() - 1 && rightDiagMap.containsKey(symbol)){
            rightDiagMap.put(symbol,
                    rightDiagMap.get(symbol) - 1);
        }
    }
}
