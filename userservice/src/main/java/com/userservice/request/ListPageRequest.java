package com.userservice.request;

import javax.validation.constraints.Min;

public class ListPageRequest {

	@Min(0)
	private long page;
	private long totalInList;
	private String searchText;
	


	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public long getPage() {
		return page;
	}

	public void setPage(long page) {
		this.page = page;
	}

	public long getTotalInList() {
		return totalInList;
	}

	public void setTotalInList(long totalInList) {
		this.totalInList = totalInList;
	}

	

}
