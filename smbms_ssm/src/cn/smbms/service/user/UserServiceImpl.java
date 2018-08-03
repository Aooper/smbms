package cn.smbms.service.user;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import cn.smbms.dao.user.UserMapper;
import cn.smbms.pojo.User;

@Service("userService")
@Lazy
public class UserServiceImpl implements UserService {
	@Resource
	private UserMapper userMapper;

	@Override
	public boolean add(User user) {
		// TODO Auto-generated method stub
		boolean flag = false;
		int updateRows = userMapper.add(user);
		if (updateRows > 0) {
			flag = true;
			System.out.println("add success!");
		} else {
			System.out.println("add failed!");
		}

		return flag;
	}

	@Override
	public User login(String userCode, String userPassword) {
		// TODO Auto-generated method stub
		User user = null;
		user = userMapper.getLoginUser(userCode);
		// 匹配密码
		if (null != user) {
			if (!user.getUserPassword().equals(userPassword))
				user = null;
		}
		return user;
	}

	@Override
	public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<User> userList = null;

		userList = userMapper.getUserList(queryUserName, queryUserRole, (currentPageNo - 1) * pageSize, pageSize);
		return userList;
	}

	@Override
	public User selectUserCodeExist(String userCode) {
		// TODO Auto-generated method stub
		User user = null;
		user = userMapper.getLoginUser(userCode);
		return user;
	}

	@Override
	public boolean deleteUserById(Integer delId) {
		// TODO Auto-generated method stub

		boolean flag = false;
		if (userMapper.deleteUserById(delId) > 0)
			flag = true;

		return flag;
	}

	@Override
	public User getUserById(String id) {
		// TODO Auto-generated method stub
		User user = null;
		user = userMapper.getUserById(id);

		return user;
	}

	@Override
	public boolean modify(User user) {
		// TODO Auto-generated method stub
		boolean flag = false;
		if (userMapper.modify(user) > 0) {
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean updatePwd(int id, String pwd) {
		// TODO Auto-generated method stub
		boolean flag = false;
		if (userMapper.updatePwd(id, pwd) > 0)
			flag = true;
		return flag;
	}

	@Override
	public int getUserCount(String queryUserName, int queryUserRole) {
		// TODO Auto-generated method stub
		int count = 0;
		count = userMapper.getUserCount(queryUserName, queryUserRole);
		return count;
	}

}
