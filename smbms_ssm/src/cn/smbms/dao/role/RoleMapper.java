package cn.smbms.dao.role;

import java.util.List;

import org.springframework.stereotype.Component;

import cn.smbms.pojo.Role;
@Component
public interface RoleMapper {

	public List<Role> getRoleList();

}
