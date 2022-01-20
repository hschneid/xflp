package xf.xflp.base.container;

import util.collection.LPListMap;
import xf.xflp.base.item.Item;
import xf.xflp.base.item.Position;
import xf.xflp.base.item.PositionType;

import java.util.*;

/**
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 *
 */
public class AddRemoveContainer extends ContainerBase {

	private static final Position rootPos = Position.of( -1, -1, -1);

	private final Set<Position> inactivePosList = new HashSet<>();
	private final List<Position> coveredPosList = new ArrayList<>();

	/* Relation graph between position - Father position -> [child positions] */
	private final LPListMap<Position, Position> posFollowerMap = new LPListMap<>();
	private final Map<Position, Position> posAncestorMap = new HashMap<>();

	/* Position -> Item */
	private final Map<Position, Item> positionItemMap = new HashMap<>();

	/* Is called by reflection */
	public AddRemoveContainer(
			int width,
			int length,
			int height,
			float maxWeight,
			int containerType,
			GroundContactRule groundContactRule,
			float lifoImportance
	) {
		super(width, length, height, maxWeight, containerType, groundContactRule, lifoImportance);
		init();
	}

	public AddRemoveContainer(Container containerPrototype) {
		super(containerPrototype);
		init();
	}

	private void init() {
		// Die root-Position befindet sich nicht im 3D-Raum. Alle
		// realen Positionen erben von dieser virtuellen.
		insertTree(activePosList.get(0), rootPos);
	}

	@Override
	public Container newInstance() {
		return new AddRemoveContainer(this);
	}

	/**
	 * Adds item to container and update internal data structure
	 */
	@Override
	public int add(Item item, Position pos, boolean isRotated) {
		pos = normPosition(item, pos, isRotated);

		addItem(item, pos);

		// Active position gets inactive by adding item
		switchActive2Inactive(pos);

		// Setze �berlagerte Positionen auf inaktiv
		List<Position> covPosList = findCoveredPositions(item);
		for (Position covPos : covPosList)
			switchActive2Covered(covPos);

		// Create new insert positions and spaces
		List<Position> newPosList = findInsertPositions(item);
		for (Position newPos : newPosList) {
			activePosList.add(newPos);
			// Die neue Position ist von der �bergebenen Position aus abh�ngig.
			insertTree(newPos, pos);
			// Diese Position wurde von diesem Item erzeugt.
			positionItemMap.put(newPos, item);
		}

		updateBearingCapacity(List.of(item));

		history.add(item);

		return item.index;
	}

	/**
	 * Remove item from container and update internal data structure
	 */
	@Override
	public void remove(Item item) {
		List<Item> lowerItems = zGraph.getItemsBelow(item);

		// Remove item
		removeItem(item);

		Position position = itemPositionMap.remove(item);
		if(position == null)
			throw new ArrayIndexOutOfBoundsException("BIG FEHLER: Stack ist keiner Position zugeordnet");

		// Setze Position wieder aktiv
		switchInactive2Active(position);

		// Setze alle �berdeckten Positionen auf aktiv
		List<Position> coveredPosList = findUncoveringPositions(item);
		for (Position pos : coveredPosList)
			switchCovered2Active(pos);

		// Projeziere alle Positionen auf der Oberfl�che des Objektes (Hori und Verti)
		List<Position> projectablePosHList = findProjectableHorizontalPositions(item);
		for (Position pos : projectablePosHList) {
			Item leftItem = findNextLeftElement(pos);
			Position newPosition = Position.of(
					pos.idx, (leftItem != null) ? leftItem.xw : 0, pos.y, pos.z, pos.type
			);
			replacePosition(pos, newPosition);
		}
		List<Position> projectablePosVList = findProjectableVerticalPositions(item);
		for (Position pos : projectablePosVList) {
			Item lowerItem = findNextDeeperElement(pos);
			Position newPosition = Position.of(
					pos.idx, pos.x, ((lowerItem != null) ? lowerItem.yl : 0), pos.z, pos.type
			);
			replacePosition(pos, newPosition);
		}

		// L�sche die alte Position, wenn deren Elter noch aktiv ist und selbst seine Nachfolger alle weg sind
		// Das muss rekusriv dann f�r dessen Eltern gepr�ft werden.
		// SONST setze Position auf aktiv statt inaktiv
		checkPosition(position);

		updateBearingCapacity(lowerItems);

		history.add(item);
	}

	private void switchInactive2Active(Position pos) {
		inactivePosList.remove(pos);
		activePosList.add(pos);
	}

	private void switchCovered2Active(Position pos) {
		coveredPosList.remove(pos);
		activePosList.add(pos);
	}

	private void switchActive2Inactive(Position pos) {
		activePosList.remove(pos);
		inactivePosList.add(pos);
	}

	private void switchActive2Covered(Position pos) {
		activePosList.remove(pos);
		coveredPosList.add(pos);
	}

	/**
	 *
	 */
	private List<Position> findProjectableHorizontalPositions(Item item) {
		List<Position> list = new ArrayList<>();
		for (Position pos : activePosList) {
			if(pos.type == PositionType.EXTENDED_H)
				if(pos.x == item.xw && pos.y >= item.y && pos.y < item.yl)
					list.add(pos);
		}
		return list;
	}

	/**
	 *
	 */
	private List<Position> findProjectableVerticalPositions(Item item) {
		List<Position> list = new ArrayList<>();
		for (Position pos : activePosList) {
			if(pos.type == PositionType.EXTENDED_V)
				if(pos.y == item.yl && pos.x >= item.x && pos.x < item.xw)
					list.add(pos);
		}
		return list;
	}

	/**
	 * Search inactive positions, which got uncovered through removal of an item.
	 */
	private List<Position> findUncoveringPositions(Item item) {
		List<Position> list = new ArrayList<>();
		for (Position pos : coveredPosList) {
			if(pos.z == item.z && pos.x == item.x && pos.y >= item.y && pos.y < item.yl)
				list.add(pos);
			else if(pos.z == item.z && pos.x == item.xw && pos.y >= item.y && pos.y < item.yl && pos.type  == PositionType.EXTENDED_H && !itemPositionMap.inverse().containsKey(pos))
				list.add(pos);
			else if(pos.z == item.z && pos.y == item.y && pos.x >= item.x && pos.x < item.xw)
				list.add(pos);
			else if(pos.z == item.z && pos.y == item.yl && pos.x >= item.x && pos.x < item.xw && pos.type == PositionType.EXTENDED_V && !itemPositionMap.inverse().containsKey(pos))
				list.add(pos);
		}
		return list;
	}

	/**
	 *
	 */
	private void insertTree(Position entry, Position ancestor) {
		posAncestorMap.put(entry, ancestor);
		posFollowerMap.put(ancestor, entry);
	}

	/**
	 * Recursive function
	 *
	 * Checks for a certain position, if all following positions are inactive.
	 * If so, then these follower positions can be deleted.
	 */
	private void checkTreeAndRemove2(Position pos) {
		List<Position> mapList = posFollowerMap.get(pos);
		if(mapList != null) {
			// List copy, because iteration on this list to change its contents
			List<Position> followerList = new ArrayList<>(mapList);
			for (Position follower: followerList) {
				if(inactivePosList.contains(follower))
					return;

				// If follower has more followers, deep into and check them
				if(posFollowerMap.containsKey(follower) && posFollowerMap.get(follower).size() > 0)
					checkTreeAndRemove2(follower);

				// If follower has still followers after deep check, then ignore
				if(posFollowerMap.containsKey(follower) && posFollowerMap.get(follower).size() > 0)
					return;
			}

			// Only if all positions are active and have no more follower
			// all followers can be deleted
			for (Position follower: followerList)
				removePosition(follower);
		}
	}

	/**
	 * Removes a position if it is not used as possible insert position anymore.
	 */
	private void removePosition(Position pos) {
		if(pos.type != PositionType.ROOT) {
			posFollowerMap.remove(pos);
			posFollowerMap.get(posAncestorMap.get(pos)).remove(pos);
			posAncestorMap.remove(pos);
			activePosList.remove(pos);
			inactivePosList.remove(pos);
			coveredPosList.remove(pos);
			positionItemMap.remove(pos);
		}
	}

	/**
	 * Is used for re-projected positions during removeItem
	 */
	private void replacePosition(Position oldPosition, Position newPosition) {
		if(oldPosition.type != PositionType.ROOT) {
			posFollowerMap.put(newPosition, posFollowerMap.get(oldPosition));
			for (Position key : posFollowerMap.keySet()) {
				List<Position> follower = posFollowerMap.get(key);
				if(follower != null && follower.contains(oldPosition)) {
					follower.remove(oldPosition);
					follower.remove(newPosition);
				}
			}

			posAncestorMap.put(newPosition, posAncestorMap.get(oldPosition));
			for (Map.Entry<Position, Position> e : posAncestorMap.entrySet()) {
				if(e.getValue() == oldPosition) {
					posAncestorMap.put(e.getKey(), newPosition);
				}
			}

			positionItemMap.put(newPosition, positionItemMap.get(oldPosition));
			for (Map.Entry<Item, Position> e : itemPositionMap.entrySet()) {
				if(e.getValue() == oldPosition)
					itemPositionMap.put(e.getKey(), newPosition);
			}

			activePosList.remove(oldPosition);
			activePosList.add(newPosition);
			inactivePosList.remove(oldPosition);
			inactivePosList.add(newPosition);
			coveredPosList.remove(oldPosition);
			coveredPosList.add(newPosition);
		}

	}

	/**
	 *
	 */
	private void checkPosition(Position pos) {
		// Removes active unused follower-positions
		checkTreeAndRemove2(pos);

		Position ancestor = posAncestorMap.get(pos);

		if(
			// Wenn die Position keine Nachfolger mehr hat, weil durch CheckTreeAndRemove gel�scht wurde und
				(!posFollowerMap.containsKey(pos) || posFollowerMap.get(pos).isEmpty())
						// Wenn Vorg�nger (der die Position erzeugt hat) frei ist und
						&& activePosList.contains(ancestor)
						// die Position nicht der Root ist, dann l�sche die Position
						&& pos.type != PositionType.ROOT) {
			// L�sche pos
			removePosition(pos);

			// Pr�fe den Vorg�nger ebenfalls (rekursiv)
			checkPosition(ancestor);
		}
	}

	/**
	 *
	 */
	private void removeItem(Item item) {
		Integer index = item.index;

		// Delete from Z-Graph
		zGraph.remove(item);

		itemList.remove(item.index);

		xMap.get(item.x).remove(index);
		xMap.get(item.xw).remove(index);
		yMap.get(item.y).remove(index);
		yMap.get(item.yl).remove(index);
		zMap.get(item.z).remove(index);
		zMap.get(item.zh).remove(index);

		weight -= item.weight;
		item.h = item.origH;

		item.containerIndex = -1;
	}
}
