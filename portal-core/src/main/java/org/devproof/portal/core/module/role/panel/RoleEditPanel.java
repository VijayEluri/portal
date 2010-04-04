/*
 * Copyright 2009-2010 Carsten Hufe devproof.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.devproof.portal.core.module.role.panel;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.CollectionModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devproof.portal.core.module.right.entity.RightEntity;
import org.devproof.portal.core.module.right.service.RightService;
import org.devproof.portal.core.module.role.entity.RoleEntity;
import org.devproof.portal.core.module.role.service.RoleService;

/**
 * @author Carsten Hufe
 */
public abstract class RoleEditPanel extends Panel {

	private static final long serialVersionUID = 1L;
	@SpringBean(name = "roleService")
	private RoleService roleService;
	@SpringBean(name = "rightService")
	private RightService rightService;
	private FeedbackPanel feedback;
	private IModel<RoleEntity> roleModel;

	public RoleEditPanel(String id, IModel<RoleEntity> roleModel) {
		super(id, roleModel);
		this.roleModel = roleModel;
		add(createFeedbackPanel());
		add(createRoleEditForm());
	}

	private Form<RoleEntity> createRoleEditForm() {
		Form<RoleEntity> form = new Form<RoleEntity>("form", new CompoundPropertyModel<RoleEntity>(roleModel));
		form.add(createRoleDescriptionField());
		form.add(createActiveCheckBox());
		form.add(createRightPalette());
		form.add(createSaveButton());
		form.add(createCancelButton());
		form.setOutputMarkupId(true);
		return form;
	}

	private Palette<RightEntity> createRightPalette() {
		IChoiceRenderer<RightEntity> renderer = new ChoiceRenderer<RightEntity>("description", "right");
        IModel<Collection<RightEntity>> allRights = createAllRightsModel();
        IModel<List<RightEntity>> roleRights = new PropertyModel<List<RightEntity>>(roleModel, "rights");
		return newRightsPalette(renderer, allRights, roleRights);
	}

    private IModel<Collection<RightEntity>> createAllRightsModel() {
        return new LoadableDetachableModel<Collection<RightEntity>>() {
            private static final long serialVersionUID = 8162794261959630483L;
            @Override
            protected Collection<RightEntity> load() {
                return rightService.findAllOrderByDescription();
            }
        };
    }

    private Palette<RightEntity> newRightsPalette(IChoiceRenderer<RightEntity> renderer,
			IModel<Collection<RightEntity>> allRights, IModel<List<RightEntity>> roleRights) {
        return new Palette<RightEntity>("rights", roleRights, allRights, renderer, 10, false) {
            private static final long serialVersionUID = 1L;

            @Override
            protected ResourceReference getCSS() {
                return null;
            }

            @Override
            protected Component newAvailableHeader(String componentId) {
                return new Label(componentId, getString("palette.available"));
            }

            @Override
            protected Component newSelectedHeader(String componentId) {
                return new Label(componentId, getString("palette.selected"));
            }
        };
	}

	private AjaxLink<Void> createCancelButton() {
		return new AjaxLink<Void>("cancelButton") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				onCancel(target);
			}
		};
	}

	private AjaxButton createSaveButton() {
		return new AjaxButton("saveButton") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				roleService.save((RoleEntity) form.getModelObject());
				RoleEditPanel.this.onSave(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// repaint the feedback panel so errors are shown
				target.addComponent(feedback);
			}
		};
	}

	private CheckBox createActiveCheckBox() {
		return new CheckBox("active");
	}

	private FormComponent<String> createRoleDescriptionField() {
		return new RequiredTextField<String>("description");
	}

	private FeedbackPanel createFeedbackPanel() {
		feedback = new FeedbackPanel("feedbackPanel");
		feedback.setOutputMarkupId(true);
		return feedback;
	}

	public abstract void onSave(AjaxRequestTarget target);

	public abstract void onCancel(AjaxRequestTarget target);
}
