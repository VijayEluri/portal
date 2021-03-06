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
package org.devproof.portal.core.module.box.page;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.*;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devproof.portal.core.config.ModulePage;
import org.devproof.portal.core.config.Secured;
import org.devproof.portal.core.module.box.BoxConstants;
import org.devproof.portal.core.module.box.entity.Box;
import org.devproof.portal.core.module.box.panel.BoxEditPanel;
import org.devproof.portal.core.module.box.registry.BoxRegistry;
import org.devproof.portal.core.module.box.service.BoxService;
import org.devproof.portal.core.module.common.CommonConstants;
import org.devproof.portal.core.module.common.page.TemplatePage;
import org.devproof.portal.core.module.common.panel.AuthorPanel;
import org.devproof.portal.core.module.common.panel.BubblePanel;

import java.util.Arrays;
import java.util.List;

/**
 * @author Carsten Hufe
 */
@Secured(BoxConstants.ADMIN_RIGHT)
@ModulePage(mountPath = "/admin/boxes", registerGlobalAdminLink = true)
public class BoxPage extends TemplatePage {

    private static final long serialVersionUID = 1L;

    @SpringBean(name = "boxDataProvider")
    private SortableDataProvider<Box> boxDataProvider;
    @SpringBean(name = "boxService")
    private BoxService boxService;
    @SpringBean(name = "boxRegistry")
    private BoxRegistry boxRegistry;
    private BubblePanel bubblePanel;
    private WebMarkupContainer repeatingBoxesInRefreshContainer;

    public BoxPage(PageParameters params) {
        super(params);
        add(createRepeatingBoxesWithRefreshContainer());
        add(createBubblePanel());
    }

    @Override
    protected Component newPageAdminBoxLink(String linkMarkupId, String labelMarkupId) {
        return createCreateBoxLink(linkMarkupId, labelMarkupId);
    }

    private BoxDataView createRepeatingBoxes() {
        return new BoxDataView("repeatingBoxes");
    }

    private BubblePanel createBubblePanel() {
        bubblePanel = new BubblePanel("bubblePanel");
        return bubblePanel;
    }

    private AjaxLink<Box> createCreateBoxLink(String linkMarkupId, String labelMarkupId) {
        AjaxLink<Box> createLink = newCreateBoxLink(linkMarkupId);
        createLink.add(createBoxLinkLabel(labelMarkupId));
        return createLink;
    }

    private Label createBoxLinkLabel(String labelMarkupId) {
        return new Label(labelMarkupId, getString("createLink"));
    }

    private AjaxLink<Box> newCreateBoxLink(String linkMarkupId) {
        return new AjaxLink<Box>(linkMarkupId) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(final AjaxRequestTarget target) {
                BoxEditPanel boxEditPanel = newBoxEditPanel();
                setBoxEditPanelToBubblePanel(target, boxEditPanel);
            }

            private BoxEditPanel newBoxEditPanel() {
                IModel<Box> boxModel = Model.of(boxService.newBoxEntity());
                return new BoxEditPanel(bubblePanel.getContentId(), boxModel) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onSave(AjaxRequestTarget target) {
                        target.addComponent(repeatingBoxesInRefreshContainer);
                        target.addComponent(BoxPage.this.getFeedback());
                        info(getString("msg.saved"));
                        bubblePanel.hide(target);
                    }

                    @Override
                    public void onCancel(AjaxRequestTarget target) {
                        bubblePanel.hide(target);
                    }
                };
            }

            private void setBoxEditPanelToBubblePanel(final AjaxRequestTarget target, BoxEditPanel boxEditPanel) {
                bubblePanel.setContent(boxEditPanel);
                bubblePanel.showModal(target);
            }
        };
    }

    private WebMarkupContainer createRepeatingBoxesWithRefreshContainer() {
        repeatingBoxesInRefreshContainer = new WebMarkupContainer("refreshTable");
        repeatingBoxesInRefreshContainer.add(createRepeatingBoxes());
        repeatingBoxesInRefreshContainer.setOutputMarkupId(true);
        return repeatingBoxesInRefreshContainer;
    }

    private class BoxDataView extends DataView<Box> {
        private static final long serialVersionUID = 1L;

        public BoxDataView(String id) {
            super(id, boxDataProvider);
        }

        @Override
        protected void populateItem(Item<Box> item) {
            IModel<Box> boxModel = item.getModel();
            item.add(createSortLabel(boxModel));
            item.add(createTypeLabel(boxModel));
            item.add(createTitleLabel(boxModel));
            item.add(createAuthorPanel(boxModel));
            item.add(createMoveUpLink(boxModel));
            item.add(createMoveDownLink(boxModel));
            item.add(createClassEvenOddModifier(item));
        }

        private AttributeModifier createClassEvenOddModifier(final Item<Box> item) {
            return new AttributeModifier("class", true, new AbstractReadOnlyModel<String>() {
                private static final long serialVersionUID = 1L;

                @Override
                public String getObject() {
                    return (item.getIndex() % 2 != 0) ? "even" : "odd";
                }
            });
        }

        private MarkupContainer createMoveDownLink(IModel<Box> boxModel) {
            AjaxLink<Box> moveDownLink = newMoveDownLink(boxModel);
            moveDownLink.add(createMoveDownLinkImage());
            return moveDownLink;
        }

        private Image createMoveDownLinkImage() {
            return new Image("downImage", CommonConstants.REF_DOWN_IMG);
        }

        private AjaxLink<Box> newMoveDownLink(final IModel<Box> boxModel) {
            return new AjaxLink<Box>("downLink") {
                private static final long serialVersionUID = 1L;

                @Override
                public void onClick(AjaxRequestTarget target) {
                    boxService.moveDown(boxModel.getObject());
                    target.addComponent(repeatingBoxesInRefreshContainer);
                }
            };
        }

        private MarkupContainer createMoveUpLink(IModel<Box> boxModel) {
            AjaxLink<Box> moveUpLink = newMoveUpLink(boxModel);
            moveUpLink.add(createMoveUpLinkImage());
            return moveUpLink;
        }

        private Image createMoveUpLinkImage() {
            return new Image("upImage", CommonConstants.REF_UP_IMG);
        }

        private AjaxLink<Box> newMoveUpLink(final IModel<Box> boxModel) {
            return new AjaxLink<Box>("upLink") {
                private static final long serialVersionUID = 1L;

                @Override
                public void onClick(AjaxRequestTarget target) {
                    boxService.moveUp(boxModel.getObject());
                    target.addComponent(repeatingBoxesInRefreshContainer);
                }
            };
        }

        private AuthorPanel<Box> createAuthorPanel(final IModel<Box> boxModel) {
            return new AuthorPanel<Box>("authorButtons", boxModel) {
                private static final long serialVersionUID = 1L;

                @Override
                public void onDelete(AjaxRequestTarget target) {
                    boxService.delete(boxModel.getObject());
                    info(getString("msg.deleted"));
                    target.addComponent(repeatingBoxesInRefreshContainer);
                    target.addComponent(getFeedback());
                }

                @Override
                public void onEdit(final AjaxRequestTarget target) {
                    BoxEditPanel editUserPanel = new BoxEditPanel(bubblePanel.getContentId(), boxModel) {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void onSave(AjaxRequestTarget target) {
                            bubblePanel.hide(target);
                            info(getString("msg.saved"));
                            target.addComponent(repeatingBoxesInRefreshContainer);
                            target.addComponent(getFeedback());
                        }

                        @Override
                        public void onCancel(AjaxRequestTarget target) {
                            bubblePanel.hide(target);
                        }
                    };

                    bubblePanel.setContent(editUserPanel);
                    bubblePanel.showModal(target);
                }

            };
        }

        private Label createTitleLabel(IModel<Box> boxModel) {
            IModel<String> titleModel = new PropertyModel<String>(boxModel, "title");
            return new Label("title", titleModel);
        }

        private Label createTypeLabel(IModel<Box> boxModel) {
            IModel<String> typeModel = typeModel(boxModel);
            return new Label("type", typeModel);
        }

        private IModel<String> typeModel(final IModel<Box> boxModel) {
            return new LoadableDetachableModel<String>() {
                private static final long serialVersionUID = 3674902001006638462L;

                @Override
                protected String load() {
                    Box box = boxModel.getObject();
                    return boxRegistry.getNameBySimpleClassName(box.getBoxType());
                }
            };
        }

        private Label createSortLabel(IModel<Box> boxModel) {
            IModel<Integer> sortModel = new PropertyModel<Integer>(boxModel, "title");
            return new Label("sort", sortModel);
        }
    }
}
