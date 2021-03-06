var validator = $($formName).validate({
	rules:
    { 
		globalId:{required:true,idcard:true},
		globalDesc:{required:true},
		issDate:{required:true},
		effectDate:{required:true},
		expiryDate:{required:true},
		clientName:{required:true},
		birthDate:{required:true},
		custManagerId:{required:true},
		recordTellerNo:{required:true},
		registeredDate:{required:true},
		clientBelongOrg:{required:true},
		registeredTellerNo:{required:true},
		registOrgNo:{required:true},
		orgRegisteredDate:{required:true},
		openAcctBranchId:{required:true},
		openTellerNo:{required:true},
		openAcctDate:{required:true},
		address:{required:true},
		postalCode:{required:true},
		contactMode:{required:true},
		companyName:{required:true}
     },
messages:
    {
		globalId:{required:"证件号码不能为空"},
		globalDesc:{required:"证件描述不能为空"},
		issDate:{required:"签发日期不能为空"},
		effectDate:{required:"生效日期不能为空"},
		expiryDate:{required:"到期日期不能为空"},
		clientName:{required:"客户名称不能为空"},
		birthDate:{required:"出生日期不能为空"},
		custManagerId:{required:"客户经理代码不能为空"},
		recordTellerNo:{required:"录入柜员不能为空"},
		registeredDate:{required:"登记日期不能为空"},
		clientBelongOrg:{required:"客户所属机构不能为空"},
		registeredTellerNo:{required:"登记柜员号不能为空"},
		registOrgNo:{required:"登记机构不能为空"},
		orgRegisteredDate:{required:"登记日期不能为空"},
		openAcctBranchId:{required:"开户机构不能为空"},
		openTellerNo:{required:"开户柜员不能为空"},
		openAcctDate:{required:"开户日期不能为空"},
		address:{required:"地址不能为空"},
		postalCode:{required:"邮编不能为空"},
		contactMode:{required:"联系方式不能为空"},
		companyName:{required:"单位名称不能为空"}
   },
	errorPlacement : function(error, element) {
		element.after(error);
		if(layout){
			layout.resizeLayout();
		}
	}
});