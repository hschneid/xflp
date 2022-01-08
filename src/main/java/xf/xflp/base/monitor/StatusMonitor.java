package xf.xflp.base.monitor;

/** 
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * Interface for Status Monitor Objects, which can be used to
 * transfer planning informations from inside the XFVRP package
 * to the user.
 * @author hschneid
 */
public interface StatusMonitor {

	/**
	 * Method is called, when a message occurs in the planning suite.
	 * 
	 * @param code Type of the message
	 * @param message The message itself
	 */
	public void getMessage(StatusCode code, String message);
	
}
