package xf.xflp.report;

import xf.xflp.base.problem.Container;


/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * Summary of a values for a route.
 * 
 * Events can be added, where the values are updated directly.
 * 
 * @author hschneid
 *
 */
public class ContainerReportSummary {

	private final Container container;
	
	private int nbrOfLoadedPackages = 0;
	private int nbrOfUnLoadedPackages = 0;
	private float maxUsedVolume = 0;
	private float maxVolume = 0;
	private float maxUsedWeight = 0;

	/**
	 * 
	 * @param con
	 */
	public ContainerReportSummary(Container con) {
		this.container = con;
		maxVolume = con.getMaxVolume();
	}
	
	/**
	 * 
	 * @param e
	 */
	public void add(LPPackageEvent e) {
		if(e.getType() == PackageEventType.LOAD)
			nbrOfLoadedPackages++;
		else if(e.getType() == PackageEventType.UNLOAD)
			nbrOfUnLoadedPackages++;
		maxUsedVolume += e.getUsedVolumeInContainer();
		maxUsedWeight = Math.max(maxUsedWeight, e.getUsedWeightInContainer());
	}

	/**
	 * @return the vehicle
	 */
	public final Container getVehicle() {
		return container;
	}

	/**
	 * @return the nbrOfLoadedPackages
	 */
	public final int getNbrOfLoadedPackages() {
		return nbrOfLoadedPackages;
	}

	/**
	 * @return the nbrOfUnLoadedPackages
	 */
	public final int getNbrOfUnLoadedPackages() {
		return nbrOfUnLoadedPackages;
	}

	/**
	 * @return the maxUsedVolume
	 */
	public final float getMaxUsedVolume() {
		return maxUsedVolume;
	}

	/**
	 * @return the maxUsedWeight
	 */
	public final float getMaxUsedWeight() {
		return maxUsedWeight;
	}
	
	/**
	 * 
	 * @return
	 */
	public float getMaxVolume() {
		return maxVolume;
	}
	
	/**
	 * 
	 * @return
	 */
	public float getUtilization() {
		return maxUsedVolume/maxVolume;
	}
}
