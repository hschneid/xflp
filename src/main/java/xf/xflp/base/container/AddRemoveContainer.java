package xf.xflp.base.container;

import util.collection.LPListMap;
import xf.xflp.base.item.ItemPlacement;
import xf.xflp.base.item.Position;
import xf.xflp.base.item.PositionType;
import xf.xflp.base.item.Space;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Copyright (c) 2012-2026 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 *
 */
public final class AddRemoveContainer extends ContainerBase implements Container {

	private static final Position rootPos = Position.of( -1, -1, -1);

	private final Set<Position> inactivePosList = new HashSet<>();
	private final List<Position> coveredPosList = new ArrayList<>();

	/* Relation graph between position - Father position -> [child positions] */
	private final LPListMap<Position, Position> posFollowerMap = new LPListMap<>();
	private final Map<Position, Position> posAncestorMap = new HashMap<>();

	/* Position -> Item - Which item is responsible, that this position was created. */
	private final Map<Position, ItemPlacement> positionItemMap = new HashMap<>();


	/* Is called by reflection */
	public AddRemoveContainer(
			int width,
			int length,
			int height,
			float maxWeight,
			int containerType,
			ContainerParameter parameter
	) {
		super(width, length, height, maxWeight, containerType, parameter);
		init();
	}

	public AddRemoveContainer(Container containerPrototype) {
		super(containerPrototype);
		init();
	}

	private void init() {
		// Die root-Position befindet sich nicht im 3D-Raum. Alle
		// realen Positionen erben von dieser virtuellen.
		insertTree(activePosList.getFirst(), rootPos);
	}

	@Override
	public Container newInstance() {
		return new AddRemoveContainer(this);
	}

	/**
	 * Adds item to container and update internal data structure
	 */
	@Override
	public int add(ItemPlacement item, Position pos, boolean isRotated) {
		pos = normPosition(item, pos, isRotated);

		addItem(item, pos);

		// Active position gets inactive by adding item
		switchActive2Inactive(pos);

		// Switch covered positions to inactive
		List<Position> covPosList = findCoveredPositions(item);
		for (Position covPos : covPosList) {
			switchActive2Covered(covPos);
			spacePositions.remove(covPos);
		}

		// Check existing spaces, if new item will shrink them
		checkExistingSpaces(item);

		// Create new insert positions and spaces
		List<Position> newPosList = findInsertPositions(item);
		for (Position newPos : newPosList) {
			if(uniquePositionKeys.contains(newPos.getKey())) {
				continue;
			}

			activePosList.add(newPos);
			uniquePositionKeys.add(newPos.getKey());

			List<Space> newSpaces = createSpaces(newPos);
			if(!newSpaces.isEmpty()) {
				// Erzeuge wirklich die neue Position, weil es einen gültigen Space gibt
				spacePositions.put(newPos, newSpaces);

				// Die neue Position ist von der �bergebenen Position aus abh�ngig.
				insertTree(newPos, pos);
				// Diese Position wurde von diesem Item erzeugt.
				positionItemMap.put(newPos, item);
			} else {
				// The free space of the new position is so small, that the position is not valid anymore.
				removeNewPosition(newPos);
			}
		}

		updateBearingCapacity(List.of(item));
		addToCenterOfGravity(item, pos);

		history.add(item);
		return item.index;
	}

	private void check() {
		for (Map.Entry<Position, Position> e : posAncestorMap.entrySet()) {
			if(e.getValue() == null)
				System.out.println("MISS_ANC: " + e.getKey());
		}

		for (Position e : posFollowerMap.keySet()) {
			if(posFollowerMap.get(e) == null)
				System.out.println("MISS_FOL: " + e.getKey());
		}
	}

	private void output() {
		// OUTPUT
		System.out.println("ITM\n"+itemList.stream()
				.filter(Objects::nonNull)
				.map(i -> "  "+ i)
				.collect(Collectors.joining("\n")));
		System.out.println("POS "+activePosList.stream()
				.map(Position::toString)
				.collect(Collectors.joining(",")));
		System.out.println("INV "+inactivePosList.stream()
				.map(Position::toString)
				.collect(Collectors.joining(",")));
		System.out.println("FOL\n"+posFollowerMap.keySet().stream()
				.map(k -> "  "+k.toString()+" ->\n" +
						posFollowerMap.get(k).stream().map(p -> "     "+p.toString()).collect(Collectors.joining("\n"))
				)
				.collect(Collectors.joining("\n")));
		System.out.println("ANC\n"+posAncestorMap.keySet().stream()
				.map(k -> "  "+k.toString()+" -> " +
						posAncestorMap.get(k).toString())
				.collect(Collectors.joining("\n")));
		System.out.println("---------------");
	}


	/**
	 * Remove item from container and update internal data structure
	 */
	@Override
	public void remove(ItemPlacement item) {
		List<ItemPlacement> lowerItems = zGraph.getItemsBelow(item);

		// Remove item
		removeItem(item);

		Position position = itemPositionMap.remove(item);
		if(position == null)
			throw new ArrayIndexOutOfBoundsException("BIG ERROR: Item is not allocated to any position");

		// Setze Position wieder aktiv
		switchInactive2Active(position);
		recreateSpaces(position);

		// SPACES ---------------------
		checkExistingSpacesForRemovedItem(item);

		// Setze alle �berdeckten Positionen auf aktiv
		List<Position> coveredPosList = findUncoveringPositions(item);
		for (Position pos : coveredPosList) {
			switchCovered2Active(pos);
			recreateSpaces(pos);
		}

		// Projeziere alle Positionen auf der Oberfl�che des Objektes (Hori und Verti)
		List<Position> projectablePosHList = findProjectableHorizontalPositions(item);
		for (Position pos : projectablePosHList) {
			ItemPlacement leftItem = findNextLeftElement(pos);
			int newX = (leftItem != null) ? leftItem.xw : 0;
			Position newPosition = createPosition(pos.idx(), newX, pos.y(), pos.z(), pos.type());
			replacePosition(pos, newPosition);
			recreateSpaces(newPosition);
		}
		List<Position> projectablePosVList = findProjectableVerticalPositions(item);
		for (Position pos : projectablePosVList) {
			ItemPlacement lowerItem = findNextDeeperElement(pos);
			int newY = (lowerItem != null) ? lowerItem.yl : 0;
			Position newPosition = createPosition(pos.idx(), pos.x(), newY, pos.z(), pos.type());
			if(!pos.equals(newPosition)) {
				replacePosition(pos, newPosition);
				recreateSpaces(newPosition);
			}
		}

		// L�sche die alte Position, wenn deren Elter noch aktiv ist und selbst seine Nachfolger alle weg sind
		// Das muss rekusriv dann f�r dessen Eltern gepr�ft werden.
		// SONST setze Position auf aktiv statt inaktiv
		checkPosition(position);

		updateBearingCapacity(lowerItems);
		removeFromCenterOfGravity(item, position);

		// Check dominated spaces
		spaceService.getDominatingSpaces(
				spacePositions.values().stream().flatMap(Collection::stream).toList()
		);
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
		spacePositions.remove(pos);
	}

	private void switchActive2Covered(Position pos) {
		activePosList.remove(pos);
		coveredPosList.add(pos);
	}

	private List<Position> findProjectableHorizontalPositions(ItemPlacement item) {
		List<Position> list = new ArrayList<>();
		for (Position pos : activePosList) {
			if(pos.type() == PositionType.EXTENDED_H)
				if(pos.x() == item.xw && pos.y() >= item.y && pos.y() < item.yl)
					list.add(pos);
		}
		return list;
	}

	private List<Position> findProjectableVerticalPositions(ItemPlacement item) {
		List<Position> list = new ArrayList<>();
		for (Position pos : activePosList) {
			if(pos.type() == PositionType.EXTENDED_V)
				if(pos.y() == item.yl && pos.x() >= item.x && pos.x() < item.xw)
					list.add(pos);
		}
		return list;
	}

	/**
	 * Search inactive positions, which got uncovered through removal of an item.
	 */
	private List<Position> findUncoveringPositions(ItemPlacement item) {
		List<Position> list = new ArrayList<>();
		for (Position pos : coveredPosList) {
			if(pos.z() == item.z && pos.x() == item.x && pos.y() >= item.y && pos.y() < item.yl)
				list.add(pos);
			else if(pos.z() == item.z && pos.x() == item.xw && pos.y() >= item.y && pos.y() < item.yl && pos.type()  == PositionType.EXTENDED_H && !itemPositionMap.inverse().containsKey(pos))
				list.add(pos);
			else if(pos.z() == item.z && pos.y() == item.y && pos.x() >= item.x && pos.x() < item.xw)
				list.add(pos);
			else if(pos.z() == item.z && pos.y() == item.yl && pos.x() >= item.x && pos.x() < item.xw && pos.type() == PositionType.EXTENDED_V && !itemPositionMap.inverse().containsKey(pos))
				list.add(pos);
		}
		return list;
	}

	private void insertTree(Position entry, Position ancestor) {
		posAncestorMap.put(entry, ancestor);
		posFollowerMap.put(ancestor, entry);
	}

	/**
	 * Recursive function
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
				if(posFollowerMap.containsKey(follower) && !posFollowerMap.get(follower).isEmpty())
					checkTreeAndRemove2(follower);

				// If follower has still followers after deep check, then ignore
				if(posFollowerMap.containsKey(follower) && !posFollowerMap.get(follower).isEmpty())
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
	@Override
	protected void removePosition(Position pos) {
		if(pos.type() != PositionType.ROOT) {
			posFollowerMap.remove(pos);
			posFollowerMap.get(posAncestorMap.get(pos)).remove(pos);
			posAncestorMap.remove(pos);
			activePosList.remove(pos);
			inactivePosList.remove(pos);
			coveredPosList.remove(pos);
			positionItemMap.remove(pos);
			spacePositions.remove(pos);
			uniquePositionKeys.remove(pos.getKey());
			immersiveDepthCache.remove(pos);
		}
	}

	/**
	 * Removes a position if it is not used as possible insert position anymore.
	 */
	private void removeNewPosition(Position pos) {
		if(pos.type() != PositionType.ROOT) {
			if(posFollowerMap.containsKey(pos)) {
				posFollowerMap.remove(pos);
			}

			var posAncestor = posAncestorMap.get(pos);
			if(posAncestor != null) {
				posFollowerMap.get(posAncestor).remove(pos);
				posAncestorMap.remove(pos);
			}

			activePosList.remove(pos);
			inactivePosList.remove(pos);
			coveredPosList.remove(pos);
			positionItemMap.remove(pos);
			spacePositions.remove(pos);
			uniquePositionKeys.remove(pos.getKey());
			immersiveDepthCache.remove(pos);
		}
	}

	/**
	 * Is used for re-projected positions during removeItem
	 */
	private void replacePosition(Position oldPosition, Position newPosition) {
		if(oldPosition.type() != PositionType.ROOT) {
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
			for (Map.Entry<ItemPlacement, Position> e : itemPositionMap.entrySet()) {
				if(e.getValue() == oldPosition)
					itemPositionMap.put(e.getKey(), newPosition);
			}

			activePosList.remove(oldPosition);
			activePosList.add(newPosition);
			inactivePosList.remove(oldPosition);
			inactivePosList.add(newPosition);
			coveredPosList.remove(oldPosition);
			coveredPosList.add(newPosition);

			spacePositions.put(newPosition, spacePositions.get(oldPosition));
			spacePositions.remove(oldPosition);

			uniquePositionKeys.add(newPosition.getKey());
			uniquePositionKeys.remove(oldPosition.getKey());
		}

	}

	private void checkPosition(Position pos) {
		// Removes active unused follower-positions
		checkTreeAndRemove2(pos);

		Position ancestor = posAncestorMap.get(pos);

		if(
			// Wenn die Position keine Nachfolger mehr hat, weil durch CheckTreeAndRemove gel�scht wurde und
				(!posFollowerMap.containsKey(pos) || posFollowerMap.get(pos) == null || posFollowerMap.get(pos).isEmpty())
						// Wenn Vorg�nger (der die Position erzeugt hat) frei ist und
						&& activePosList.contains(ancestor)
						// die Position nicht der Root ist, dann l�sche die Position
						&& pos.type() != PositionType.ROOT) {
			// L�sche pos
			removePosition(pos);

			// Pr�fe den Vorg�nger ebenfalls (rekursiv)
			checkPosition(ancestor);
		}
	}

	private void removeItem(ItemPlacement item) {
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

		weight -= item.getItem().weight;
		item.h = item.getItem().origH;

		// Recompute maxYl if necessary
		if (item.yl >= maxYl) {
			maxYl = 0;
			for (int i = itemList.size() - 1; i >= 0; i--) {
				ItemPlacement it = itemList.get(i);
				if (it != null && it.yl> maxYl) {
					maxYl = it.yl;
				}
			}
		}

		item.containerIndex = -1;

		// Recompute immersive depth cache for positions that were sitting on this item
		recomputeImmersiveDepthCacheForRemovedItem(item);
	}

	/**
	 * When an item is removed, all active/inactive/covered positions that were on its top face
	 * need their cached immersive depth recomputed, because the removed item no longer contributes.
	 */
	private void recomputeImmersiveDepthCacheForRemovedItem(ItemPlacement item) {
		recomputeImmersiveDepthForPositions(item, activePosList);
		recomputeImmersiveDepthForPositions(item, inactivePosList);
		recomputeImmersiveDepthForPositions(item, coveredPosList);
	}

	private void recomputeImmersiveDepthForPositions(ItemPlacement item, Iterable<Position> positions) {
		int itemZh = item.zh;
		for (Position pos : positions) {
			if (pos.z() == itemZh &&
					pos.x() >= item.x && pos.x() < item.xw &&
					pos.y() >= item.y && pos.y() < item.yl) {
				immersiveDepthCache.put(pos, computeMinImmersiveDepthAtPosition(pos.x(), pos.y(), pos.z()));
			}
		}
	}


	/**
	 * Returns a comprehensive string describing the full internal state of this container.
	 * Useful for comparing two containers for exact equality during tests.
	 */
	public String getStateDescription() {
		StringBuilder sb = new StringBuilder();

		sb.append("container:\n");
		sb.append("  index: ").append(index).append('\n');
		sb.append("  width: ").append(width).append('\n');
		sb.append("  length: ").append(length).append('\n');
		sb.append("  height: ").append(height).append('\n');
		sb.append("  maxWeight: ").append(maxWeight).append('\n');
		sb.append("  containerType: ").append(containerType).append('\n');
		sb.append("  weight: ").append(weight).append('\n');
		sb.append("  centerOfGravityForY: ").append(centerOfGravityForY).append('\n');
		sb.append("  maxYl: ").append(maxYl).append('\n');

		// Items
		sb.append("  items:\n");
		for (int i = 0; i < itemList.size(); i++) {
			ItemPlacement ip = itemList.get(i);
			if (ip == null) {
				sb.append("    - null\n");
			} else {
				appendItemPlacement(sb, "    - ", ip);
			}
		}

		// Active positions
		sb.append("  activePositions:\n");
		activePosList.stream()
				.sorted(Comparator.comparingInt(Position::idx))
				.forEach(p -> appendPosition(sb, "    - ", p));

		// Inactive positions
		sb.append("  inactivePositions:\n");
		inactivePosList.stream()
				.sorted(Comparator.comparingInt(Position::idx))
				.forEach(p -> appendPosition(sb, "    - ", p));

		// Covered positions
		sb.append("  coveredPositions:\n");
		coveredPosList.stream()
				.sorted(Comparator.comparingInt(Position::idx))
				.forEach(p -> appendPosition(sb, "    - ", p));

		// Item-Position mapping
		sb.append("  itemPositionMap:\n");
		itemPositionMap.entrySet().stream()
				.sorted(Comparator.comparingInt(e -> e.getKey().index))
				.forEach(e -> {
					sb.append("    - itemIdx: ").append(e.getKey().index).append('\n');
					sb.append("      position: {idx: ").append(e.getValue().idx())
							.append(", x: ").append(e.getValue().x())
							.append(", y: ").append(e.getValue().y())
							.append(", z: ").append(e.getValue().z())
							.append(", type: ").append(e.getValue().type())
							.append("}\n");
				});

		// Position follower map
		sb.append("  posFollowerMap:\n");
		posFollowerMap.keySet().stream()
				.sorted(Comparator.comparingInt(Position::idx))
				.filter(k -> posFollowerMap.get(k) != null && !posFollowerMap.get(k).isEmpty())
				.forEach(k -> {
					sb.append("    - parent: {idx: ").append(k.idx())
							.append(", x: ").append(k.x())
							.append(", y: ").append(k.y())
							.append(", z: ").append(k.z())
							.append("}\n");
					List<Position> followers = posFollowerMap.get(k);
					sb.append("      followers:\n");
					followers.stream()
							.sorted(Comparator.comparingInt(Position::idx))
							.forEach(f -> sb.append("        - {idx: ").append(f.idx())
									.append(", x: ").append(f.x())
									.append(", y: ").append(f.y())
									.append(", z: ").append(f.z())
									.append(", type: ").append(f.type())
									.append("}\n"));
				});

		// Position ancestor map
		sb.append("  posAncestorMap:\n");
		posAncestorMap.entrySet().stream()
				.sorted(Comparator.comparingInt(e -> e.getKey().idx()))
				.forEach(e -> sb.append("    - position: {idx: ").append(e.getKey().idx())
						.append(", x: ").append(e.getKey().x())
						.append(", y: ").append(e.getKey().y())
						.append(", z: ").append(e.getKey().z())
						.append("} -> ancestor: {idx: ").append(e.getValue().idx())
						.append(", x: ").append(e.getValue().x())
						.append(", y: ").append(e.getValue().y())
						.append(", z: ").append(e.getValue().z())
						.append("}\n"));

		// Position-Item map
		sb.append("  positionItemMap:\n");
		positionItemMap.entrySet().stream()
				.sorted(Comparator.comparingInt(e -> e.getKey().idx()))
				.forEach(e -> {
					sb.append("    - position: {idx: ").append(e.getKey().idx())
							.append(", x: ").append(e.getKey().x())
							.append(", y: ").append(e.getKey().y())
							.append(", z: ").append(e.getKey().z())
							.append("}\n");
					sb.append("      itemIdx: ").append(e.getValue().index).append('\n');
				});

		// Unique position keys
		sb.append("  uniquePositionKeys:\n");
		new TreeSet<>(uniquePositionKeys).forEach(k -> sb.append("    - \"").append(k).append("\"\n"));

		// Space positions
		sb.append("  spacePositions:\n");
		spacePositions.entrySet().stream()
				.sorted(Comparator.comparingInt(e -> e.getKey().idx()))
				.forEach(e -> {
					sb.append("    - position: {idx: ").append(e.getKey().idx())
							.append(", x: ").append(e.getKey().x())
							.append(", y: ").append(e.getKey().y())
							.append(", z: ").append(e.getKey().z())
							.append("}\n");
					sb.append("      spaces:\n");
					e.getValue().forEach(s -> sb.append("        - {l: ").append(s.l())
							.append(", w: ").append(s.w())
							.append(", h: ").append(s.h())
							.append("}\n"));
				});

		// X/Y/Z Maps
		appendIntMap(sb, "xMap", xMap);
		appendIntMap(sb, "yMap", yMap);
		appendIntMap(sb, "zMap", zMap);

		// Bearing capacities (only for items currently in container)
		sb.append("  bearingCapacities:\n");
		new TreeMap<>(bearingCapacities).forEach((k, v) -> {
			if (k >= 0 && k < itemList.length() && itemList.get(k) != null) {
				sb.append("    ").append(k).append(": ").append(v).append('\n');
			}
		});

		// Immersive depth cache
		sb.append("  immersiveDepthCache:\n");
		immersiveDepthCache.entrySet().stream()
				.sorted(Comparator.comparingInt(e -> e.getKey().idx()))
				.forEach(e -> sb.append("    - position: {idx: ").append(e.getKey().idx())
						.append(", x: ").append(e.getKey().x())
						.append(", y: ").append(e.getKey().y())
						.append(", z: ").append(e.getKey().z())
						.append("} -> ").append(e.getValue())
						.append('\n'));

		// History
		sb.append("  history:\n");
		for (int i = 0; i < history.size(); i++) {
			appendItemPlacement(sb, "    - ", history.get(i));
		}

		return sb.toString();
	}

	private void appendItemPlacement(StringBuilder sb, String prefix, ItemPlacement ip) {
		if (ip == null) {
			sb.append(prefix).append("null\n");
			return;
		}
		sb.append(prefix).append("idx: ").append(ip.index)
				.append(", pos: [").append(ip.x).append(", ").append(ip.y).append(", ").append(ip.z)
				.append("], end: [").append(ip.xw).append(", ").append(ip.yl).append(", ").append(ip.zh)
				.append("], dim: [").append(ip.w).append(", ").append(ip.l).append(", ").append(ip.h)
				.append("], rot: ").append(ip.isRotated)
				.append(", cIdx: ").append(ip.containerIndex)
				.append(", item: ").append(ip.getItem() != null ? ip.getItem().externalIndex : "null")
				.append('\n');
	}

	private void appendPosition(StringBuilder sb, String prefix, Position p) {
		sb.append(prefix).append("{idx: ").append(p.idx())
				.append(", x: ").append(p.x())
				.append(", y: ").append(p.y())
				.append(", z: ").append(p.z())
				.append(", type: ").append(p.type())
				.append("}\n");
	}

	private void appendIntMap(StringBuilder sb, String name, LPListMap<Integer, Integer> map) {
		sb.append("  ").append(name).append(":\n");
		map.keySet().stream().sorted().forEach(k ->
				sb.append("    ").append(k).append(": ").append(map.get(k)).append('\n'));
	}

	public void checkExistingSpacesForRemovedItem(ItemPlacement item) {
		for (Position pos : activePosList) {
			if(!spacePositions.containsKey(pos))
				continue;

			if(item.xw > pos.x() &&
					item.yl > pos.y() &&
					item.zh > pos.z()) {
				// Removed item is potentially in the range of an existing space
				recreateSpaces(pos);
			}
		}
	}
}
