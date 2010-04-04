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
package org.devproof.portal.module.article.service;

import junit.framework.TestCase;
import org.devproof.portal.core.module.role.entity.RoleEntity;
import org.devproof.portal.core.module.tag.service.TagService;
import org.devproof.portal.module.article.dao.ArticleDao;
import org.devproof.portal.module.article.dao.ArticlePageDao;
import org.devproof.portal.module.article.entity.ArticleEntity;
import org.devproof.portal.module.article.entity.ArticlePageEntity;
import org.devproof.portal.module.article.entity.ArticleTagEntity;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;

/**
 * @author Carsten Hufe
 */
public class ArticleServiceImplTest extends TestCase {
	private ArticleServiceImpl impl;
	private ArticleDao mock;
	private ArticlePageDao mockPage;
	private TagService<ArticleTagEntity> mockTag;

	@Override
	public void setUp() throws Exception {
		mock = createStrictMock(ArticleDao.class);
		mockPage = createStrictMock(ArticlePageDao.class);
		@SuppressWarnings("unchecked")
		TagService<ArticleTagEntity> tagService = createStrictMock(TagService.class);
		mockTag = tagService;
		impl = new ArticleServiceImpl();
		impl.setArticleDao(mock);
		impl.setArticlePageDao(mockPage);
		impl.setArticleTagService(mockTag);
	}

	public void testSave() {
		ArticleEntity e = createArticleEntity();
		e.setId(1);
		expect(mock.save(e)).andReturn(e);
		mockTag.deleteUnusedTags();
		replay(mock);
		replay(mockTag);
		impl.save(e);
		verify(mock);
		verify(mockTag);
	}

	public void testDelete() {
		ArticleEntity e = createArticleEntity();
		e.setId(1);
		mock.delete(e);
		mockTag.deleteUnusedTags();
		replay(mock);
		replay(mockTag);
		impl.delete(e);
		verify(mock);
		verify(mockTag);
	}

	public void testFindById() {
		ArticleEntity e = createArticleEntity();
		e.setId(1);
		expect(mock.findById(1)).andReturn(e);
		replay(mock);
		assertEquals(impl.findById(1), e);
		verify(mock);
	}

	public void testNewArticleEntity() {
		assertNotNull(impl.newArticleEntity());
	}

	public void testNewArticlePageEntity() {
		ArticleEntity a = createArticleEntity();
		a.setId(1);
		ArticlePageEntity ap = impl.newArticlePageEntity(a, 1);
		assertNotNull(ap);
		assertEquals(a, ap.getArticle());
	}

	public void testGetPageCount() {
		expect(mockPage.getPageCount("contentId")).andReturn(4l);
		replay(mockPage);
		assertEquals(impl.getPageCount("contentId"), 4l);
		verify(mockPage);
	}

	public void testExistsContentId() {
		expect(mock.existsContentId("contentId")).andReturn(1l);
		replay(mock);
		assertTrue(impl.existsContentId("contentId"));
		verify(mock);
	}

	public void testFindAllArticlesForRoleOrderedByDateDesc() {
		RoleEntity role = new RoleEntity();
		List<ArticleEntity> list = new ArrayList<ArticleEntity>();
		list.add(createArticleEntity());
		list.add(createArticleEntity());
		expect(mock.findAllArticlesForRoleOrderedByDateDesc(role, 0, 2)).andReturn(list);
		replay(mock);
		assertEquals(impl.findAllArticlesForRoleOrderedByDateDesc(role, 0, 2), list);
		verify(mock);
	}

	private ArticleEntity createArticleEntity() {
		return new ArticleEntity();
	}
}
