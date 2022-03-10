let questionsApp = new Vue({
    el: '#questionsApp',
    data: {
        questions: [],
        pageInfo: {},
        user: {}
    },
    methods: {
        loadQuestions: function (pageNum) {
            if (pageNum == null) {
                pageNum = 1;
            }
            $.ajax({
                url: '/v1/questions/my',
                method: "GET",
                data: {pageNum: pageNum},//向控制器的页码参数传值
                success: function (r) {
                    console.log("成功加载数据");
                    console.log(r);
                    if (r.code === OK) {
                        questionsApp.questions = r.data.list;
                        questionsApp.pageInfo = r.data;
                        //为question对象添加持续时间属性
                        questionsApp.updateDuration();
                        console.log(questionsApp.questions)
                        console.log("成功");
                    }
                }
            });
        },
        addFollow: function (followI) {
            console.log('执行了 addFollow');
            let follow = followI;
            $.ajax({
                url: "/v1/users/" + follow,
                method: 'POST',
                success: function (r) {
                    console.log(r);
                    if (r.code === OK) {
                        console.log('成功获取添加关注');
                        alert(r.message);
                    } else {
                        alert(r.message);
                    }
                }
            })
        },
        updateDuration: function () {
            let questions = this.questions;
            for (let i = 0; i < questions.length; i++) {
                addDuration(questions[i]);
            }
        },
        loadCurrentUser: function () {
            $.ajax({
                url: "/v1/users/me",
                method: "get",
                success: function (r) {
                    console.log(r)
                    if (r.code == OK) {
                        questionsApp.user = r.data;
                        console.log("当前用户");
                        console.log(questionsApp.user)
                    } else {
                        alert(r.message);
                    }
                }
            })
        },
    },
    created: function () {
        console.log("执行了方法");
        this.loadQuestions(1);
        this.loadCurrentUser();
    }
});










