/*
 * Copyright (c) 2010-2020 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-FileCopyrightText: 2010-2020 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.dirigible.oauth;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.dirigible.api.v3.http.access.IAccessManager;
import org.eclipse.dirigible.oauth.utils.JwtUtils;

public class OAuthAccessManager implements IAccessManager {

	@Override
	public boolean isInRole(HttpServletRequest request, String role) {
		return JwtUtils.isInRole(request, role);
	}
}
