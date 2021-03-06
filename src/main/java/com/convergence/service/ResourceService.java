package com.convergence.service;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.domain.PageRequest;

import com.convergence.domain.ResourceDTO;
import com.convergence.support.PageInfo;

public interface ResourceService {
    int deleteByPrimaryKey(Integer reourceId);

    int insert(ResourceDTO record);

    int insertSelective(ResourceDTO record);

    ResourceDTO selectByPrimaryKey(Integer reourceId);

    int updateByPrimaryKeySelective(ResourceDTO record);

    int updateByPrimaryKey(ResourceDTO record);

    Set<ResourceDTO> selectResourcesByRoleId(String roleId);

    PageInfo<ResourceDTO> findAll(PageRequest pageRequest);

    /**
     * 获取角色的权限树
     * 
     * @param roleId
     * @return
     */
    List<com.convergence.domain.vo.ZtreeView> tree(int roleId);

	void delete(Integer id);

	void saveOrUpdate(Resource resource);

	ResourceDTO find(Integer id);

	void saveOrUpdate(ResourceDTO resource);

	List<ResourceDTO> findAll();

	List<ResourceDTO> findAllForShiro();

}
