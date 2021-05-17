// 创建品牌控制器
    	app.controller("brandController",function($scope,brandService,$controller){
    		// 继承baseController
    		$controller("baseController",{$scope:$scope});
    		
    		// 查询所有品牌
    		$scope.findAll=function(){
    			brandService.findAll().success(
    				function(response){
    					$scope.list=response;
    			});
       		};
       		
       		
       		
       		/* // 请求后端分页数据(已被search方法代替)
       		$scope.findPage=function(page, size){
       			$http.get("../brand/findPage.do?page="+page+"&size="+size).success(
       				function(pageResult){
       					$scope.brandList=pageResult.rows;
       					$scope.paginationConf.totalItems=pageResult.total;
       				}		
       			
       			);
       			
       		}  */
       		
       		// 添加品牌和修改品牌
       		$scope.save=function(){
       			var object = null;
       			if($scope.entity.id!=null){
       				object=brandService.update($scope.entity);
       			}else{
       				object=brandService.add($scope.entity);
       			}
       			object.success(
       				function(response){
       					if(response.success){
       						$scope.reloadList();
       					}else{
       						alert(response.message);
       					}
       				}		
       			
       			);
       		}
       		
       		// 通过id查找用户
       		$scope.findOne=function(id){
       			brandService.findOne(id).success(
       				function(response){
       					$scope.brand=response;
       				}		
       			)
       			
       		}
       		
       		
       		// 定义一个id数组
       		$scope.selectIds=[];
       		// 批量删除
    		$scope.dele=function(){
    			brandService.dele($scope.selectIds).success(
       				function(response){
       					if(response.success){
       						$scope.reloadList();
       						$scope.selectIds=[];
       					}else{
       						alert(response.message);
       					}
       				}		
       			);
       			
       		}
       		
       		// 定义搜索对象
       		$scope.searchEntity={};
       		
       		// 定义搜索方法
       		$scope.search=function(page,size){
       			brandService.search(page, size, $scope.searchEntity).success(
       				function(response){
       					$scope.list=response.rows;
       					$scope.paginationConf.totalItems=response.total;
       				}		
       			
       			)
       		}
    		
    		
    		
    	});
    