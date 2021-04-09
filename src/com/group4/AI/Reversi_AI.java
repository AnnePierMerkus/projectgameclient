package com.group4.AI;

import com.group4.util.Player;
import com.group4.util.Tile;

import java.util.Comparator;
import java.util.List;

public class Reversi_AI implements AI_Interface {
    @Override
    public Tile makeMove(List<Tile> availableOptions)
    {
        availableOptions.sort((t1, t2) -> Integer.compare(t2.getWeight(), t1.getWeight()));
        System.out.println(availableOptions.get(0).getWeight());
        return availableOptions.get(0);
    }
}
