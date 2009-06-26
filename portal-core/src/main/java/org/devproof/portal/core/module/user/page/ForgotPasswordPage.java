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
package org.devproof.portal.core.module.user.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.captcha.CaptchaImageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.devproof.portal.core.module.common.page.MessagePage;
import org.devproof.portal.core.module.common.page.TemplatePage;
import org.devproof.portal.core.module.common.util.PortalUtil;
import org.devproof.portal.core.module.configuration.service.ConfigurationService;
import org.devproof.portal.core.module.email.bean.EmailPlaceholderBean;
import org.devproof.portal.core.module.email.service.EmailService;
import org.devproof.portal.core.module.user.UserConstants;
import org.devproof.portal.core.module.user.entity.UserEntity;
import org.devproof.portal.core.module.user.service.UserService;

/**
 * @author Carsten Hufe
 */
public class ForgotPasswordPage extends TemplatePage {

	private static final long serialVersionUID = 1L;

	@SpringBean(name = "emailService")
	private transient EmailService emailService;
	@SpringBean(name = "userService")
	private transient UserService userService;
	@SpringBean(name = "configurationService")
	private transient ConfigurationService configurationService;

	public ForgotPasswordPage(final PageParameters params) {
		super(params);
		Form<Serializable> form = new Form<Serializable>("form");
		form.setOutputMarkupId(true);
		addOrReplace(form);

		FormComponent<String> fc;

		final RequiredTextField<?> emailoruser = new RequiredTextField<String>("emailoruser", Model.of(""));
		form.add(emailoruser);

		Boolean enableCaptcha = this.configurationService.findAsBoolean(UserConstants.CONF_REGISTRATION_CAPTCHA);
		WebMarkupContainer trCaptcha1 = new WebMarkupContainer("trCaptcha1");
		WebMarkupContainer trCaptcha2 = new WebMarkupContainer("trCaptcha2");
		trCaptcha1.setVisible(enableCaptcha);
		trCaptcha2.setVisible(enableCaptcha);

		form.add(trCaptcha1);
		form.add(trCaptcha2);

		final CaptchaImageResource captchaImageResource = new CaptchaImageResource(PortalUtil.randomString(6, 8));
		trCaptcha1.add(new Image("captchacodeimage", captchaImageResource));

		fc = new TextField<String>("captchacode", Model.of(""));
		fc.setRequired(enableCaptcha);
		trCaptcha2.add(fc);

		if (enableCaptcha) {
			fc.add(new AbstractValidator<String>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onValidate(final IValidatable<String> ivalidatable) {
					if (!captchaImageResource.getChallengeId().equalsIgnoreCase(ivalidatable.getValue())) {
						captchaImageResource.invalidate();
						this.error(ivalidatable);
					}
				}

				@Override
				protected String resourceKey() {
					return "wrong.captchacode";
				}
			});
		}

		form.add(new Button("requestButton") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				String value = emailoruser.getValue();
				UserEntity userByName = ForgotPasswordPage.this.userService.findUserByUsername(value);
				List<UserEntity> users = new ArrayList<UserEntity>();
				if (userByName == null) {
					users = ForgotPasswordPage.this.userService.findUserByEmail(value);
				} else {
					users.add(userByName);
				}

				if (users.size() == 0) {
					this.error(this.getString("not.found"));
				} else {
					for (UserEntity user : users) {
						user.setChangedAt(PortalUtil.now());
						user.setForgotPasswordCode(PortalUtil.generateMd5(getSession().getId() + Math.random()));
						ForgotPasswordPage.this.userService.save(user);

						EmailPlaceholderBean placeholder = PortalUtil.getEmailPlaceHolderByUser(user);
						StringBuffer url = ForgotPasswordPage.this.getWebRequestCycle().getWebRequest().getHttpServletRequest().getRequestURL();
						PageParameters param = new PageParameters();
						param.add(ResetPasswordPage.PARAM_USER, user.getUsername());
						param.add(ResetPasswordPage.PARAM_KEY, user.getForgotPasswordCode());
						url = new StringBuffer(StringUtils.substringBeforeLast(url.toString(), "/")).append("/");
						url.append(ForgotPasswordPage.this.getWebRequestCycle().urlFor(ResetPasswordPage.class, param));
						placeholder.setResetPasswordLink(url.toString());
						ForgotPasswordPage.this.emailService.sendEmail(ForgotPasswordPage.this.configurationService.findAsInteger(UserConstants.CONF_PASSWORDFORGOT_EMAIL), placeholder);
					}
					this.setResponsePage(MessagePage.getMessagePage(this.getString("email.sent")));
				}
			}
		});
	}
}
