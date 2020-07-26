layui.use(['table', 'jquery', 'form', 'layedit', 'layer', 'laydate'], function () {
    var table = layui.table;
    var storage = window.localStorage;
    var $ = layui.jquery;
    var layer = layui.layer;
    var form = layui.form;

    table.render({
        elem: '#list'
        , url: 'http://localhost:8202/admin/admin/'
        , toolbar: '#toolbarTop' //开启头部工具栏，并为其绑定左侧模板
        , title: '数据表'
        , headers: {
            Authorization: storage.getItem('token')
        }
        , cols: [[
            { type: 'checkbox', fixed: 'left' }
            , { field: 'id', title: 'ID', width: 60 }
            , { field: 'username', title: '账号', width: 180, fedit: 'text', unresize: true, sort: true }
            , { field: 'sex', title: '性别', width: 60 }
            , { field: 'email', title: '邮箱', width: 180 }
            , {
                field: 'roles', title: '角色', width: 150, templet: function (res) {
                    var roles = [];
                    $.each(res.roles, function (i, items) {
                        roles.push(items.role_name);
                    })
                    return roles;
                }
            }
            , { field: 'createTime', title: '创建时间', width: 150 }
            , {
                field: 'status', title: '状态', width: 100, templet: function (res) {
                    return res.status == "0" ? "锁定" : "正常"
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
    table.on('toolbar(test)', function (obj) {
        var checkStatus = table.checkStatus(obj.config.id);
        switch (obj.event) {//添加用户
            case 'add':
                layer.open({
                    title: '添加用户',
                    type: 2,
                    shade: false,
                    area: ['430px', '420px'],
                    offset: ['25px', '25%'],
                    content: 'addAdmin.html',
                    shade: [0.3, '#393D49'], //遮罩层
                    zIndex: layer.zIndex,
                    success: function (layero) {
                        layer.setTop(layero); //设置窗口为最顶层
                    }
                });
                break;
        };
    });

    //监听行工具事件
    table.on('tool(test)', function (obj) {
        var data = obj.data;
        if (obj.event === 'del') {//删除用户
            layer.open({
                content: '确定要删除该用户？',
                yes: function (index, layero) {
                    layer.close(index);
                    $.ajax({
                        url: 'http://localhost:8202/admin/admin/' + data.id,
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
                }
            });

        } else if (obj.event === 'edit') {//编辑用户
            $.ajax({
                url: 'http://localhost:8202/admin/admin/' + data.id,
                type: 'get',
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                headers: {
                    Authorization: storage.getItem('token')
                },
                success: function (res) {
                    if (res.status == '200') {
                        layer.open({
                            title: '编辑用户',
                            type: 2,
                            shade: false,
                            area: ['430px', '420px'],
                            offset: ['25px', '25%'],
                            content: 'editAdmin.html',
                            shade: [0.3, '#393D49'], //遮罩层
                            zIndex: layer.zIndex,
                            success: function (layero) {
                                layer.setTop(layero); //设置窗口为最顶层
                                storage.setItem('adminId', data.id);
                            }
                        });
                    } else {
                        layer.msg(res.message);
                    }
                },
            });

        } else if (obj.event === 'resetPwd') {//重置密码
            layer.open({
                content: '确定要重置密码吗，初始密码为123456',
                yes: function (index, layero) {
                    layer.close(index);
                    $.ajax({
                        url: 'http://localhost:8202/admin/admin/resetPwd/' + data.id,
                        type: 'put',
                        dataType: "json",
                        contentType: "application/json;charset=utf-8",
                        headers: {
                            Authorization: storage.getItem('token')
                        },
                        success: function (data) {
                            if (data.status == '200') {
                                layer.msg(data.message);
                            }
                        }
                    })
                    table.reload('list', {});
                }
            });
        }
    });

    //搜索
    form.on('submit(formDemo)', function (data) {
        var keyword = $('.searchContent').val();
        console.log(keyword);
        table.reload('list', {
            url: 'http://localhost:8202/admin/admin/search/' + keyword
        });
        return false;
    })

    //重置
    $('#searchBtn button').eq(1).click(function () {
        table.reload('list', {
            url: 'http://localhost:8202/admin/admin/'
        });
    })
});