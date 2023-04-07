package xf.xflp.report;

/** 
 * Copyright (c) 2012-2023 Holger Schneider
 * All rights reserved.
 * <p>
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 **/
public record LPPackageEvent(
		 String id,
		 int x,
		 int y,
		 int z,
		 int w,
		 int l,
		 int h,
		 long stackingGrp,
		 float weight,
		 float weightLimit,
		 boolean isInvalid,
		 LoadType type,
		 float usedVolumeInContainer,
		 float usedWeightInContainer,
		 int nbrStacksInContainer
) {
}
