package xf.xflp.report;

import xf.xflp.base.container.Container;

import java.util.HashMap;
import java.util.Map;

/** 
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 */
public class LPReportSummary {

	private static final int LENGTH = 2;
	
	private int nbrOfUsedVehicles = 0;
	private int nbrOfNotLoadedPackages = 0;
	private float utilizationSum = 0;
	
	private final Map<Container, float[]> dataMap = new HashMap<>();
	
	public void add(ContainerReport t) {
		if(!dataMap.containsKey(t.getContainer()))
			dataMap.put(t.getContainer(), new float[LENGTH]);
		float[] data = dataMap.get(t.getContainer());
		
		ContainerReportSummary routeSummary = t.getSummary();
		
		data[0]++;
		nbrOfUsedVehicles++;
		
		utilizationSum += routeSummary.getUtilization();
	}
	
	public void addUnplannedPackage(LPPackageEvent pkg) {
		if(pkg.getType() == LoadType.LOAD)
			nbrOfNotLoadedPackages++;
	}

	public float getNbrOfUsedVehicles(Container veh) {
		return dataMap.get(veh)[0];
	}
	
	public float getNbrOfNotLoadedPackages(Container veh) {
		return dataMap.get(veh)[1];
	}
	
	public float getNbrOfUsedVehicles() {
		return nbrOfUsedVehicles;
	}
	
	public float getNbrOfNotLoadedPackages() {
		return nbrOfNotLoadedPackages;
	}
	
	public float getUtilization() {
		return (nbrOfUsedVehicles > 0) ? utilizationSum / nbrOfUsedVehicles : 0;
	}
}
