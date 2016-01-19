package com.cardpay.pccredit.intopieces.web;

import java.util.List;

public class Pic {
	private int totalCount;
	List<PicPojo> pics;
	
	public int getTotalCount() {
		return totalCount;
	}


	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}


	public List<PicPojo> getPics() {
		return pics;
	}


	public void setPics(List<PicPojo> pics) {
		this.pics = pics;
	}
}
