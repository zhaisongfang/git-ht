package com.haitai.seal.biz.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haitai.seal.bean.SealPublishLog;
import com.haitai.seal.biz.SealPublishLogBiz;
import com.haitai.seal.dao.SealPublishLogDao;

@Service
@Transactional(rollbackFor=Throwable.class)
public class SealPublishLogBizImpl implements SealPublishLogBiz {

    @Autowired
    protected SealPublishLogDao sealPublishLogDao;

    private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Page<SealPublishLog> findPage(final String sealId, Pageable pageable) {
		logger.debug("查询印章发放记录,印章id为:{}",sealId);
		Specification<SealPublishLog> spec=new Specification<SealPublishLog>() {
            @Override
            public Predicate toPredicate(Root<SealPublishLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                
                if(sealId!=null){
                    predicate.add(cb.equal(root.get("stampID").as(String.class),sealId));
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
		return sealPublishLogDao.findAll(spec, pageable);
	}


}
