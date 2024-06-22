package models.winningstrategies;

import models.Board;
import models.Cell;
import models.Move;
import models.Symbol;

import java.util.HashMap;
import java.util.Map;

public class RowWinningStrategy implements WinningStrategy{
    Map<Integer, Map<Symbol, Integer>> map = new HashMap<>();
    @Override
    public boolean checkWinner(Move move, Board board) {
        int row = move.getCell().getRow();
        Symbol symbol = move.getPlayer().getSymbol();

        if(!map.containsKey(row)){
            map.put(row, new HashMap<Symbol, Integer>());
        }

        Map<Symbol, Integer> rowMap = map.get(row);
        rowMap.put(symbol, rowMap.getOrDefault(symbol, 0 ) + 1);


        if(rowMap.get(symbol) == board.getSize()){
            return true;
        }

        return false;
    }

    @Override
    public void handleUndo(Move move, Board board) {
        int row = move.getCell().getRow();
        Symbol symbol = move.getPlayer().getSymbol();
        if(map.containsKey(row)){
            Map<Symbol, Integer> rowMap = map.get(row);
            rowMap.put(symbol, rowMap.get(symbol) - 1);
        }
    }
}
