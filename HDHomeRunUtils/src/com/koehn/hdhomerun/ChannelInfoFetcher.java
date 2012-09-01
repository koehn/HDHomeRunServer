package com.koehn.hdhomerun;

import java.util.ArrayList;
import java.util.List;

public class ChannelInfoFetcher {
	private static final int START_CHANNEL = 1;
	private static final int END_CHANNEL = 158;

	public List<Channel> getChannels(String deviceId, int tuner) {
		List<Channel> channels = new ArrayList<Channel>(999);
		
		for (int channelNumber = START_CHANNEL; channelNumber <= END_CHANNEL; channelNumber++) {
			ConfigRunner.tune(deviceId, tuner, channelNumber);
			try {
				Thread.sleep(1250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<Channel> channelPrograms = ConfigRunner.channelPrograms(deviceId, tuner, channelNumber);
			channels.addAll(channelPrograms);
		}
				
		return channels;
	}
	
}
