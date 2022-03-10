let userInfoUApp = new Vue({
    el: "#userInfoUApp",
    data: {
        table: {},
        user: {},
        files: []
    },
    methods: {
        loadUser: function (){
            let username = location.search;
            if (username.indexOf("?") == -1){
                this.getUser();
                return;
            }
            username = username.substring(1);
            $.ajax({
                url: "/v1/auth/user",
                method: "get",
                data: {username: username},
                success: function (user){
                    userInfoUApp.user=user
                    userInfoUApp.loadCurrentTable();
                }
            })
        },
        loadCurrentTable: function () {
            let username = location.search;
            if (username.indexOf("?") == -1){
                username = userInfoUApp.user.username
            } else {
                username = username.substring(1);
            }
            $.ajax({
                url: "/v1/users/table/byUsername/" + username,
                method: "get",
                success: function (r) {
                    console.log(r)
                    if (r.code == OK) {
                        userInfoUApp.table = r.data;
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
        },
        getFile (e) {
            let form=new FormData();
            userInfoUApp.files = e.target.files
            console.log(userInfoUApp.files)
            let img = userInfoUApp.files[0]
            console.log(img)
            form.append("imageFile",img);
            $.ajax({
                url: "/v1/users/update/img",
                method: "post",
                data: form,
                contentType:false,
                processData:false,
                success: function (r) {
                    console.log(form)
                    alert(r);
                    window.location.reload()
                }
            })
        },
        getUser: function () {
            $.ajax({
                url: "/v1/users/me",
                method: "get",
                success: function (r) {
                    if (r.code == OK) {
                        console.log(r.data)
                        userInfoUApp.user = r.data
                        userInfoUApp.loadCurrentTable();
                    } else {
                        alert(r.message());
                    }
                }
            })
        },
    },
    created: function () {
        this.loadUser();
        }
});