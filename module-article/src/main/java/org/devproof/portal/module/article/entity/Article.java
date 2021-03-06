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
package org.devproof.portal.module.article.entity;

import org.apache.commons.lang.StringUtils;
import org.devproof.portal.core.config.RegisterGenericDataProvider;
import org.devproof.portal.core.module.common.annotation.CacheQuery;
import org.devproof.portal.core.module.right.entity.Right;
import org.devproof.portal.module.article.ArticleConstants;
import org.devproof.portal.module.article.query.ArticleQuery;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Carsten Hufe
 */
@Entity
@Table(name = "article")
@CacheQuery(region = ArticleConstants.QUERY_CACHE_REGION)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = ArticleConstants.ENTITY_CACHE_REGION)
@RegisterGenericDataProvider(value = "articleDataProvider", sortProperty = "title", sortAscending = true, queryClass = ArticleQuery.class)
public class Article extends BaseArticle {
    private static final long serialVersionUID = 1L;

    @OneToMany(mappedBy = "article", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticlePage> articlePages;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "article_right_xref", joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "right_id", referencedColumnName = "right_id"))
    @Fetch(FetchMode.SUBSELECT)
    private List<Right> allRights;
    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "article_tag_xref", joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "tagname", referencedColumnName = "tagname"))
    private List<ArticleTag> tags;

    public void copyFrom(ArticleHistorized modification) {
        super.copyFrom(modification);
        setFullArticle(modification.getFullArticle());
    }

    @Transient
    public List<Right> getCommentViewRights() {
        return getRightsStartingWith(allRights, "article.comment.view");
    }

    @Transient
    public List<Right> getCommentWriteRights() {
        return getRightsStartingWith(allRights, "article.comment.write");
    }

    @Transient
    public List<Right> getViewRights() {
        return getRightsStartingWith(allRights, "article.view");
    }

    @Transient
    public List<Right> getReadRights() {
        return getRightsStartingWith(allRights, "article.read");
    }

    public List<ArticlePage> getArticlePages() {
        if (articlePages == null) {
            articlePages = new ArrayList<ArticlePage>();
        }
        return articlePages;
    }

    public void setArticlePages(List<ArticlePage> articlePages) {
        this.articlePages = articlePages;
    }

    public List<Right> getAllRights() {
        if (allRights == null) {
            allRights = new ArrayList<Right>();
        }
        return allRights;
    }

    public void setAllRights(List<Right> allRights) {
        this.allRights = allRights;
    }

    public List<ArticleTag> getTags() {
        return tags;
    }

    public void setTags(List<ArticleTag> tags) {
        this.tags = tags;
    }

    @Transient
    public String getFullArticle() {
        String back = "";
        if (articlePages != null) {
            StringBuilder buf = new StringBuilder();
            boolean firstArticlePage = true;
            for (ArticlePage page : articlePages) {
                if (firstArticlePage) {
                    firstArticlePage = false;
                } else {
                    buf.append(ArticleConstants.PAGEBREAK);
                }
                buf.append(page.getContent());
            }
            back = buf.toString();
        }
        return back;
    }

    @Transient
    public void setFullArticle(String fullArticle) {
        if (fullArticle == null) {
            fullArticle = " ";
        }
        List<ArticlePage> pages = getArticlePages();
        List<String> splittedPages = getSplittedPages(fullArticle);
        for (int i = 0; i < splittedPages.size(); i++) {
            final ArticlePage page;
            boolean isUpdatablePageAvailable = articlePages != null && articlePages.size() > i;
            if (isUpdatablePageAvailable) {
                page = articlePages.get(i);
            } else {
                page = newArticlePageEntity(i + 1);
                page.setArticle(this);
                pages.add(page);
            }
            page.setContent(splittedPages.get(i));
            // pages.add(page);
        }
        for (int i = pages.size() - 1; i >= splittedPages.size(); i--) {
            pages.remove(i);
        }
    }

    @Transient
    public ArticlePage newArticlePageEntity(Integer page) {
        ArticlePage e = new ArticlePage();
        e.setArticle(this);
        e.setPage(page);
        return e;
    }

    private List<String> getSplittedPages(String pages) {
        String[] splitted = StringUtils.splitByWholeSeparator(pages, "page-break-after");
        List<String> result = new ArrayList<String>();
        if (splitted.length > 1) {
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < splitted.length; i++) {
                String actual = splitted[i];
                int open = actual.lastIndexOf('<');
                int close = actual.lastIndexOf('>');
                if (open < 0 || close > open) {
                    // kein tag
                    buf.append(actual);
                    if (splitted.length - 1 != i) buf.append("page-break-after");
                } else {
                    // tag
                    buf.append(StringUtils.substringBeforeLast(actual, "<"));
                    result.add(buf.toString());
                    buf = new StringBuilder();
                    String closeTag = StringUtils.substringAfterLast(actual, "<");
                    closeTag = "</" + StringUtils.substringBefore(closeTag, " ") + ">";
                    splitted[i + 1] = StringUtils.substringAfter(splitted[i + 1], closeTag);
                }
            }
            if (buf.length() > 0) {
                result.add(buf.toString());
            }
        } else {
            result.add(pages);
        }
        return result;
    }
}
