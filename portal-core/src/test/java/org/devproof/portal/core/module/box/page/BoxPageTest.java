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
package org.devproof.portal.core.module.box.page;

import junit.framework.TestCase;

import org.apache.wicket.util.tester.WicketTester;
import org.devproof.portal.test.PortalTestUtil;

/**
 * @author Carsten Hufe
 */
public class BoxPageTest extends TestCase {
	private WicketTester tester;

	@Override
	public void setUp() throws Exception {
		this.tester = PortalTestUtil.createWicketTesterWithSpringAndDatabase();
		PortalTestUtil.loginDefaultAdminUser(this.tester);
	}

	@Override
	protected void tearDown() throws Exception {
		PortalTestUtil.destroy(this.tester);
	}

	public void testRenderDefaultPage() {
		this.tester.startPage(BoxPage.class);
		this.tester.assertRenderedPage(BoxPage.class);
	}
}