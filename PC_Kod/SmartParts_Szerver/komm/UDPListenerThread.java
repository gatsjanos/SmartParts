import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class UDPListenerThread extends Thread
{
	DatagramSocket socket;

	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				//Keep a socket open to listen to all the UDP trafic that is destined for this port
				socket = new DatagramSocket(FoClass.UDPBroadcastPortSzam, InetAddress.getByName("0.0.0.0"));
				socket.setBroadcast(true);

				System.out.println(getClass().getName() + ">>>Ready to receive broadcast packets!");

				//Receive a packet
				byte[] recvBuf = new byte[1500];
				DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);

				socket.setSoTimeout(30000);
				try
				{
					socket.receive(packet);
				}
				catch (SocketTimeoutException e)
				{
					socket.close();
					continue;
				}

				//Packet received
				System.out.println(getClass().getName() + ">>>Discovery packet received from: "
						+ packet.getAddress().getHostAddress());
				System.out.println(getClass().getName() + ">>>Packet received; data: " + new String(packet.getData()));

				//See if the packet holds the right command (message)
				String message = new String(packet.getData()).trim();
				if(message.equals("UDPBROADCAST_Felderites_SmartPartsSzerver_KERELEM_1998895900"))
				{
					byte[] sendData = "UDPBROADCAST_Felderites_SmartPartsSzerver_VALASZ_1998895900".getBytes();

					//Send a response
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(),
							packet.getPort());
					socket.send(sendPacket);

					System.out.println(
							getClass().getName() + ">>>Sent packet to: " + sendPacket.getAddress().getHostAddress());
				}

			}
			catch (IOException ex)
			{
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}
			try
			{
				socket.close();
			}
			catch (Exception e)
			{}
		}
	}

	public static UDPListenerThread getInstance()
	{
		return DiscoveryThreadHolder.INSTANCE;
	}

	private static class DiscoveryThreadHolder
	{

		private static final UDPListenerThread INSTANCE = new UDPListenerThread();
	}

}