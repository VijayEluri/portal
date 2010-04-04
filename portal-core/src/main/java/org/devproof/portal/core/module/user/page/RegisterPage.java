package org.devproof.portal.core.module.user.page;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.devproof.portal.core.module.common.factory.CommonMarkupContainerFactory;
import org.devproof.portal.core.module.common.page.MessagePage;
import org.devproof.portal.core.module.common.page.TemplatePage;
import org.devproof.portal.core.module.common.panel.BubblePanel;
import org.devproof.portal.core.module.common.panel.captcha.CaptchaAjaxButton;
import org.devproof.portal.core.module.common.registry.SharedRegistry;
import org.devproof.portal.core.module.configuration.service.ConfigurationService;
import org.devproof.portal.core.module.user.UserConstants;
import org.devproof.portal.core.module.user.entity.UserEntity;
import org.devproof.portal.core.module.user.service.UrlCallback;
import org.devproof.portal.core.module.user.service.UserService;

/**
 * @author Carsten Hufe
 */
public class RegisterPage extends TemplatePage {

	private static final long serialVersionUID = 1L;
	public static final String PARAM_USER = "user";
	public static final String PARAM_KEY = "confirm";

	@SpringBean(name = "userService")
	private UserService userService;
	@SpringBean(name = "configurationService")
	private ConfigurationService configurationService;
	@SpringBean(name = "sharedRegistry")
	private SharedRegistry sharedRegistry;
	private PageParameters params;
	private IModel<UserEntity> userModel;
	private PasswordTextField password1;
	private PasswordTextField password2;
	private BubblePanel bubblePanel;

	public RegisterPage(PageParameters params) {
		super(params);
		this.params = params;
		this.userModel = createUserModel();
        activateUserIfParamsGiven();
		add(createBubblePanel());
		add(createRegisterForm());
	}

    private Component createBubblePanel() {
		bubblePanel = new BubblePanel("bubblePanel");
		return bubblePanel;
	}

	private Form<UserEntity> createRegisterForm() {
		Form<UserEntity> form = new Form<UserEntity>("form", new CompoundPropertyModel<UserEntity>(userModel));
		form.add(createUsernameField());
		form.add(createFirstnameField());
		form.add(createLastnameField());
		form.add(createBirthdayField());
		form.add(createEmailField());
		form.add(createPasswordField1());
		form.add(createPasswordField2());
		form.add(createEqualPasswordValidator());
		form.add(createTermsOfUseCheckBox());
		form.add(createTermsOfUseLink());
		form.add(createRegisterButton());
		form.setOutputMarkupId(true);
		return form;
	}

	private Component createTermsOfUseCheckBox() {
		CheckBox checkBox = new CheckBox("termsOfUse", Model.of(Boolean.FALSE));
		checkBox.add(createTermsOfUseValidator());
		return checkBox;
	}

	private AbstractValidator<Boolean> createTermsOfUseValidator() {
		return new AbstractValidator<Boolean>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onValidate(IValidatable<Boolean> ivalidatable) {
				if (!ivalidatable.getValue()) {
					error(ivalidatable);
				}
			}

			@Override
			protected String resourceKey() {
				return "termsOfUse.mustAccepted";
			}
		};
	}

	private Component createTermsOfUseLink() {
		CommonMarkupContainerFactory factory = sharedRegistry.getResource("termsOfUseLink");
		MarkupContainer termsOfUseLink;
		if (factory != null) {
			termsOfUseLink = factory.newInstance("termsOfUseLink");
		} else {
			termsOfUseLink = new WebMarkupContainer("termsOfUseLink");
		}
		return termsOfUseLink;
	}

	private CaptchaAjaxButton createRegisterButton() {
		return new CaptchaAjaxButton("registerButton", bubblePanel) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClickAndCaptchaValidated(AjaxRequestTarget target) {
                UserEntity user = userModel.getObject();
				String msg = "success";
				if (configurationService.findAsBoolean(UserConstants.CONF_EMAIL_VALIDATION)) {
					msg = "confirm.email";
				}
				user.setPlainPassword(password1.getValue());
				userService.registerUser(user, createRegisterUrlCallback());
				setResponsePage(MessagePage.getMessagePage(getString(msg)));
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.addComponent(getFeedback());
			}

			private UrlCallback createRegisterUrlCallback() {
				return new UrlCallback() {
					@Override
					public String getUrl(String generatedCode) {
                        UserEntity user = userModel.getObject();
						String requestUrl = getRequestURL();
						PageParameters param = new PageParameters();
						param.add(PARAM_USER, user.getUsername());
						param.add(PARAM_KEY, generatedCode);
						StringBuffer url = new StringBuffer(StringUtils.substringBeforeLast(requestUrl, "/"))
								.append("/");
						url.append(getWebRequestCycle().urlFor(RegisterPage.class, param));
						return url.toString();
					}
				};
			}
		};
	}

	private EqualPasswordInputValidator createEqualPasswordValidator() {
		return new EqualPasswordInputValidator(password1, password2);
	}

	private PasswordTextField createPasswordField1() {
		password1 = createPasswordField("password1");
		return password1;
	}

	private PasswordTextField createPasswordField2() {
		password2 = createPasswordField("password2");
		return password2;
	}

	private PasswordTextField createPasswordField(String id) {
		PasswordTextField password = new PasswordTextField(id, new Model<String>());
		password.add(StringValidator.minimumLength(5));
		password.setRequired(true);
		return password;
	}

	private DateTextField createBirthdayField() {
		String dateFormat = configurationService.findAsString("input_date_format");
		DateTextField dateTextField = new DateTextField("birthday", dateFormat);
		dateTextField.add(new DatePicker());
		dateTextField
				.setRequired(configurationService.findAsBoolean(UserConstants.CONF_REGISTRATION_REQUIRED_BIRTHDAY));
		return dateTextField;
	}

	private FormComponent<String> createEmailField() {
		FormComponent<String> fc = new RequiredTextField<String>("email");
		fc.add(EmailAddressValidator.getInstance());
		fc.add(StringValidator.maximumLength(100));
		return fc;
	}

	private FormComponent<String> createLastnameField() {
		FormComponent<String> fc = new TextField<String>("lastname");
		fc.add(StringValidator.maximumLength(100));
		fc.setRequired(configurationService.findAsBoolean(UserConstants.CONF_REGISTRATION_REQUIRED_NAME));
		return fc;
	}

	private FormComponent<String> createFirstnameField() {
		FormComponent<String> fc = new TextField<String>("firstname");
		fc.add(StringValidator.maximumLength(100));
		fc.setRequired(configurationService.findAsBoolean(UserConstants.CONF_REGISTRATION_REQUIRED_NAME));
		return fc;
	}

	private FormComponent<String> createUsernameField() {
		FormComponent<String> fc = new RequiredTextField<String>("username");
		fc.add(StringValidator.lengthBetween(3, 30));
		fc.add(createExistingUsernameValidator());
		fc.add(new PatternValidator("[A-Za-z0-9\\.]*"));
		return fc;
	}

	private AbstractValidator<String> createExistingUsernameValidator() {
		return new AbstractValidator<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onValidate(IValidatable<String> ivalidatable) {
				if (userService.existsUsername(ivalidatable.getValue())) {
					error(ivalidatable);
				}
			}

			@Override
			protected String resourceKey() {
				return "existing.username";
			}
		};
	}

	private IModel<UserEntity> createUserModel() {
		return Model.of(userService.newUserEntity());
	}

	private void activateUserIfParamsGiven() {
		if (params.containsKey(PARAM_USER) && params.containsKey(PARAM_KEY)) {
			activateUser();
		}
	}

	private void activateUser() {
		String username = params.getString(PARAM_USER);
		String confirmationCode = params.getString(PARAM_KEY);
		if (userService.activateUser(username, confirmationCode)) {
			setResponsePage(MessagePage.getMessagePage(getString("confirmed")));
		} else {
			setResponsePage(MessagePage.getMessagePage(getString("notconfirmed")));
		}
	}
}
