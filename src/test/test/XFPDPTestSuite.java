package test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import xf.xfvrp.XFVRP;
import xf.xfvrp.base.LoadType;
import xf.xfvrp.base.metric.EucledianMetric;
import xf.xfvrp.opt.XFVRPOptType;
import xf.xfvrp.report.Report;
import xf.xfvrp.report.StringWriter;

/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 **/
public class XFPDPTestSuite {

	/**
	 * Test if two shipments are planned on one route.
	 */
	@Test
	public void testOneRoute() {
		XFVRP v = new XFVRP();
		v.setMetric(new EucledianMetric());
		v.allowsPDPPlanning();
		v.addDepot().setExternID("D").setXlong(0).setYlat(0);
		v.addCustomer().setExternID("A1").setXlong(10).setYlat(10).setDemand(new float[]{5,0,0}).setLoadType(LoadType.PICKUP).setShipID("A");
		v.addCustomer().setExternID("A2").setXlong(10).setYlat(0).setDemand(new float[]{-5,0,0}).setLoadType(LoadType.PICKUP).setShipID("A");
		v.addCustomer().setExternID("B1").setXlong(-10).setYlat(-10).setDemand(new float[]{5,0,0}).setLoadType(LoadType.PICKUP).setShipID("B");
		v.addCustomer().setExternID("B2").setXlong(-10).setYlat(0).setDemand(new float[]{-5,0,0}).setLoadType(LoadType.PICKUP).setShipID("B");
		v.addVehicle().setName("V").setCapacity(new float[]{10,0,0}).setMaxRouteDuration(4000);

		v.addOptType(XFVRPOptType.PDP_RELOCATE);
		v.executeRoutePlanning();

		Report r = v.getReport();
		assertTrue(r.getRoutes().size() == 1);
	}

	/**
	 * Test, if two shipments will be planned mixed (first all pickups, then all deliveries)
	 */
	@Test
	public void testOneRouteMixed() {
		XFVRP v = new XFVRP();
		v.setMetric(new EucledianMetric());
		v.allowsPDPPlanning();
		v.addDepot().setExternID("D").setXlong(0).setYlat(0);
		v.addCustomer().setExternID("A1").setXlong(-10).setYlat(10).setDemand(new float[]{5,0,0}).setLoadType(LoadType.PICKUP).setShipID("A");
		v.addCustomer().setExternID("A2").setXlong(10).setYlat(-10).setDemand(new float[]{-5,0,0}).setLoadType(LoadType.PICKUP).setShipID("A");
		v.addCustomer().setExternID("B1").setXlong(-10).setYlat(0).setDemand(new float[]{5,0,0}).setLoadType(LoadType.PICKUP).setShipID("B");
		v.addCustomer().setExternID("B2").setXlong(0).setYlat(-10).setDemand(new float[]{-5,0,0}).setLoadType(LoadType.PICKUP).setShipID("B");
		v.addVehicle().setName("V").setCapacity(new float[]{10,0,0}).setMaxRouteDuration(4000);

		v.addOptType(XFVRPOptType.PDP_RELOCATE);
		v.executeRoutePlanning();

		Report r = v.getReport();
		System.out.println(StringWriter.write(r));
		assertTrue(r.getRoutes().size() == 1);
		assertTrue(!r.getRoutes().get(0).getEvents().get(1).getShipID().equals(
				r.getRoutes().get(0).getEvents().get(2).getShipID()
				)
				);
	}

	/**
	 * Test if Insertion heuristic works proper 
	 **/
	@Test
	public void testInsertionHeuristic() {
		XFVRP v = new XFVRP();
		v.setMetric(new EucledianMetric());
		v.allowsPDPPlanning();
		v.addDepot().setExternID("D").setXlong(0).setYlat(0);

		addCustomers(v, 1000);

		v.addVehicle().setName("V").setCapacity(new float[]{3,0,0}).setMaxRouteDuration(30);

		v.addOptType(XFVRPOptType.PDP_CHEAPEST_INSERT);

		v.executeRoutePlanning();

		Report r = v.getReport();
		assertTrue(r.getRoutes().size() < 1000/3f);
	}

	/**
	 * Test, if reinsert gains more quality than without reinsert.
	 */
	@Test
	public void testReinsert() {
		
	}

	/**
	 * 
	 * @param v
	 * @param nbrOfCustomers
	 */
	private void addCustomers(XFVRP v, int nbrOfCustomers) {
		Random rand = new Random(1234);

		List<float[]> pointList = new ArrayList<>();
		for (int i = 0; i < 360; i++)
			pointList.add(new float[] {-5 + (10 * rand.nextFloat()), -5 + (10 * rand.nextFloat())});

		int nodeIdx = 0;
		for (int i = 0; i < nbrOfCustomers; i++) {
			float[] p1 = pointList.get(rand.nextInt(pointList.size()));
			float[] p2 = null;
			do {
				p2 = pointList.get(rand.nextInt(pointList.size()));
			} while(p1 == p2);
	
			v.addCustomer().setExternID("N"+(nodeIdx++)).setXlong(p1[0]).setYlat(p1[1]).setDemand(new float[]{1,0,0}).setLoadType(LoadType.PICKUP).setShipID("S"+i);
			v.addCustomer().setExternID("N"+(nodeIdx++)).setXlong(p2[0]).setYlat(p2[1]).setDemand(new float[]{-1,0,0}).setLoadType(LoadType.PICKUP).setShipID("S"+i);
		}
	}

}
