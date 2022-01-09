package xf.xflp.report;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** 
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 */
public class LPReport implements Iterable<ContainerReport> {

	private final LPReportSummary summary = new LPReportSummary();
	private final List<ContainerReport> reportList = new ArrayList<>();
	private final List<LPPackageEvent> unplannedPackageList = new ArrayList<>();

	/**
	 * A LPReport is the structral representation of a load planning solution
	 */
	public LPReport() {
	}
	
	/* Add-Functions */

	/**
	 * 
	 * @param containerRep
	 */
	public void add(ContainerReport containerRep) {
		if(containerRep.getSummary().getNbrOfLoadedPackages() + containerRep.getSummary().getNbrOfUnLoadedPackages() > 0) {
			summary.add(containerRep);
			reportList.add(containerRep);
		}
	}
	
	public void addUnplannedPackages(LPPackageEvent pkg) {
		unplannedPackageList.add(pkg);
		summary.addUnplannedPackage(pkg);
	}

	/* GetFunctions-Functions */

	public List<ContainerReport> getContainerReports() {
		return reportList;
	}
	
	public List<LPPackageEvent> getUnplannedPackages() {
		return unplannedPackageList;
	}

	public LPReportSummary getSummary() {
		return summary;
	}
	
	/**
	 * Import the route reports of another report object into this report.
	 * 
	 * @param rep Another report object
	 */
	public void importReport(LPReport rep) {
		for (ContainerReport tRep : rep.getContainerReports())
			add(tRep);
		for (LPPackageEvent event : rep.getUnplannedPackages()) {
			addUnplannedPackages(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<ContainerReport> iterator() {
		return reportList.iterator();
	}
}
