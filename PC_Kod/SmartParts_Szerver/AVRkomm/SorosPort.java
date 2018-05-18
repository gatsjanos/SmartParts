import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;
import java.util.*;

public class SorosPort
{
	public jssc.SerialPort sp;
	public int HibaSzamlalo = 0;
	
	public List<Short> FogadottByteok = new LinkedList<Short>();

	public void Kezfogas()
	{
		String UARTAzonositoKulcs = "SmartPartsKP";

		String[] portok = SerialPortList.getPortNames();

		boolean sikeresport = false;
		for(int k = 0; k < 2 && !sikeresport; k++)
		{
			for(int i = 0; i < portok.length && !sikeresport; ++i)
			{
				try
				{
					try
					{
						if(sp.isOpened())
							sp.closePort();
					}
					catch (Exception e)
					{}
					try
					{
						sp = new SerialPort(portok[i]);

						sp.openPort();
						sp.setParams(SerialPort.BAUDRATE_57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
								SerialPort.PARITY_NONE);
					}
					catch (Exception e)
					{
						continue;
					}
					sp.addEventListener(new SerialPortEventListener()
					{

						@Override
						public void serialEvent(SerialPortEvent arg0)
						{
							if(arg0.isRXCHAR())
							{
								try
								{
									//System.out.println("UART Data Received!");
									byte[] buff = sp.readBytes();

									for(byte item : buff)
									{
										FogadottByteok.add(AVRCom.ByteElojelEltavolit(item));
									}
								}
								catch (SerialPortException e)
								{
									System.out.println(e.getMessage());
									e.printStackTrace();
								}
							}

						}

					});
					FogadottByteok.clear();

					this.WriteByte((short) 3);
					this.WriteByte((short) 3);
					this.WriteByte((short) 255);

					sikeresport = false;
					for(int x = 0; x < 300 / 4; ++x)
					{
						try
						{
							Thread.sleep(4);
						}
						catch (Exception e)
						{}
						if(FogadottByteok.size() >= UARTAzonositoKulcs.length())
						{
							String bejovo = "";
							for(Short item : FogadottByteok)
							{
								bejovo += (char) (item & 0x00FF);
							}
							if(bejovo.equals(UARTAzonositoKulcs))
							{
								sikeresport = true;
								break;
							}
						}
					}
				}
				catch (Exception e)
				{
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}
		}

		if(sikeresport)
		{
			this.HibaSzamlalo = 0;
		}
		else
		{
			System.out.println("Hiba a Központhoz való csatlakozás során!");
		}
			

	}

	public void WriteByte(short be) throws SerialPortException
	{
		sp.writeByte((byte) (be & 0x00FF));
	}
}
