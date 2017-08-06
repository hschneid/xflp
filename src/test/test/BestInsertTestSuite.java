package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import xf.xfvrp.XFVRP;
import xf.xfvrp.base.LoadType;
import xf.xfvrp.base.metric.EucledianMetric;
import xf.xfvrp.opt.XFVRPOptType;
import xf.xfvrp.report.Report;

/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 **/
public class BestInsertTestSuite {

	/*
	 * Test, if VRP insertion heuristic can reduce the number of routes significantly for small instance
	 */
	@Test
	public void basicVRPTest() {
		XFVRP v = new XFVRP();
		v.setMetric(new EucledianMetric());
		v.addDepot().setExternID("D").setXlong(0).setYlat(0);

		addCustomers(v, 100);

		v.addVehicle().setName("V").setCapacity(new float[]{10,0,0}).setMaxRouteDuration(30);

		v.addOptType(XFVRPOptType.FIRST_BEST);

		v.executeRoutePlanning();

		Report r = v.getReport();
		assertTrue(r.getRoutes().size() < 100/3f);
	}

	/*
	 * Test, if PDP insertion heuristic can reduce the number of routes significantly for small instance
	 */
	@Test
	public void basicPDPTest() {
		XFVRP v = new XFVRP();
		v.setMetric(new EucledianMetric());
		v.allowsPDPPlanning();
		v.addDepot().setExternID("D").setXlong(0).setYlat(0);

		addShipments(v, 100);

		v.addVehicle().setName("V").setCapacity(new float[]{3,0,0}).setMaxRouteDuration(30);

		v.addOptType(XFVRPOptType.PDP_CHEAPEST_INSERT);

		v.executeRoutePlanning();

		Report r = v.getReport();
		assertTrue(r.getRoutes().size() < 100/3f);
	}

	/*
	 * Test, if VRP insertion heuristic is able to be as good as Savings with maximal 10% deviation in nbr of routes and 200% deviation in route length
	 */
	@Test
	public void compareSavingsVRPTest() {
		XFVRP v = new XFVRP();
		v.setMetric(new EucledianMetric());
		v.addDepot().setExternID("D").setXlong(0).setYlat(0);

		addCustomers(v, 100);

		v.addVehicle().setName("V").setCapacity(new float[]{10,0,0}).setMaxRouteDuration(30);

		{
			v.addOptType(XFVRPOptType.CONST);

			v.executeRoutePlanning();

			Report r = v.getReport();
			int nbrOfRoutesSavings = r.getRoutes().size();
			float lengthSavings = r.getSummary().getDistance();

			v.clearOptTypes();

			v.addOptType(XFVRPOptType.FIRST_BEST);

			v.executeRoutePlanning();

			r = v.getReport();
			assertTrue(r.getRoutes().size() <= nbrOfRoutesSavings * 1.1);
			assertTrue(r.getSummary().getDistance() <= lengthSavings * 3);
		}
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
			pointList.add(new float[]{-5 + (10 * rand.nextFloat()), -5 + (10 * rand.nextFloat())});

		int nodeIdx = 0;
		for (int i = 0; i < nbrOfCustomers; i++) {
			float[] p1 = pointList.get(rand.nextInt(pointList.size()));
			float[] p2 = null;
			do {
				p2 = pointList.get(rand.nextInt(pointList.size()));
			} while(p1 == p2);

			v.addCustomer().setExternID("N"+(nodeIdx++)).setXlong(p1[0]).setYlat(p1[1]).setDemand(new float[]{1,0,0}).setLoadType(LoadType.PICKUP);
		}
	}

	/**
	 * 
	 * @param v
	 * @param nbrOfShipments
	 */
	private void addShipments(XFVRP v, int nbrOfShipments) {
		Random rand = new Random(1234);

		List<float[]> pointList = new ArrayList<>();
		for (int i = 0; i < 360; i++)
			pointList.add(new float[]{-5 + (10 * rand.nextFloat()), -5 + (10 * rand.nextFloat())});

		int nodeIdx = 0;
		for (int i = 0; i < nbrOfShipments; i++) {
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
