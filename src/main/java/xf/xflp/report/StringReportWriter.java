package xf.xflp.report;

/**
 * Copyright (c) 2012-2025 Holger Schneider
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
				sb.append(e.id()+" ");
				sb.append(e.type().toString()+" ");
				sb.append(e.weight()+" ");
				sb.append(e.weightLimit()+" ");
				sb.append(e.stackingGrp()+" | ");
				sb.append(e.w()+" ");
				sb.append(e.l()+" ");
				sb.append(e.h()+" : ");
				sb.append(e.x()+" ");
				sb.append(e.y()+" ");
				sb.append(e.z()+"\n");
			}
		}

		return sb.toString();
	}
}
