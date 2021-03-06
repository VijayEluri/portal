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
package org.devproof.portal.core.module.role.service;

import org.devproof.portal.core.module.common.service.CrudService;
import org.devproof.portal.core.module.role.entity.Role;

import java.util.List;

/**
 * @author Carsten Hufe
 */
public interface RoleService extends CrudService<Role, Integer> {
    /**
     * Returns all roles
     */
    List<Role> findAll();

    /**
     * Returns a new instance of role
     */
    Role newRoleEntity();

    /**
     * Returns all roles ordered by description
     */
    List<Role> findAllOrderByDescription();

    /**
     * returns the guest role
     */
    Role findGuestRole();

    /**
     * returns the default registration role
     */
    Role findDefaultRegistrationRole();
}
