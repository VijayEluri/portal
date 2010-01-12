package org.devproof.portal.module.comment.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.markup.html.form.Form;

public abstract class CaptchaAjaxButton extends AjaxFallbackButton {
	private static final long serialVersionUID = 1L;
	private BubblePanel bubblePanel;

	public CaptchaAjaxButton(String id, BubblePanel bubblePanel, Form<?> form) {
		super(id, form);
		this.bubblePanel = bubblePanel;
	}

	@Override
	final protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		if (bubblePanel.isRenderAllowed()) {
			bubblePanel.replace(new CaptchaPanel(bubblePanel.getContentId()) {
				private static final long serialVersionUID = 1L;
				@Override
				protected void onClickAndCaptchaValidated(AjaxRequestTarget target) {
					bubblePanel.hide(target);
					CaptchaAjaxButton.this.onClickAndCaptchaValidated(target);
				}
				@Override
				protected void onCancel(AjaxRequestTarget target) {
					bubblePanel.hide(target);
				}
			});
			bubblePanel.show(getMarkupId(), target);
			// String js = "var p = $(\"#" + getMarkupId() +
			// "\");\n var pos = p.position();";
			// js += "$(\"#" + captchaBubblePanel.getMarkupId()
			// +
			// "\").css( {\"position\": \"absolute\", \"left\": (pos.left) + \"px\", \"top\":(pos.top - $(\"#"
			// + captchaBubblePanel.getMarkupId() +
			// "\").height() - 3) + \"px\" } );";
			//
			// js += "$(\".captchaPopup\").fadeOut(\"fast\");";
			// js += "$(\"#" + captchaBubblePanel.getMarkupId() +
			// "\").fadeIn(\"slow\");";
			// js += "hideLoadingIndicator();";
			// target.appendJavascript(js);
		} else {
			onClickAndCaptchaValidated(target);
		}
	}

	public abstract void onClickAndCaptchaValidated(AjaxRequestTarget target);
}
