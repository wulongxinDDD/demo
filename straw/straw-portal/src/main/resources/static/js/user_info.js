let userApp = new Vue({
    el: "#userApp",
    data: {
        table: {},
        user: {},
        nickname: '',
        gender: [],
        birthday: '',
        autograph: '',
        files: [],
        id: '',
        name: '',
        deptname:  '',
        deptpostname: ''


    },
    methods: {
            infoUser: function (){
                let data = {
                    empNo: this.id,
                    empName: this.name,
                    empSex: this.gender,
                    empDate: this.birthday,
                    empAddress: this.autograph,
                    empDeptName: this.deptname,
                    empDeptpostName: this.deptpostname
                }
                console.log(data);
                $.ajax({
                    url: "/v1/info",
                    method: "post",
                    data: data,
                    success: function (r){
                        if (r.code == OK){
                            alert(r.message);
                        } else {
                            alert(r.message);
                        }
                    }
                })
            },
        updateUser: function (){
            let data = {
                nickname: this.nickname,
                gender: this.gender,
                birthday: this.birthday,
                autograph: this.autograph
            }
            console.log(data);
            $.ajax({
                url: "/v1/users/updateUser",
                method: "post",
                data: data,
                success: function (r){
                    if (r.code == OK){
                        alert(r.message);
                    } else {
                        alert(r.message);
                    }
                }
            })
        },
        loadUser: function (){
            $.ajax({
                url: "/v1/users/me",
                method: "get",
                success: function (r){
                    if (r.code == OK){
                        console.log(r.data)
                        userApp.user=r.data
                    } else {
                        alert(r.message());
                    }
                }
            })
        },
        loadCurrentTable: function () {
            $.ajax({
                url: "/v1/users/table",
                method: "get",
                success: function (r) {
                    console.log(r)
                    if (r.code == OK) {
                        userApp.table = r.data;
                        console.log(userApp.table)
                    } else {
                        alert(r.message);
                    }
                }
            })
        },
        loadCurrentTableU: function () {
            let questionId = location.search;
            console.log(questionId);
            if (!questionId) {
                alert("必须制定问题ID");
                return;
            }
            questionId = questionId.substring(1);
            $.ajax({
                url: "/v1/users/table/" + questionId,
                method: "get",
                success: function (r) {
                    console.log(r)
                    if (r.code == OK) {
                        userApp.table = r.data;
                        console.log(userApp.table)
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
                        location.href = '../index.html';
                    } else {
                        alert(r.message);
                    }
                }
            })
        }
    },
    created: function () {
        // this.loadUser();
        // let questionId = location.search;
        // if (questionId.indexOf("?") != -1) {
        //     this.loadCurrentTableU();
        // } else {
        //     this.loadCurrentTable();
        // }
    }
})