package objects;



import java.util.List;
import java.util.ArrayList;
import java.awt.Point;

import core.GameMngr;

import constants.Const;



public class Enemy extends ObjectTemplate_Point
{
	private int lifes;
	private long lastUpdateTime;	
	private int energy;

	{
		lifes = Const.DEFAULT_ENEMY_LIFES;
		lastUpdateTime = System.currentTimeMillis();
		energy = Const.DEFAULT_ENEMY_ENERGY;
	}

	public Enemy(int x, int y)
	{
		super(x, y);
	}

	public int lifes() { return lifes; }
	public int energy() { return energy; }

	public void bump() { lifes -= 1; }
	public void energy(int e) { energy = e; }

	public void next()
	{
		long now = System.currentTimeMillis();
    long diff = now - lastUpdateTime;

    if (diff < Const.ENEMY_UPDATE_MS)
      return;

    lastUpdateTime = now;

		Point p = GameMngr.nextStepToNearestPlayer(this, x, y);
		
		if (p == null)
			return;

		x = p.x;
		y = p.y;
	}
}
