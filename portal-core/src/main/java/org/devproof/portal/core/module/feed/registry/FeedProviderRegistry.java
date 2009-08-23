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
package org.devproof.portal.core.module.feed.registry;

import java.util.Map;

import org.devproof.portal.core.module.feed.provider.FeedProvider;

/**
 * Registry for {@link FeedProvider}
 * 
 * @author Carsten Hufe
 */
public interface FeedProviderRegistry {
	/**
	 * Registers a {@link FeedProvider}
	 * 
	 * @param path
	 *            e.g. blog f�r a blog entry, throws an
	 *            {@link IllegalArgumentException} if you register a path twice
	 * @param feedProvider
	 *            feed provider
	 */
	public void registerFeedProvider(String path, FeedProvider feedProvider);

	/**
	 * Removes a {@link FeedProvider}
	 */
	public void removeFeedProvider(String path);

	/**
	 * Returns the {@link FeedProvider} by the path
	 */
	public FeedProvider getFeedProviderByPath(String path);

	/**
	 * Returns all {@link FeedProvider} in a {@link Map}
	 */
	public Map<String, FeedProvider> getAllFeedProvider();
}