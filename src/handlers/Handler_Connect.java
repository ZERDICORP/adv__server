package handlers;



import java.io.IOException;
import java.util.List;

import core.Server;
import core.GameMngr;
import core.Route;
import core.Handler;
import core.Packet;

import objects.Player;

import builders.Builder_Init;

import constants.Const;
import constants.LogMsg;
import constants.PacketType;

import tools.Tools;



@Route(PacketType.CONNECT)
public class Handler_Connect extends Handler
{
	@Override
	public void handle(Packet req, Packet res)
	{
		if (GameMngr.serverIsFull())
    {
      res.type(PacketType.SERVER_IS_FULL);
 
      Server.send(res);

      return;
    }   
 
    int id = GameMngr.getUnusedId();
 
    List<Integer> initData = Builder_Init.build(id);  

    res
      .type(PacketType.ACCEPT)
      .payload(initData
				.stream()
				.mapToInt(Integer::intValue)
				.toArray());

    Server.send(res);

    Player player = new Player(req.address(), req.port(), id);

    GameMngr.addPlayer(player);
	}
}
