layui.use('jquery', function () {
    var $ = layui.jquery;

    //系统首页跳转
    $('#index a').on('click', function () {
        layer.msg($(this).text(), { icon: -1 });
        $('#bread a').eq(0).text(
            $(this).text()
        );
        $('#bread a').eq(1).html('');
        $('#content iframe').attr('src', 'systemIndex.htm');
    })

    //下拉菜单面包屑导航
    $('.layui-nav-child a').on('click', function () {
        layer.msg($(this).text(), { icon: -1 });
        $('#bread a').eq(0).text(
            $(this).parents('.layui-nav-child').siblings('a').text()
        );
        $('#bread a').eq(1).html(
            $(this).text()
        );
    })

    //没有子菜单的菜单 面包屑导航
    $('#choice a').on('click', function () {
        layer.msg($(this).text(), { icon: -1 });
        $('#bread a').eq(0).text(
            $(this).text()
        );
        $('#bread a').eq(1).html('');
    })

    //文章路由
    $('.layui-nav-tree li').eq(1).find('dl dd').eq(0).on('click', function () {
        $('#content iframe').attr('src', 'article/articleList.html');
    })
    $('.layui-nav-tree li').eq(1).find('dl dd').eq(1).on('click', function () {
        $('#content iframe').attr('src', 'article/category/category.html');
    })
  
 
    //系统管理路由
    $('.layui-nav-tree li').eq(2).find('dl dd').eq(0).on('click', function () {
        $('#content iframe').attr('src', 'system/admin.html');
    })
    $('.layui-nav-tree li').eq(2).find('dl dd').eq(1).on('click', function () {
        $('#content iframe').attr('src', 'system/roles.html');
    })
    $('.layui-nav-tree li').eq(2).find('dl dd').eq(2).on('click', function () {
        $('#content iframe').attr('src', 'system/port/portManage.html');
    })
    //友情链接
    $('.layui-nav-tree li').eq(3).on('click', function () {
        $('#content iframe').attr('src', 'link.html');
    })
    //网站设置
    $('.layui-nav-tree li').eq(4).on('click', function () {
        $('#content iframe').attr('src', 'config.html');
    })
    //断路监控
    $('.layui-nav-tree li').eq(5).on('click', function () {
        $('#content iframe').attr('src', 'http://localhost:9998/hystrix/');
    })
    //链路追踪
    $('.layui-nav-tree li').eq(6).on('click', function () {
        $('#content iframe').attr('src', 'http://localhost:9411/');
    })
    //接口文档
    $('.layui-nav-tree li').eq(7).on('click', function () {
        $('#content iframe').attr('src', 'http://localhost:8202/swagger-ui.html');
    })

})