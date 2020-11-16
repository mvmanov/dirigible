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
package org.eclipse.dirigible.database.sql.test.postgres;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.dirigible.database.sql.SqlFactory;
import org.eclipse.dirigible.database.sql.dialects.postgres.PostgresSqlDialect;
import org.junit.Test;

// TODO: Auto-generated Javadoc
/**
 * The Class SequenceTest.
 */
public class SequenceTest {
	
	/**
	 * Creates the sequence.
	 */
	@Test
	public void createSequence() {
		String sql = SqlFactory.getNative(new PostgresSqlDialect())
			.create()
			.sequence("CUSTOMERS_SEQUENCE")
			.build();
		
		assertNotNull(sql);
		assertEquals("CREATE SEQUENCE CUSTOMERS_SEQUENCE", sql);
	}
	
	/**
	 * Drop sequnce.
	 */
	@Test
	public void dropSequnce() {
		String sql = SqlFactory.getNative(new PostgresSqlDialect())
			.drop()
			.sequence("CUSTOMERS_SEQUENCE")
			.build();
		
		assertNotNull(sql);
		assertEquals("DROP SEQUENCE CUSTOMERS_SEQUENCE", sql);
	}
	
	/**
	 * Nextval sequnce.
	 */
	@Test
	public void nextvalSequnce() {
		String sql = SqlFactory.getNative(new PostgresSqlDialect())
			.nextval("CUSTOMERS_SEQUENCE")
			.build();
		
		assertNotNull(sql);
		assertEquals("SELECT nextval('CUSTOMERS_SEQUENCE')", sql);
	}

}
