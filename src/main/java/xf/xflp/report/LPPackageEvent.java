package xf.xflp.report;

/** 
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 **/
public class LPPackageEvent {

	private String id = "";
	private int x = -1;
	private int y = -1;
	private int z = -1;
	private int w = -1;
	private int l = -1;
	private int h = -1;
	private int stackingGrp = -1;
	private float weight = -1;
	private float weightLimit = -1;
	private boolean isInvalid = false;
	
	private LoadType type = LoadType.LOAD;
	
	private float usedVolumeInContainer = 0;
	private float usedWeightInContainer = 0;
	private int nbrStacksInContainer = 0;
	
	/**
	 * 
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @return x
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * 
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * 
	 * @return y
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * 
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getZ() {
		return z;
	}
	
	/**
	 * 
	 * @param z
	 */
	public void setZ(int z) {
		this.z = z;
	}

	/**
	 * @param isInvalid the isInvalid to set
	 */
	public void setInvalid(boolean isInvalid) {
		this.isInvalid = isInvalid;
	}

	/**
	 * @return the isInvalid
	 */
	public boolean isInvalid() {
		return isInvalid;
	}

	/**
	 * @return the usedVolumeInContainer
	 */
	public float getUsedVolumeInContainer() {
		return usedVolumeInContainer;
	}

	/**
	 * @param usedVolumeInContainer the usedVolumeInContainer to set
	 */
	public void setUsedVolumeInContainer(float usedVolumeInContainer) {
		this.usedVolumeInContainer = usedVolumeInContainer;
	}

	/**
	 * @return the nbrStacksInContainer
	 */
	public int getNbrStacksInContainer() {
		return nbrStacksInContainer;
	}

	/**
	 * @param nbrStacksInContainer the nbrStacksInContainer to set
	 */
	public void setNbrStacksInContainer(int nbrStacksInContainer) {
		this.nbrStacksInContainer = nbrStacksInContainer;
	}

	/**
	 * @return the type
	 */
	public final LoadType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public final void setType(LoadType type) {
		this.type = type;
	}

	/**
	 * @return the usedWeightInContainer
	 */
	public final float getUsedWeightInContainer() {
		return usedWeightInContainer;
	}

	/**
	 * @param usedWeightInContainer the usedWeightInContainer to set
	 */
	public final void setUsedWeightInContainer(float usedWeightInContainer) {
		this.usedWeightInContainer = usedWeightInContainer;
	}

	/**
	 * @return the w
	 */
	public final int getWidth() {
		return w;
	}

	/**
	 * @param w the w to set
	 */
	public final void setWidth(int w) {
		this.w = w;
	}

	/**
	 * @return the l
	 */
	public final int getLength() {
		return l;
	}

	/**
	 * @param l the l to set
	 */
	public final void setLength(int l) {
		this.l = l;
	}

	/**
	 * @return the h
	 */
	public final int getHeight() {
		return h;
	}

	/**
	 * @param h the h to set
	 */
	public final void setHeight(int h) {
		this.h = h;
	}

	/**
	 * @return the stackingGrp
	 */
	public final int getStackingGrp() {
		return stackingGrp;
	}

	/**
	 * @param stackingGrp the stackingGrp to set
	 */
	public final void setStackingGrp(int stackingGrp) {
		this.stackingGrp = stackingGrp;
	}

	/**
	 * @return the weight
	 */
	public final float getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public final void setWeight(float weight) {
		this.weight = weight;
	}

	/**
	 * @return the weightLimit
	 */
	public final float getWeightLimit() {
		return weightLimit;
	}

	/**
	 * @param weightLimit the weightLimit to set
	 */
	public final void setWeightLimit(float weightLimit) {
		this.weightLimit = weightLimit;
	}
	
	
}
