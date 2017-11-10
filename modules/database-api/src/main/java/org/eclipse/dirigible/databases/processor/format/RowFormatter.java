/*
 * Copyright (c) 2017 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * SAP - initial API and implementation
 */

package org.eclipse.dirigible.databases.processor.format;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Interface RowFormatter.
 *
 * @param <T> the generic type
 */
public interface RowFormatter<T> {
	
	/**
	 * Write.
	 *
	 * @param columnDescriptors the column descriptors
	 * @param resultSetMetaData the result set meta data
	 * @param resultSet the result set
	 * @return the t
	 * @throws SQLException the SQL exception
	 */
	T write(List<ColumnDescriptor> columnDescriptors, ResultSetMetaData resultSetMetaData, ResultSet resultSet) throws SQLException;
}
