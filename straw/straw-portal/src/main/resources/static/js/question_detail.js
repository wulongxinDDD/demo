Vue.component('v-select', VueSelect.VueSelect);
let questionApp = new Vue({
    el: "#questionApp",
    data: {
        question: {},
        user: {},
        title: '',
        isFriend: false,
        isCollect: false,
    },
    methods: {
        loadQuestion: function () {
            console.log("!");
            //从url中获得?之后的内容(包含?)  ?12
            let questionId = location.search;
            console.log(questionId);
            if (!questionId) {
                alert("必须制定问题ID");
                return;
            }
            //去掉?    ?12  -> 12
            questionId = questionId.substring(1);
            $.ajax({
                url: "/v1/questions/" + questionId,
                method: "get",
                success: function (r) {
                    console.log(r)
                    if (r.code == OK) {
                        questionApp.question = r.data;
                        addDuration(questionApp.question);
                        answersApp.loadAnswers();
                        questionApp.loadFriend();
                        questionApp.loadCollect();
                    } else {
                        alert(r.message);
                    }
                }
            })
        },
        addFollow: function (follow) {
            let username = follow;
            $.ajax({
                url: "/v1/users/" + username,
                method: 'POST',
                success: function (r) {
                    console.log(r);
                    if (r.code === OK) {
                        console.log('成功获取添加关注');
                        alert(r.message);
                        window.location.reload()
                    } else {
                        alert(r.message);
                    }
                }
            })
        },
        addCollect: function(id){
            $.ajax({
                url: "/v1/users/addCollect/" + id,
                method: 'POST',
                success: function (r) {
                    console.log(r);
                    if (r.code ==OK){
                        alert(r.message);
                        window.location.reload()
                    } else {
                        alert(r.message);
                    }
                }
            })
        },
        addBlank: function (blank) {
            let username = blank;
            $.ajax({
                url: "/v1/users/blank/" + username,
                method: "post",
                success: function (r) {
                    console.log(r)
                    if (r.code == OK) {
                        alert(r.message);
                        window.location.reload();
                    } else {
                        alert(r.message);
                    }
                }
            })
        },
        loadCurrentUser: function () {
            $.ajax({
                url: "/v1/users/me",
                method: "get",
                success: function (r) {
                    console.log(r)
                    if (r.code == OK) {
                        questionApp.user = r.data;
                        console.log(questionApp.user)
                    } else {
                        alert(r.message);
                    }
                }
            })
        },
        deleteQuestion: function (id){
            let questionId = id;
            $.ajax({
                url: "/v1/questions/delete/" + questionId,
                method: "POST",
                success: function (r){
                    console.log(r)
                    if (r.code == OK){
                        window.history.back();
                    }else {
                        alert(r.message)
                    }
                }
            })
        },
        deleteFriend: function (follow){
            let username = follow;
            $.ajax({
                url: "/v1/users/deleteFriend/" + username,
                method: "POST",
                success: function (r){
                    if (r.code == OK){
                        alert(r.message);
                        window.location.reload()
                    }else {
                        alert(r.message)
                    }
                }
            })
        },
        deleteCollect: function (){
            $.ajax({
                url: "/v1/users/deleteCollect/" + questionApp.question.id,
                method: "POST",
                success: function (r){
                    if (r.code == OK){
                        alert(r.message);
                        window.location.reload()
                    }else {
                        alert(r.message);
                    }
                }
            })
        },
        updateQuestion: function (){
            let questionId = location.search;
            let content = $('#summernote').val();
            console.log(content);
            console.log(questionId)
            questionId = questionId.substring(1);
            let data = {
                title: this.question.title,
                content: content,
            };
            $.ajax({
                url: "/v1/questions/update/" + questionId,
                traditional: true,
                method: "POST",
                data: data,
                success: function (r){
                    console.log(r)
                    if (r.code == OK){
                        location.href = "/question/detail.html?" + questionId
                    }else {
                        alert("修改失败")
                    }
                }
            })
        },
        loadFriend: function (){
            $.ajax({
                url:"/v1/users/selectFriendByQuestionId/" + questionApp.question.id,
                method: "get",
                success: function (r){
                    if (r.code == OK){
                        questionApp.isFriend = true;
                    }
                    console.log(questionApp.isFriend);
                }
            })
        },
        loadCollect: function (){
            $.ajax({
                url:"/v1/users/selectCollectByQuestionId/" + questionApp.question.id,
                method: "get",
                success: function (r){
                    if (r.code == OK){
                        questionApp.isCollect = true;
                    }
                    console.log(questionApp.isCollect);
                }
            })
        }
    },
    created: function () {
        console.log("执行了主题");
        this.loadQuestion();
        this.loadCurrentUser();
    }
})

let answersApp = new Vue({
    el: "#answersApp",
    data: {
        answers: []
    },
    methods: {
        loadAnswers: function () {
            let questionId = location.search;
            console.log(questionId);
            if (!questionId) {
                alert("必须制定问题ID");
                return;
            }
            console.log("评论查询开始执行")
            questionId = questionId.substring(1)
            $.ajax({
                url: "/v1/answers/question/" + questionId,
                methods: "get",
                success: function (r) {
                    console.log(r)
                    if (r.code == OK) {
                        answersApp.answers = r.data;
                        answersApp.updateDuration();
                    } else {
                        alert(r.message);
                    }
                }
            })
        },
        updateDuration: function () {
            for (let i = 0; i < this.answers.length; i++) {
                addDuration(this.answers[i]);
            }
        },
    },
})
let answersCreate = new Vue({
    el: "#answersCreate",
    methods: {
        createAnswer: function () {
            console.log("执行添加评论");
            let questionId = location.search;
            console.log(questionId);
            if (!questionId) {
                alert("必须制定问题ID");
                return;
            }
            questionId = questionId.substring(1)
            let content = $('#summernote').val();
            console.log(content);
            //data 对象，与服务器端QuestionVo的属性对应
            let data = {
                questionId: questionId,
                content: content,
            };
            JSON.stringify(data)
            console.log(data);
            $.ajax({
                url: '/v1/answers',
                traditional: true,  //采用传统数组编码方式，SpringMVC才能接收
                method: 'POST',
                data: data,
                success: function (r) {
                    if (r.code == OK){
                        window.location.reload()
                    }else {
                        alert(r.message)
                    }
                }
            });
        },
    }
})








