/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-FileCopyrightText: 2022 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.dirigible.engine.js.api;

import java.util.Map;

import org.eclipse.dirigible.commons.api.scripting.ScriptingException;

/**
 * The Javascript Engine Processor interface.
 */
public interface IJavascriptEngineProcessor {

	/**
	 * Execute service.
	 *
	 * @param module            the module
	 * @return the object
	 * @throws ScriptingException             the scripting exception
	 */
	public Object executeService(String module, Map<Object, Object> parameters) throws ScriptingException;

}
