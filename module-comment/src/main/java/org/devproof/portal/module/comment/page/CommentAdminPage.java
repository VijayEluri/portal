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
package org.devproof.portal.module.comment.page;

import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devproof.portal.core.module.common.page.TemplatePage;
import org.devproof.portal.core.module.configuration.service.ConfigurationService;
import org.devproof.portal.module.comment.CommentConstants;
import org.devproof.portal.module.comment.config.CommentConfiguration;
import org.devproof.portal.module.comment.panel.CommentPanel;
import org.devproof.portal.module.comment.panel.CommentSearchBoxPanel;
import org.devproof.portal.module.comment.query.CommentQuery;

/**
 * @author Carsten Hufe
 */
public class CommentAdminPage extends TemplatePage {

	private static final long serialVersionUID = 1L;
	public static final String PARAM_ID = "id";

	@SpringBean(name = "configurationService")
	private ConfigurationService configurationService;
	private CommentPanel commentPanel;
	private CommentQuery query;

	public CommentAdminPage(PageParameters params) {
		super(params);
		setCommentQuery();
		add(createCommentPanel());
		addFilterBox(createCommentSearchBoxPanel());
	}

	private CommentSearchBoxPanel createCommentSearchBoxPanel() {
		return new CommentSearchBoxPanel("box", query) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				target.addComponent(commentPanel);
			}
		};
	}

	private Component createCommentPanel() {
		commentPanel = newCommentPanel();
		commentPanel.setOutputMarkupId(true);
		return commentPanel;
	}

	private CommentPanel newCommentPanel() {
		return new CommentPanel("comments", new CommentAdminConfiguration()) {
			private static final long serialVersionUID = 1L;

			@Override
			public int getNumberOfPages() {
				return configurationService.findAsInteger(CommentConstants.CONF_COMMENT_NUMBER_PER_PAGE_ADMIN);
			}

			@Override
			public boolean hideInput() {
				return true;
			}

			@Override
			protected CommentQuery createCommentQuery() {
				return query;
			}
		};
	}

	private void setCommentQuery() {
		query = new CommentQuery();
	}

	private static class CommentAdminConfiguration implements CommentConfiguration {
		private static final long serialVersionUID = 1L;

		@Override
		public String getModuleContentId() {
			return null;
		}

		@Override
		public String getModuleName() {
			return null;
		}

		@Override
		public boolean isAllowedToWrite() {
			return false;
		}

		@Override
		public boolean isAllowedToView() {
			return true;
		}
	}
}
