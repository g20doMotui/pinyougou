// 品牌服务层
app.service("brandService",function($http){
	
	// 查询所有品牌
	this.findAll=function(){
		return $http.get("../brand/findAll.do");
	}
	
	// 添加品牌
	this.add=function(entity){
		return $http.post("../brand/add.do",entity);
	}
	
	// 修改品牌
	this.update=function(entity){
		return $http.post("../brand/update.do",entity);
	}
	
	// 通过id查找用户
	this.findOne=function(id){
		return $http.get("../brand/findOne.do?id="+id);
	}
	
	// 批量删除
	this.dele=function(selectIds){
		return $http.get("../brand/delete.do?ids="+selectIds);
	}
	
	// 条件搜索
	this.search=function(page,size,searchEntity){
		return $http.post("../brand/search.do?page="+page+"&size="+size,searchEntity);
	}
	
	// 下拉列表数据
	this.selectOptionList=function(){
		return $http.get("../brand/selectOptionList.do");
	}
	
})