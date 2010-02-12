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
package org.devproof.portal.core.module.common.panel;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.devproof.portal.core.module.common.CommonConstants;

/**
 * Shows the edit and delete button
 * 
 * @author Carsten Hufe
 * 
 * @param <T>
 *            Entity type
 */
public abstract class AuthorPanel<T> extends Panel {
	private static final long serialVersionUID = 1L;

	private T entity;
	private Class<? extends Page> redirectPageClazz = null;
	private PageParameters redirectParams = null;
	private BubblePanel bubblePanel;

	public AuthorPanel(String id, T entity) {
		super(id);
		this.entity = entity;
		add(createBubblePanel());
		add(createEditLink());
		add(createDeleteLink());
	}

	private MarkupContainer createDeleteLink() {
		AjaxLink<T> link = newDeleteLink();
		link.add(createDeleteImage());
		return link;
	}

	private AjaxLink<T> newDeleteLink() {
		return new AjaxLink<T>("deleteLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				bubblePanel.setContent(createConfirmDeletePanel());
				bubblePanel.showModal(target);
			}

			private ConfirmDeletePanel<T> createConfirmDeletePanel() {
				return new ConfirmDeletePanel<T>(bubblePanel.getContentId(), entity, bubblePanel) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onDelete(AjaxRequestTarget target, Form<?> form) {
						bubblePanel.hide(target);
						AuthorPanel.this.onDelete(target);
						if (redirectPageClazz != null) {
							setResponsePage(AuthorPanel.this.redirectPageClazz, AuthorPanel.this.redirectParams);
						}
					}

				};
			}
		};
	}

	private Image createDeleteImage() {
		return new Image("deleteImage", CommonConstants.REF_DELETE_IMG);
	}

	private MarkupContainer createEditLink() {
		AjaxLink<T> link = newEditLink();
		link.add(createEditImage());
		return link;
	}

	private AjaxLink<T> newEditLink() {
		return new AjaxLink<T>("editLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				AuthorPanel.this.onEdit(target);
			}
		};
	}

	private Image createEditImage() {
		return new Image("editImage", CommonConstants.REF_EDIT_IMG);
	}

	private BubblePanel createBubblePanel() {
		bubblePanel = new BubblePanel("bubblePanel");
		return bubblePanel;
	}

	public T getEntity() {
		return this.entity;
	}

	public AuthorPanel<T> setRedirectPage(Class<? extends Page> redirectPageClazz, PageParameters redirectParams) {
		this.redirectPageClazz = redirectPageClazz;
		this.redirectParams = redirectParams;
		return this;
	}

	public abstract void onEdit(AjaxRequestTarget target);

	public abstract void onDelete(AjaxRequestTarget target);
}
