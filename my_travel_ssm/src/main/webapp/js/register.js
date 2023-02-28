/*
    表单校验
        1.用户名：单词字符，8-20位长度
        2.密码：单词字符，8-20位长度
        3.email: 邮箱格式
        4.姓名： 非空
        5.手机号： 手机号格式
        6.出生日期： 非空
        7.验证码： 非空
 */
//校验用户名
function checkUsername(){
    //获取用户名值
    var username = $("#username").val();
    //定义正则
    var reg_username = /^\w{8,20}$/;

    //3.判断，给出提示信息
    var flag = reg_username.test(username);
    if(flag){
        //用户名合法
        $("#username").css("border","");
    }else {
        //用户名非法
        $("#username").css("border","1px solid red");
    }
    return flag;
}

//校验密码
function checkPassword(){
    //获取密码值
    var password = $("#password").val();
    //定义正则
    var reg_password = /^\w{8,20}$/;

    //3.判断，给出提示信息
    var flag = reg_password.test(password);
    if(flag){
        //密码合法
        $("#password").css("border","");
    }else {
        //密码非法
        $("#password").css("border","1px solid red");
    }
    return flag;
}
//校验邮箱
function checkEmail(){
    //1.获取邮箱
    var email = $("#email").val();
    //定义正则
    var reg_email = /^\w+@\w+\.\w+$/;
    //判断
    var flag = reg_email.test(email);
    if(flag){
        $("#email").css("border","");
    }else {
        $("#email").css("border","1px solid red");
    }
    return flag;
}

$(function (){
    //当表单提交时，调用所有的校验方法
    $("#registerForm").submit(function () {
        //1.发送数据到服务器
        if(checkUsername() && checkPassword() && checkEmail()){
            //校验通过,发送ajax请求，提交表单的数据   username=zhangsan&password=123

            $.post("user/regist",$(this).serialize(),function(data){
                //处理服务器响应的数据  data  {flag:true,errorMsg:"注册失败"}
                if(data.flag){
                    //注册成功，跳转成功页面
                    location.href="register_ok.html";
                }else{
                    //注册失败,给errorMsg添加提示信息
                    $("#errorMsg").html(data.errorMsg);

                }
            });
        }
        //2.不让页面跳转
        return false;
        //如果这个方法没有返回值，或者返回true，则表单提交，如果放回为false，则表单不提交
    });

    //某一组件失去焦点时，调用对应的校验方法
    $("#password").blur(checkPassword);
    $("#username").blur(checkUsername);
    $("#email").blur(checkEmail);
});

