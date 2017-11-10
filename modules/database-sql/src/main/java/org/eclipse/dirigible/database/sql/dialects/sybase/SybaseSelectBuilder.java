/*
 * Copyright (c) 2017 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * SAP - initial API and implementation
 */

package org.eclipse.dirigible.database.sql.dialects.sybase;

import org.eclipse.dirigible.database.sql.ISqlDialect;
import org.eclipse.dirigible.database.sql.builders.records.SelectBuilder;

// TODO: Auto-generated Javadoc
/**
 * The Class SybaseSelectBuilder.
 */
public class SybaseSelectBuilder extends SelectBuilder {

	/**
	 * Instantiates a new sybase select builder.
	 *
	 * @param dialect the dialect
	 */
	public SybaseSelectBuilder(ISqlDialect dialect) {
		super(dialect);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.dirigible.database.sql.builders.AbstractQuerySqlBuilder#generateLimitAndOffset(java.lang.StringBuilder, int, int)
	 */
	@Override
	protected void generateLimitAndOffset(StringBuilder sql, int limit, int offset) {
		if (limit > -1) {
			sql.append(SPACE).append(KEYWORD_ROWS).append(SPACE).append(KEYWORD_LIMIT).append(SPACE).append(limit);
		}
		if (offset > -1) {
			sql.append(SPACE).append(KEYWORD_OFFSET).append(SPACE).append(offset);
		}
	}

}
