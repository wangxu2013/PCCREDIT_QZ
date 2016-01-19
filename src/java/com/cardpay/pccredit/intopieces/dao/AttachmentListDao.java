package com.cardpay.pccredit.intopieces.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cardpay.pccredit.intopieces.filter.IntoPiecesFilter;
import com.cardpay.pccredit.intopieces.model.QzApplnAttachmentBatch;
import com.cardpay.pccredit.intopieces.model.QzApplnAttachmentDetail;
import com.cardpay.pccredit.intopieces.model.QzApplnAttachmentList;
import com.wicresoft.util.annotation.Mapper;

@Mapper
public interface AttachmentListDao {
	public QzApplnAttachmentList findAttachmentList(@Param("customerId")String customerId,@Param("applicationId")String applicationId);
	public QzApplnAttachmentList findAttachmentListByAppId(@Param("applicationId")String applicationId);
	public List<QzApplnAttachmentBatch> findAttachmentBatchByAppId(@Param("applicationId")String applicationId);
	public List<QzApplnAttachmentDetail> findDetailByFilter(IntoPiecesFilter filter);
	public int findDetailCountByFilter(IntoPiecesFilter filter);
}
