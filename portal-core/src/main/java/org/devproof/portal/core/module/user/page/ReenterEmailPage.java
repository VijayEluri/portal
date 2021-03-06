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
package org.devproof.portal.core.module.user.page;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devproof.portal.core.module.common.component.ValidationDisplayBehaviour;
import org.devproof.portal.core.module.common.page.MessagePage;
import org.devproof.portal.core.module.common.page.TemplatePage;
import org.devproof.portal.core.module.user.entity.User;
import org.devproof.portal.core.module.user.service.UrlCallback;
import org.devproof.portal.core.module.user.service.UserService;

/**
 * @author Carsten Hufe
 */
public class ReenterEmailPage extends TemplatePage {

    private static final long serialVersionUID = 1L;
    @SpringBean(name = "userService")
    private UserService userService;
    private IModel<User> userModel;
    private IModel<String> usernameModel;

    public ReenterEmailPage(IModel<String> usernameModel) {
        super(new PageParameters());
        this.usernameModel = usernameModel;
        this.userModel = createUserModel();
        add(createReenterEmailForm());
    }

    private Form<User> createReenterEmailForm() {
        Form<User> form = new Form<User>("form", new CompoundPropertyModel<User>(userModel));
        form.add(createEmailField());
        form.add(createRequestButton());
        form.setOutputMarkupId(true);
        return form;
    }

    private TextField<String> createEmailField() {
        RequiredTextField<String> tf = new RequiredTextField<String>("email");
        tf.add(new ValidationDisplayBehaviour());
        return tf;
    }

    private Button createRequestButton() {
        return new Button("requestButton") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                User user = userModel.getObject();
                userService.resendConfirmationCode(user, createConfirmationUrlCallback());
                setResponsePage(MessagePage.getMessagePageWithLogout(getString("rerequest.email")));
            }

            private UrlCallback createConfirmationUrlCallback() {
                return new UrlCallback() {
                    @Override
                    public String getUrl(String generatedCode) {
                        User user = userModel.getObject();
                        String requestUrl = getRequestURL();
                        PageParameters param = new PageParameters();
                        param.add(RegisterPage.PARAM_USER, user.getUsername());
                        param.add(RegisterPage.PARAM_KEY, generatedCode);
                        StringBuffer url = new StringBuffer(StringUtils.substringBeforeLast(requestUrl, "/")).append("/");
                        url.append(ReenterEmailPage.this.getWebRequestCycle().urlFor(RegisterPage.class, param));
                        return url.toString();
                    }
                };
            }
        };
    }

    private IModel<User> createUserModel() {
        return new LoadableDetachableModel<User>() {
            private static final long serialVersionUID = 1627241792273434554L;

            @Override
            protected User load() {
                return userService.findUserByUsername(usernameModel.getObject());
            }
        };
    }
}
