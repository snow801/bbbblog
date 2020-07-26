//获取表单数据
function getFormData(classname) {
    var formData = {};
    var t = $(classname).serializeArray();
    $.each(t, function () {
        formData[this.name] = this.value;
    });
    return formData;
}