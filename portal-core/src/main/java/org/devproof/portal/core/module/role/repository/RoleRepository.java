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
package org.devproof.portal.core.module.role.repository;

import org.devproof.portal.core.config.GenericRepository;
import org.devproof.portal.core.module.common.CommonConstants;
import org.devproof.portal.core.module.common.annotation.CacheQuery;
import org.devproof.portal.core.module.common.annotation.Query;
import org.devproof.portal.core.module.common.repository.CrudRepository;
import org.devproof.portal.core.module.role.entity.Role;

import java.util.List;

/**
 * @author Carsten Hufe
 */
@GenericRepository("roleRepository")
@CacheQuery(region = CommonConstants.QUERY_CORE_CACHE_REGION)
public interface RoleRepository extends CrudRepository<Role, Integer> {
    @Query("Select r from Role r")
    List<Role> findAll();

    @Query("select r from Role r order by r.description asc")
    List<Role> findAllOrderByDescription();
}
