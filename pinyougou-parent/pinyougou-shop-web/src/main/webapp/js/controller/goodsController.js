 //控制层 
app.controller('goodsController' ,function($scope,$controller,uploadService  ,goodsService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//添加
	$scope.add=function(){	
		
		$scope.entity.goodsDesc.introduction=editor.html();

		goodsService.add( $scope.entity  ).success(
			function(response){
				if(response.success){
					alert("保存成功");
					$scope.entity={};
					// 清空富文本编辑器内容
					editor.html("");
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
    
	// 上传图片
	$scope.upload=function(){
		uploadService.uploadFile().success(
				function(response){
					alert(response.message);
					if(response.success){
						
						$scope.image_entity.url=response.message;
					}else{
						alert(response.message);
					}
				}
		)
	}
	
	
	// 定义页面实体结构
	$scope.entity={goods:{},goodsDesc:{itemImages:[]}};
	
	// 在实体结构里面添加图片实体
	$scope.add_image_entity=function(){
		
		$scope.entity.goodsDesc.itemImages.push($scope.image_entity);
		
	}
	
	// 删除图片实体
	$scope.dele_image_entity=function(index){
		$scope.entity.goodsDesc.itemImages.splice(index,1);
	}
	
});	
