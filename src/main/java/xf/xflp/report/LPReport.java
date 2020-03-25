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

	private final XFLPModel model;
	
	private LPReportSummary summary = new LPReportSummary();
	private List<ContainerReport> reportList = new ArrayList<>();
	private List<LPPackageEvent> unplannedPackageList = new ArrayList<>();
	private Set<Container> containerSet = new HashSet<>();
	
	/**
	 * A LPReport is the structral representation of a route planning solution.
	 * 
	 * @param model The used data model for gaining this solution.
	 */
	public LPReport(XFLPModel model) {
		this.model = model;
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<Container> getContainers() {
		return containerSet;
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
			containerSet.add(containerRep.getContainer());
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
	 * Returns the used planning model for obtaining this solution report.
	 * @return
	 */
	public XFLPModel getModel() {
		return model;
	}
	
	/**
	 * Import the route reports of another report object into this report.
	 * 
	 * @param rep Another report object
	 */
	public void importReport(LPReport rep) {
		for (ContainerReport tRep : rep.getContainerReports())
			add(tRep);
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
