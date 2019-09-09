package controlador;

import java.io.IOException;
import java.util.Scanner;

import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.Packet;

public class JpcapUtil {
	private JpcapCaptor captor=null;
	private JpcapSender sender=null;
	private Thread captorThread=null;
	private static JpcapUtil instance=null;
	
	/**
	 * argv: -1 choose netcard
	 * 		 >0 using assigned netcardID
	 * 		on zyf pc 1 means wlan0 3 means reltek
	 * 		on zyf_work_pc 1 means wlan
	 */
	private static int deviceID=0;
	private JpcapUtil(){
		super();
	}
	
	
	public boolean startCaptorWithReciever(final PacketReceiver receiver){
		boolean is_success=false;
		if(instance==null)
			return is_success;
		if(captorThread==null){
			captorThread=new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(instance!=null){
						instance.captor.loopPacket(-1, receiver);
					}
				}
			});
		}
		captorThread.start();
		is_success=true;
		return is_success;
	}
	public void stopCaptor(){
		captorThread.stop();
	}
	public void closeCaptor(){
		captorThread.stop();
		captor.close();
	}
	public void sendPacket(Packet p){
		if(sender!=null)
			sender.sendPacket(p);
	}
	/**
	 * TODO dinamicly get
	 * @return
	 */
	public static byte[] getMyMac(){
//		byte[] mac={0x74,(byte) 0xe5,0x0b,0x45,(byte) 0x82,0x7a};//无线网卡
//		byte[] mac={(byte) 0xf0,(byte) 0xde,(byte) 0xf1,(byte) 0x8d,0x3f,(byte) 0xda};//有线网卡
	
		byte[] rMac=new byte[6];
		for(int i=0;i<6;i++){
			rMac[i]=((JpcapCaptor.getDeviceList()[deviceID]).mac_address[i]);
		}
		return rMac;
	}
}
