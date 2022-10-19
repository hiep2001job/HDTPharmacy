package com.cp2196g03gr01.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;



import lombok.Data;

@Data
public class PageRender<T> {

	private String url;

	private Page<T> page;

	private int totalPages;

	private int numElementsByPage;

	private int actualPage;

	private List<PageItem> pages;

	public PageRender(String url, Page<T> page) {
		this.url = url;
		this.page = page;
		this.pages = new ArrayList<PageItem>();

		numElementsByPage = page.getSize();
		totalPages = page.getTotalPages();
		actualPage = page.getNumber() + 1;

		/*----- Paginator - First << 1 2 ... n >> Last -----*/
		int from, upto;
		if (totalPages <= numElementsByPage) {
			from = 1;
			upto = totalPages;
		} else {
			if (actualPage <= numElementsByPage / 2) {
				from = 1;
				upto = numElementsByPage;
			} else if (actualPage >= totalPages - numElementsByPage / 2) {
				from = totalPages - numElementsByPage + 1;
				upto = numElementsByPage;
			} else {
				from = actualPage - numElementsByPage / 2;
				upto = numElementsByPage;
			}
		}
		for (int i = 0; i < upto; i++) {
			pages.add(new PageItem(from + i, actualPage == from + i));
		}
	}
	public boolean isFirst() { return page.isFirst(); }
	
	public boolean isLast() { return page.isLast(); }
	
	public boolean isHasNext() { return page.hasNext(); }
	
	public boolean isHasPrevious() { return page.hasPrevious(); }
	
}
