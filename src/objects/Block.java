package objects;



import java.util.List;
import java.util.ArrayList;



public class Block extends ObjectTemplate_Point
{
	private int strength = 3;
	private int creatorId;

	public Block(int x, int y, int creatorId)
	{
		super(x, y);
		this.creatorId = creatorId;
	}

	public int strength() { return strength; }
	public int creatorId() { return creatorId; }

	public void bump() { strength -= 1; }
}
