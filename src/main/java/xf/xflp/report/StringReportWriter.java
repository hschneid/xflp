package xf.xflp.report;

/**
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 **/
public class StringReportWriter {

	public String write(LPReport r) {
		StringBuilder sb = new StringBuilder();
		sb
				.append(">>> ")
				.append(r.getSummary().getNbrOfUsedVehicles())
				.append(" (")
				.append(r.getSummary().getUtilization() * 100f)
				.append("%)\n");

		for (ContainerReport cr : r.getContainerReports()) {
			sb.append("--- "+cr.getContainerTypeName()+"("+(cr.getSummary().getMaxUsedVolume()/cr.getSummary().getMaxVolume())*100f+"%)\n");
			for (LPPackageEvent e : cr.getPackageEvents()) {
				sb.append(e.getId()+" ");
				sb.append(e.getType().toString()+" ");
				sb.append(e.getWeight()+" ");
				sb.append(e.getWeightLimit()+" ");
				sb.append(e.getStackingGrp()+" | ");
				sb.append(e.getWidth()+" ");
				sb.append(e.getLength()+" ");
				sb.append(e.getHeight()+" : ");
				sb.append(e.getX()+" ");
				sb.append(e.getY()+" ");
				sb.append(e.getZ()+"\n");
			}
		}

		return sb.toString();
	}
}
