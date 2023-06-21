package henry.hotel.dao;

import henry.hotel.model.Role;

//DAO Pattern for Role
public interface RoleDao {

	public Role findRoleByName(String roleName);
	
}
