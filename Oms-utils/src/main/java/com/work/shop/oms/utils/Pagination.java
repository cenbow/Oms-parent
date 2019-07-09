package com.work.shop.oms.utils;

import java.util.List;

/**
 * 分页类
 * 
 * @author
 * 
 */
public class Pagination {
	public static final int DEFUALT_PAGE_SIZE = 20;
	
	private String searchId;
	
	private int totalSize; // 总行数

	private int pageSize = DEFUALT_PAGE_SIZE; // 每页显示的行数

	private int currentPage; // 当前页号

	private int totalPages; // 总页数

	private int startRow; // 当前页在数据库中的起始行

	private List<?> data;//当前页数据
	
	private String sorts;//排序对象

	
	public Pagination(int pageSize) {
		super();
		this.pageSize = pageSize;
	}
	
	public Pagination(int currentPage, int pageSize) {
		super();
		this.pageSize = pageSize;
		this.currentPage = currentPage;
	}

	/**
	 * @return the totalSize
	 */
	public int getTotalSize() {
		return totalSize;
	}

	/**
	 * @param totalSize the totalSize to set
	 */
	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the currentPage
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * @param currentPage the currentPage to set
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * @return the totalPages
	 */
	public int getTotalPages() {
		totalPages = (totalSize % pageSize == 0? totalSize/pageSize : totalSize/pageSize + 1);
		return totalPages;
	}


	/**
	 * @return the startRow
	 */
	public int getStartRow() {
		startRow = (currentPage - 1) * pageSize;
		return startRow;
	}


	/**
	 * @return the data
	 */
	public List<?> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<?> data) {
		this.data = data;
	}

	/**
	 * @return the searchId
	 */
	public String getSearchId() {
		return searchId;
	}

	/**
	 * @param searchId the searchId to set
	 */
	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}

	public String getSorts() {
		return sorts;
	}

	public void setSorts(String sorts) {
		this.sorts = sorts;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}


}
