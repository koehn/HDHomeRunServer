package com.koehn.hdhomerun;

import java.text.Collator;
import java.util.List;
import java.util.TreeMap;

public class HDHomeRun {

	public static void main(String args[]) {

		ChannelInfoFetcher channelInfoFetcher = new ChannelInfoFetcher();
		List<String> deviceIds = ConfigRunner.discover();
		for (String deviceId : deviceIds) {
			List<Channel> channels = channelInfoFetcher
					.getChannels(deviceId, 1);

			TreeMap<Integer, Channel> channelsByUserChannelNumber = new TreeMap<Integer, Channel>();
			TreeMap<String, Channel> channelsByLabel = new TreeMap<String, Channel>(Collator.getInstance());

			for (Channel channel : channels) {
				channelsByUserChannelNumber.put(channel.getUserChannel(),
						channel);
				channelsByLabel.put(channel.getLabel(), channel);
			}

			for (Channel channel : channelsByUserChannelNumber.values()) {
				System.out.println("Channel " + channel.getUserChannel()
						+ " is " + channel.getLabel() + " on cable channel "
						+ channel.getChannel() + ", program "
						+ channel.getProgram());
			}

			for (Channel channel : channelsByLabel.values()) {
				System.out.println("Channel " + channel.getLabel()
						+ " is channel " + channel.getUserChannel()
						+ " on cable channel " + channel.getChannel()
						+ ", program " + channel.getProgram());
			}
		}
	}
}
