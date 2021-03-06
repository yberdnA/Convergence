package com.convergence.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.convergence.domain.UserDTO;
import com.convergence.domain.UserRoleDTO;

@Mapper
public interface UserDao {
    int deleteByPrimaryKey(Integer userId);

    int insert(UserDTO record);

    int insertSelective(UserDTO record);

    UserDTO selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(UserDTO record);

    int updateByPrimaryKey(UserDTO record);

	UserDTO findByUserName(@Param("username")String username);

	List<UserDTO> findAll(@Param("offset")int offset,@Param("pageSize") int pageSize);

	int findTotalCount();

	void insertOrUpdate(UserDTO user);

	void deleteUserRoleByUserId(@Param("userId")Integer userId);

	void insertUserRoles(@Param("set")Set<UserRoleDTO> userRoles);
}
