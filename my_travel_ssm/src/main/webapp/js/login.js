
$(function (){
    //1.给登录按钮绑定单机事件
    $("#btn_sub").click(function () {
        //发送ajax请求，提交表单数据
        $.post("user/login",$("#loginForm").serialize(),function (data) {
            //data : {flag: false,errorMsg: ''}
            if(data.flag){
                //登录成功
                location.href="index.html";
            }else {
                //登录失败
                $("#errorMsg").html(data.errorMsg);
            }
        })
    })
})
