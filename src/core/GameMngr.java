package core;



import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.awt.Point;

import core.Packet;
import constants.PacketType;

import objects.Player;
import objects.Block;
import objects.Enemy;

import builders.Builder_Game;
import builders.Builder_Player;
import builders.Builder_Block;
import builders.Builder_Enemy;

import constants.Const;
import constants.ObjectType;
import constants.Direction;

import tools.Tools;
import tools.PathFinder;



public class GameMngr
{
	private static volatile ArrayList<Player> players;  
  private static volatile ArrayList<Block> blocks;
  private static volatile ArrayList<Enemy> enemies;
 
  private static long lastUpdateTime;
  private static int timeToNewWave;
	private static int waveSize;
  private static boolean wave;
 
  static
  {   
    players = new ArrayList<>();
    blocks = new ArrayList<>();
    enemies = new ArrayList<>();

		waveSize = Const.WAVE_SIZE_STEP;

    waitingForWave();

    new Thread(new GameProcessor()).start();
  }

  public static synchronized boolean serverIsFull() { return players.size() == Const.MAX_PLAYERS; }	

	public static synchronized void addPlayer(Player player)
	{
		/*\
		 * If the user logged into an empty
		 * server - reset the game.
		 */

		if (players.size() == 0)
			resetGame();

		players.add(player);
	}

	public static synchronized void waitingForWave()
  {   
    lastUpdateTime = System.currentTimeMillis();
    timeToNewWave = Const.TIME_TO_NEW_WAVE;
    wave = false;
  }

	public static synchronized void updateEnemies()
	{
		for (Enemy enemy : enemies)
		{
			if (enemy.lifes() == 0)
				continue;

			enemy.next();

			if (enemy.energy() < Const.DEFAULT_ENEMY_ENERGY)
				enemy.energy(enemy.energy() + 1);
		}
	}

	public static synchronized void updatePlayers()
	{
		for (int i = 0; i < players.size(); ++i)
		{
			Player player = players.get(i);
			
			if (player.afk())
			{
				Server.send(
					new Packet(player.address(), player.port())
						.type(PacketType.KICKED_AFK)
				);

				players.remove(i);
				i--;
				continue;
			}

			if (player.dead())
			{
				player.updateTimeToRebirth();
				continue;
			}

			if (player.cartridges() == 0)
			{
				player.updateReloadingTime();
				continue;
			}
		}
	}

	public static synchronized void updateWave()
	{
		if (!wave)
		{
			/*\
			 * If the wave has not started, check
			 * if the time has come for it to start.
			 */

			long now = System.currentTimeMillis();	
			long diff = now - lastUpdateTime;

			if (diff >= 1000)
			{   
				timeToNewWave -= 1;
				lastUpdateTime = now;
			}

			if (timeToNewWave == 0)
			{   
				wave = true;
				generateWave();
			}   
		}   
		else
		{
			/*\
			 * If the wave is still going on, and
			 * there are no more enemies, then it's
			 * time to end it.
			 */

			if (enemies.size() == 0)
			{   
				waitingForWave();
				
				/*\
				 * We will also restore the default
				 * value of the blocks by the entire
				 * user.
				 */

				for (Player player : players)
					player.blocks(Const.DEFAULT_PLAYER_BLOCKS);
			}   
		}
	}

	public static synchronized List<Integer> buildObjects()
	{
		List<Integer> objects = new ArrayList<>();
		
		for (Player player : players)
			objects.addAll(Builder_Player.build(player));
		
		for (Block block : blocks)
			objects.addAll(Builder_Block.build(block));

		for (Enemy enemy : enemies)
			objects.addAll(Builder_Enemy.build(enemy));

		return objects;	
	}

	public static synchronized void notifyAllPlayers()
  {
		List<Integer> objects = buildObjects();

		/*\
		 * Creating the final payload.
		 */

		int[] payload = new int[
			objects.size() +
			Builder_Game.size()
		];

		int j = 0;

		for (int i = 0; i < objects.size(); ++i, ++j)
			payload[j] = objects.get(i);

    for (int i = 0; i < players.size(); ++i)
    {
      Player player = players.get(i);

			List<Integer> gameData = Builder_Game.build(
				players.size(),
				player,
				timeToNewWave
			);

			for (int k = 0; k < Builder_Game.size(); ++k)
				payload[j + k] = gameData.get(k);

      Server.send(new Packet(player.address(), player.port())
        .type(PacketType.UPDATE)
        .payload(payload));
    }
  }

	public static synchronized Player getNearestPlayer(int x, int y)
	{
		Player nearest = null;
		int minDist = Const.MAX_POSSIBLE_DISTANCE;

		for (Player player : players)
		{
			if (player.dead())
				continue;

			int dist = (int) Math.sqrt(Math.pow(player.x() - x, 2) + Math.pow(player.y() - y, 2));
			if (dist < minDist)
			{
				minDist = dist;
				nearest = player;
			}
		}

		return nearest;
	}

	public static synchronized Point nextStepToNearestPlayer(Enemy curr, int x, int y)
	{
		/*\
		 * Let's create a mathematical model of
		 * the current state of the map for the
		 * pathfinder.
		 */

		int[] map = new int[Const.MAP_LEN];

		/*\
		 * Let's mark the targets on the map.
		 */

		for (Player player : players)
			if (!player.dead())
				map[player.y() * Const.MAP_H + player.x()] = PathFinder.TARGET;

		/*\
		 * Also, mark the barriers on the map.
		 */

		for (Block block : blocks)
			map[block.y() * Const.MAP_H + block.x()] = PathFinder.BARRIER;
		
		for (Enemy enemy : enemies)
			if (!enemy.collide(x, y))
				map[enemy.y() * Const.MAP_H + enemy.x()] = PathFinder.BARRIER;
	
		/*\
		 * Trying to find the nearest unobstructed
		 * path to one of the players.
		 */

		Point next = PathFinder.nextStep(map, Const.MAP_H, Const.MAP_H, new Point(x, y));

		/*\
		 * If there is no way to the players,
		 * then we will try to get to them
		 * through all the obstacles.
		 */

		if (next == null)
		{
			Player nearest = getNearestPlayer(x, y);
			
			/*\
			 * If there are no live players
			 * at all, we stay in place.
			 */

			if (nearest == null)
				return null;

			/*\
			 * Now let's calculate the next
			 * step.
			 */

			int vx = (int) Math.signum(nearest.x() - x);
			int vy = vx == 0 ? (int) Math.signum(nearest.y() - y) : 0;
			vx = vy == 0 ? vx : 0;

			next = new Point(x + vx, y + vy);
			
			/*\
			 * If there is a block in front of
			 * us, we will start breaking it.
			 */

			Block block = getBlockByPosition(next.x, next.y);
			
			/*\
			 * But, if we do not have enough
			 * energy, we stay in place when
			 * the energy is not restored.
			 */

			if (curr.energy() < Const.DEFAULT_ENEMY_ENERGY)
				return null;

			if (block != null)
			{
				block.bump();
  
				if (block.strength() == 0)
					blocks.remove(block);

				curr.energy(0);

				return null;
			}
		}

		/*\
		 * If there is an enemy in front of
		 * us, we remain in place.
		 */

		Enemy enemy = getEnemyByPosition(next.x, next.y);
		if (enemy != null)
			return null;

		/*\
		 * If there is a player in front of
		 * us, we will kill him!
		 */

		Player player = getPlayerByPosition(next.x, next.y);
		if (player != null && !player.dead())
			player.die();

		return next;
	}

	public static synchronized void addBlock(Player player)
	{
		if (player.blocks() == 0)
			return;

		Point p = nextStep(player.x(), player.y(), player.direction());
		if
		(
			p == null ||
			!Tools.in2DArea(
				p.x - Const.LIVING_SECTOR_X,
				p.y - Const.LIVING_SECTOR_Y,
				Const.LIVING_SECTOR_W,
				Const.LIVING_SECTOR_H
			)
		)
			return;

		player.useBlock();

		blocks.add(new Block(p.x, p.y, player.id()));
	}

	public static synchronized Point nextStep(int x, int y, int d)
	{
		Point p = Tools.vectorByDirection(d);

		x += p.x;
		y += p.y;

		if (!validPosition(x, y))
			return null;

		return new Point(x, y);
	}

	public static synchronized Point getFreePosition()
	{
		Point p = null;

		while (true)
		{
			p = new Point(
				new Random().nextInt(Const.MAP_H),
				new Random().nextInt(Const.MAP_H)
			);

			if (validPosition(p.x, p.y))
				break;
		}

		return p;
	}

	public static synchronized void generateWave()
	{
		while (enemies.size() < waveSize)
		{
			int rx = new Random().nextInt(Const.LIVING_SECTOR_X * 2);
			int ry = new Random().nextInt(Const.LIVING_SECTOR_Y * 2);

			if (rx >= Const.LIVING_SECTOR_X)
				rx = Const.MAP_H - Const.LIVING_SECTOR_X + (rx - Const.LIVING_SECTOR_X);

			if (ry >= Const.LIVING_SECTOR_Y)
				ry = Const.MAP_H - Const.LIVING_SECTOR_Y + (ry - Const.LIVING_SECTOR_Y);
			
			if (!validPosition(rx, ry))
				continue;

			enemies.add(new Enemy(rx, ry));
		}
		
		if (waveSize <= Const.MAX_ENEMIES)
			waveSize += Const.WAVE_SIZE_STEP;
	}

	public static synchronized void resetGame()
	{
		waveSize = Const.WAVE_SIZE_STEP; 
		enemies.clear();
		blocks.clear();
		waitingForWave();
	}

	public static synchronized void removePlayer(int id)
	{
		Player player = getPlayerById(id);

		if (player == null)
			return;

		players.remove(player);

		removeBlocksByPlayerId(id);
	}

	public static synchronized void removeEnemy(Enemy enemy)
	{
		for (int i = 0; i < enemies.size(); ++i)
			if (enemies.get(i) == enemy)
			{
				enemies.remove(i);
				break;
			}
	}

	public static synchronized void removeBlock(Block block)
	{
		for (int i = 0; i < blocks.size(); ++i)
			if (blocks.get(i) == block)
			{
				blocks.remove(i);
				break;
			}
	}

	public static synchronized void removeBlocksByPlayerId(int id)
	{
		for (int i = 0; i < blocks.size(); ++i)
			if (blocks.get(i).creatorId() == id)
			{
				blocks.remove(i);
				i--;
			}
	}

	public static synchronized Player getPlayerById(int id)
	{
		for (Player player : players)
			if (player.id() == id)
				return player;
		return null;
	}

	public static synchronized Player getPlayerByPosition(int x, int y)
	{
		for (Player player : players)
			if (player.collide(x, y))
				return player;
		return null;
	}

	public static synchronized Block getBlockByPosition(int x, int y)
	{
		for (Block block : blocks)
			if (block.collide(x, y))
				return block;
		return null;
	}

	public static synchronized Enemy getEnemyByPosition(int x, int y)
	{
		for (Enemy enemy : enemies)
			if (enemy.collide(x, y))
				return enemy;
		return null;
	}

	public static synchronized boolean validPosition(int x, int y)
	{
		return Tools.in2DArea(x, y, Const.MAP_H, Const.MAP_H) &&
			isThereAnyObject(x,y) == false;
	}

	public static synchronized boolean isThereAnyObject(int x, int y)
	{
		Player player = getPlayerByPosition(x, y);

		return (player != null && !player.dead()) ||
					 getBlockByPosition(x, y) != null ||
					 getEnemyByPosition(x, y) != null;
	}

	public static synchronized int getUnusedId()
	{
		Set<Integer> usedIds = new HashSet<>();

		for (Player player : players)
			usedIds.add(player.id());

		Set<Integer> allIds = new HashSet<Integer>(Arrays.asList(Const.IDS));
 		allIds.removeAll(usedIds);

		return allIds.iterator().next();
	}
}
