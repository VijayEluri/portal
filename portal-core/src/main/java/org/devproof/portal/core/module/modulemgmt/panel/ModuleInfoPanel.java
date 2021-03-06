/*
 * Copyright 2009-2011 Carsten Hufe devproof.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.devproof.portal.core.module.modulemgmt.panel;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.PropertyModel;
import org.devproof.portal.core.config.BoxConfiguration;
import org.devproof.portal.core.config.ModuleConfiguration;
import org.devproof.portal.core.config.PageConfiguration;
import org.devproof.portal.core.config.Secured;
import org.devproof.portal.core.module.modulemgmt.ModuleMgmtConstants;

import javax.persistence.Table;

/**
 * @author Carsten Hufe
 */
@Secured(ModuleMgmtConstants.ADMIN_RIGHT)
public class ModuleInfoPanel extends Panel {

    private static final long serialVersionUID = 1L;

    private ModuleConfiguration module;
    // FIXME use models !!!
    public ModuleInfoPanel(String id, ModuleConfiguration module) {
        super(id);
        this.module = module;
        add(basePackageLabel());
        add(createRepeatingPages());
        add(createRepeatingBoxes());
        add(createRepeatingEntities());
    }

    private Label basePackageLabel() {
        return new Label("basePackage", new PropertyModel<String>(module, "basePackage"));
    }

    private RepeatingView createRepeatingEntities() {
        RepeatingView view = new RepeatingView("repeatingEntities");
        if (module.getEntities().isEmpty()) {
            view.add(createEmptyEntitiesRow(view.newChildId()));
        } else {
            for (Class<?> entity : module.getEntities()) {
                view.add(createEntityRow(view.newChildId(), entity));
            }
        }
        return view;
    }

    private WebMarkupContainer createEntityRow(String id, Class<?> entity) {
        WebMarkupContainer row = new WebMarkupContainer(id);
        row.add(createEntitySimpleClassNameLabel(entity));
        row.add(createTableNameLabel(entity));
        return row;
    }

    private Label createTableNameLabel(Class<?> entity) {
        String table = getTableName(entity);
        return new Label("table", table);
    }

    private Label createEntitySimpleClassNameLabel(Class<?> entity) {
        return new Label("entity", entity.getSimpleName());
    }

    private String getTableName(Class<?> entity) {
        String table = entity.getSimpleName();
        Table tableAnno = entity.getAnnotation(Table.class);
        if (tableAnno != null && StringUtils.isNotEmpty(tableAnno.name())) {
            table = tableAnno.name();
        }
        return table;
    }

    private WebMarkupContainer createEmptyEntitiesRow(String id) {
        WebMarkupContainer row = new WebMarkupContainer(id);
        row.add(createNoEntitiesLabel());
        row.add(createEmptyTableNameLabel());
        return row;
    }

    private Label createEmptyTableNameLabel() {
        return new Label("table", "");
    }

    private Label createNoEntitiesLabel() {
        return new Label("entity", getString("noentities"));
    }

    private RepeatingView createRepeatingBoxes() {
        RepeatingView view = new RepeatingView("repeatingBoxes");
        if (module.getBoxes().isEmpty()) {
            view.add(createNoBoxesRow(view.newChildId()));
        } else {
            for (BoxConfiguration box : module.getBoxes()) {
                view.add(createBoxRow(view.newChildId(), box));
            }
        }
        return view;
    }

    private WebMarkupContainer createBoxRow(String id, BoxConfiguration box) {
        WebMarkupContainer row = new WebMarkupContainer(id);
        row.add(createBoxSimpleClassNameLabel(box));
        row.add(createBoxNameLabel(box));
        return row;
    }

    private Label createBoxNameLabel(BoxConfiguration box) {
        return new Label("name", box.getName());
    }

    private Label createBoxSimpleClassNameLabel(BoxConfiguration box) {
        return new Label("box", box.getBoxClass().getSimpleName());
    }

    private WebMarkupContainer createNoBoxesRow(String id) {
        WebMarkupContainer row = new WebMarkupContainer(id);
        row.add(createNoBoxesLabel());
        row.add(createEmptyBoxNameLabel());
        return row;
    }

    private Label createEmptyBoxNameLabel() {
        return new Label("name", "");
    }

    private Label createNoBoxesLabel() {
        return new Label("box", getString("noboxes"));
    }

    private RepeatingView createRepeatingPages() {
        RepeatingView view = new RepeatingView("repeatingPages");
        if (module.getPages().isEmpty()) {
            view.add(createNoPagesRow(view.newChildId()));
        } else {
            for (PageConfiguration page : module.getPages()) {
                view.add(createPageRow(view.newChildId(), page));
            }
        }
        return view;
    }

    private WebMarkupContainer createPageRow(String id, PageConfiguration page) {
        WebMarkupContainer row = new WebMarkupContainer(id);
        row.add(createPageNameLabel(page));
        row.add(createMounthPathLabel(page));
        return row;
    }

    private Label createPageNameLabel(PageConfiguration page) {
        return new Label("page", page.getPageClass().getSimpleName());
    }

    private Label createMounthPathLabel(PageConfiguration page) {
        return new Label("mountPath", page.getMountPath());
    }

    private WebMarkupContainer createNoPagesRow(String id) {
        WebMarkupContainer row = new WebMarkupContainer(id);
        row.add(createNoPagesLabel());
        row.add(createEmptyMounthPathLabel());
        return row;
    }

    private Label createEmptyMounthPathLabel() {
        return new Label("mountPath", "");
    }

    private Label createNoPagesLabel() {
        return new Label("page", getString("nopages"));
    }
}
