package an.dpr.manteniket.components;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.Model;

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;

/**
 * Toolbar that displays links used to navigate the pages of the datatable as
 * well as a message about which rows are being displayed and their total number
 * in the data table.
 * 
 * NavigationToolbar with BootstrapPagingNavigator
 * 
 * @author Ricardo Saez
 */
public class BootstrapNavigationToolbar extends NavigationToolbar {

    private static final long serialVersionUID = 5792574399056487335L;

    public BootstrapNavigationToolbar(DataTable<?, ?> table) {
	super(table);
    }

    /**
     * Factory method used to create the paging navigator that will be used by
     * the datatable
     * 
     * @param navigatorId
     *            component id the navigator should be created with
     * @param table
     *            dataview used by datatable
     * @return paging navigator that will be used to navigate the data table
     */
    protected PagingNavigator newPagingNavigator(final String navigatorId, final DataTable<?, ?> table) {
	return new BootstrapPagingNavigator(navigatorId, table);
    }

    /**
     * Factory method used to create the navigator label that will be used by
     * the datatable
     * 
     * @param navigatorId
     *            component id navigator label should be created with
     * @param table
     *            dataview used by datatable
     * @return navigator label that will be used to navigate the data table
     * 
     */
    protected WebComponent newNavigatorLabel(final String navigatorId, final DataTable<?, ?> table) {
	return new Label(navigatorId, Model.of(""));
    }

}
