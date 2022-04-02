package handlers;



import java.io.IOException;

import core.GameMngr;
import core.Route;
import core.Handler;
import core.Packet;
import constants.PacketType;

import objects.Player;



@Route(PacketType.MOVE)
public class Handler_Move extends Handler
{
	@Override
	public void handle(Packet req, Packet res)
	{
		int[] payload = req.payload();
             
    if (payload.length < 2)
      return;

		Player player = GameMngr.getPlayerById(payload[0]);

		if (player == null || player.dead())
      return;

		player.next(payload[1]);
		player.iAmNotAfk();
	}
}
