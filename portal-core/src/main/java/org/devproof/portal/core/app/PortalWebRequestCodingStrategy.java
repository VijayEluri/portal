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
package org.devproof.portal.core.app;

import org.apache.wicket.IRequestTarget;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.protocol.http.request.WebRequestCodingStrategy;
import org.apache.wicket.request.target.coding.IRequestTargetUrlCodingStrategy;
import org.apache.wicket.request.target.component.BookmarkablePageRequestTarget;
import org.devproof.portal.core.module.mount.service.MountService;

/**
 * Required to mount custom url on all paths
 *
 * @author Carsten Hufe
 */
public class PortalWebRequestCodingStrategy extends WebRequestCodingStrategy {
    private CustomMountUrlCodingStrategy customMountUrlCodingStrategy;
    private MountService mountService;

    public PortalWebRequestCodingStrategy(MountService mountService) {
        this.mountService = mountService;
        customMountUrlCodingStrategy = new CustomMountUrlCodingStrategy(mountService);
    }

    @Override
    public IRequestTargetUrlCodingStrategy urlCodingStrategyForPath(String path) {
        if(customMountUrlCodingStrategy.matches(path, true)) {
            return customMountUrlCodingStrategy;
        }
        return super.urlCodingStrategyForPath(path);
    }

    @Override
    protected IRequestTargetUrlCodingStrategy getMountEncoder(IRequestTarget requestTarget) {
        if(requestTarget instanceof BookmarkablePageRequestTarget) {
            BookmarkablePageRequestTarget bp = (BookmarkablePageRequestTarget) requestTarget;
            Class<? extends Page> pageClass = bp.getPageClass();
            PageParameters pageParameters = bp.getPageParameters();
            if(mountService.canHandlePageClass(pageClass, pageParameters)) {
                return customMountUrlCodingStrategy;
            }
        }
        return super.getMountEncoder(requestTarget);
    }
}
