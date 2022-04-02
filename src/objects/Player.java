package objects;



import java.net.InetAddress;
import java.util.List;
import java.util.ArrayList;
import java.awt.Point;

import core.GameMngr;

import objects.Player;
import objects.Block;
import objects.Enemy;

import constants.Direction;
import constants.Const;

import tools.Tools;



public class Player extends ObjectTemplate_Point
{
	private InetAddress address;
	private int port;
	private int id;
	private long lastActionTime;
	private long lastUpdateTime;
	private int timeToRebirth;
	private int direction;
	private int blocks;
	private int timeToReload;
	private int cartridges;

	{
		direction = Direction.UP.ordinal();
		lastActionTime = System.currentTimeMillis();
	}

	public Player(InetAddress address, int port, int id)
	{
		this.address = address;
		this.port = port;
		this.id = id;

		restore();
		
		blocks = Const.DEFAULT_PLAYER_BLOCKS;
	}

	public void restore()
	{
		cartridges = Const.DEFAULT_PLAYER_CARTRIDGES;

		Point p = GameMngr.getFreePosition();

		x = (int) p.getX();
		y = (int) p.getY();
	}

	public InetAddress address() { return address; }
	public int port() { return port; }
	public int id() { return id; }
	public int blocks() { return blocks; }
	public int timeToReload() { return timeToReload; }
	public int cartridges() { return cartridges; }
	public int timeToRebirth() { return timeToRebirth; }
	public int direction() { return direction; }
	public boolean dead() { return timeToRebirth > 0; }
	
	public void iAmNotAfk() { lastActionTime = System.currentTimeMillis(); }
	public void blocks(int b) { blocks = b; }
	public void useBlock() { blocks -= 1; }
	
	public boolean afk()
	{
		long now = System.currentTimeMillis();
    long diff = now - lastActionTime;

    if (diff >= Const.TIME_TO_KICK_AFK_MS)
      return true;
		
		return false;
	}

	public void takeShot()
	{
		if (cartridges == 0)
			return;

		cartridges -= 1;
		
		Point v = Tools.vectorByDirection(direction);

		int nx = x;
		int ny = y;

		while (true)
		{
			nx = nx + v.x;
			ny = ny + v.y;

			if (!Tools.in2DArea(nx, ny, Const.MAP_W, Const.MAP_H))
				break;

			Player player = GameMngr.getPlayerByPosition(nx, ny);

			if
			(
			 	player != null &&
				player.id() != id &&
				!player.dead()
			)
			{
				player.die();
				break;
			}

			Block block = GameMngr.getBlockByPosition(nx, ny);

			if (block != null)
			{
				block.bump();

				if (block.strength() == 0)
					GameMngr.removeBlock(block);
				break;
			}

			Enemy enemy = GameMngr.getEnemyByPosition(nx, ny);

			if (enemy != null && enemy.lifes() > 0)
			{
				enemy.bump();

				if (enemy.lifes() == 0)
					GameMngr.removeEnemy(enemy);

				break;
			}
		}

		if (cartridges == 0)
		{
			lastUpdateTime = System.currentTimeMillis();
			timeToReload = Const.RELOADING_TIME;
		}
	}

	public void updateReloadingTime()
	{
		long now = System.currentTimeMillis();
    long diff = now - lastUpdateTime;

    if (diff < 1000)
      return;

		lastUpdateTime = now;

		timeToReload -= 1;

		if (timeToReload == 0)
			cartridges = Const.DEFAULT_PLAYER_CARTRIDGES;
	}

	public void updateTimeToRebirth()
	{
		long now = System.currentTimeMillis();
    long diff = now - lastUpdateTime;
        
    if (diff < 1000)
      return;
  
    lastUpdateTime = now;

		timeToRebirth -= 1;

		if (timeToRebirth == 0)
			restore();
	}

	public void die()
	{
		timeToReload = 0;
		lastUpdateTime = System.currentTimeMillis();
		timeToRebirth = Const.REBIRTH_TIME;
	}

	public void next(int d)
	{
		if (direction != d)
		{
			direction = d;
			return;
		}
		
		Point p = GameMngr.nextStep(x, y, direction);
    if (p == null)
      return;

		pos((int) p.getX(), (int) p.getY());
	}
}
