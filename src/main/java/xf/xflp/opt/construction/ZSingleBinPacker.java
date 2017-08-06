package xf.xflp.opt.construction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xf.xflp.base.XFLPModel;
import xf.xflp.base.problem.Container;
import xf.xflp.base.problem.HighestLowerLeft;
import xf.xflp.base.problem.Item;
import xf.xflp.base.problem.Position;
import xf.xflp.base.problem.StrategyIf;
import xf.xflp.opt.XFLPBase;
import xf.xflp.report.PackageEventType;

/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * Item packer for single container with adding and removing items
 * 
 * This packer puts the items in a sequence into one single container.
 * It is able to add and to remove the items with respect ot theit loading type.
 * There is no optimization in container allocation or item sequence.
 * 
 * @author hschneid
 *
 */
public class ZSingleBinPacker extends XFLPBase {

	public static boolean VERBOSE = false;
	public static boolean VERBOSE_KEYFIGURES = true;

	/*
	 * (non-Javadoc)
	 * @see de.psi.efload.packerapi.IntegralPacker#createLoadingPlan(de.psi.efload.packerapi.TourTransport[], de.psi.efload.packerapi.ContainerType[])
	 */
	@Override
	public void execute(XFLPModel model) {
		Container container = new Container(model.getContainerTypes()[0], 1f);
		
		Map<Integer, Item> loadedItemMap = new HashMap<>();

		List<Item> unplannedItemList = new ArrayList<>();
		//		StrategyIf strategy = new TouchingPerimeter();
		StrategyIf strategy = new HighestLowerLeft();
		//		StrategyIf strategy = new MaxFreeLoadingMeter();

		// �ber alle Items in der sortierten Reihenfolge
		Item[] items = model.getItems();
		for (int i = 0; i < items.length; i++) {
			Item item = items[i];

			if(item.loadingType == PackageEventType.LOAD) {

				// F�ge Item zum Container hinzu
				Position insertPosition = null;

				// Pr�fe die Vertr�glichkeit des Stack mit dem ContainerTyp ab
				if(item.allowedContainerSet.contains(container.getContainerType())) {				
					// Hole die vorhandenen Einf�ge-Positionen
					List<Position> posList = container.getPossibleInsertPositionList(item);

					if(posList.size() != 0) {
						// Je nach Einf�ge-Strategie
						insertPosition = strategy.choose(item, container, posList);
					}
				}

				// F�ge Item dem Container hinzu
				if(insertPosition != null) {						
					container.add(item, insertPosition);
					loadedItemMap.put(item.externalIndex, item);
				} else {
					if(VERBOSE)
						System.out.println("Item "+item.index +" konnte nicht hinzugef�gt werden.");
					unplannedItemList.add(item);
				}
			} else {
				// Remove item from container
				// It is not checked if item was really loaded to container.
				// Before removing the unloading item must be replaced by the loaded item object
				// for index problems
				if(loadedItemMap.containsKey(item.externalIndex))
					container.remove(loadedItemMap.get(item.externalIndex));
			}
		}

		// Put result into model
		model.setContainers(new Container[]{container});
		model.setUnplannedItems(unplannedItemList.toArray(new Item[0]));
	}
}
