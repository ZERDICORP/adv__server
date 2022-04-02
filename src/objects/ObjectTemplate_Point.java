package objects;



public class ObjectTemplate_Point
{
	protected int x;
	protected int y;
	
	protected ObjectTemplate_Point() {}
	protected ObjectTemplate_Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public int x() { return x; }
	public int y() { return y; }

	public void pos(int x, int y) { this.x = x; this.y = y; }

	public boolean collide(int x, int y) { return this.x == x && this.y == y; }
}
