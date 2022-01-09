package xf.xflp.report;

import xf.xflp.base.container.Container;


/** 
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * Summary of a values for a route.
 * 
 * Events can be added, where the values are updated directly.
 * @author hschneid
 */
public class ContainerReportSummary {

	private int nbrOfLoadedPackages = 0;
	private int nbrOfUnLoadedPackages = 0;
	private float maxUsedVolume = 0;
	private float maxVolume = 0;
	private float maxUsedWeight = 0;

	public ContainerReportSummary(Container con) {
		maxVolume = con.getHeight() * con.getLength() * con.getWidth();
	}
	
	public void add(LPPackageEvent e) {
		if(e.getType() == LoadType.LOAD) {
			nbrOfLoadedPackages++;
			// Only loaded items increase the max loaded volume/weight values
			maxUsedVolume += e.getUsedVolumeInContainer();
			maxUsedWeight = Math.max(maxUsedWeight, e.getUsedWeightInContainer());
		} else if(e.getType() == LoadType.UNLOAD) {
			nbrOfUnLoadedPackages++;
		}
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
	 * @return max volume
	 */
	public float getMaxVolume() {
		return maxVolume;
	}
	
	/**
	 * 
	 * @return utilization of container
	 */
	public float getUtilization() {
		return (maxVolume > 0) ? maxUsedVolume / maxVolume : 0;
	}
}
