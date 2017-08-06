package xf.xflp.base.problem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashBiMap;

import util.collection.LPListMap;

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
public class Container extends AbstractContainer {

	private static final int EXTENDED_H = 3;
	private static final int EXTENDED_V = 4;

	private final Position tmpPosition = new Position(-1, -1);
	private List<Position> inactivePosList = new ArrayList<>();
	private List<Position> coveredPosList = new ArrayList<>();

	/* Beziehungsgraph der Positionen zu den hieraus entstandenen Positionen */
	private LPListMap<Position, Position> posFollowerMap = new LPListMap<>();
	private Map<Position, Position> posAncestorMap = new HashMap<>();

	/* Beziehungsgraph in der Z-Achse: dr�ber und drunter*/
	private final ZItemGraph zGraph = new ZItemGraph();

	/* Verkn�pft das Item mit den daraus entstehenden Positionen */
	private Map<Position, Item> positionItemMap = new HashMap<>();
	/* Verkn�pft ein Item mit seiner gesetzten Position */
	private HashBiMap<Item, Position> itemPositionMap = HashBiMap.create();

	/* Historie der Einf�gungen und L�schungen auf diesem Container f�r Auswertung */
	private List<Item> history = new ArrayList<>();
	
	private int maxPosIdx = 0;
	private final float lifoImportance;

	public boolean WITH_STACK_FENCE = false;

	/**
	 * 
	 * @param width
	 * @param length
	 * @param height
	 * @param maxWeight
	 * @param containerType
	 * @param lifoImportance
	 */
	public Container(int width, int length, int height, float maxWeight, int containerType, float lifoImportance) {
		super(height, width, length, maxWeight, containerType);
		this.lifoImportance = lifoImportance;

		init();
	}
	
	/**
	 * 
	 * @param containerPrototype
	 * @param lifoImportance
	 */
	public Container(Container containerPrototype, float lifoImportance) {
		this(
				containerPrototype.getHeight(),
				containerPrototype.getWidth(),
				containerPrototype.getLength(),
				containerPrototype.getMaxWeight(),
				containerPrototype.getContainerType(),
				lifoImportance
		);
	}

	/**
	 * 
	 * @param item
	 * @param pos
	 * @return
	 */
	public int add(Item item, Position pos) {
		pos = normPosition(item, pos);

		// F�ge Element in Container ein
		addElement(item, pos);

		if(item.z == 0) {
			// Pr�fe, ob das neue Objekt Projektionslinien schneidet
			List<Position> projHPosList = findHorizontalProjectedPositions(item);
			for (Position projPos : projHPosList) {
				Item s = positionItemMap.get(projPos);
				tmpPosition.setXY(s.x, s.yl);
				Item leftItem = findNextLeftElement(tmpPosition);
				// Projeziere diese Position komplett neu von ihrem Objekt aus
				// Ersetze dabei nur die x-y-Koordinaten
				projPos.x = (leftItem != null) ? leftItem.xw : 0; 
			}
			List<Position> projVPosList = findVerticalProjectedPositions(item);
			for (Position projPos : projVPosList) {
				Item s = positionItemMap.get(projPos);
				tmpPosition.setXY(s.xw, s.y);
				Item lowerItem = findNextLowerElement(tmpPosition);
				// Projeziere diese Position komplett neu von ihrem Objekt aus
				// Ersetze dabei nur die x-y-Koordinaten
				projPos.y = (lowerItem != null) ? lowerItem.yl : 0;
			}
		}

		// Setze �berlagerte Positionen auf inaktiv
		List<Position> covPosList = findCoveredPositions(item);
		for (Position covPos : covPosList)
			switchActive2Covered(covPos);

		// Erzeuge neue Einf�ge-Punkte und f�ge sie in Tree ein
		List<Position> newPosList = findInsertPositions(item);
		for (Position newPos : newPosList) {
			activePosList.add(newPos);
			// Die neue Position ist von der �bergebenen Position aus abh�ngig.
			insertTree(newPos, pos);
			// Diese Position wurde von diesem Item erzeugt.
			positionItemMap.put(newPos, item);
		}

		history.add(item);
		
		return item.index;
	}

	/**
	 * 
	 * @param item
	 */
	public void remove(Item item) {
		// Entferne Objekt
		removeElement(item);

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
			pos.x = (leftItem != null) ? leftItem.xw : 0;
		}
		List<Position> projectablePosVList = findProjectableVerticalPositions(item);
		for (Position pos : projectablePosVList) {
			Item lowerItem = findNextLowerElement(pos);
			pos.y = (lowerItem != null) ? lowerItem.yl : 0;
		}

		// L�sche die alte Position, wenn deren Elter noch aktiv ist und selbst seine Nachfolger alle weg sind
		// Das muss rekusriv dann f�r dessen Eltern gepr�ft werden.
		// SONST setze Position auf aktiv statt inaktiv
		checkPosition(position);
		
		history.add(item);
	}

	/**
	 * 
	 * @param newItem
	 * @return
	 */
	public List<Position> getPossibleInsertPositionList(Item newItem) {
		List<Position> posList = new ArrayList<>();

		int itemW = newItem.w, itemL = newItem.l, itemH = newItem.h;

		int orientationType = (newItem.spinable && itemW != itemL) ? 2 : 1;
		int nbrOfItems = itemList.size();
		int nbrOfActivePositions = activePosList.size();

		// Pr�fe maximales Zuladungsgewicht
		if(this.weight + newItem.weight > maxWeight)
			return posList;

		// F�r jeden Orientierungsmodus
		for (int rotation = 0; rotation < orientationType; rotation++) {
			if(rotation > 0) {
				itemW = newItem.l;
				itemL = newItem.w;
			}

			OUTER:
				// F�r jede aktive Einf�geposition
				for (int k = nbrOfActivePositions - 1; k >= 0; k--) {
					Position pos = activePosList.get(k);

					// Pr�fe �berlappung mit Frachtraumw�nden
					if((pos.x + itemW) > width)
						continue;
					if((pos.z + itemH) > height)
						continue;
					if(!ignoreMaxLength && (pos.y + itemL) > length)
						continue;

					// Pr�fe �berlappung der Position mit dem neuen Item
					// gegen�ber vorhandenen Items
					for (int j = nbrOfItems - 1; j >= 0; j--) {
						Item item = itemList.get(j);
						if(item == null) 
							continue;

						if(
								item.x < (pos.x + itemW) && item.xw > pos.x && 
								item.y < (pos.y + itemL) && item.yl > pos.y && 
								item.z < (pos.z + itemH) && item.zh > pos.z
								)
							continue OUTER;

						// Pr�fe die LIFO-Eigenschaften
						if(lifoImportance == 1) {
							// Liegt das Item weiter entfernt von der Ladekante als die Position
							// Liegt das Item im Entladekorridor zur Ladekante
							if(item.yl <= pos.y && item.x < (pos.x + itemW) && item.xw > pos.x) {
								// Wenn der Entladerang des neuen Items gr��er als der
								// Entladeran des Items ist, dann geht diese Position nicht.
								// Das bestehende Item m�sste fr�her entladen werden, als
								// das verstellende neue Item
								if(newItem.unLoadingLoc > item.unLoadingLoc)
									// Das Item muss fr�her entladen werden, als
									// das neue Item, was laut LIFO nicht sein darf.
									continue OUTER;

							}
						}

						// Sonst passt die Position
						// => Ergo mache nix
					}
					// Stapelbegrenzung auf Basiselement (z = 0)
					if(WITH_STACK_FENCE && !checkStackBaseFenceing(pos, newItem))
						continue;

					// Pr�fe Stapelfaktor

					// Pr�fe Stapelgruppe & Auflagefl�che
					// Das darunterliegende Objekt muss die Stapelgruppe des neuen Objektes
					// akzeptieren und (x und xw, sowie y und yl sind nicht in der Luft)
					if(!checkStackingAndSeatOn(pos, itemW, itemL, newItem.stackingGroup))
						continue;

					// Pr�fe Auflast
					if(!checkLoadBearing(pos, newItem, rotation)) 
						continue;

					// Wenn alle Items passen, nur dann
					// darf die Position akzeptiert werden.
					posList.add((rotation == 0) ? pos : new RotatedPosition(pos));
				}
		}

		return posList;
	}

	/**
	 * Checks whether the new item is placed ontop of remaining items. It is tested
	 * that all 4 corners of the new item have a current item directly below that item. 
	 * 
	 * @param pos
	 * @param item
	 * @param itemLList
	 */
	private boolean checkStackingAndSeatOn(Position pos, int w, int l, int stackingGroup) {
		if(pos.z == 0)
			return true;

		List<Integer> zList = zMap.get(pos.getZ());
		if(zList == null || zList.size() == 0)
			return true;

		int itemW = pos.x + w;
		int itemL = pos.y + l;
		boolean corner1, corner2, corner3, corner4;
		corner1 = corner2 = corner3 = corner4 = false;

		for(int i = 0, size = zList.size(); i < size; i++) {	
			Item fi = itemList.get(zList.get(i));
			if(fi.zh == pos.getZ()) {
				// Is the sgItem below the newItem at position pos
				if(fi.xw > pos.getX() && fi.x < itemW && fi.yl > pos.getY() && fi.y < itemL) {
					// AND-operation of two binary representations. If no bit fits
					// then result is zero
					if((fi.allowedStackingGroups & stackingGroup) == 0) {
						return false;
					}
				} else
					continue;

				if(pos.x >= fi.x && pos.x <= fi.xw && pos.y >= fi.y && pos.y <= fi.yl)
					corner1 = true;
				if(itemW > fi.x && itemW <= fi.xw && pos.y >= fi.y && pos.y <= fi.yl)
					corner2 = true;
				if(pos.x >= fi.x && pos.x <= fi.xw && itemL > fi.y && itemL <= fi.yl)
					corner3 = true;
				if(itemW > fi.x && itemW <= fi.xw && itemL > fi.y && itemL <= fi.yl)
					corner4 = true;
			}
		}

		return corner1 && corner2 && corner3 && corner4;
	}

	/**
	 * @param item
	 * @param pos
	 * @return
	 */
	private Position normPosition(Item item, Position pos) {
		// Rotiere falls notwendig
		if(pos instanceof RotatedPosition) {
			RotatedPosition rPos = (RotatedPosition)pos;
			item.rotate();
			pos = rPos.pos;
		}
		return pos;
	}

	/**
	 * Checks whether there is an item which reaches over two stacks. Stacks are
	 * settled by the base item on the floor. no item should overlap the borders
	 * of the base item.
	 * 
	 * @param pos
	 * @param item
	 * @param itemLList
	 * @return
	 */
	private boolean checkStackBaseFenceing(Position pos, Item item) {
		if(pos.z == 0)
			return true;

		item.setPosition(pos);

		Set<Item> foundSet = Tools.getAllFloorItems(item, itemList);

		item.clearPosition();

		if(foundSet.size() > 1)
			return false;

		return true;
	}

	/**
	 * 
	 * @param pos
	 * @param item
	 * @param rotation
	 * @return
	 */
	private boolean checkLoadBearing(Position pos, Item item, int rotation) {
		if(pos.z == 0)
			return true;

		if(rotation == 1) item.rotate();
		item.setPosition(pos);
		itemList.add(item);
		zGraph.add(item, itemList, zMap);

		List<Item> ceilItems = zGraph.getCeilItems(item, itemList);
		LoadBearingCheck lbc = new LoadBearingCheck();
		boolean result = lbc.checkLoadBearing(ceilItems, zGraph);

		zGraph.remove(item);
		item.clearPosition();
		itemList.remove(item.index);
		if(rotation == 1) item.rotate();

		return result;
	}

	/**
	 * 
	 * @param item
	 * @return
	 */
	private List<Position> findCoveredPositions(Item item) {
		List<Position> coveredPositionList = new ArrayList<>();

		// Suche alle Einf�gepositionen ab
		for (Position pos : activePosList) {
			// Liegt eine Position auf der unteren Kante des Objekts, ist sie �berdeckt.
			if(pos.z == item.z && pos.x >= item.x && pos.x < item.xw && pos.y == item.y)
				coveredPositionList.add(pos);
			// Leigt eine Position auf der linken Kante des Objekts, ist sie �berdeckt.
			else if(pos.z == item.z && pos.y >= item.y && pos.y < item.yl && pos.x == item.x)
				coveredPositionList.add(pos);
		}

		return coveredPositionList;
	}
	
	public float getMaxVolume() {
		return height * length * width;
	}

	public float getLoadedVolume() {
		float sum = 0;
		for (Item item : this.itemList)
			if(item != null)
				sum += item.volume;

		return sum;
	}

	public float getLoadedWeight() {
		float sum = 0;
		for (Item item : this.itemList)
			sum += item.weight;

		return sum;
	}

	public float getMaxEmptyArea() {
		List<Position> posList = new ArrayList<>(activePosList);
		Collections.sort(posList, new Comparator<Position>() {
			@Override
			public int compare(Position o1, Position o2) {
				return (o1.x - o2.x);
			}
		});

		// Obere Absch�tzung der Fl�che
		float area = 0;
		for (int i = 1; i < posList.size(); i++) {
			Position prevPos = posList.get(i - 1);
			Position pos = posList.get(i);
			area += (pos.x - prevPos.x) * (this.length - prevPos.y);
		}

		// F�ge die Rest-Fl�che bis zur Wand hinzu
		// Annahme dass es immer am unteren Ende einen geben muss.
		Position lastPos = posList.get(posList.size() - 1);
		area += (this.width - lastPos.x) * (this.length - 0);

		return area;
	}

	/*******************************************************************/

	/**
	 * @param pos
	 */
	private void switchInactive2Active(Position pos) {
		inactivePosList.remove(pos);
		activePosList.add(pos);
	}

	/**
	 * @param pos
	 */
	private void switchCovered2Active(Position pos) {
		coveredPosList.remove(pos);
		activePosList.add(pos);
	}

	/**
	 * @param pos
	 */
	private void switchActive2Inactive(Position pos) {
		activePosList.remove(pos);
		inactivePosList.add(pos);
	}

	/**
	 * @param pos
	 */
	private void switchActive2Covered(Position pos) {
		activePosList.remove(pos);
		coveredPosList.add(pos);
	}

	/**
	 * 
	 * @param item
	 * @return
	 */
	private List<Position> findProjectableHorizontalPositions(Item item) {
		List<Position> list = new ArrayList<>();
		for (Position pos : activePosList) {
			if(pos.type == EXTENDED_H) 
				if(pos.x == item.xw && pos.y >= item.y && pos.y < item.yl)
					list.add(pos);
		}
		return list;
	}

	/**
	 * 
	 * @param item
	 * @return
	 */
	private List<Position> findProjectableVerticalPositions(Item item) {
		List<Position> list = new ArrayList<>();
		for (Position pos : activePosList) {
			if(pos.type == EXTENDED_V)
				if(pos.y == item.yl && pos.x >= item.x && pos.x < item.xw)
					list.add(pos);
		}
		return list;
	}

	/**
	 * Sucht inaktive Positionen, die durch das Entfernen des Items
	 * wieder frei geschaufelt werden k�nnen. 
	 * 
	 * @param stack
	 * @return
	 */
	private List<Position> findUncoveringPositions(Item item) {
		List<Position> list = new ArrayList<>();
		for (Position pos : coveredPosList) {
			if(pos.z == item.z && pos.x == item.x && pos.y >= item.y && pos.y < item.yl)
				list.add(pos);
			else if(pos.z == item.z && pos.x == item.xw && pos.y >= item.y && pos.y < item.yl && pos.type  == EXTENDED_H && !itemPositionMap.inverse().containsKey(pos))
				list.add(pos);
			else if(pos.z == item.z && pos.y == item.y && pos.x >= item.x && pos.x < item.xw)
				list.add(pos);
			else if(pos.z == item.z && pos.y == item.yl && pos.x >= item.x && pos.x < item.xw && pos.type == EXTENDED_V && !itemPositionMap.inverse().containsKey(pos))
				list.add(pos);
		}
		return list;
	}

	/**
	 * 
	 * @param item
	 * @return
	 */
	private List<Position> findHorizontalProjectedPositions(Item item) {
		List<Position> posList = new ArrayList<>();
		for (Position pos : activePosList) {
			if(pos.type == EXTENDED_H) {
				Item s = positionItemMap.get(pos);
				if(s == null)
					System.out.println();

				if(pos.z == item.z && pos.x <= item.xw && s.x > item.x && pos.y >= item.y && pos.y <= item.yl)
					posList.add(pos);
			}
		}
		return posList;
	}

	/**
	 * 
	 * @param item
	 * @return
	 */
	private List<Position> findVerticalProjectedPositions(Item item) {
		List<Position> posList = new ArrayList<>();
		for (Position pos : activePosList) {
			if(pos.type == EXTENDED_V) {
				Item s = positionItemMap.get(pos);

				if(pos.z == item.z && pos.y <= item.yl && s.y > item.y && pos.x >= item.x && pos.x <= item.xw)
					posList.add(pos);
			}
		}
		return posList;
	}

	/**
	 * 
	 * @param entry
	 * @param ancestor
	 */
	private void insertTree(Position entry, Position ancestor) {
		posAncestorMap.put(entry, ancestor);
		posFollowerMap.put(ancestor, entry);
	}

	private void checkTreeAndRemove2(Position pos) {
		List<Position> mapList = posFollowerMap.get(pos);
		if(mapList != null) {
			// List copy, because iteration on this list to change its contents
			List<Position> followerList = new ArrayList<>(mapList);
			for (Position follower: followerList) {
				if(inactivePosList.contains(follower))
					return;

				if(posFollowerMap.containsKey(follower) && posFollowerMap.get(follower).size() > 0)
					checkTreeAndRemove2(follower);

				if((posFollowerMap.containsKey(follower) && posFollowerMap.get(follower).size() > 0))
					return;
			}

			// Only if all positions are active and have no more follower
			// all followers can be deleted
			for (Position follower: followerList)
				removePosition(follower);
		}
	}

	/**
	 * 
	 * @param pos
	 */
	private void removePosition(Position pos) {
		if(pos.type != ROOT) {
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
	 * 
	 * @param pos
	 * @return
	 */
	private void checkPosition(Position pos) {
		// L�sche aktive unbesetzte Nachfolger-Positionen
		checkTreeAndRemove2(pos);

		Position ancestor = posAncestorMap.get(pos);

		if(
				// Wenn die Position keine Nachfolger mehr hat, weil durch CheckTreeAndRemove gel�scht und
				(!posFollowerMap.containsKey(pos) || posFollowerMap.get(pos).size() == 0) 
				// Wenn Vorg�nger (der die Position erzeugt hat) frei ist und
				&& activePosList.contains(ancestor) 
				// die Position nicht der Root ist, dann l�sche die Position
				&& pos.type != ROOT) {
			// L�sche pos
			removePosition(pos);

			// Pr�fe den Vorg�nger ebenfalls (rekursiv)
			checkPosition(ancestor);
		}
	}

	/**
	 * 
	 * @param item
	 * @param pos
	 */
	private void addElement(Item item, Position pos) {
		item.setPosition(pos);
		itemList.add(item);
		item.containerIndex = this.index;

		itemPositionMap.put(item, pos);

		xMap.put(item.x, item.index);
		xMap.put(item.xw, item.index);
		yMap.put(item.y, item.index);
		yMap.put(item.yl, item.index);
		zMap.put(item.z, item.index);
		zMap.put(item.zh, item.index);

		weight += item.weight;

		// Insert into Z-Graph
		zGraph.add(item, itemList, zMap);

		// Die nun besetzte Position wird inaktiv
		switchActive2Inactive(pos);
	}

	/**
	 * 
	 * @param item
	 */
	private void removeElement(Item item) {
		Integer index = new Integer(item.index);

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

		item.containerIndex = -1;
	}

	/**
	 * 
	 * @param item
	 * @return
	 */
	private List<Position> findInsertPositions(Item item) {
		List<Position> posList = new ArrayList<>();

		// 2 triviale Positionen
		Position verticalPosition = null, horizontalPosition = null;
		if(item.yl < this.length) {
			verticalPosition = createPosition(item.x, item.yl, item.z, BASIC, false);
			posList.add(verticalPosition);
		}
		if(item.xw < this.width) {
			horizontalPosition = createPosition(item.xw, item.y, item.z, BASIC, false);
			posList.add(horizontalPosition);
		}

		if(item.z == 0) {
			// 2 projezierte Positionen
			if(item.x > 0 && verticalPosition != null) {
				Item leftElement = findNextLeftElement(verticalPosition);
				int leftPos = (leftElement != null) ? leftElement.xw : 0;
				posList.add(createPosition(leftPos, item.yl, item.z, EXTENDED_H, false));
			}

			if(item.y > 0 && horizontalPosition != null) {
				Item lowerElement = findNextLowerElement(horizontalPosition);
				int lowerPos = (lowerElement != null) ? lowerElement.yl : 0;
				posList.add(createPosition(item.xw, lowerPos, item.z, EXTENDED_V, false));
			}
		}

		// 1 H�hen-Position (�bergabe des Boden-Items)
		if(item.z + item.h < this.height)
			posList.add(createPosition(item.x, item.y, item.z + item.h, BASIC, false));

		return posList;
	}

	/**
	 * 
	 * @param pos
	 * @return
	 */
	private Item findNextLeftElement(Position pos) {
		Item leftItem = null;

		for (Item item : itemList) {
			if(item == null || item.y > pos.y || item.yl < pos.y || item.x > pos.x || item.xw > pos.x || pos.y == item.yl)
				continue;

			if(leftItem == null || item.xw > leftItem.xw)
				leftItem = item;
		}

		return leftItem;
	}

	/**
	 * 
	 * @param pos
	 * @return
	 */
	private Item findNextLowerElement(Position pos) {
		Item lowerItem = null;

		for (Item item : itemList) {
			if(item == null || item.x > pos.x || item.xw < pos.x || item.y > pos.y || item.yl > pos.y || pos.x == item.xw)
				continue;

			if(lowerItem == null || item.yl > lowerItem.yl)
				lowerItem = item;
		}

		return lowerItem;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param type
	 * @param isProjected
	 * @return
	 */
	private Position createPosition(int x, int y, int z, int type, boolean isProjected) {
		return new Position(maxPosIdx++, x, y, z, type, isProjected);
	}

	/**
	 * 
	 */
	private void init() {
		// idx, x, y, z, type, isProjected
		Position start = new Position(0, 0, 0, 0, ROOT, false);
		activePosList.add(start);

		// Die root-Position befindet sich nicht im 3D-Raum. Alle
		// realen Positionen erben von dieser virtuellen.
		insertTree(start, rootPos);
	}

	/**
	 * 
	 */
	@Override
	public void clear() {
		super.clear();
		inactivePosList.clear();
		coveredPosList.clear();
		posFollowerMap.clear();
		posAncestorMap.clear();
		positionItemMap.clear();
		itemPositionMap.clear();
		maxPosIdx = 0;
		zGraph.clear();

		init();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Item> getHistory() {
		return history;
	}

	/**
	 * Only for Unit Tests
	 * 
	 * @return
	 */
	public List<Position> getActivePosList() {
		return activePosList;
	}
	/**
	 * Only for Unit Tests
	 * 
	 * @return
	 */
	public List<Position> getInActivePosList() {
		return inactivePosList;
	}

	/**
	 * 
	 * @return
	 */
	public List<Position> getCoveredPosList() {
		return coveredPosList;
	}

	/**
	 * 
	 * @return
	 */
	public float getLifoImportance() {
		return lifoImportance;
	}
	
	/**
	 * 
	 * @return
	 */
	public Item[] getItemList() {
		return itemList.toArray(new Item[0]);
	}
}
