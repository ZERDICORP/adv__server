package tools;



import java.io.IOException;
import java.awt.Point;
import java.util.Queue;
import java.util.LinkedList;



public class PathFinder
{
	public static final int TARGET = 1;
	public static final int BARRIER = -1;

	private static final int[] vx = new int[] { 0, 0, -1, 1 };
	private static final int[] vy = new int[] { -1, 1, 0, 0 };

	private static boolean isEmpty(int[] phemap, int[] map, int w, int h, int x, int y)
	{
		return Tools.in2DArea(x, y, w, h) &&
			phemap[y * w + x] == 0 &&
			map[y * w + x] != BARRIER; 
	}

	private static Point find(int[] phemap, int[] map, int w, int h, Point start)
	{
		Queue<Point> q = new LinkedList<>();

		q.add(start);
		phemap[start.y * w + start.x] = 1;

		Point end = null;

		while (q.size() > 0)
		{
			Point p = q.poll();

			if (map[p.y * w + p.x] == TARGET)
			{
				end = p;
				break;
			}

			for (int i = 0; i < 4; i++)
			{
				int nx = p.x + vx[i];
				int ny = p.y + vy[i];

				if (isEmpty(phemap, map, w, h, nx, ny))
				{
					q.add(new Point(nx, ny));
					phemap[ny * w + nx] = phemap[p.y * w + p.x] + 1;
				}
			}
		}

		return end;
	}

	public static Point nextStep(int[] map, int w, int h, Point start)
	{
		if (map[start.y * w + start.x] == TARGET)
			return start;

		int[] phemap = new int[map.length];

		Point p = find(phemap, map, w, h, start);
		
		if (p != null)	
			while (true)
			{
				for (int i = 0; i < 4; i++)
				{
					int nx = p.x + vx[i];
					int ny = p.y + vy[i];

					if (Tools.in2DArea(nx, ny, w, h) && phemap[p.y * w + p.x] - phemap[ny * w + nx] == 1)
					{
						if (phemap[ny * w + nx] == 1)
							return p;
						
						p = new Point(nx, ny);

						break;
					}
				}
			}

		return p;
	}
}
