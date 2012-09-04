package com.koehn.hdhomerun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigRunner {

	public static Process run(String... commands) {
		try {
			String[] allCommands = new String[commands.length + 1];
			System.arraycopy(commands, 0, allCommands, 1, commands.length);
			allCommands[0] = "hdhomerun_config";

			dumpCommand(allCommands);

			Process process = Runtime.getRuntime().exec(allCommands);
			return process;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	static void dumpCommand(String[] allCommands) {
		for (String s : allCommands) {
			System.err.print(s + " ");
		}
		System.err.println();
	}

	private static final Pattern DISCOVER_PATTERN = Pattern
			.compile("hdhomerun device ([a-f0-9A-F]+) found at ([0-9.]+)");

	public static List<String> discover() {
		Process process = run("discover");
		List<String> lines = readProcessOutput(process);
		List<String> deviceIds = new ArrayList<>(lines.size());
		for (String line : lines) {
			Matcher m = DISCOVER_PATTERN.matcher(line);
			if (m.matches()) {
				String deviceId = m.group(1);
				// String ipAddress = m.group(2);
				deviceIds.add(deviceId);
			} else {
				System.out.println("Unexpected output: " + line);
			}
		}
		return deviceIds;
	}

	public static void main(String args[]) {
		List<String> results = discover();
		for (String string : results) {
			System.out.println(string);
		}
	}

	protected static List<String> readProcessOutput(Process process) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			List<String> results = new ArrayList<>();
			String line;
			line = reader.readLine();
			while (line != null) {
				results.add(line);
				line = reader.readLine();
			}
			return results;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private static String tunerNumber(int i) {
		return "/tuner" + i;
	}

	private static String channelNumber(int num) {
		return "auto:" + num;
	}

	public static void tune(String deviceId, int tuner, int channelNumber) {
		try {
			Process process = run(deviceId, "set", tunerNumber(tuner)
					+ "/channel", channelNumber(channelNumber));
			process.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private static final Pattern CHANNEL_PATTERN = Pattern
			.compile("([0-9]+): ([0-9]+) (.*)");

	public static List<Channel> channelPrograms(String deviceId, int tuner,
			int channelNumber) {
		Process process = run(deviceId, "get", tunerNumber(tuner)
				+ "/streaminfo");
		List<String> lines = readProcessOutput(process);
		List<Channel> channels = new ArrayList<>(lines.size());
		for (String line : lines) {
			Matcher m = CHANNEL_PATTERN.matcher(line);
			if (m.matches()) {
				int userChannel = Integer.parseInt(m.group(2));
				if (userChannel != 0) {
					int programNumber = Integer.parseInt(m.group(1));
					String label = m.group(3);
					Channel channel = new Channel();
					channel.setChannel(channelNumber);
					channel.setProgram(programNumber);
					channel.setUserChannel(userChannel);
					channel.setLabel(label);
					channels.add(channel);
				}
			}
		}
		return channels;
	}
}
