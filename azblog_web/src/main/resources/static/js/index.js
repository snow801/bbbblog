
layui.use(['element', 'jquery', 'layer', 'laytpl'], function () {
    var element = layui.element;
    var $ = layui.jquery;
    var layer = layui.layer;
    var laytpl = layui.laytpl;
    var storage = window.localStorage;
    var webSocket = null;

    //验证token
    $.ajax({
        url: 'http://localhost:8202/admin/admin/refreshToken',
        type: 'get',
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        headers: {
            Authorization: storage.getItem('token')
        },
        success: function (data) {
            if (data.status == '503') {
                storage.removeItem('token');
                layer.open({
                    content: '身份认证失败，请重新登录',
                    yes: function (index, layero) {
                        layer.close(index);
                        location.href = 'http://localhost:8888/';
                    }
                });
            }
        }
    })

    //显示用户名
    $.ajax({
        url: 'http://localhost:8202/admin/admin/getUsername',
        type: 'get',
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        headers: {
            Authorization: storage.getItem('token')
        },
        success: function (data) {
            $('#username').text(data.data)
        }
    })

    //刷新token
    setInterval(function () {
        $.ajax({
            url: 'http://localhost:8202/admin/refreshToken',
            type: 'get',
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            headers: {
                Authorization: storage.getItem('token')
            },
            success: function (data) {
                if (data.status == '500') {
                    storage.removeItem('token');
                    layer.open({
                        content: '身份过期，请重新登录',
                        yes: function (index, layero) {
                            layer.close(index);
                            location.href = 'http://localhost:8888/';
                        }
                    });
                }
            }
        })
    }, 1000 * 60 * 24)

    //退出登录
    $('#loginOut').click(function () {
        storage.removeItem('token');
        location.href = 'http://localhost:8888/'
    })


    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        webSocket = new WebSocket('ws://localhost:8207/webSocket');
        console.log("当前浏览器支持WebSocket")
    } else {
        console.log("当前浏览器不支持WebSocket");
    }

    webSocket.οnerrοr = function () {
        console.log("WebSocket连接发生错误！");
    }

    webSocket.onopen = function () {
        console.log("WebSocket连接成功！")
    }
});
