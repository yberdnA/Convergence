package com.convergence.support.shiro;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.convergence.domain.ResourceDTO;
import com.convergence.domain.RoleDTO;
import com.convergence.domain.UserDTO;
import com.convergence.service.UserService;
import com.convergence.support.MD5Utils;

public class MyRealm extends AuthorizingRealm {

	public MyRealm() {
		super(new AllowAllCredentialsMatcher());
		setAuthenticationTokenClass(UsernamePasswordToken.class);

		// FIXME: 暂时禁用Cache
		setCachingEnabled(true);
	}

	@Autowired
	private UserService userService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		UserDTO user = (UserDTO) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		UserDTO dbUser = userService.findByUserName(user.getUserName());
		Set<String> shiroPermissions = new HashSet<>();
		Set<String> roleSet = new HashSet<String>();
		Set<RoleDTO> roles = dbUser.getRoles();
		for (RoleDTO role : roles) {
			Set<ResourceDTO> resources = role.getResources();
			for (ResourceDTO resource : resources) {
				shiroPermissions.add(resource.getSourceKey());

			}
			roleSet.add(role.getRoleKey());
		}
		authorizationInfo.setRoles(roleSet);
		authorizationInfo.setStringPermissions(shiroPermissions);
		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal();
		//
		UserDTO user = userService.findByUserName(username);

		String password = new String((char[]) token.getCredentials());

		// 账号不存在
		 if (user == null) {
		 throw new UnknownAccountException("账号或密码不正确");
		 }
		 // 密码错误
		 if (!MD5Utils.md5(password).equals(user.getPassword())) {
		 throw new IncorrectCredentialsException("账号或密码不正确");
		 }
		 // 账号锁定
		 if (user.getLocked() == 1) {
		 throw new LockedAccountException("账号已被锁定,请联系管理员");
		 }

		  SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user,
		  password, getName());

		  return info;
		 
	}

}
