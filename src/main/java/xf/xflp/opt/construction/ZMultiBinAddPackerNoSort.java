package xf.xflp.opt.construction;

import java.util.ArrayList;
import java.util.List;

import xf.xflp.base.problem.Container;
import xf.xflp.base.problem.Item;


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
public class ZMultiBinAddPackerNoSort {

	public static boolean VERBOSE = false;
	public static boolean VERBOSE_KEYFIGURES = true;

	/*
	 * (non-Javadoc)
	 * @see de.psi.efload.packerapi.IntegralPacker#createLoadingPlan(de.psi.efload.packerapi.TourTransport[], de.psi.efload.packerapi.ContainerType[])
	 */

	public Container[] createLoadingPlan(Item[] items, Container container) {
		ZSingleBinAddPacker singlePacker = new ZSingleBinAddPacker();
		
		int containerIdx = container.getIndex() + 1;
		
		List<Container> containerList = new ArrayList<>();
		Item[] unpackedItems = items;
		
//		int sumVolume = 0;
		while(unpackedItems.length > 0) {
			Container currentContainer = new Container(
					container.getWidth(),
					container.getLength(),
					container.getHeight(),
					container.getMaxWeight(),
					container.getContainerType(),
					container.getLifoImportance());
			currentContainer.setIndex(containerIdx++);
			
			unpackedItems = singlePacker.createLoadingPlan(unpackedItems, currentContainer);
			
//			sumVolume += currentContainer.getLoadedVolume();
			
			if(currentContainer.getNbrOfLoadedThings() > 0)
				containerList.add(currentContainer);
		}
		
//		System.out.println("Nbr = "+containerList.size()+" Utilisation = "+((sumVolume / (float)containerList.size())/108f)*100+" %");
			
		return containerList.toArray(new Container[0]);
	}
}
