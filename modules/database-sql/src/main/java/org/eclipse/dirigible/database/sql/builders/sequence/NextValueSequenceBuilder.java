/*
 * Copyright (c) 2017 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * SAP - initial API and implementation
 */

package org.eclipse.dirigible.database.sql.builders.sequence;

import org.eclipse.dirigible.database.sql.ISqlDialect;
import org.eclipse.dirigible.database.sql.builders.AbstractQuerySqlBuilder;

// TODO: Auto-generated Javadoc
/**
 * The Class NextValueSequenceBuilder.
 */
public class NextValueSequenceBuilder extends AbstractQuerySqlBuilder {
	
	/** The sequence. */
	private String sequence = null;
	
	/**
	 * Instantiates a new next value sequence builder.
	 *
	 * @param dialect the dialect
	 * @param sequence the sequence
	 */
	public NextValueSequenceBuilder(ISqlDialect dialect, String sequence) {
		super(dialect);
		this.sequence = sequence;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.dirigible.database.sql.ISqlBuilder#generate()
	 */
	@Override
	public String generate() {
		StringBuilder sql = new StringBuilder();
		
		// SELECT
		generateSelect(sql);
		
		// NEXTVAL
		generateNextValue(sql);
		
		return sql.toString();
	}

	/**
	 * Generate select.
	 *
	 * @param sql the sql
	 */
	protected void generateSelect(StringBuilder sql) {
		sql.append(KEYWORD_SELECT);
	}

	/**
	 * Generate next value.
	 *
	 * @param sql the sql
	 */
	protected void generateNextValue(StringBuilder sql) {
			sql.append(SPACE)
				.append(KEYWORD_NEXT_VALUE_FOR)
				.append(SPACE)
				.append(sequence);
	}
	
	/**
	 * Gets the sequence.
	 *
	 * @return the sequence
	 */
	public String getSequence() {
		return sequence;
	}

}
