package script;

import util.XFVRPFileUtil;
import xf.xfvrp.XFVRP;
import xf.xfvrp.base.monitor.StatusCode;
import xf.xfvrp.base.monitor.StatusMonitor;
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
public class PDPFromFileTest {

	public PDPFromFileTest() {
		StatusMonitor testStatusManager = new StatusMonitor() {
			@Override
			public void getMessage(StatusCode code, String message) {
				System.out.println(message);
			}
		};
		
		String name = "/Users/hschneid/Downloads/krummen.xfvrp/Krummen.17.instance.1500.stops.txt.gz";
		String s = XFVRPFileUtil.readCompressedFile(name);
		
		XFVRP x = new XFVRP();
		x.importFromString(s);
		s = null;
		
//		x.addOptType(XFVRPOptType.PDP_CHEAPEST_INSERT);
		x.addOptType(XFVRPOptType.PDP_RELOCATE);
//		x.addOptType(XFVRPOptType.PDP_ILS);
		
		x.allowsPDPPlanning();
		x.setStatusMonitor(testStatusManager);
		x.setNbrOfLoopsForILS(50);
		
		long t = System.nanoTime();
		x.executeRoutePlanning();
		System.out.println("T = "+(System.nanoTime() - t)/1000000.0);
		
		Report r = x.getReport();
		System.out.println(StringWriter.write(r));
	}
	
	public static void main(String[] args) {
		new PDPFromFileTest();
	}

}
