package com.haitai.seal.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.haitai.seal.bean.SealCenter;

public interface SealCenterDao extends JpaRepository<SealCenter, String>{

	SealCenter findByIssystem(String string);

	List<SealCenter> findByEscenterid(String systemID);

	List<SealCenter> findByEscenteridAndSystemtypeNot(String systemID, Integer type);

	List<SealCenter> findByEscenteridAndSystemtypeAndServiceaddressIsNotNull(String systemID, Integer type);

	List<SealCenter> findByParentid(String parentid);

	SealCenter findBySystemid(String systemid);

	SealCenter findBySystemname(String systemname);

	@Modifying
	@Query(value="delete from SealCenter o where o.id=?1")
	public void deleteByid(String id);
	
    SealCenter findByIdNotAndSystemid(String id,String systemid);
    
	SealCenter findByIdNotAndSystemname(String id,String systemname); 
}
