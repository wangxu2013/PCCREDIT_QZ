/*    */ package com.dcfs.esb.server.service;
/*    */ 
/*    */ import com.cardpay.pccredit.ipad.web.IpadCommonController;
import com.dcfs.esb.config.ServerConfig;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.util.spring.Beans;

import java.util.Properties;
/*    */ 
/*    */ public class ServiceFactory
/*    */ {
/*    */   private static final String serviceImpl = "service.impl";
/* 26 */   private static Service service = null;
/*    */ 
/*    */   public static Service getService()
/*    */   {
/* 34 */     if (service == null) {
/* 35 */       getInstance();
/*    */     }
/* 37 */     return service;
/*    */   }
/*    */ 
/*    */   private static synchronized void getInstance()
/*    */   {
/* 46 */     if (service != null) {
/* 47 */       return;
/*    */     }
/*    */ 
/* 50 */     Properties prop = ServerConfig.getConfig();
/*    */ 
/* 52 */     String serviceName = prop.getProperty("service.impl");
/*    */     try
/*    */     {
/* 55 */       service = (Service)Beans.get(IpadCommonController.class);
/*    */     
/*    */     } catch (Exception e) {
/* 61 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\QZCB\桌面\esb_client.jar
 * Qualified Name:     com.dcfs.esb.server.service.ServiceFactory
 * JD-Core Version:    0.6.2
 */