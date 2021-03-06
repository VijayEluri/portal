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
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.protocol.http.PageExpiredException;
import org.apache.wicket.protocol.http.WebRequestCycleProcessor;
import org.apache.wicket.protocol.http.request.WebRequestCodingStrategy;
import org.apache.wicket.request.IRequestCodingStrategy;
import org.apache.wicket.request.RequestParameters;
import org.apache.wicket.request.target.basic.StringRequestTarget;
import org.apache.wicket.request.target.coding.IRequestTargetUrlCodingStrategy;
import org.apache.wicket.request.target.component.BookmarkablePageRequestTarget;
import org.apache.wicket.request.target.component.PageRequestTarget;
import org.devproof.portal.core.module.common.CommonConstants;
import org.devproof.portal.core.module.common.page.NoStartPage;
import org.devproof.portal.core.module.common.page.UnsupportedOperationPage;
import org.devproof.portal.core.module.configuration.service.ConfigurationService;
import org.devproof.portal.core.module.email.bean.EmailPlaceholderBean;
import org.devproof.portal.core.module.email.service.EmailService;
import org.devproof.portal.core.module.mount.service.MountService;
import org.devproof.portal.core.module.user.entity.User;
import org.devproof.portal.core.module.user.service.UserService;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Rollback of transaction when a runtime exception occurs Inform the admin
 * about the runtime exception
 *
 * @author Carsten Hufe
 */
public class PortalRequestCycleProcessor extends WebRequestCycleProcessor {
    private ConfigurationService configurationService;
    private UserService userService;
    private EmailService emailService;
    private MountService mountService;

    public PortalRequestCycleProcessor(ApplicationContext context) {
        configurationService = (ConfigurationService) context.getBean("configurationService");
        userService = (UserService) context.getBean("userService");
        emailService = (EmailService) context.getBean("emailService");
        mountService = (MountService) context.getBean("mountService");
    }

    @Override
    protected Page onRuntimeException(Page page, RuntimeException e) {
        if (e instanceof WicketRuntimeException) {
            WicketRuntimeException wre = (WicketRuntimeException) e;
            if (wre.getCause() instanceof InvocationTargetException) {
                InvocationTargetException ite = (InvocationTargetException) wre.getCause();
                if (ite.getTargetException() instanceof UnsupportedOperationException) {
                    return new UnsupportedOperationPage();
                }
            }
        }
        // send mail to the admin!
        if (!(e instanceof PageExpiredException) && !(e instanceof UnauthorizedInstantiationException)) {
            Integer templateId = configurationService.findAsInteger(CommonConstants.CONF_UNKNOWN_ERROR_EMAIL);

            Writer content = new StringWriter();
            PrintWriter printWriter = new PrintWriter(content);
            e.printStackTrace(printWriter);
            sendEmailToUsers(templateId, content.toString());

        }
        return super.onRuntimeException(page, e);
    }

    @Override
    protected IRequestCodingStrategy newRequestCodingStrategy() {
        return new PortalWebRequestCodingStrategy(mountService);
    }

    private void sendEmailToUsers(Integer templateId, String content) {
        EmailPlaceholderBean placeholder = new EmailPlaceholderBean();
        placeholder.setContent(content);
        List<User> users = userService.findUserWithRight("emailnotification.unknown.application.error");
        for (User user : users) {
            placeholder.setUsername(user.getUsername());
            placeholder.setFirstname(user.getFirstname());
            placeholder.setLastname(user.getLastname());
            placeholder.setEmail(user.getEmail());
            emailService.sendEmail(templateId, placeholder);
        }
    }
}
