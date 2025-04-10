package util;
import xf.xflp.XFLP;

/**
 * Copyright (c) 2012-2025 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * @author hschneid
 *
 */
public class Application {

	public static void main(String[] args) {
		try {
			new XFLP().executeLoadPlanning();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
