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
package org.devproof.portal.core.module.common.component.richtext;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.PackageResource;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.MapModel;
import org.apache.wicket.protocol.http.RequestUtils;
import org.apache.wicket.util.collections.MiniMap;
import org.apache.wicket.util.string.UrlUtils;
import org.apache.wicket.util.template.TextTemplateHeaderContributor;
import org.devproof.portal.core.module.common.CommonConstants;
import org.devproof.portal.core.module.common.util.PortalUtil;

import java.util.Map;

/**
 * @author Carsten Hufe
 */
public class FullRichTextArea extends TextArea<String> {
    private static final long serialVersionUID = 1L;
    private static ResourceReference REF_CK_EDITOR_CONFIG = new ResourceReference(FullRichTextArea.class, "custom/config.js");


    public FullRichTextArea(String id) {
        this(id, null);
    }

    public FullRichTextArea(String id, IModel<String> model) {
        super(id, model);
        add(createCKEditorResource());
        setOutputMarkupId(true);
    }

    private HeaderContributor createCKEditorResource() {
        return JavascriptPackageResource.getHeaderContribution(FullRichTextArea.class, "ckeditor/ckeditor.js");
    }

    @Override
    protected void onRender(MarkupStream markupStream) {
        super.onRender(markupStream);
        Map<String, Object> variables = new MiniMap<String, Object>(2);
        variables.put("defaultCss", RequestUtils.toAbsolutePath(urlFor(CommonConstants.REF_DEFAULT_CSS).toString()));
        variables.put("markupId", getMarkupId());
        String javascript = TextTemplateHeaderContributor.forJavaScript(FullRichTextArea.class, "FullRichTextArea.js", new MapModel<String, Object>(variables)).toString();
        getResponse().write(javascript);
    }
}
