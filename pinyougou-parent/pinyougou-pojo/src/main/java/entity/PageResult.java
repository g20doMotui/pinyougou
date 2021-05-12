package entity;

import java.io.Serializable;
import java.util.List;

public class PageResult<E> implements Serializable {
	// 总记录数
	private Long total;
	// 每页显示的记录
	private List<E> rows;
	
	
	public PageResult() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public PageResult(Long total, List<E> rows) {
		super();
		this.total = total;
		this.rows = rows;
	}


	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public List<E> getRows() {
		return rows;
	}
	public void setRows(List<E> rows) {
		this.rows = rows;
	}
	
	
	
	
	
}
