package com.group4.AI;

import com.group4.util.Tile;

import java.util.List;

public interface AI_Interface {
    Tile makeMove(List<Tile> availableOptions);
}
