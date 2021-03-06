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
package org.devproof.portal.module.otherpage.factory;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.util.string.UrlUtils;
import org.devproof.portal.core.module.common.factory.CommonMarkupContainerFactory;
import org.devproof.portal.core.module.common.registry.SharedRegistry;
import org.devproof.portal.module.otherpage.page.OtherPageViewPage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Factory to create the terms of use link
 *
 * @author Carsten Hufe
 */
@Service
public class TermsOfUseLinkFactory implements CommonMarkupContainerFactory, InitializingBean {
    private SharedRegistry sharedRegistry;

    @Override
    public MarkupContainer newInstance(String id, Object... obj) {
        Request request = RequestCycle.get().getRequest();
        return new ExternalLink(id,  UrlUtils.rewriteToContextRelative("terms_of_use", request));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        sharedRegistry.registerResource("termsOfUseLink", this);
    }

    @Autowired
    public void setSharedRegistry(SharedRegistry sharedRegistry) {
        this.sharedRegistry = sharedRegistry;
    }
}
