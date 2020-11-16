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
package org.eclipse.dirigible.database.ds.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The schema model representation.
 */
public class DataStructureSchemaModel extends DataStructureContentModel {
	
	private List<DataStructureTableModel> tables = new ArrayList<DataStructureTableModel>();

	private List<DataStructureViewModel> views = new ArrayList<DataStructureViewModel>();

	/**
	 * Get the tables
	 * @return the tables list
	 */
	public List<DataStructureTableModel> getTables() {
		return tables;
	}

	/**
	 * Get the views
	 * @return the views list
	 */
	public List<DataStructureViewModel> getViews() {
		return views;
	}

}
