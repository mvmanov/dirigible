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
package org.eclipse.dirigible.oauth.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.eclipse.dirigible.oauth.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter(urlPatterns = {
		"/services/v3/core/*",
        "/websockets/v3/core/*",
        "/services/v4/core/*",
        "/websockets/v4/core/*",
        "/services/v3/ops/*",
        "/websockets/v3/ops/*",
        "/services/v4/ops/*",
        "/websockets/v4/ops/*",
        "/services/v3/transport/*",
        "/services/v4/transport/*"
,}, filterName = "Operator Role Security Filter", description = "Check all URIs for the Operator role permission")
public class OperatorRoleFilter extends AbstractOAuthFilter {

	private static final Logger logger = LoggerFactory.getLogger(OperatorRoleFilter.class);

	private static final String FORBIDDEN_MESSAGE = "The logged in user does not have any of the required roles for the requested URI";

	private static final String ROLE = "Operator";

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (!JwtUtils.isInRole(request, ROLE)) {
			forbidden(request, response, FORBIDDEN_MESSAGE);
			return;
		}

		chain.doFilter(request, response);
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}
}
