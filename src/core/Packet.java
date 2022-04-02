package core;
  
  
  
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ByteOrder;
import java.io.IOException;
import java.net.InetAddress;

import constants.PacketType;
  


public class Packet
{
	private InetAddress address;
  private int port;
  private int type;
  private int[] payload;
 
	{
		type = -1;
		payload = new int[0];
	}

  public Packet type(PacketType t) { type = t.ordinal(); return this; }
  public Packet payload(int[] p) { payload = p; return this; }
 	
  public int type() { return type; }
  public int[] payload() { return payload; }
	public InetAddress address() { return address; }
  public int port() { return port; }
 	
	public Packet() {}
  public Packet(InetAddress address, int port)
  {   
    this.address = address;
    this.port = port;
  }

  public boolean parse(byte[] data)
  {
		IntBuffer intBuf = ByteBuffer.wrap(data)
			.order(ByteOrder.BIG_ENDIAN)
			.asIntBuffer();

		int[] asInt = new int[intBuf.remaining()];
		intBuf.get(asInt);
		
		if (asInt.length < 1)
			return false;

    type = asInt[0];
		payload = new int[asInt.length - 1];

		for (int i = 0; i < payload.length; ++i)
			payload[i] = asInt[i + 1];

		return true;
  }   
 
  public byte[] make()
  {
		ByteBuffer byteBuffer = ByteBuffer.allocate((payload.length + 1) * 4); 
    IntBuffer intBuffer = byteBuffer.asIntBuffer();
 
		intBuffer.put(type);
    intBuffer.put(payload);
 
    return byteBuffer.array();
  }   
}
