package xf.xflp.report;

import xf.xflp.base.XFLPModel;
import xf.xflp.base.problem.Container;

import java.util.*;

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
public class LPReport implements Iterable<ContainerReport> {

	private LPReportSummary summary = new LPReportSummary();
	private List<ContainerReport> reportList = new ArrayList<>();
	private List<LPPackageEvent> unplannedPackageList = new ArrayList<>();

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
	
	/**
	 * 
	 * @param pkg
	 */
	public void addUnplannedPackages(LPPackageEvent pkg) {
		unplannedPackageList.add(pkg);
		summary.addUnplannedPackage(pkg);
	}

	/* GetFunctions-Functions */

	/**
	 * 
	 * @return
	 */
	public List<ContainerReport> getContainerReports() {
		return reportList;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<LPPackageEvent> getUnplannedPackages() {
		return unplannedPackageList;
	}

	/**
	 * 
	 * @return
	 */
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
