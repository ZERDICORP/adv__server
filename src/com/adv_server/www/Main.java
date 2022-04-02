package com.adv_server.www;



import java.net.SocketException;

import tools.Tools;
import tools.ConfigLoader;

import constants.LogMsg;

import configs.ServerConfig;

import core.Server;

import handlers.Handler_Move;
import handlers.Handler_Shoot;
import handlers.Handler_SetBlock;
import handlers.Handler_Connect;
import handlers.Handler_Disconnect;



public class Main
{
	public static void main(String[] args)
	{
		ConfigLoader.load(ServerConfig.class, "resources/server.cfg");

		Server.addHandler(new Handler_Move());
		Server.addHandler(new Handler_Shoot());
		Server.addHandler(new Handler_SetBlock());
		Server.addHandler(new Handler_Connect());
		Server.addHandler(new Handler_Disconnect());

		try
		{
			Server.run();
		}
		catch (SocketException e)
		{
			Tools.log(LogMsg.SERVER_WENT_DOWN_FOR_SOME_REASON);
			e.printStackTrace();
		}
	}
}
