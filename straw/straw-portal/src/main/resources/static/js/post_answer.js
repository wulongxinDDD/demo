$(document).ready(function () {
    $('#summernote').summernote({
        height: 300,
        tabsize: 2,
        lang: 'zh-CN',
        placeholder: '请输入回答内容',
        callbacks: {
            //summernote内部定义好的特殊方法,会自动在用户选中图片后运行
            onImageUpload: function (files) {
                //参数是个数组,实际上数组中的第一个元素就是我们选中的图片
                let file = files[0];
                let form = new FormData();
                form.append("imageFile", file);
                $.ajax({
                    url: "/upload/image",
                    method: "post",
                    data: form,
                    cache: false,//本次提交的内容不缓存
                    contentType: false,
                    processData: false,
                    success: function (r) {
                        console.log(r);
                        if (r.code == OK) {
                            //图片上传如果成功,将图片显示在富文本编辑器中
                            let img = new Image();
                            img.src = r.message;
                            //将设置好路径的img添加到富文本编辑器中
                            $("#summernote").summernote("insertNode", img);
                        } else {
                            alert(r.message)
                        }
                    }
                })
            }
        }
    });
});


let postAnswerApp = new Vue({
    el: "#postAnswerApp",
    data: {
        message: '',
        hasError: false
    },
    methods: {
        postAnswer: function () {
            //先将错误信息的内容隐藏
            this.hasError = false;
            //获得question的id
            let questionId = location.search;
            if (!questionId) {
                this.message = "没有问题ID"
                this.hasError = true;
                return;
            }
            //去掉?
            questionId = questionId.substring(1);
            //获得讲师回答的内容
            let content = $("#summernote").val();
            if (!content) {
                this.message = "必须填写回答内容";
                this.hasError = true;
                return;
            }
            //提交数据form的声明
            let form = {
                questionId: questionId,
                content: content
            }
            //提交到ajax去新增回答
            $.ajax({
                url: "/v1/answers",
                method: "post",
                data: form,
                success: function (r) {
                    if (r.code == CREATED) {
                        //接收新增成功的answer对象
                        let answer = r.data;
                        //将这个answer对象追加到answers数组
                        answersApp.answers.push(answer);
                        //清空summernote
                        $("#summernote").summernote('reset');
                        postAnswerApp.message = r.message;
                        postAnswerApp.hasError = true;
                        //显示的消息会在3秒后自动消失
                        setTimeout(function () {
                            postAnswerApp.hasError = false;
                        }, 3000);
                    } else {
                        postAnswerApp.message = r.message;
                        postAnswerApp.hasError = true;
                    }
                }
            })
        }
    }
})