/*
 * Copyright 2009-2011 Carsten Hufe devproof.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.devproof.portal.core.module.feed.registry;

import org.apache.wicket.Page;
import org.devproof.portal.core.config.PageConfiguration;
import org.devproof.portal.core.config.Registry;
import org.devproof.portal.core.module.common.locator.PageLocator;
import org.devproof.portal.core.module.feed.locator.FeedProviderLocator;
import org.devproof.portal.core.module.feed.provider.FeedProvider;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Carsten Hufe
 */
@Registry("feedProviderRegistry")
public class FeedProviderRegistryImpl implements FeedProviderRegistry {
    private PageLocator pageLocator;
    private FeedProviderLocator feedProviderLocator;
    private final Map<String, FeedProvider> feedProviders = new HashMap<String, FeedProvider>();
    private final Map<Class<? extends Page>, String> feedPaths = new HashMap<Class<? extends Page>, String>();

    @Override
    public Map<String, FeedProvider> getAllFeedProvider() {
        return Collections.unmodifiableMap(feedProviders);
    }

    @Override
    public FeedProvider getFeedProviderByPath(String path) {
        String newPath = getPathWithoutLeadingSlash(path);
        return feedProviders.get(newPath);
    }

    @Override
    public void registerFeedProvider(String path, FeedProvider feedProvider) {
        String newPath = getPathWithoutLeadingSlash(path);
        if (feedProviders.containsKey(newPath)) {
            throw new IllegalArgumentException(newPath + " does already exist in the FeedProviderRegistry!");
        }
        feedProviders.put(newPath, feedProvider);
        registerFeedPath(path);
    }

    @Override
    public void removeFeedProvider(String path) {
        String newPath = getPathWithoutLeadingSlash(path);
        feedProviders.remove(newPath);
    }

    private String getPathWithoutLeadingSlash(String path) {
        String newPath = path;
        if (newPath.startsWith("/")) {
            newPath = path.substring(1);
        }
        return newPath;
    }

    @Override
    public String getPathByPageClass(Class<? extends Page> pageClass) {
        return feedPaths.get(pageClass);
    }

    @Override
    public boolean hasFeedSupport(Class<? extends Page> pageClass) {
        return feedPaths.containsKey(pageClass);
    }

    @PostConstruct
    public void afterPropertiesSet() {
        Collection<PageConfiguration> pages = pageLocator.getPageConfigurations();
        Collection<FeedProvider> feeds = feedProviderLocator.getFeedProviders();
        for (FeedProvider feed : feeds) {
            for (PageConfiguration page : pages) {
                if (feed.getSupportedFeedPages().contains(page.getPageClass())) {
                    registerFeedProvider(page.getMountPath(), feed);
                }
            }
        }
    }

    private void registerFeedPath(String mountPath) {
        Collection<PageConfiguration> pages = pageLocator.getPageConfigurations();
        for (PageConfiguration page : pages) {
            if (page.getMountPath().equals(mountPath)) {
                String newPath = getPathWithoutLeadingSlash(mountPath);
                if (feedPaths.containsKey(page.getPageClass())) {
                    throw new IllegalArgumentException(newPath + " does already exist in the FeedProviderRegistry!");
                }
                feedPaths.put(page.getPageClass(), newPath);
                break;
            }
        }
    }

    @Autowired
    public void setPageLocator(PageLocator pageLocator) {
        this.pageLocator = pageLocator;
    }

    @Autowired
    public void setFeedProviderLocator(FeedProviderLocator feedProviderLocator) {
        this.feedProviderLocator = feedProviderLocator;
    }
}
