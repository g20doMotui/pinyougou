app.controller("indexController",function($scope, loginService){

    // 读取当前登录用户名
    $scope.showLoginName=function () {
        loginService.showName().success(
            function(response){
                console.log(response);
                $scope.loginName=response.loginName;
            }
        )
    }




})