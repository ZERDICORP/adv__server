package constants;



import java.util.stream.IntStream;



public interface Const
{
	int MAP_W = 20; 
  int MAP_H = 20; 
  int MAP_LEN = MAP_W * MAP_H;
  int LIVING_SECTOR_W = 12; 
  int LIVING_SECTOR_H = 12; 
  int LIVING_SECTOR_X = (MAP_W - LIVING_SECTOR_W) / 2;
  int LIVING_SECTOR_Y = (MAP_H - LIVING_SECTOR_H) / 2;
	int MAX_POSSIBLE_DISTANCE = (int) Math.sqrt(Math.pow(MAP_W, 2) + Math.pow(MAP_H, 2));
  int GAME_UPDATE_MS = 100;	
	int PAYLOAD_OBJECT_END = -1;
	int MAX_PLAYERS = 4;
	int MAX_ENEMIES = 30;
	int MAX_UNIQUE_WAVES = 6;
	int WAVE_SIZE_STEP = MAX_ENEMIES / MAX_UNIQUE_WAVES;
	int REBIRTH_TIME = 5;
	int DEFAULT_PLAYER_BLOCKS = 20;
	int DEFAULT_PLAYER_CARTRIDGES = 4;
	int ENEMY_UPDATE_MS = 500;
	int RELOADING_TIME = 3;
	int DEFAULT_ENEMY_LIFES = 3;
	int TIME_TO_NEW_WAVE = 100;
	int DEFAULT_ENEMY_ENERGY = 50;
	int TIME_TO_KICK_AFK_MS = 60000;

	Integer[] IDS = IntStream
		.range(0, MAX_PLAYERS)
		.boxed()
		.toArray(Integer[]::new);
}
