package core;



import java.awt.Point;

import objects.Player;
import objects.Block;
import objects.Enemy;

import tools.Tools;

import constants.Const;



public class GameProcessor implements Runnable
{
	/* main game cycle */
	@Override
	public void run()
	{
		long start = System.currentTimeMillis();

		while (true)
		{
			long now = System.currentTimeMillis();
			long diff = now - start;

			if (diff < Const.GAME_UPDATE_MS)
				continue;

			GameMngr.updateWave();
			GameMngr.updatePlayers();
			GameMngr.updateEnemies();

			GameMngr.notifyAllPlayers();

			start = System.currentTimeMillis();
		}
	}
}
