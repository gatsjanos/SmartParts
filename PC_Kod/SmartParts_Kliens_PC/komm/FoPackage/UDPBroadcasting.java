package FoPackage;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketTimeoutException;
import java.util.Enumeration;

public class UDPBroadcasting
{
	public static void Broadcast()
	{
		for(int i = 0; i < 3; ++i)
		{
			DatagramSocket c;
			// Find the server using UDP broadcast
			try
			{
				//Open a random port to send the package
				c = new DatagramSocket();
				c.setBroadcast(true);

				byte[] sendData = "UDPBROADCAST_Felderites_SmartPartsSzerver_KERELEM_1998895900".getBytes();

				//Try the 255.255.255.255 first
				try
				{
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
							InetAddress.getByName("255.255.255.255"), FoClass.UDPBroadcastPortSzam);
					c.send(sendPacket);
					System.out.println(">>> Request packet sent to: 255.255.255.255 (DEFAULT)");
				}
				catch (Exception e)
				{}

				// Broadcast the message over all the network interfaces
				Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
				while(interfaces.hasMoreElements())
				{
					NetworkInterface networkInterface = interfaces.nextElement();

					if(networkInterface.isLoopback() || !networkInterface.isUp())
					{
						continue; // Don't want to broadcast to the loopback interface
					}

					for(InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses())
					{
						InetAddress broadcast = interfaceAddress.getBroadcast();
						if(broadcast == null)
						{
							continue;
						}

						// Send the broadcast package!
						try
						{
							DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, FoClass.UDPBroadcastPortSzam);
							c.send(sendPacket);
						}
						catch (Exception e)
						{}

						System.out.println(">>> Request packet sent to: " + broadcast.getHostAddress() + "; Interface: "
								+ networkInterface.getDisplayName());
					}
				}

				System.out.println(">>> Done looping over all network interfaces. Now waiting for a reply!");

				//Wait for a response
				byte[] recvBuf = new byte[1500];
				DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
				c.setSoTimeout(5000);
				c.receive(receivePacket);

				//We have a response
				System.out
						.println(">>> Broadcast response from server: " + receivePacket.getAddress().getHostAddress());

				//Check if the message is correct
				String message = new String(receivePacket.getData()).trim();
				if(message.equals("UDPBROADCAST_Felderites_SmartPartsSzerver_VALASZ_1998895900"))
				{
					//DO SOMETHING WITH THE SERVER'S IP
					System.out.println("IP Address of the server: " + receivePacket.getAddress().getHostAddress());
					FoClass.TCPCim = receivePacket.getAddress().getHostAddress();
				}

				//Close the port!
				c.close();
				
				TCPFrissitoSzal.TCPHibaszamlalo = 0;
				return;
			}
			catch (SocketTimeoutException ex)
			{
				System.out.println("Socket idõtúllépés: " + ex.getMessage());
			}
			catch (IOException ex)
			{
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}
		}
	}
}
