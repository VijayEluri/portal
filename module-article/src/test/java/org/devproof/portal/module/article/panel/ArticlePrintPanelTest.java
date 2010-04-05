/*
 * Copyright 2009-2010 Carsten Hufe devproof.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.devproof.portal.module.article.panel;

import junit.framework.TestCase;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.TestPanelSource;
import org.apache.wicket.util.tester.WicketTester;
import org.devproof.portal.module.article.entity.ArticleEntity;
import org.devproof.portal.test.PortalTestUtil;

import java.util.Date;

/**
 * @author Carsten Hufe
 */
public class ArticlePrintPanelTest extends TestCase {
	private WicketTester tester;

	@Override
	public void setUp() throws Exception {
		tester = PortalTestUtil.createWicketTesterWithSpringAndDatabase("create_tables_hsql_article.sql",
				"insert_article.sql");
	}

	@Override
	protected void tearDown() throws Exception {
		PortalTestUtil.destroy(tester);
	}

	public void testRenderDefaultPanel() {
		tester.startPanel(createArticlePrintPanel());
		tester.assertComponent("panel", ArticlePrintPanel.class);
	}

	private TestPanelSource createArticlePrintPanel() {
		return new TestPanelSource() {
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getTestPanel(String panelId) {
				ArticleEntity article = new ArticleEntity();
				article.setTitle("foo");
				article.setTeaser("bar");
				article.setCreatedAt(new Date());
				article.setModifiedAt(new Date());
				article.setCreatedBy("foo");
				article.setModifiedBy("foo");
				return new ArticlePrintPanel(panelId, Model.of(article));
			}
		};
	}
}
