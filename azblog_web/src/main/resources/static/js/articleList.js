layui.use(['table', 'jquery', 'layer', 'form', 'table', 'layedit', 'laydate'], function () {
    var table = layui.table;
    var $ = layui.jquery;
    var layer = layui.layer;
    var storage = window.localStorage;

    table.render({
        elem: '#list'
        , url: 'http://localhost:8202/article/article'
        , toolbar: '#toolbarDemo'
        , title: '用户数据表'
        , headers: {
            Authorization: storage.getItem('token')
        }
        , cols: [[
            { type: 'checkbox', fixed: 'left' }
            , { field: 'id', title: 'id', width: 100, fedit: 'text', unresize: true, sort: true }
            , { field: 'username', title: '发布者', width: 100 }
            , { field: 'title', title: '文章标题', width: 100 }
            , { field: 'intro', title: '文章简介', width: 100 }
            , {
                field: 'category_id', title: '分类', width: 100, templet: function (res) {
                    var name = '';
                    $.ajax({
                        url: 'http://localhost:8202/category/category/' + res.category_id,
                        type: 'get',
                        dataType: "json",
                        async: false,
                        contentType: "application/json;charset=utf-8",
                        headers: {
                            Authorization: storage.getItem('token')
                        },
                        success: function (data) {
                            name = data.data
                        },

                    })
                    return name
                }
            }
            , { field: 'updateTime', title: '更新时间', width: 110, sort: true }
            , {
                field: 'status', title: '状态', width: 80, sort: true, templet: function (res) {
                    return res.status == "1" ? "通过" : "待审"
                }
            }
            , {
                field: 'recommended', title: '是否推荐', width: 100, templet: function (res) {
                    return res.recommended == "1" ? "是" : "否"
                }
            }
            , {
                field: 'is_original', title: '是否原创', width: 100, templet: function (res) {
                    return res.is_original == "1" ? "是" : "否"
                }
            }
            , { fixed: 'right', title: '操作', toolbar: '#barDemo' }
        ]]
        , page: {
            layout: ['count', 'prev', 'page', 'next', 'skip'] //自定义分页布局
            , groups: 5 //显示的连续页码
            , first: false //不显示首页
            , last: false //不显示尾页
        }
        , parseData: function (res) { //res 即为原始返回的数据
            return {
                "code": res.status, //解析接口状态
                "msg": res.message, //解析提示文本
                "data": res.data,
                "count": res.total, 
            }
        }
    });


    //头工具栏事件
    table.on('toolbar(tool)', function (obj) {
        var checkStatus = table.checkStatus(obj.config.id);
        switch (obj.event) {
            case 'add':   //添加按钮
                layer.open({
                    title: '添加文章',
                    type: 2,
                    shade: false,
                    area: ['100%', '100%'],
                    content: 'addArticle.html',
                    zIndex: layer.zIndex,
                    success: function (layero) {
                        layer.setTop(layero); //设置窗口为最顶层
                    }
                });
                break;
            case 'delMore'://批量删除
                var data = checkStatus.data;
                var ids = [];
                $.each(data, function (i, item) {
                    ids.push(item.id);
                })
                layer.msg(JSON.stringify(ids))
                $.ajax({
                    url: 'http://localhost:8202/article/article/' + ids,
                    type: 'delete',
                    dataType: "json",
                    contentType: "application/json;charset=utf-8",
                    headers: {
                        Authorization: storage.getItem('token')
                    },
                    success: function (data) {
                        if (data.status == '200') {
                            layer.msg(data.message);
                            table.reload('list', {});
                        } else {
                            layer.msg(data.message);
                        }
                    },
                })
                break;

        };
    });

    table.on('tool(tool)', function (obj) {
        var data = obj.data;
        if (obj.event === 'del') { //单个删除
            $.ajax({
                url: 'http://localhost:8202/article/article/' + data.id,
                type: 'delete',
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                headers: {
                    Authorization: storage.getItem('token')
                },
                success: function (data) {
                    if (data.status == '200') {
                        layer.msg(data.message);
                        table.reload('list', {});
                    } else {
                        layer.msg(data.message);
                    }
                }
            })

        } else if (obj.event === 'edit') {//编辑按钮
            layer.open({
                type: 2,
                shade: false,
                area: ['100%', '100%'],
                content: 'editArticle.html',
                zIndex: layer.zIndex,
                success: function (layero) {
                    layer.setTop(layero);
                    storage.setItem('articleId', data.id);
                }

            });
        }
    });

    //搜索
    $('#searchArticle').click(function () {
        var keyword = $('#searchVal').val();
        table.reload('list', {
            url: 'http://localhost:8202/article/article/' + keyword + '/1/10'
        });
    })

    //搜索重置
    $('.reset').click(function () {
        $('#searchVal').val('');
        table.reload('list', {
            url: 'http://localhost:8202/article/article',
            where: {
                page: 1,
                limit: 10
            }
        });


    })
});