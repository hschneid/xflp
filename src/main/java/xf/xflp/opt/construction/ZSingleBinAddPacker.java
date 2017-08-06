package xf.xflp.opt.construction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xf.xflp.base.problem.Container;
import xf.xflp.base.problem.HighestLowerLeft;
import xf.xflp.base.problem.Item;
import xf.xflp.base.problem.Position;
import xf.xflp.base.problem.StrategyIf;

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
public class ZSingleBinAddPacker {

	public static boolean VERBOSE = false;
	public static boolean VERBOSE_KEYFIGURES = true;

	/*
	 * (non-Javadoc)
	 * @see de.psi.efload.packerapi.IntegralPacker#createLoadingPlan(de.psi.efload.packerapi.TourTransport[], de.psi.efload.packerapi.ContainerType[])
	 */
	public Item[] createLoadingPlan(Item[] items, Container container) {
		Map<Integer, Integer> rejectedIndexMap = new HashMap<>();
		List<Item> rejectedItemList = new ArrayList<>();
//		StrategyIf strategy = new TouchingPerimeter();
		StrategyIf strategy = new HighestLowerLeft();
//		StrategyIf strategy = new MaxFreeLoadingMeter();

		// �ber alle Items in der sortierten Reihenfolge
		for (int i = 0; i < items.length; i++) {
			Item item = items[i];
//			System.out.println(i+" I "+item.externalIndex+" ("+item.w+","+item.l+","+item.h+") ["+item.weight+","+item.stackingWeightLimit+"] "+item.stackingGroup);

			// F�ge Item zum Container hinzu
			Position insertPosition = null;

			// Pr�fe die Vertr�glichkeit des Stack mit dem ContainerTyp ab
			if(item.allowedContainerSet.contains(container.getContainerType())) {				
				// Hole die vorhandenen Einf�ge-Positionen
				List<Position> posList = container.getPossibleInsertPositionList(item);

//				for (Position p : posList)
//					System.out.println(i+" - ("+p.getX()+","+p.getY()+","+p.getZ()+") "+(p instanceof RotatedPosition));
				
				if(posList.size() != 0) {
					// Je nach Einf�ge-Strategie
//					insertPosition = item.insertStrategy.get().choose(item, container, posList);
					insertPosition = strategy.choose(item, container, posList);
//					System.out.println(i+" P ("+insertPosition.getX()+","+insertPosition.getY()+","+insertPosition.getZ()+") "+(insertPosition instanceof RotatedPosition));
				}
			}

			// F�ge Item dem Container hinzu
			if(insertPosition != null) {						
				container.add(item, insertPosition);
			} else {
				if(VERBOSE)
					System.out.println("Item "+item.index +" konnte nicht hinzugef�gt werden.");
				rejectedItemList.add(item);
				rejectedIndexMap.put(item.externalIndex, null);
			}
		}

		return rejectedItemList.toArray(new Item[0]);
	}
}
