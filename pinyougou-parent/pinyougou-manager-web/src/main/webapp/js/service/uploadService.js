app.service("uploadService",function($http){
	
	this.uploadFile=function(){
		var formData = new FormData();
		
		formData.append("file",file.files[0]);
		
		return $http({
			method:"post",
			url:"../upload.do",
			data:formData,
			headers:{"content-Type":undefined},
			transformRequest:angular.identity
			
			
			
			
		})
		
		
		
	}
	
	
	
	
})