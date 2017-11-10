/*
 * Copyright (c) 2017 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * SAP - initial API and implementation
 */

package org.eclipse.dirigible.database.ds.model;

// TODO: Auto-generated Javadoc
/**
 * The Class DataStructureTableConstraintForeignKeyModel.
 */
public class DataStructureTableConstraintForeignKeyModel extends DataStructureTableConstraintModel {

	/** The referenced table. */
	private String referencedTable;
	
	/** The referenced columns. */
	private String[] referencedColumns;

	/**
	 * Gets the referenced table.
	 *
	 * @return the referenced table
	 */
	public String getReferencedTable() {
		return referencedTable;
	}

	/**
	 * Sets the referenced table.
	 *
	 * @param referencedTable the new referenced table
	 */
	public void setReferencedTable(String referencedTable) {
		this.referencedTable = referencedTable;
	}

	/**
	 * Gets the referenced columns.
	 *
	 * @return the referenced columns
	 */
	public String[] getReferencedColumns() {
		return referencedColumns;
	}

	/**
	 * Sets the referenced columns.
	 *
	 * @param referencedColumns the new referenced columns
	 */
	public void setReferencedColumns(String[] referencedColumns) {
		this.referencedColumns = referencedColumns;
	}

}
