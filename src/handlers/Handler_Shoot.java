package handlers;



import java.io.IOException;
  
import core.GameMngr;
import core.Route;
import core.Handler;
import core.Packet;
import constants.PacketType;

import objects.Player;



@Route(PacketType.SHOOT)
public class Handler_Shoot extends Handler
{
	@Override
	public void handle(Packet req, Packet res)
	{
		int[] payload = req.payload();
		
		if (payload.length == 0)
			return;

		Player player = GameMngr.getPlayerById(payload[0]);

		if (player == null)
      return;

		player.takeShot();
		player.iAmNotAfk();
	}
}
