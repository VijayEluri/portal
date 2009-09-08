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
package org.devproof.portal.module.bookmark.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.devproof.portal.core.module.common.CommonConstants;
import org.devproof.portal.core.module.common.dataprovider.SortableQueryDataProvider;
import org.devproof.portal.core.module.common.page.TemplatePage;
import org.devproof.portal.core.module.configuration.service.ConfigurationService;
import org.devproof.portal.core.module.feed.provider.FeedProvider;
import org.devproof.portal.core.module.role.entity.RoleEntity;
import org.devproof.portal.module.bookmark.BookmarkConstants;
import org.devproof.portal.module.bookmark.entity.BookmarkEntity;
import org.devproof.portal.module.bookmark.page.BookmarkPage;
import org.devproof.portal.module.bookmark.query.BookmarkQuery;
import org.springframework.beans.factory.annotation.Required;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;

/**
 * @author Carsten Hufe
 */
public class BookmarkFeedProviderImpl implements FeedProvider {
	private SortableQueryDataProvider<BookmarkEntity> bookmarkDataProvider;
	private ConfigurationService configurationService;

	@Override
	public SyndFeed getFeed(final RequestCycle rc, final RoleEntity role) {
		SyndFeed feed = generateFeed(rc);
		setRoleForDataProviderQuery(role);
		Iterator<? extends BookmarkEntity> iterator = getBookmarkEntries();
		List<SyndEntry> entries = generateFeedEntries(rc, iterator);
		feed.setEntries(entries);
		return feed;
	}

	protected Iterator<? extends BookmarkEntity> getBookmarkEntries() {
		Integer maxNumber = configurationService.findAsInteger(BookmarkConstants.CONF_BOOKMARK_ENTRIES_IN_FEED);
		Iterator<? extends BookmarkEntity> iterator = bookmarkDataProvider.iterator(0, maxNumber);
		return iterator;
	}

	protected SyndFeed generateFeed(final RequestCycle rc) {
		SyndFeed feed = new SyndFeedImpl();
		feed.setTitle(getFeedName());
		feed.setLink(getUrl(rc));
		String pageTitle = configurationService.findAsString(CommonConstants.CONF_PAGE_TITLE);
		feed.setAuthor(pageTitle);
		feed.setCopyright(pageTitle);
		// must be set for RSS2 feed
		feed.setDescription(getFeedName());
		return feed;
	}

	protected String getUrl(final RequestCycle rc) {
		return rc.urlFor(BookmarkPage.class, new PageParameters()).toString();
	}

	protected List<SyndEntry> generateFeedEntries(final RequestCycle rc,
			final Iterator<? extends BookmarkEntity> iterator) {
		List<SyndEntry> entries = new ArrayList<SyndEntry>();
		while (iterator.hasNext()) {
			BookmarkEntity bookmarkEntity = iterator.next();
			SyndEntry entry = new SyndEntryImpl();
			entry.setTitle(bookmarkEntity.getTitle());
			entry.setLink(getUrl(rc, bookmarkEntity));
			entry.setPublishedDate(bookmarkEntity.getModifiedAt());
			entry.setAuthor(bookmarkEntity.getModifiedBy());
			String content = bookmarkEntity.getDescription();
			content = content != null ? content : "";
			content = content.replaceAll("<(.|\n)*?>", "");
			SyndContent description = new SyndContentImpl();
			description.setType("text/plain");
			description.setValue(StringUtils.abbreviate(content, 200));
			entry.setDescription(description);
			entries.add(entry);
		}
		return entries;
	}

	protected String getUrl(final RequestCycle rc, final BookmarkEntity BookmarkEntity) {
		return rc.urlFor(BookmarkPage.class, new PageParameters("id=" + BookmarkEntity.getId())).toString();
	}

	protected void setRoleForDataProviderQuery(final RoleEntity role) {
		BookmarkQuery query = new BookmarkQuery();
		query.setRole(role);
		bookmarkDataProvider.setQueryObject(query);
	}

	@Override
	public List<Class<? extends TemplatePage>> getSupportedFeedPages() {
		List<Class<? extends TemplatePage>> pages = new ArrayList<Class<? extends TemplatePage>>();
		pages.add(BookmarkPage.class);
		return pages;
	}

	@Override
	public String getFeedName() {
		String pageTitle = configurationService.findAsString(CommonConstants.CONF_PAGE_TITLE);
		String feedName = configurationService.findAsString(BookmarkConstants.CONF_BOOKMARK_FEED_TITLE);
		return pageTitle + " - " + feedName;
	}

	@Required
	public void setBookmarkDataProvider(final SortableQueryDataProvider<BookmarkEntity> bookmarkDataProvider) {
		this.bookmarkDataProvider = bookmarkDataProvider;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}
}