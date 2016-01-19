/*    */ package com.dcfs.esb.server.service;
/*    */ 
/*    */ import com.dc.eai.data.CompositeData;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ 
/*    */ public class ServiceImpl
/*    */   implements Service
/*    */ {
/* 36 */   private static Log log = LogFactory.getLog(ServiceImpl.class);
/*    */ 
/*    */   public CompositeData exec(CompositeData data)
/*    */   {
/* 46 */     if (log.isDebugEnabled()) {
/* 47 */       log.info("[业务处理已经处理完成]");
/*    */     }
/* 49 */     return data;
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\QZCB\桌面\esb_client.jar
 * Qualified Name:     com.dcfs.esb.server.service.ServiceImpl
 * JD-Core Version:    0.6.2
 */