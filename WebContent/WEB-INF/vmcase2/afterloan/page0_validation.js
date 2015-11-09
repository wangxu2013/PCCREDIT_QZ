var validator = $($formName).validate({
	rules:
    { 
		checkTime:{required:true},
		remarks:{required:true}
     },
messages:
    {
		checkTime:{required:"检查时间不能为空"},
		remarks:{required:"检查结论不能为空"}
   },
	errorPlacement : function(error, element) {
		element.after(error);
		if(layout){
			layout.resizeLayout();
		}
	}
});