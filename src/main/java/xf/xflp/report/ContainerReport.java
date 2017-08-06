package xf.xflp.report;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import xf.xflp.base.problem.Container;


/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * LPReport of a route by a list of events
 * 
 * Each route report has a allocated container, which contains
 * the parameters.
 * 
 * @author hschneid
 *
 */
public class ContainerReport implements Iterable<LPPackageEvent> {

	private final ContainerReportSummary summary;
	private final Container container;
	private List<LPPackageEvent> packageEventList = new ArrayList<>();

	
	/**
	 * 
	 * @param con
	 */
	public ContainerReport(Container con) {
		this.container = con;
		this.summary = new ContainerReportSummary(con);
	}
	
	/**
	 * 
	 * @param e
	 */
	public void add(LPPackageEvent e) {
		summary.add(e);
		packageEventList.add(e);
	}
		
	/**
	 * 
	 * @return
	 */
	public ContainerReportSummary getSummary() {
		return summary;
	}
	
	/**
	 * 
	 * @return
	 */
	public Container getContainer() {
		return container;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<LPPackageEvent> getPackageEvents() {
		return packageEventList;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getContainerId() {
		return container.getIndex();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<LPPackageEvent> iterator() {
		return packageEventList.iterator();
	}
}
