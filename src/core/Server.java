package core;



import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import tools.Tools;
 
import constants.LogMsg;

import configs.ServerConfig;

import core.Packet;
import constants.PacketType;



public class Server extends ServerConfig
{
	private static byte[] buffer;
	private static DatagramSocket serverSocket;	
	private static ArrayList<Handler> handlers = new ArrayList<>();

	static
	{
		buffer = new byte[8000];
	}

	public static void addHandler(Handler handler) { handlers.add(handler); }
	
	public static void run() throws SocketException
	{
		serverSocket = new DatagramSocket(ServerConfig.port);
		
		System.out.println("Server started listening on port " + ServerConfig.port + "..");

		while (true)
		{
			Packet req = read();

			if (req == null)
				Tools.log(LogMsg.UNABLE_TO_READ_INCOMING_PACKET);

			Packet res = new Packet(req.address(), req.port());

			for (Handler handler : handlers)
			{
				Class<?> clazz = handler.getClass();
				
				if (clazz.isAnnotationPresent(Route.class))
				{
					Route ann = clazz.getAnnotation(Route.class);
					
					if (req.type() == ann.value().ordinal())
						handler.handle(req, res);
				}
			}
		}		
	}

	public static Packet read() throws SocketException
	{
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		
		try
		{
			serverSocket.receive(packet);
		}
		catch (IOException e) { return null; }

		Packet req = new Packet(
			packet.getAddress(),
			packet.getPort()
		);

		if (!req.parse(Arrays.copyOfRange(buffer, 0, packet.getLength())))
			return null;

		return req;
	}

	public static void send(Packet packet)
	{
		byte[] payload = packet.make();
		
		try
		{
			serverSocket.send(new DatagramPacket(
				payload,
				payload.length,
				packet.address(),
				packet.port()
			));
		}
		catch (IOException e)
		{
			Tools.log(LogMsg.FAILED_TO_SEND_PACKET);
		}
	}

	public static void stop()
	{
		serverSocket.close();
	}
}
