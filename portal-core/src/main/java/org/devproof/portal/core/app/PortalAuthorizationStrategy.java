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

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.extensions.markup.html.tree.table.TreeTable;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.devproof.portal.core.config.Secured;
import org.devproof.portal.core.module.right.RightConstants;
import org.devproof.portal.core.module.right.entity.Right;
import org.devproof.portal.core.module.right.service.RightService;
import org.devproof.portal.core.module.user.panel.LoginBoxPanel;
import org.devproof.portal.core.module.user.panel.UserBoxPanel;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * The whole role and rights system of the portal
 *
 * @author Carsten Hufe
 */
public class PortalAuthorizationStrategy implements IAuthorizationStrategy {
    @Override
    public boolean isActionAuthorized(Component component, Action action) {
        return PortalSession.get().hasRight(component, action);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public boolean isInstantiationAuthorized(Class componentClass) {
        return PortalSession.get().hasRight(componentClass);
    }
}
