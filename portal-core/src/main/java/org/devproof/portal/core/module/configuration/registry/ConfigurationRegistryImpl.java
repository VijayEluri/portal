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
package org.devproof.portal.core.module.configuration.registry;

import org.devproof.portal.core.config.Registry;
import org.devproof.portal.core.module.configuration.entity.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Carsten Hufe
 */
@Registry("configurationRegistry")
public class ConfigurationRegistryImpl implements ConfigurationRegistry {
    private final Map<String, Configuration> configurations = new HashMap<String, Configuration>();

    @Override
    public Configuration getConfiguration(String key) {
        return configurations.get(key);
    }

    @Override
    public void registerConfiguration(String key, Configuration configuration) {
        configurations.put(key, configuration);
    }

    @Override
    public void removeConfiguration(String key) {
        configurations.remove(key);
    }

}
