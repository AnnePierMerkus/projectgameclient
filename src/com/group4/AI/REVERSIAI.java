package com.group4.AI;

import com.group4.util.Tile;

import java.util.List;

/***
 * @author AnnePierMerkus
 */
public class REVERSIAI extends AI {
	
    @Override
    public Tile makeMove(List<Tile> availableOptions)
    {
        availableOptions.sort((t1, t2) -> Integer.compare(t2.getWeight(), t1.getWeight()));
        System.out.println(availableOptions.get(0).getWeight());
        return availableOptions.get(0);
    }
    
}
