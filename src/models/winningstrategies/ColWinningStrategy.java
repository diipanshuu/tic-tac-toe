package models.winningstrategies;

import models.Board;
import models.Move;
import models.Symbol;

import java.util.HashMap;
import java.util.Map;

public class ColWinningStrategy implements WinningStrategy{
    Map<Integer, Map<Symbol, Integer>> counts = new
            HashMap<>();
    @Override
    public boolean checkWinner(Move move, Board board) {
        int col = move.getCell().getCol();
        Symbol symbol = move.getPlayer().getSymbol();

        if(!counts.containsKey(col)){
            counts.put(col, new HashMap<Symbol, Integer>());
        }

        Map<Symbol, Integer> colMap = counts.get(col);
        colMap.put(symbol, colMap.getOrDefault(symbol, 0) + 1);

        if(colMap.get(symbol) == board.getSize()){
            return true;
        }

        return false;
    }

    @Override
    public void handleUndo(Move move, Board board) {
        int col = move.getCell().getCol();
        Symbol symbol = move.getPlayer().getSymbol();
        if(counts.containsKey(col)){
            Map<Symbol, Integer> colMap = counts.get(col);
            colMap.put(symbol, colMap.get(symbol) - 1);
        }
    }
}
