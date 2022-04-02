package builders;



import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import constants.Const;
import constants.ObjectType;

import objects.Player;



public class Builder_Game
{
	private static List<Integer> fields;	

	static
	{
		fields = new ArrayList<>(Arrays.asList(new Integer[] {
			ObjectType.GAME.ordinal(),
			0, 0, 0, 0, 0, 0,
			Const.PAYLOAD_OBJECT_END
		}));
	}
	
	public static int size() { return fields.size(); }	
	public static List<Integer> build(int players, Player me, int timeToNewWave)
	{
		fields.set(1, players);
		fields.set(2, me.timeToRebirth());
		fields.set(3, me.blocks());
		fields.set(5, me.timeToReload());
		fields.set(6, timeToNewWave);

		return fields;
	}
}
