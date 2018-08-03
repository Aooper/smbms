package cn.smbms.service.role;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import cn.smbms.dao.role.RoleMapper;
import cn.smbms.pojo.Role;
@Service("roleService")
@Lazy
public class RoleServiceImpl implements RoleService{
	@Resource
	private RoleMapper roleMapper;
	
	@Override
	public List<Role> getRoleList() {
		// TODO Auto-generated method stub
		List<Role> roleList = null;
		roleList = roleMapper.getRoleList();
		return roleList;
	}
	
}
