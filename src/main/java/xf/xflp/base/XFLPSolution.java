package xf.xflp.base;

import xf.xflp.base.problem.Container;
import xf.xflp.base.problem.Item;
import xf.xflp.report.ContainerReport;
import xf.xflp.report.LPPackageEvent;
import xf.xflp.report.LPReport;

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
public class XFLPSolution {

	private final XFLPModel model;
	
	/**
	 * 
	 * @param model
	 */
	public XFLPSolution(XFLPModel model) {
		this.model = model;
	}
	
	/**
	 * 
	 * @return
	 */
	public LPReport getReport() {
		LPReport rep = new LPReport(model);
		
		// Add packed containers to report
		for (Container con : model.getContainers()) {
			ContainerReport cRep = new ContainerReport(con);
			
			for (Item item : con.getHistory()) {
				LPPackageEvent e = new LPPackageEvent();
				e.setId(item.externalIndex+"");
				e.setX(item.x);
				e.setY(item.y);
				e.setZ(item.z);
				e.setWidth(item.w);
				e.setLength(item.l);
				e.setHeight(item.h);
				e.setType(item.loadingType);
				e.setWeight(item.weight);
				e.setWeightLimit(item.stackingWeightLimit);
				e.setStackingGrp(item.stackingGroup);
				e.setUsedVolumeInContainer(item.volume);
				cRep.add(e);
			}
			
			rep.add(cRep);
		}
		
		// Add unplanned items to report
		for (Item unplannedItem : model.getUnplannedItems()) {
			LPPackageEvent e = new LPPackageEvent();
			e.setId(unplannedItem.externalIndex+"");
			e.setWidth(unplannedItem.w);
			e.setLength(unplannedItem.l);
			e.setHeight(unplannedItem.h);
			e.setType(unplannedItem.loadingType);
			e.setWeight(unplannedItem.weight);
			e.setWeightLimit(unplannedItem.stackingWeightLimit);
			e.setStackingGrp(unplannedItem.stackingGroup);
			e.setUsedVolumeInContainer(unplannedItem.volume);

			rep.addUnplannedPackages(e);
		}
		
		return rep;
	}
}
