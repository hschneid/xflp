package xf.xflp.report;

import java.util.HashMap;
import java.util.Map;

import xf.xflp.base.problem.Container;

/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * 
 * @author hschneid
 *
 */
public class LPReportSummary {

	private static final int LENGTH = 2;
	
	private int nbrOfUsedVehicles = 0;
	private int nbrOfNotLoadedPackages = 0;
	private float utilizationSum = 0;
	
	private Map<Container, float[]> dataMap = new HashMap<>();
	
	/**
	 * 
	 * @param t
	 */
	public void add(ContainerReport t) {
		if(!dataMap.containsKey(t.getContainer()))
			dataMap.put(t.getContainer(), new float[LENGTH]);
		float[] data = dataMap.get(t.getContainer());
		
		ContainerReportSummary routeSummary = t.getSummary();
		
		data[0]++;
		nbrOfUsedVehicles++;
		
		utilizationSum += routeSummary.getUtilization();
	}
	
	/**
	 * 
	 * @param pkg
	 */
	public void addUnplannedPackage(LPPackageEvent pkg) {
		if(pkg.getType() == PackageEventType.LOAD)
			nbrOfNotLoadedPackages++;
	}

	/**
	 * 
	 * @param veh
	 * @return the distance
	 */
	public float getNbrOfUsedVehicles(Container veh) {
		return dataMap.get(veh)[0];
	}
	
	/**
	 * 
	 * @param veh
	 * @return the tour
	 */
	public float getNbrOfNotLoadedPackages(Container veh) {
		return dataMap.get(veh)[1];
	}
	
	/**
	 * 
	 * @return
	 */
	public float getNbrOfUsedVehicles() {
		return nbrOfUsedVehicles;
	}
	
	/**
	 * 
	 * @return
	 */
	public float getNbrOfNotLoadedPackages() {
		return nbrOfNotLoadedPackages;
	}
	
	/**
	 * 
	 * @return
	 */
	public float getUtilization() {
		return utilizationSum / nbrOfUsedVehicles;
	}
}
