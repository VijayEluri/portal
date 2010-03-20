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
package org.devproof.portal.core.module.user.page;

import junit.framework.TestCase;

import org.apache.wicket.Page;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.devproof.portal.core.app.PortalSession;
import org.devproof.portal.core.module.common.page.MessagePage;
import org.devproof.portal.core.module.right.entity.RightEntity;
import org.devproof.portal.test.PortalTestUtil;

/**
 * @author Carsten Hufe
 */
public class ForgotPasswordPageTest extends TestCase {
	private WicketTester tester;

	@Override
	public void setUp() throws Exception {
		tester = PortalTestUtil.createWicketTesterWithSpringAndDatabase();
	}

	@Override
	protected void tearDown() throws Exception {
		PortalTestUtil.destroy(tester);
	}

	public void testRenderDefaultPage() {
		tester.startPage(ForgotPasswordPage.class);
		tester.assertRenderedPage(ForgotPasswordPage.class);
	}

	public void testRequestEmail() {
		Page page = tester.startPage(ForgotPasswordPage.class);
		tester.assertRenderedPage(ForgotPasswordPage.class);
		PortalSession.get().getRights().add(new RightEntity("captcha.disabled"));
		FormTester ft = tester.newFormTester("form");
		ft.setValue("emailoruser", "admin");
		tester.executeAjaxEvent("form:requestButton", "onclick");
		tester.assertNoErrorMessage();
		tester.assertRenderedPage(MessagePage.class);
		tester.assertContains(page.getString("email.sent"));
	}
}
