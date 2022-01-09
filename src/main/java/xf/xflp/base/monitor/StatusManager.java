package xf.xflp.base.monitor;

import java.util.ArrayList;
import java.util.List;

/** 
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * Manages several Status Monitor objects from the user
 * and distributes the messages to the monitors.
 * @author hschneid
 */
public class StatusManager {

	private List<StatusMonitor> observerList = new ArrayList<>();
	
	private long startTime = 0;
	
	/**
	 * 
	 * @param mon
	 */
	public void addObserver(StatusMonitor mon) {
		observerList.add(mon);
	}
	
	/**
	 * 
	 * @param code
	 * @param message
	 */
	public void fireMessage(StatusCode code, String message) {
		for (StatusMonitor mon : observerList)
			mon.getMessage(code, message);
	}
	
	/**
	 * Sets an internal value to the current time
	 */
	public void setStartTime() {
		startTime = System.currentTimeMillis();
	}
	
	/**
	 * 
	 * @return Get the duration in seconds since the set start time
	 */
	public long getDurationSinceStartInSec() {
		return (long)((System.currentTimeMillis() - startTime) / 1000f);
	}

	/**
	 * Removes all accounted observers
	 */
	public void clearObserver() {
		observerList.clear();
	}
}
