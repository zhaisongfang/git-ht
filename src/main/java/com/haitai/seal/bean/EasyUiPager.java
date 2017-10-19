package com.haitai.seal.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;


public class EasyUiPager {

	protected static final int DEFAULT_PAGE_SIZE=10;
	
	/**
	 * 每页最大条数
	 */
	protected Integer rows;
	/**
	 * 第几页(从1开始)
	 */
	protected Integer page=1;
	
	/**
	 * 排序字段,以,分割
	 */
	protected String sort;
	/**
	 * 排序顺序,以,分割
	 */
	protected String order;

	
	/**
	 * @return 查询使用的分页辅助对象,里面包含了easyui的datagrid分页中所有的参数
	 */
	public Pageable getPageable() {
		if(page==null){
			page=1;
		}
		if(rows==null){
			rows=DEFAULT_PAGE_SIZE;
		}
		Pageable pageable;
		if(StringUtils.isBlank(sort)){
			pageable=new PageRequest(page-1, rows);
		}else{
			String[] fields=sort.split(",");
			String[] orders=order.split(",");
			List<Order> list=new ArrayList<Sort.Order>();
			for (int i = 0; i < fields.length; i++) {
				Order o=new Order(Direction.fromString(orders[i]),fields[i]);
				list.add(o);
			}
			Sort s=new Sort(list);
			pageable=new PageRequest(page-1, rows, s);
		}
		return pageable;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer row) {
		this.rows = row;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

}
