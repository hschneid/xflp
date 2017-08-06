package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

import xf.xfvrp.XFVRP;
import xf.xfvrp.base.LoadType;
import xf.xfvrp.base.metric.EucledianMetric;
import xf.xfvrp.base.monitor.DefaultStatusMonitor;
import xf.xfvrp.base.monitor.StatusCode;
import xf.xfvrp.base.monitor.StatusMonitor;
import xf.xfvrp.opt.XFVRPOptType;
import xf.xfvrp.report.Report;
import xf.xfvrp.report.RouteReport;
import xf.xfvrp.report.StringWriter;

/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 **/
public class XFVRPBasisTestSuite {

	StatusMonitor testStatusManager = new StatusMonitor() {
		@Override
		public void getMessage(StatusCode code, String message) {
			if(code == StatusCode.EXCEPTION)
				System.out.println();
			assertTrue(code != StatusCode.EXCEPTION);
			System.out.println(message);
		}
	};

	// Teste Zeitfenster (1,2)
	// Teste Vorgaben (Sequenz, Block)
	// Teste Ung�ltigkeit
	// Teste mehrere Depots
	// Teste mehrere Fahrzeuge
	// Teste jeweils Report Integrit�t
	// teste Ergebnis
	/*
	 * 
	 */
	@Test
	public void testOneDepotNoCustomer() {
		boolean failure = false;
		try {
			XFVRP v = createXFVRPEuclead();
			addDepot(v, "DEP", 0, 0);

			v.executeRoutePlanning();

			Report rep = v.getReport();
			assertTrue(rep == null);
		} catch (Exception e) {
			e.printStackTrace();
			failure = true;
		}
		assertFalse(failure);
	}

	/*
	 * 
	 */
	@Test
	public void testOneDepotOneCustomer() {
		boolean failure = false;
		try {
			XFVRP v = createXFVRPEuclead();
			addDepot(v, "DEP", 0, 0);
			addCustomer(v, "1", 2, 0);

			v.executeRoutePlanning();

			Report rep = v.getReport();
			assertTrue(rep.getSummary().getDistance() == 4);
			assertTrue(rep.getSummary().getNbrOfUsedVehicles() == 1);
			assertTrue(rep.getRoutes().size() == 1);
		} catch (Exception e) {
			e.printStackTrace();
			failure = true;
		}
		assertFalse(failure);
	}

	/*
	 * 
	 */
	@Test
	public void testNoDepotOneCustomer() {
		boolean failure = false;
		try {
			XFVRP v = createXFVRPEuclead();
			addCustomer(v, "1", 2, 0);

			v.executeRoutePlanning();

			assertTrue(failure);
		} catch (Exception e) {
			failure = true;
		}
		assertTrue(failure);
	}

	/*
	 * 
	 */
	@Test
	public void testCapacity() {
		boolean failure = false;
		try {
			XFVRP v = createXFVRPEuclead();
			addDepot(v, "DEP", 0, 0);
			addCustomer(v, "1", 2, 0, 2, 0, 0);
			addCustomer(v, "2", 2, 0, 0, 2, 0);
			addCustomer(v, "3", 2, 0, 0, 0, 2);

			// No Vehicle is invalid
			{
				addVehicle(v, "VV", 2, 2, 2);
				v.executeRoutePlanning();
				Report rep = v.getReport();
				for (RouteReport rr : rep.getRoutes())
					assertTrue(!rr.getVehicle().name.equals("INVALID"));
			}

			// One Vehicle is invalid
			{
				v.clearVehicles(); addVehicle(v, "VV", 2, 2, 1);
				v.executeRoutePlanning();
				Report rep = v.getReport();
				int nbrOfInvalidRoutes = 0;
				for (RouteReport rr : rep.getRoutes())
					if(rr.getVehicle().name.equals("INVALID")) nbrOfInvalidRoutes++;
				assertTrue(nbrOfInvalidRoutes == 1);
			}

			// Two Vehicle are invalid
			{
				v.clearVehicles(); addVehicle(v, "VV", 1, 2, 1);
				v.executeRoutePlanning();
				Report rep = v.getReport();
				int nbrOfInvalidRoutes = 0;
				for (RouteReport rr : rep.getRoutes())
					if(rr.getVehicle().name.equals("INVALID")) nbrOfInvalidRoutes++;
				assertTrue(nbrOfInvalidRoutes == 2);
			}

			// All Vehicles are invalid
			{
				v.clearVehicles(); addVehicle(v, "VV", 1, 1, 1);
				v.executeRoutePlanning();
				Report rep = v.getReport();
				int nbrOfInvalidRoutes = 0;
				for (RouteReport rr : rep.getRoutes())
					if(rr.getVehicle().name.equals("INVALID")) nbrOfInvalidRoutes++;
				assertTrue(nbrOfInvalidRoutes == 3);

				assertTrue(rep.getErrors().getStatistics().get("CAPACITY") == 3);
			}
		} catch (Exception e) {
			e.printStackTrace();
			failure = true;
		}
		assertFalse(failure);
	}

	/*
	 * 
	 */
	@Test
	public void testTimeWindows() {
		boolean failure = false;
		try {

			XFVRP v = createXFVRPEuclead();
			v.addOptType(XFVRPOptType.ILS);
			addDepot(v, "DEP", 0, 0);
			addVehicle(v, "VV", 3, 0, 0);

			// Big Time windows
			{
				addCustomer(v, "1", 10, 0, 1, 0, 1000, 0, 1000);
				addCustomer(v, "2", 10, 10, 1, 0, 1000, 0, 1000);
				addCustomer(v, "3", 0, 10, 1, 0, 1000, 0, 1000);

				v.executeRoutePlanning();
				Report rep = v.getReport();
				assertTrue(rep.getSummary().getNbrOfUsedVehicles() == 1);
				for (RouteReport rr : rep.getRoutes())
					assertTrue(!rr.getVehicle().name.equals("INVALID"));
			}

			// One Narrow time window, which changes nothing
			{
				v.clearCustomers();
				addCustomer(v, "1", 10, 0, 1, 10, 11, 10, 11);
				addCustomer(v, "2", 10, 10, 1, 0, 1000, 0, 1000);
				addCustomer(v, "3", 0, 10, 1, 0, 1000, 0, 1000);

				v.executeRoutePlanning();
				Report rep = v.getReport();
				assertTrue(rep.getSummary().getNbrOfUsedVehicles() == 1);
				assertTrue(rep.getRoutes().get(0).getEvents().get(1).getID().equals("1"));
				for (RouteReport rr : rep.getRoutes())
					assertTrue(!rr.getVehicle().name.equals("INVALID"));
			}
			// Two Narrow time window
			{
				v.clearCustomers();
				addCustomer(v, "1", 10, 0, 1, 10, 11, 10, 11);
				addCustomer(v, "2", 10, 10, 1, 20, 21, 20, 21);
				addCustomer(v, "3", 0, 10, 1, 0, 1000, 0, 1000);

				v.executeRoutePlanning();
				Report rep = v.getReport();
				assertTrue(rep.getSummary().getNbrOfUsedVehicles() == 1);
				assertTrue(rep.getRoutes().get(0).getEvents().get(1).getID().equals("1"));
				for (RouteReport rr : rep.getRoutes())
					assertTrue(!rr.getVehicle().name.equals("INVALID"));
			}
			// One Narrow time window, which changes route direction
			{
				v.clearCustomers();
				addCustomer(v, "1", 10, 0, 1, 0, 1000, 0, 1000);
				addCustomer(v, "2", 10, 10, 1, 0, 1000, 0, 1000);
				addCustomer(v, "3", 0, 10, 1, 10, 11, 10, 11);

				v.executeRoutePlanning();
				Report rep = v.getReport();
				assertTrue(rep.getSummary().getNbrOfUsedVehicles() == 1);
				assertTrue(rep.getRoutes().get(0).getEvents().get(1).getID().equals("3"));
				for (RouteReport rr : rep.getRoutes())
					assertTrue(!rr.getVehicle().name.equals("INVALID"));
			}
			// narrow time window, which leads to two routes
			{
				v.clearCustomers();
				addCustomer(v, "1", 10, 0, 1, 12, 13, 12, 13);
				addCustomer(v, "2", 10, 10, 1, 20, 21, 20, 21);
				addCustomer(v, "3", 0, 10, 1, 0, 1000, 0, 1000);

				v.executeRoutePlanning();
				Report rep = v.getReport();
				assertTrue(rep.getSummary().getNbrOfUsedVehicles() == 2);
				for (RouteReport rr : rep.getRoutes())
					assertTrue(!rr.getVehicle().name.equals("INVALID"));
			}
			// Narrow time windows which lead to an invalid route.
			{
				v.clearCustomers();
				addCustomer(v, "1", 10, 10, 1, 0, 1000, 0, 1000);
				addCustomer(v, "2", 10, 0, 1, 0, 1, 0, 1);
				addCustomer(v, "3", 0, 10, 1, 0, 1000, 0, 1000);

				v.executeRoutePlanning();
				Report rep = v.getReport();
				assertTrue(rep.getSummary().getNbrOfUsedVehicles() == 2);
				int nbrOfInvalids = 0;
				for (RouteReport rr : rep.getRoutes())
					if(rr.getVehicle().name.equals("INVALID")) {
						rr.getEvents().get(1).getID().equals("2");
						nbrOfInvalids++;
					}
				assertTrue(nbrOfInvalids == 1);
			}

			// Two time windows positiv
			{
				v.clearCustomers();
				addCustomer(v, "1", 10, 10, 1, 0, 5, 10, 15);

				v.executeRoutePlanning();
				Report rep = v.getReport();
				assertTrue(rep.getSummary().getNbrOfUsedVehicles() == 1);
				for (RouteReport rr : rep.getRoutes())
					assertTrue(!rr.getVehicle().name.equals("INVALID"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			failure = true;
		}
		assertFalse(failure);
	}

	/*
	 * 
	 */
	@Test
	public void testInvalidReasons() {
		boolean failure = false;
		try {

			XFVRP v = createXFVRPEuclead();
			addDepot(v, "DEP1", 0, 0);
			addDepot(v, "DEP2", 2, 2);

			// Kein Invalid
			{
				addVehicle(v, "VV", 3, 0, 0);
				addCustomer(v, "1", 1, 1, 1, 0, 1000, 0, 1000);

				v.executeRoutePlanning();
				Report rep = v.getReport();
				assertTrue(rep.getErrors().getStatistics().size() == 0);
			}

			// Invalid reason: Travel time
			{
				v.clearCustomers();
				v.clearVehicles();
				v.addVehicle().setName("VV").setCapacity(new float[]{3,0,0}).setMaxRouteDuration(10);
				addCustomer(v, "1", -10, -10, 1, 0, 1000, 0, 1000);

				v.executeRoutePlanning();
				Report rep = v.getReport();
				assertTrue(rep.getErrors().getStatistics().size() > 0);
				assertTrue(rep.getErrors().getStatistics().get("TRAVEL_TIME") > 0);
			}

			// Invalid reason: Time window of customer
			{
				v.clearDepots();
				v.clearCustomers();
				v.clearVehicles();
				v.addDepot().setExternID("DEP").setXlong(0).setYlat(0);
				v.addVehicle().setName("VV").setCapacity(new float[]{3,0,0});
				addCustomer(v, "1", 10, 10, 1, 2, 5, 3, 6);

				v.executeRoutePlanning();
				Report rep = v.getReport();
				assertTrue(rep.getErrors().getStatistics().size() > 0);
				assertTrue(rep.getErrors().getStatistics().get("TIME_WINDOW") > 0);
			}

			// Invalid reason: Time window of depot
			{
				v.clearDepots();
				v.clearCustomers();
				v.clearVehicles();
				v.addDepot().setExternID("DEP").setXlong(0).setYlat(0).setTimeWindow(0, 13);
				v.addVehicle().setName("VV").setCapacity(new float[]{3,0,0});
				addCustomer(v, "1", 10, 10, 1, 0, 0);

				v.executeRoutePlanning();
				Report rep = v.getReport();
				assertTrue(rep.getErrors().getStatistics().size() > 0);
				assertTrue(rep.getErrors().getStatistics().get("TIME_WINDOW") > 0);
			}

			// Invalid reason: Time window of depot
			{
				v.clearDepots();
				v.clearCustomers();
				v.clearVehicles();
				v.addDepot().setExternID("DEP").setXlong(0).setYlat(0);
				v.addVehicle().setName("VV").setCapacity(new float[]{1,0,0});
				addCustomer(v, "1", 10, 10, 2, 0, 0);

				v.executeRoutePlanning();
				Report rep = v.getReport();
				assertTrue(rep.getErrors().getStatistics().size() > 0);
				assertTrue(rep.getErrors().getStatistics().get("CAPACITY") > 0);
				assertTrue(rep.getErrors().getErrorDescriptions().get(0).contains("Capacity 1"));

				//---------
				v.clearDepots();
				v.clearCustomers();
				v.clearVehicles();
				v.addDepot().setExternID("DEP").setXlong(0).setYlat(0);
				v.addVehicle().setName("VV").setCapacity(new float[]{0,1,0});
				addCustomer(v, "1", 10, 10, 0, 2, 0);

				v.executeRoutePlanning();
				rep = v.getReport();
				assertTrue(rep.getErrors().getStatistics().size() > 0);
				assertTrue(rep.getErrors().getStatistics().get("CAPACITY") > 0);
				assertTrue(rep.getErrors().getErrorDescriptions().get(0).contains("Capacity 2"));

				//---------
				v.clearDepots();
				v.clearCustomers();
				v.clearVehicles();
				v.addDepot().setExternID("DEP").setXlong(0).setYlat(0);
				v.addVehicle().setName("VV").setCapacity(new float[]{0,0,1});
				addCustomer(v, "1", 10, 10, 0, 0, 2);

				v.executeRoutePlanning();
				rep = v.getReport();
				assertTrue(rep.getErrors().getStatistics().size() > 0);
				assertTrue(rep.getErrors().getStatistics().get("CAPACITY") > 0);
				assertTrue(rep.getErrors().getErrorDescriptions().get(0).contains("Capacity 3"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			failure = true;
		}
		assertFalse(failure);
	}

	/*
	 * 
	 */
	@Test
	public void testInvalidRoutes() {
		boolean failure = false;
		try {
			// All customers in blocks
			{
				XFVRP v = createXFVRPEuclead();
				addDepot(v, "DEP1", 0, 0);
				v.addVehicle().setName("VV").setCapacity(new float[]{1,0,0});
				v.addCustomer().setExternID("1").setXlong(1).setYlat(1).setDemand(new float[]{2,0,0}).setPresetBlockName("B1").setPresetBlockPos(2);
				v.addCustomer().setExternID("2").setXlong(2).setYlat(2).setDemand(new float[]{2,0,0}).setPresetBlockName("B1").setPresetBlockPos(1);
				v.addCustomer().setExternID("3").setXlong(3).setYlat(3).setDemand(new float[]{2,0,0}).setPresetBlockName("B2");

				v.executeRoutePlanning();
				Report rep = v.getReport();
				assertTrue(rep.getErrors().getStatistics().size() > 0);
				assertTrue(rep.getErrors().getStatistics().get("CAPACITY") == 3);
				assertTrue(rep.getRoutes().size() == 2);
			}

			// With one default block
			{
				XFVRP v = createXFVRPEuclead();
				addDepot(v, "DEP1", 0, 0);
				v.addVehicle().setName("VV").setCapacity(new float[]{1,0,0});
				v.addCustomer().setExternID("1").setXlong(1).setYlat(1).setDemand(new float[]{2,0,0}).setPresetBlockName("B1").setPresetBlockPos(2);
				v.addCustomer().setExternID("2").setXlong(2).setYlat(2).setDemand(new float[]{2,0,0}).setPresetBlockName("B1").setPresetBlockPos(1);
				v.addCustomer().setExternID("3").setXlong(3).setYlat(3).setDemand(new float[]{2,0,0});

				v.executeRoutePlanning();
				Report rep = v.getReport();
				assertTrue(rep.getErrors().getStatistics().size() > 0);
				assertTrue(rep.getErrors().getStatistics().get("CAPACITY") == 3);
				assertTrue(rep.getRoutes().size() == 2);
			}
		} catch (Exception e) {
			e.printStackTrace();
			failure = true;
		}
		assertFalse(failure);
	}

	/*
	 * 
	 */
	@Test
	public void testReplenishment() {
		boolean failure = false;
		try {
			// Two customers, tiny vehicle = two routes
			{
				XFVRP v = createXFVRPEuclead();
				addDepot(v, "DEP", 0, 0);
				v.addVehicle().setName("VV").setCapacity(new float[]{1,0,0}).setMaxRouteDuration(30);
				v.addCustomer().setExternID("1").setXlong(5).setYlat(5).setDemand(new float[]{1,0,0});
				v.addCustomer().setExternID("2").setXlong(-5).setYlat(5).setDemand(new float[]{1,0,0});
				v.addOptType(XFVRPOptType.ILS);

				v.executeRoutePlanning();
				Report rep = v.getReport();
				assertTrue(rep.getErrors().getStatistics().size() == 0);
				assertTrue(rep.getRoutes().size() == 2);
			}

			// Two customers, tiny vehicle, replenishment on route = only one route
			{
				XFVRP v = createXFVRPEuclead();
				addDepot(v, "DEP", 0, 0);
				v.addReplenishment().setExternID("REP").setXlong(0).setYlat(5);
				v.addVehicle().setName("VV").setCapacity(new float[]{1,0,0}).setMaxRouteDuration(30).setFixCost(10);
				v.addCustomer().setExternID("1").setXlong(5).setYlat(5).setDemand(new float[]{1,0,0});
				v.addCustomer().setExternID("2").setXlong(-5).setYlat(5).setDemand(new float[]{1,0,0});
				v.addOptType(XFVRPOptType.ILS);

				v.executeRoutePlanning();
				Report rep = v.getReport();
				assertTrue(rep.getErrors().getStatistics().size() == 0);
				assertTrue(rep.getRoutes().size() == 1);
			}
			
			// 10 customers, 2 depots, 2 replenishments, far away from depot, tiny vehicle = one route
			{
				XFVRP v = createXFVRPEuclead();
				addDepot(v, "DEP", 0, 0);
				addDepot(v, "DEP2", 5, 5);
				v.addVehicle().setName("VV").setCapacity(new float[]{5,0,0});
				v.addReplenishment().setExternID("REP").setXlong(0).setYlat(10);
				v.addReplenishment().setExternID("REP2").setXlong(10).setYlat(10);
				
				for (int i = 0; i < 10; i++) {
					double x = 0 + 10 * Math.cos(Math.PI /11*i);
					double y = 0 + 10 * Math.sin(Math.PI /11*i);
					
					v.addCustomer().setExternID(""+i).setXlong((float)x).setYlat((float)y).setDemand(new float[]{1,0,0}).setLoadType(LoadType.PICKUP);
				}
				
				v.addOptType(XFVRPOptType.ILS);
				v.setNbrOfLoopsForILS(500);
				v.executeRoutePlanning();
				Report rep = v.getReport();
				assertTrue(rep.getErrors().getStatistics().size() == 0);
				assertTrue(rep.getRoutes().size() == 1);
				assertTrue(rep.getRoutes().get(0).getEvents().get(0).getID().equals("DEP"));
				assertTrue(rep.getRoutes().get(0).getEvents().get(6).getID().equals("REP"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			failure = true;
		}
		assertFalse(failure);
	}

	/*
	 * 
	 */
	@Test
	public void testPresetPosSequence() {
		boolean failure = false;
		try {
			XFVRP v = createXFVRPEuclead();
			addDepot(v, "DEP", 0, 0);
			v.addVehicle().setName("VV").setCapacity(new float[]{1,0,0});
			v.addOptType(XFVRPOptType.RELOCATE);

			// Kein Block, kein Preset
			{
				v.addCustomer().setExternID("1").setXlong(5).setYlat(5);
				v.addCustomer().setExternID("2").setXlong(0).setYlat(5);
				v.addCustomer().setExternID("3").setXlong(-5).setYlat(5);

				v.executeRoutePlanning();

				Report rep = v.getReport();
				assertTrue(rep.getRoutes().size() == 1);
				assertTrue(rep.getRoutes().get(0).getEvents().get(1).getID().equals("1"));
				assertTrue(rep.getRoutes().get(0).getEvents().get(2).getID().equals("2"));
				assertTrue(rep.getRoutes().get(0).getEvents().get(3).getID().equals("3"));
				assertTrue(rep.getErrors().getStatistics().size() == 0);
			}
			// Block und Preset
			{
				v.clearCustomers();
				v.addCustomer().setExternID("1").setXlong(5).setYlat(5).setPresetBlockName("B").setPresetBlockPos(2);
				v.addCustomer().setExternID("2").setXlong(0).setYlat(5).setPresetBlockName("B");
				v.addCustomer().setExternID("3").setXlong(-5).setYlat(5).setPresetBlockName("B").setPresetBlockPos(1);

				v.executeRoutePlanning();

				Report rep = v.getReport();
				assertTrue(rep.getRoutes().size() == 1);
				assertTrue(rep.getRoutes().get(0).getEvents().get(1).getID().equals("2"));
				assertTrue(rep.getRoutes().get(0).getEvents().get(2).getID().equals("3"));
				assertTrue(rep.getRoutes().get(0).getEvents().get(3).getID().equals("1"));
				assertTrue(rep.getErrors().getStatistics().size() == 0);
			}
			// Kein Block aber Preset
			{
				v.clearCustomers();
				v.addCustomer().setExternID("1").setXlong(5).setYlat(5).setPresetBlockPos(2);
				v.addCustomer().setExternID("2").setXlong(0).setYlat(5);
				v.addCustomer().setExternID("3").setXlong(-5).setYlat(5).setPresetBlockPos(1);

				v.executeRoutePlanning();

				Report rep = v.getReport();
				assertTrue(rep.getRoutes().size() == 1);
				assertTrue(rep.getRoutes().get(0).getEvents().get(1).getID().equals("2"));
				assertTrue(rep.getRoutes().get(0).getEvents().get(2).getID().equals("3"));
				assertTrue(rep.getRoutes().get(0).getEvents().get(3).getID().equals("1"));
				assertTrue(rep.getErrors().getStatistics().size() == 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			failure = true;
		}
		assertFalse(failure);
	}

	/*
	 * 
	 */
	@Test
	public void testPresetDepot() {
		boolean failure = false;
		try {
			XFVRP v = createXFVRPEuclead();
			v.setStatusMonitor(testStatusManager);
			addDepot(v, "DEP1", 0, 0);
			addDepot(v, "DEP2", 10, 10);
			v.addVehicle().setName("VV").setCapacity(new float[]{1,0,0});
			v.addOptType(XFVRPOptType.RELOCATE);

			Set<String> s1 = new HashSet<>();s1.add("DEP1");
			Set<String> s2 = new HashSet<>();s2.add("DEP2");

			// Kein Block, kein Preset
			{
				v.addCustomer().setExternID("1").setXlong(2).setYlat(2);
				v.addCustomer().setExternID("2").setXlong(12).setYlat(12);

				v.executeRoutePlanning();

				Report rep = v.getReport();
				//				System.out.println(new StringWriter().write(rep));
				assertTrue(rep.getRoutes().size() == 2);
				assertTrue(rep.getErrors().getStatistics().size() == 0);
			}
			// Preset with single depots but no change
			{
				v.clearCustomers();

				v.addCustomer().setExternID("1").setXlong(2).setYlat(2).setPresetDepotList(s1);
				v.addCustomer().setExternID("2").setXlong(12).setYlat(12).setPresetDepotList(s2);

				v.executeRoutePlanning();

				Report rep = v.getReport();
				assertTrue(rep.getRoutes().size() == 2);
				assertTrue(rep.getErrors().getStatistics().size() == 0);

				assertTrue(
						rep.getRoutes().get(0).getEvents().get(0).getID().equals("DEP1") &&
						rep.getRoutes().get(0).getEvents().get(1).getID().equals("1")
						);
				assertTrue(
						rep.getRoutes().get(1).getEvents().get(0).getID().equals("DEP2") &&
						rep.getRoutes().get(1).getEvents().get(1).getID().equals("2")
						);
			}
			// Preset with single depots
			{
				v.clearCustomers();
				v.addCustomer().setExternID("1").setXlong(2).setYlat(2).setPresetDepotList(s2);
				v.addCustomer().setExternID("2").setXlong(12).setYlat(12).setPresetDepotList(s1);

				v.executeRoutePlanning();

				Report rep = v.getReport();
				assertTrue(rep.getRoutes().size() == 2);
				assertTrue(rep.getErrors().getStatistics().size() == 0);

				assertTrue(
						rep.getRoutes().get(0).getEvents().get(0).getID().equals("DEP2") &&
						rep.getRoutes().get(0).getEvents().get(1).getID().equals("1")
						);
				assertTrue(
						rep.getRoutes().get(1).getEvents().get(0).getID().equals("DEP1") &&
						rep.getRoutes().get(1).getEvents().get(1).getID().equals("2")
						);
			}
			// Preset with single depots with block
			{
				v.clearCustomers();
				v.addCustomer().setExternID("1").setXlong(2).setYlat(2).setPresetDepotList(s2);
				v.addCustomer().setExternID("2").setXlong(12).setYlat(12).setPresetBlockName("B").setPresetDepotList(s1);
				v.addCustomer().setExternID("3").setXlong(12).setYlat(12).setPresetBlockName("B").setPresetDepotList(s1);

				v.executeRoutePlanning();

				Report rep = v.getReport();
				assertTrue(rep.getRoutes().size() == 2);
				assertTrue(rep.getErrors().getStatistics().size() == 0);

				assertTrue(
						rep.getRoutes().get(0).getEvents().get(0).getID().equals("DEP2") &&
						rep.getRoutes().get(0).getEvents().get(1).getID().equals("1")
						);
				assertTrue(
						rep.getRoutes().get(1).getEvents().get(0).getID().equals("DEP1") &&
						rep.getRoutes().get(1).getEvents().get(1).getID().equals("2")
						);
			}
			// Preset with single and multi depots
			{
				v.clearCustomers();
				v.clearVehicles();
				
				Set<String> s3 = new HashSet<>();
				s3.addAll(s1);
				s3.addAll(s2);

				v.addVehicle().setName("VV").setCapacity(new float[]{2,0,0});

				v.addCustomer().setExternID("1").setXlong(0).setYlat(12).setPresetDepotList(s2);
				v.addCustomer().setExternID("2").setXlong(12).setYlat(0).setPresetDepotList(s3);

				v.executeRoutePlanning();

				Report rep = v.getReport();
				assertTrue(rep.getRoutes().size() == 1);
				assertTrue(rep.getErrors().getStatistics().size() == 0);

				assertTrue(rep.getRoutes().get(0).getEvents().get(0).getID().equals("DEP2"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			failure = true;
		}
		assertFalse(failure);
	}
	
	@Test
	public void testFirstBestPerformance() {
		XFVRP v = createXFVRPRadialInstance(640);
		v.addOptType(XFVRPOptType.CONST);
//		v.addOptType(XFVRPOptType.RUIN_AND_RECREATE);
		v.addOptType(XFVRPOptType.ILS);
		v.setNbrOfLoopsForILS(50);
		v.addOptType(XFVRPOptType.RELOCATE);
		
		long time = System.nanoTime();
		v.executeRoutePlanning();
		System.out.println("Time: "+((System.nanoTime() - time)/1000000f));
		
		Report r = v.getReport();
		System.out.println(StringWriter.write(r));
	}
	
	@Test
	public void testVehiclePriority() {
		XFVRP v = createXFVRPEuclead();
		v.addOptType(XFVRPOptType.RELOCATE);
		addDepot(v, "D", 0, 0);
		addCustomer(v, "C1", 1, 1, 1, 1, 1);
		addCustomer(v, "C2", 2, 2, 1, 1, 1);

		// No priorities given (biggest vehicle must be taken)
		v.addVehicle()
			.setName("V1")
			.setCapacity(new float[]{2, 2, 2});
		v.addVehicle()
			.setName("V2")
			.setCapacity(new float[]{3, 3, 3});
		v.executeRoutePlanning();
		
		Report r = v.getReport();
		assertTrue(r.getRoutes().size() == 1);
		assertTrue(r.getRoutes().get(0).getVehicle().name.equals("V2"));
		
		// All with priority given (not biggest vehicle is used)
		v.clearVehicles();
		v.addVehicle()
			.setName("V1")
			.setCapacity(new float[]{2, 2, 2})
			.setPriority(1);
		v.addVehicle()
			.setName("V2")
			.setCapacity(new float[]{3, 3, 3})
			.setPriority(2);
		v.executeRoutePlanning();
		
		r = v.getReport();
		assertTrue(r.getRoutes().size() == 1);
		assertTrue(r.getRoutes().get(0).getVehicle().name.equals("V1"));
		
		// Some with priorities given (First take vehicle with given prio, then the rest with descending capacity)
		addCustomer(v, "C3", 2, 2, 2, 2, 2);
		
		v.clearVehicles();
		v.addVehicle()
			.setName("V1")
			.setCapacity(new float[]{2, 2, 2})
			.setCount(1)
			.setPriority(1);
		v.addVehicle()
			.setName("V2")
			.setCapacity(new float[]{3, 3, 3})
			.setCount(1);
		v.addVehicle()
			.setName("V3")
			.setCapacity(new float[]{2, 2, 2})
			.setCount(1);
		v.executeRoutePlanning();
		
		r = v.getReport();
		assertTrue(r.getRoutes().size() == 2);
		assertTrue(r.getRoutes().get(0).getVehicle().name.equals("V1"));
		assertTrue(r.getRoutes().get(1).getVehicle().name.equals("V2"));
	}
	
	@Test
	public void testMaxRunningTime() {
		// Running time without restriction
		XFVRP v = createXFVRPRadialInstance(200);
		v.addOptType(XFVRPOptType.ILS);
		v.setNbrOfLoopsForILS(150);
		v.clearStatusMonitor();
		
		long test1Time = System.nanoTime();
		v.executeRoutePlanning();
		test1Time = (long)((System.nanoTime() - test1Time)/1000000f);

		Report r = v.getReport();
		float test1Quality =  r.getSummary().getDistance();
		
		// Running time with restriction of 5 seconds running time
		// Excluding of time for finishing the software
		v = createXFVRPRadialInstance(200);
		v.addOptType(XFVRPOptType.ILS);
		v.setNbrOfLoopsForILS(150);
		v.clearStatusMonitor();

		v.setMaxRunningTime(5);
		long test2Time = System.nanoTime();
		v.executeRoutePlanning();
		test2Time = (long)((System.nanoTime() - test2Time)/1000000f);

		r = v.getReport();
		float test2Quality =  r.getSummary().getDistance();

		// 5 seconds max running time + finishing time = 5,5 seconds (5500 millis)
		assertTrue(test1Time > test2Time);
		assertTrue(test1Time > 5500);
		assertTrue(test2Time < 5500);
		assertTrue((test2Quality*1000f) >= (test1Quality*1000f));
	}
	
	/**
	 * 
	 * @param nbrOfNodes
	 * @return
	 */
	private XFVRP createXFVRPRadialInstance(int nbrOfNodes) {
		Random rand = new Random(1234);
		
		XFVRP v = new XFVRP();
		v.setStatusMonitor(new DefaultStatusMonitor());
		v.setMetric(new EucledianMetric());
		
		v.addDepot().setExternID("D").setXlong(0).setYlat(0);
		
		List<float[]> pointList = new ArrayList<>();

		int B = 16, A = 40;
		int gamma = 0;
		for (int k = 1; k <= B; k++) {
			gamma = 30 * k;
			for (int i = 1; i <= A; i++) {
				pointList.add(new float[]{
						(float)(gamma * Math.cos(2*(i-1)*Math.PI/A)),
						(float)(gamma * Math.sin(2*(i-1)*Math.PI/A)),
						(i % 4 == 2 || i % 4 == 3) ? 30 : 10
				});
			}
		}
		
		int nodeIdx = 0;
		for (int i = 0; i < nbrOfNodes; i++) {
			float[] p1 = pointList.get(i);

			float time = (float)Math.sqrt(Math.pow(p1[0], 2) + Math.pow(p1[1], 2));
			float open = time + rand.nextFloat() * 15f;
			float close = open + 5 + rand.nextFloat() * 5f;
			
			v.addCustomer()
			.setExternID("N"+(nodeIdx++))
			.setXlong(p1[0])
			.setYlat(p1[1])
			.setDemand(new float[]{p1[2], 0, 0})
			.setLoadType(LoadType.PICKUP)
//			.setOpen1(open)
//			.setClose1(close)
			.setServiceTime(0);
		}
		
		v.addVehicle().setName("V").setCapacity(new float[]{1400,0,0}).setMaxRouteDuration(2200);
		
		return v;
	}


	/**
	 * 
	 * @return
	 */
	private XFVRP createXFVRPEuclead() {
		XFVRP v = new XFVRP();
		v.setMetric(new EucledianMetric());

		return v; 
	}

	/**
	 * 
	 * @param v
	 * @param id
	 * @param xlong
	 * @param ylat
	 */
	private void addDepot(XFVRP v, String id, float xlong, float ylat) {
		v.addDepot().setExternID(id).setXlong(xlong).setYlat(ylat);
	}

	/**
	 * 
	 * @param v
	 * @param id
	 * @param xlong
	 * @param ylat
	 */
	private void addCustomer(XFVRP v, String id, float xlong, float ylat) {
		v.addCustomer().setExternID(id).setXlong(xlong).setYlat(ylat);
	}

	/**
	 * 
	 * @param v
	 * @param id
	 * @param xlong
	 * @param ylat
	 * @param demand1
	 * @param demand2
	 * @param demand3
	 */
	private void addCustomer(XFVRP v, String id, float xlong, float ylat, float demand1, float demand2, float demand3) {
		v.addCustomer()
			.setExternID(id)
			.setXlong(xlong)
			.setYlat(ylat)
			.setDemand(new float[]{demand1, demand2, demand3});
	}

	/**
	 * 
	 * @param v
	 * @param id
	 * @param xlong
	 * @param ylat
	 * @param demand1
	 * @param open1
	 * @param close1
	 * @param open2
	 * @param close2
	 */
	private void addCustomer(XFVRP v, String id, float xlong, float ylat, float demand1, int open1, int close1, int open2, int close2) {
		v.addCustomer()
			.setExternID(id)
			.setXlong(xlong)
			.setYlat(ylat)
			.setDemand(new float[]{demand1,0,0})
			.setOpen1(open1)
			.setClose1(close1)
			.setOpen2(open2)
			.setClose2(close2);
	}

	/**
	 * 
	 * @param v
	 * @param id
	 * @param cap1
	 * @param cap2
	 * @param cap3
	 */
	private void addVehicle(XFVRP v, String id, float cap1, float cap2, float cap3) {
		v.addVehicle()
			.setName(id)
			.setCapacity(new float[]{cap1,cap2,cap3});
	}
}
