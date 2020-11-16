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
package org.eclipse.dirigible.database.sql.dialects.hana;

import java.sql.Connection;
import java.sql.SQLException;

import org.eclipse.dirigible.database.sql.builders.AlterBranchingBuilder;
import org.eclipse.dirigible.database.sql.builders.DropBranchingBuilder;
import org.eclipse.dirigible.database.sql.builders.records.DeleteBuilder;
import org.eclipse.dirigible.database.sql.builders.records.InsertBuilder;
import org.eclipse.dirigible.database.sql.builders.records.SelectBuilder;
import org.eclipse.dirigible.database.sql.builders.records.UpdateBuilder;
import org.eclipse.dirigible.database.sql.dialects.DefaultSqlDialect;

/**
 * The HANA SQL Dialect.
 */
public class HanaSqlDialect extends
		DefaultSqlDialect<SelectBuilder, InsertBuilder, UpdateBuilder, DeleteBuilder, HanaCreateBranchingBuilder, AlterBranchingBuilder, DropBranchingBuilder, HanaNextValueSequenceBuilder, HanaLastValueIdentityBuilder> {

	private static final String IDENTITY_ARGUMENT = "GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1)";

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.database.sql.dialects.DefaultSqlDialect#nextval(java.lang.String)
	 */
	@Override
	public HanaNextValueSequenceBuilder nextval(String sequence) {
		return new HanaNextValueSequenceBuilder(this, sequence);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.database.sql.dialects.DefaultSqlDialect#create()
	 */
	@Override
	public HanaCreateBranchingBuilder create() {
		return new HanaCreateBranchingBuilder(this);
	}

	@Override
	public String getIdentityArgument() {
		return IDENTITY_ARGUMENT;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.database.sql.dialects.DefaultSqlDialect#nextval(java.lang.String)
	 */
	@Override
	public HanaLastValueIdentityBuilder lastval(String... args) {
		return new HanaLastValueIdentityBuilder(this, args);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.database.sql.ISqlDialect#exists(java.sql.Connection, java.lang.String)
	 */
	@Override
	public boolean exists(Connection connection, String table) throws SQLException {
		try {
			count(connection, table);
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.database.sql.ISqlDialect#isSchemaFilterSupported()
	 */
	@Override
	public boolean isSchemaFilterSupported() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.database.sql.ISqlDialect#getSchemaFilterScript()
	 */
	@Override
	public String getSchemaFilterScript() {
		return "SELECT * FROM \"SYS\".\"SCHEMAS\"";
	}

}
