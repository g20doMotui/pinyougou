//基本控制层
app.controller("baseController", function ($scope) {

    // 分页控件配置
    $scope.paginationConf = {
        currentPage: 1,// 当前页
        totalItems: 10,// 总记录数
        itemsPerPage: 10,// 每页的记录数
        perPageOptions: [10, 20, 30, 40, 50],// 分页选项
        onChange: function () {
            $scope.reloadList();
        }
    }

    // 重新发送当前页码和每页的记录数到后台请求数据
    $scope.reloadList = function () {
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    }


    $scope.selectIds = [];
    // 通过checkbox的选定状态给数组添加或删除id
    $scope.updateSelection = function ($event, id) {
        if ($event.target.checked) {// 复选框为选定状态
            $scope.selectIds.push(id);
        } else {
            var index = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(index, 1);// index代表在数组中的位置，1代表的是删除的个数

        }
    }

    //提取json字符串数据中某个属性，返回拼接字符串 逗号分隔
    $scope.jsonToString = function (jsonString, key) {
        var json = JSON.parse(jsonString);
        var value = "";
        for (var i = 0; i < json.length; i++) {
            if (i > 0) {
                value += ",";
            }
            value += json[i][key];
        }

        return value;
    }


    // 通过key查询集合中的值
	$scope.searchObjectByKey=function(list,key,keyValue){

        for(var i=0; i<list.length;i++){
            if(list[i][key]==keyValue){
                return list[i];
            }
        }

        return null;
    }

})