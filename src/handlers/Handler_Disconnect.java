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



@Route(PacketType.DISCONNECT)
public class Handler_Disconnect extends Handler
{
	@Override
	public void handle(Packet req, Packet res)
	{
		int[] payload = req.payload();

		if (payload.length == 0)
			return;

    GameMngr.removePlayer(payload[0]);
	}
}
