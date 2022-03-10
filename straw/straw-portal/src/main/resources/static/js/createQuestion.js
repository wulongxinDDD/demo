Vue.component('v-select', VueSelect.VueSelect);
let createQuestionApp = new Vue({
    el: '#createQuestionApp',
    data: {
        title: '',
    },
    methods: {
        createQuestion: function () {
            let content = $('#summernote').val();
            console.log(content);
            //data 对象，与服务器端QuestionVo的属性对应
            let data = {
                title: this.title,
                content: content,
            };
            console.log(data);
            $.ajax({
                url: '/v1/questions',
                traditional: true,  //采用传统数组编码方式，SpringMVC才能接收
                method: 'POST',
                data: data,
                success: function (r) {
                    console.log(r);
                    if (r.code === OK) {
                        console.log(r.message);
                        location.href = 'index.html';
                    } else {
                        alert(r.message);
                        console.log(r.message);
                    }
                }
            });
        },
    },
});