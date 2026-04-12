package xf.xflp.base;

import xf.xflp.base.container.Container;
import xf.xflp.base.fleximport.DataManager;
import xf.xflp.base.item.Item;
import xf.xflp.base.item.ItemPlacement;
import xf.xflp.report.ContainerReport;
import xf.xflp.report.LPPackageEvent;
import xf.xflp.report.LPReport;

/**
 * Copyright (c) 2012-2026 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 */
public class XFLPSolution {

	private final XFLPModel model;
	private final DataManager dataManager;

	public XFLPSolution(XFLPModel model, DataManager dataManager) {
		this.model = model;
		this.dataManager = dataManager;
	}

	public LPReport getReport() {
		LPReport rep = new LPReport();

		// Add packed containers to report
		for (Container con : model.getContainers()) {
			String containerTypeName = dataManager.getContainerTypeName(con.getContainerType());
			ContainerReport cRep = new ContainerReport(containerTypeName, con);

			for (ItemPlacement item : con.getHistory()) {
				LPPackageEvent e = new LPPackageEvent(
						dataManager.getItemId(item.getItem().externalIndex),
						item.x,
						item.y,
						item.z,
						item.w,
						item.l,
						item.h,
						item.getItem().stackingGroup,
						item.getItem().weight,
						item.getItem().stackingWeightLimit,
						false, // isInvalid
						item.getItem().loadingType,
						item.getVolume(),
						item.getItem().getWeight(),
						0, // NbrOfStacks
						item.isRotated
				);
				cRep.add(e);
			}

			rep.add(cRep);
		}

		// Add unplanned items to report
		for (Item unplannedItem : model.getUnplannedItems()) {
			LPPackageEvent e = new LPPackageEvent(
					dataManager.getItemId(unplannedItem.externalIndex),
					-1,
					-1,
					-1,
					unplannedItem.origW,
					unplannedItem.origL,
					unplannedItem.origH,
					unplannedItem.stackingGroup,
					unplannedItem.weight,
					unplannedItem.stackingWeightLimit,
					false, // isInvalid
					unplannedItem.loadingType,
					unplannedItem.getVolume(),
					0,
					0, // NbrOfStacks
					false
			);

			rep.addUnplannedPackages(e);
		}

		return rep;
	}

	public XFLPModel getModel() {
		return model;
	}
}
