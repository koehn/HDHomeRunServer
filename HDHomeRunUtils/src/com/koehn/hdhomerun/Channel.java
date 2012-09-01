package com.koehn.hdhomerun;

public class Channel {

	private int channel;
	private int program;
	private int userChannel;
	private String label;

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channelNumber) {
		this.channel = channelNumber;
	}

	public int getProgram() {
		return program;
	}

	public void setProgram(int program) {
		this.program = program;
	}

	public int getUserChannel() {
		return userChannel;
	}

	public void setUserChannel(int userChannel) {
		this.userChannel = userChannel;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		label = label.replace(" (encrypted)", "");
		this.label = label;
	}

}
