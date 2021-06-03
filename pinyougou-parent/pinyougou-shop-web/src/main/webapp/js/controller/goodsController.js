//控制层
app.controller('goodsController', function ($scope, $controller, uploadService, itemCatService, goodsService
                    ,typeTemplateService, $location) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        goodsService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        goodsService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体，回显到页面
    $scope.findOne = function () {

        var id = $location.search()["id"];


        if(id==null){
            return;
        }

        goodsService.findOne(id).success(
            function (response) {
                $scope.entity = response;
                // 给富文本框赋值
                editor.html(response.goodsDesc.introduction );
                // 给图片列表赋值
                $scope.entity.goodsDesc.itemImages=JSON.parse(response.goodsDesc.itemImages);
                // 给扩展属性赋值
                $scope.entity.goodsDesc.customAttributeItems=JSON.parse(response.goodsDesc.customAttributeItems);
                // 给规格列表赋值
                $scope.entity.goodsDesc.specificationItems = JSON.parse(response.goodsDesc.specificationItems);
                // 给sku列表赋值
                for (var i=0;i<response.itemList.length; i++) {

                    $scope.entity.itemList[i].spec=JSON.parse(response.itemList[i].spec);
                }
            }
        );
    }

    //保存
    $scope.save=function(){
        $scope.entity.goodsDesc.introduction = editor.html();
        var serviceObject;//服务层对象
        if($scope.entity.goods.id!=null){//如果有ID
            serviceObject=goodsService.update( $scope.entity ); //修改
        }else{
            serviceObject=goodsService.add( $scope.entity  );//增加
        }
        serviceObject.success(
            function(response){
                if(response.success){
                    alert("保存成功");
                   location.href="goods.html";
                }else{
                    alert(response.message);
                }
            }
        );
    }



    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        goodsService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        goodsService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    $scope.image_entity={};

    // 上传图片
    $scope.upload = function () {
        uploadService.uploadFile().success(
            function (response) {

                if (response.success) {

                    $scope.image_entity.url = response.message;
                } else {
                    alert(response.message);
                }
            }
        )
    }


    // 定义页面实体结构
    $scope.entity = {goods: {}, goodsDesc: {itemImages: [],specificationItems:[]}};

    // 在实体结构里面添加图片实体
    $scope.add_image_entity = function () {

        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);

    }

    // 删除图片实体
    $scope.dele_image_entity = function (index) {
        $scope.entity.goodsDesc.itemImages.splice(index, 1);
    }


    // 查询一级分类列表
    $scope.selectItemCat1List = function () {

        itemCatService.findByParentId(0).success(
            function (response) {
                $scope.itemCat1List = response;
            }
        )

    }

    // 查询二级分类列表
    $scope.$watch("entity.goods.category1Id",function(newValue, oldValue){

        itemCatService.findByParentId(newValue).success(
            function (response) {

                $scope.itemCat2List = response;
            }
        )

    })

    // 查询三级分类列表
    $scope.$watch("entity.goods.category2Id",function(newValue, oldValue){


        itemCatService.findByParentId(newValue).success(
            function (response) {

                $scope.itemCat3List = response;
            }
        )


    })

    // 三级分类后查询模板id
    $scope.$watch("entity.goods.category3Id",function(newValue,oldValue){

        itemCatService.findOne(newValue).success(
            function(response){
                $scope.entity.goods.typeTemplateId=response.typeId;
            }
        )

    })

    // 读取模板id后读取品牌列表,读取模板的扩展属性赋值给goodsDesc的扩展属性
    $scope.$watch("entity.goods.typeTemplateId",function(newValue,odlValue){

        typeTemplateService.findOne(newValue).success(
            function(response){

                $scope.typeTemplate = response;

                $scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);

                if($location.search()["id"]==null){
                    $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);
                }

            }
        );

        // 返回规格列表
        typeTemplateService.findSpecList(newValue).success(
            function(response){
                //
                $scope.specList=response;
            }
        )
    })


    // 将用户选中的规格选项保存在tb_goods_desc表的specification_items

    $scope.updateSpecificationAttribute=function($event,name, value){

        // specificationItems格式 [{"attributeName":"网络制式","attributeValue":["移动3G","移动4G"]},
        //                          {"attributeName":"屏幕尺寸","attributeValue":["6寸","5.5寸"]}]
        var object = $scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems,"attributeName",name);

        if(object!=null){
            if ($event.target.checked){
                object.attributeValue.push(value);
            }else{
                // 取消勾选
                object.attributeValue.splice(object.attributeValue.indexOf(value),1);
                if (object.attributeValue.length==0){
                    $scope.entity.goodsDesc.specificationItems.splice($scope.entity.goodsDesc.specificationItems.indexOf(object),1);
                }
            }

        }else{
            $scope.entity.goodsDesc.specificationItems.push({"attributeName":name,"attributeValue":[value]});
        }


    }

    //创建SKU列表
    $scope.createItemList=function() {
        $scope.entity.itemList = [{spec: {}, price: 0, num: 99999, status: '0', isDefault: '0'}];//初始

        //[{"attributeName":"网络制式","attributeValue":["移动3G","移动4G"]}, {"attributeName":"屏幕尺寸","attributeValue":["6寸","5.5寸"]}]
        var items = $scope.entity.goodsDesc.specificationItems;

        for (var i=0; i<items.length; i++){
            $scope.entity.itemList = addColumn($scope.entity.itemList,items[i].attributeName, items[i].attributeValue);
        }
    }

    // 添加列值
    addColumn=function(list, columnName, columnValues){
        var newList = [];
        for(var i=0; i<list.length; i++){
            var oldRow = list[i];
            for(var j=0; j<columnValues.length; j++){
                // 深克隆
                var newRow = JSON.parse(JSON.stringify(oldRow));
                newRow.spec[columnName]=columnValues[j];
                newList.push(newRow);

            }
        }

        return newList;

    }

    // 定义商品审核状态数组用于页面显示
    $scope.status=["未审核","审核通过","审核未通过","关闭"];


    // 定义商品分类数组
    $scope.itemCatList=[];
    // 查询所有商品分类列表
    $scope.findItemCatList=function(){
        itemCatService.findAll().success(
            function(response){
                for (var i=0;i<response.length;i++){
                    $scope.itemCatList[response[i].id]=response[i].name;
                }
            }
        )
    }


    // 规格选项勾选确认
    $scope.checkAttributeValue=function(specName,optionName){
        // [{"attributeName":"网络制式","attributeValue":["移动3G","移动4G","联通3G"]},{"attributeName":"屏幕尺寸","attributeValue":["5.5寸","6寸"]}]
        var items = $scope.entity.goodsDesc.specificationItems;
        var object = $scope.searchObjectByKey(items, "attributeName",specName);

        if(object != null){
            if(object.attributeValue.indexOf(optionName)>=0){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }

    }




});	
