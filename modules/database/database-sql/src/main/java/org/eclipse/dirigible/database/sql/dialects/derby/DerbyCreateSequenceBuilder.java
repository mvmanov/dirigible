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
package org.eclipse.dirigible.database.sql.dialects.derby;

import org.eclipse.dirigible.database.sql.ISqlDialect;
import org.eclipse.dirigible.database.sql.builders.sequence.CreateSequenceBuilder;

/**
 * The Derby Create Sequence Builder.
 */
public class DerbyCreateSequenceBuilder extends CreateSequenceBuilder {

	/**
	 * Instantiates a new derby create sequence builder.
	 *
	 * @param dialect
	 *            the dialect
	 * @param sequence
	 *            the sequence
	 */
	public DerbyCreateSequenceBuilder(ISqlDialect dialect, String sequence) {
		super(dialect, sequence);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.dirigible.database.sql.builders.sequence.CreateSequenceBuilder#generateStart(java.lang.StringBuilder)
	 */
	@Override
	protected void generateStart(StringBuilder sql) {
		sql.append(SPACE).append(KEYWORD_AS).append(SPACE).append("BIGINT").append(SPACE).append(KEYWORD_START).append(SPACE).append(KEYWORD_WITH)
				.append(SPACE).append(0);
	}

}
