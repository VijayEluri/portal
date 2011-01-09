/*
 * Copyright 2009-2010 Carsten Hufe devproof.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.devproof.portal.core.app;

import org.apache.wicket.protocol.http.request.WebRequestCodingStrategy;
import org.apache.wicket.request.target.coding.IRequestTargetUrlCodingStrategy;

/**
 * Required to mount custom url on all paths
 *
 * @author Carsten Hufe
 */
public class PortalWebRequestCodingStrategy extends WebRequestCodingStrategy {
    @Override
    public IRequestTargetUrlCodingStrategy urlCodingStrategyForPath(String path) {
        IRequestTargetUrlCodingStrategy strategy = super.urlCodingStrategyForPath(path);
        if (strategy != null) {
            return strategy;
        }
        return new CustomMountUrlCodingStrategy();
    }
}