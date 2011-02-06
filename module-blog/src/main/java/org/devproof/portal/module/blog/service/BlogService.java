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
package org.devproof.portal.module.blog.service;

import org.devproof.portal.core.module.common.service.CrudService;
import org.devproof.portal.module.blog.entity.Blog;
import org.devproof.portal.module.blog.entity.BlogHistorized;

/**
 * @author Carsten Hufe
 */
public interface BlogService extends CrudService<Blog, Integer> {
    /**
     * Returns a new instance of Blog
     *
     * @return new instance of {@link org.devproof.portal.module.blog.entity.Blog}
     */
    Blog newBlogEntity();

    /**
     * Restores a blog entry from history
     *
     * @param historized historied blog entry
     */
    void restoreFromHistory(BlogHistorized historized);
}
