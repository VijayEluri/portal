/*
 * Copyright 2009 Carsten Hufe devproof.org
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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.resource.loader.ClassStringResourceLoader;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devproof.portal.core.module.common.CommonConstants;
import org.devproof.portal.core.module.common.registry.PageAdminPageRegistry;

/**
 * Contains link for upload center and the "add" links
 * 
 * @author Carsten Hufe
 */
public class PageAdminBoxPanel extends Panel {

	private static final long serialVersionUID = 1L;

	@SpringBean(name = "pageAdminPageRegistry")
	private PageAdminPageRegistry adminPageRegistry;
	private final RepeatingView repeating;

	public PageAdminBoxPanel(final String id) {
		super(id);
		this.repeating = new RepeatingView("repeatingNav1");
		this.add(this.repeating);
		List<Class<? extends Page>> registeredAdminPages = this.adminPageRegistry.getRegisteredPageAdminPages();
		RepeatingView repeating2 = new RepeatingView("repeatingNav2");
		this.add(repeating2);
		for (Class<? extends Page> pageClass : registeredAdminPages) {
			WebMarkupContainer item = new WebMarkupContainer(repeating2.newChildId());
			repeating2.add(item);
			String label = new ClassStringResourceLoader(pageClass).loadStringResource(null, CommonConstants.GLOBAL_ADMIN_BOX_LINK_LABEL);
			if (StringUtils.isEmpty(label)) {
				label = new ClassStringResourceLoader(pageClass).loadStringResource(null, CommonConstants.CONTENT_TITLE_LABEL);
			}
			BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>("adminLink", pageClass);
			link.add(new Label("adminLinkLabel", label));
			item.add(link);
		}
	}

	public void addLink(final Component link) {
		WebMarkupContainer container = new WebMarkupContainer(this.repeating.newChildId());
		this.repeating.add(container);
		container.add(link);
	}
}