package com.sainsburys.dpas.provider.dataaccess.enumeration;

import java.util.HashMap;
import java.util.Map;

public enum ChannelType {
	SAINSBURYS ("sainsburys-sku","S"),
	ARGOS ("argos-sku","A"),
	HABITAT ("habitat-sku","H");
	
	private String channelDescriptor;
	private String channelName;
	private final static Map<String, ChannelType> nameMap = new HashMap<String, ChannelType>();
	private final static Map<String, ChannelType> descriptorMap = new HashMap<String, ChannelType>();
	static {
		nameMap.put(ARGOS.getChannelName(), ARGOS);
		nameMap.put(SAINSBURYS.getChannelName(), SAINSBURYS);
		nameMap.put(HABITAT.getChannelName(), HABITAT);
		
		descriptorMap.put(ARGOS.getChannelDescriptor(), ARGOS);
		descriptorMap.put(SAINSBURYS.getChannelDescriptor(), SAINSBURYS);
		descriptorMap.put(HABITAT.getChannelDescriptor(), HABITAT);
	}
	
	
	ChannelType(String channelName, String channelDescriptor) {
		this.channelDescriptor = channelDescriptor;
		this.channelName = channelName;
	}

	public String getChannelDescriptor() {
		return channelDescriptor;
	}

	public String getChannelName() {
		return channelName;
	}


	public static ChannelType valueByIdentifier(String identifier) {
		return descriptorMap.get(identifier);
		
	}
	
	public static ChannelType valueByName(String name) {
		return nameMap.get(name);
	}
}
