package core;



import core.Packet;



public abstract class Handler
{
	public abstract void handle(Packet req, Packet res);
}
