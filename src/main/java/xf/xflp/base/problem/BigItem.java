package xf.xflp.base.problem;

import java.util.HashSet;
import java.util.Set;

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
public class BigItem extends Item {

	private final Item[] items;
	private final int[][] positions;
	@SuppressWarnings("unused")
	private final boolean[] rotations;

	/**
	 * 
	 * @param items
	 * @param positions
	 * @param rotations
	 * @param bigPos
	 * @param externalIdx
	 * @param orderIndex
	 * @param loadingLoc
	 * @param unLoadingLoc
	 * @param width
	 * @param length
	 * @param height
	 * @param weight
	 * @param bearingCapacity
	 * @param allowedContainerSet
	 * @param stackingGroup
	 * @param allowedStackingGroups
	 * @param isSpinable
	 * @param isLoading
	 */
	private BigItem(
			Item[] items,
			int[][] positions,
			boolean[] rotations,
			Position bigPos,
			int externalIdx,
			int orderIndex,
			int loadingLoc,
			int unLoadingLoc,
			int width,
			int length,
			int height,
			float weight,
			float bearingCapacity,
			Set<Integer> allowedContainerSet,
			int stackingGroup,
			int allowedStackingGroups,
			boolean isSpinable,
			boolean isLoading
			) {
		super(externalIdx,
				orderIndex,
				loadingLoc,
				unLoadingLoc,
				width,
				length,
				height,
				weight,
				bearingCapacity,
				allowedContainerSet,
				stackingGroup,
				allowedStackingGroups,
				isSpinable,
				isLoading);
		this.items = items;
		this.positions = positions;
		this.rotations = rotations;
		
		// Set position of big block
		clearPosition();
		setPosition(bigPos);
	}
	
	/**
	 * 
	 * @param itemsParam
	 * @return
	 */
	public static final BigItem create(Item[] itemsParam) {
		Item[] items = new Item[itemsParam.length];
		System.arraycopy(itemsParam, 0, items, 0, itemsParam.length);
		
		// Eval measures of item heap -> new big block
		int maxW = 0, minX = Integer.MAX_VALUE;
		int maxL = 0, minY = Integer.MAX_VALUE;
		int maxZ = 0, minZ = Integer.MAX_VALUE;
		int stackingGroup = items[0].stackingGroup;
		int allowedStackingGroups = items[0].allowedStackingGroups;
		float weight = 0;
		Set<Integer> allowedContainerSet = new HashSet<>();
		float stackingWeightLimit = Float.MAX_VALUE;
		
		for (int i = 0; i < items.length; i++) {
			minX = Math.min(minX, items[i].x);
			maxW = Math.max(maxW, items[i].xw);
			minY = Math.min(minY, items[i].y);
			maxL = Math.max(maxL, items[i].yl);
			minZ = Math.min(minZ, items[i].z);
			maxZ = Math.max(maxZ, items[i].zh);
			weight += items[i].weight;
			stackingGroup &= items[i].stackingGroup;
			allowedStackingGroups &= items[i].allowedStackingGroups;
			allowedContainerSet.addAll(items[0].allowedContainerSet);
			stackingWeightLimit = Math.min(stackingWeightLimit, items[i].stackingWeightLimit);
		}
		
		// Positions of individual items in the big block
		int[][] positions = new int[items.length][3];
		for (int i = 0; i < items.length; i++) {
			positions[i][0] = items[i].x - minX;
			positions[i][1] = items[i].y - minY;
			positions[i][2] = items[i].z - minZ;
		}

		// State of rotation
		boolean[] rotations = new boolean[items.length];
		for (int i = 0; i < items.length; i++)
			rotations[i] = items[i].isRotated;

		// Assign general attributes
		int externalIdx = items[0].externalIndex;
		int orderIndex = items[0].orderIndex;
		int loadingLoc = items[0].loadingLoc;
		int unLoadingLoc = items[0].unLoadingLoc;
		int w = maxW - minX;
		int l = maxL - minY;
		int h = maxZ - minZ;
		boolean spinable = true;
		boolean isLoaded = true;
		
		return new BigItem(
				items,
				positions,
				rotations,
				new Position(minX, minY, minZ),
				externalIdx,
				orderIndex,
				loadingLoc,
				unLoadingLoc,
				w,
				l,
				h,
				weight,
				stackingWeightLimit,
				allowedContainerSet,
				stackingGroup,
				allowedStackingGroups,
				spinable,
				isLoaded
				);
	}

	/**
	 * 
	 * @return
	 */
	public Item[] getItems() {
		// Skaliere/Update Items mit Position des BigItems
		// Achte auf Rotation
		Item[] newItems = new Item[items.length];
		for (int i = 0; i < newItems.length; i++) {
			newItems[i] = items[i].copy();
			
			Position pos = null;
			
			if(this.isRotated) {
				pos = new RotatedPosition(
						new Position(
						this.x + positions[i][1], // x' = y
						this.y + (l - positions[i][0]), // y' = l - x
						this.z + positions[i][2] // z' = z
						));
				newItems[i].rotate();
			} else
				pos = new Position(
						this.x + positions[i][0],
						this.y + positions[i][1],
						this.z + positions[i][2]
						);
			
			newItems[i].setPosition(pos);
			newItems[i].containerIndex = this.containerIndex;
		}
		
		return newItems;
	}
}
