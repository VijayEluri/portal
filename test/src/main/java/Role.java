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

import com.mongodb.ReflectionDBObject;

import java.util.HashSet;
import java.util.Set;

public class Role extends ReflectionDBObject {
	private String rolename;
	private String roledesc;

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getRoledesc() {
		return roledesc;
	}

	public void setRoledesc(String roledesc) {
		this.roledesc = roledesc;
	}

	@Override
	public String toString() {
		return "Role [roledesc=" + roledesc + ", rolename=" + rolename + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((roledesc == null) ? 0 : roledesc.hashCode());
		result = prime * result + ((rolename == null) ? 0 : rolename.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (roledesc == null) {
			if (other.roledesc != null)
				return false;
		} else if (!roledesc.equals(other.roledesc))
			return false;
		if (rolename == null) {
			if (other.rolename != null)
				return false;
		} else if (!rolename.equals(other.rolename))
			return false;
		return true;
	}

	boolean partial = false;

	@Override
	public boolean isPartialObject() {
		return partial;
	}

	@Override
	public void markAsPartialObject() {
		partial = true;
	}

	@Override
	public Set<String> keySet() {
		if (partial) {
			Set<String> set = new HashSet<String>();
			set.add("_id");
			return set;
		}
		return super.keySet();
	}

}
