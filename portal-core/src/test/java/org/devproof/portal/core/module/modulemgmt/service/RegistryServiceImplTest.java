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
package org.devproof.portal.core.module.modulemgmt.service;

import junit.framework.TestCase;

import org.devproof.portal.core.module.common.registry.GlobalAdminPageRegistry;
import org.devproof.portal.core.module.common.registry.MainNavigationRegistry;
import org.devproof.portal.core.module.common.registry.PageAdminPageRegistry;
import org.devproof.portal.core.module.common.service.RegistryServiceImpl;
import org.devproof.portal.core.module.modulemgmt.entity.ModuleLinkEntity.LinkType;
import org.easymock.EasyMock;

/**
 * @author Carsten Hufe
 */
public class RegistryServiceImplTest extends TestCase {
	private RegistryServiceImpl impl;
	private MainNavigationRegistry mainNavigationRegistryMock;
	private GlobalAdminPageRegistry globalAdminPageRegistryMock;
	private PageAdminPageRegistry pageAdminPageRegistryMock;

	@Override
	public void setUp() throws Exception {
		this.mainNavigationRegistryMock = EasyMock.createStrictMock(MainNavigationRegistry.class);
		this.globalAdminPageRegistryMock = EasyMock.createStrictMock(GlobalAdminPageRegistry.class);
		this.pageAdminPageRegistryMock = EasyMock.createStrictMock(PageAdminPageRegistry.class);
		this.impl = new RegistryServiceImpl();
		this.impl.setMainNavigationRegistry(this.mainNavigationRegistryMock);
		this.impl.setGlobalAdminPageRegistry(this.globalAdminPageRegistryMock);
		this.impl.setPageAdminPageRegistry(this.pageAdminPageRegistryMock);
	}

	public void testRebuildRegistries1() {
		this.globalAdminPageRegistryMock.buildNavigation();
		EasyMock.replay(this.globalAdminPageRegistryMock);
		this.impl.rebuildRegistries(LinkType.GLOBAL_ADMINISTRATION);
		EasyMock.verify(this.globalAdminPageRegistryMock);
	}

	public void testRebuildRegistries2() {
		this.mainNavigationRegistryMock.buildNavigation();
		EasyMock.replay(this.mainNavigationRegistryMock);
		this.impl.rebuildRegistries(LinkType.TOP_NAVIGATION);
		EasyMock.verify(this.mainNavigationRegistryMock);
	}

	public void testRebuildRegistries3() {
		this.pageAdminPageRegistryMock.buildNavigation();
		EasyMock.replay(this.pageAdminPageRegistryMock);
		this.impl.rebuildRegistries(LinkType.PAGE_ADMINISTRATION);
		EasyMock.verify(this.pageAdminPageRegistryMock);
	}
}