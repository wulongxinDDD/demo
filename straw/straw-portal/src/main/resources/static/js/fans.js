let fansApp = new Vue({
    el: "#fansApp",
    data: {
        fans: [],
        pageInfo: {}
    },
    methods: {
        loadFans: function (pageNum){
            if (pageNum == null) {
                pageNum = 1;
            }
            let username = location.search;
            console.log(username);
            if (!username) {
                alert("必须有用户名");
                return;
            }
            username = username.substring(1);
            $.ajax({
                url: "/v1/users/selectFans",
                method: "get",
                data: {pageNum: pageNum,username: username},
                success: function (r){
                    if (r.code === OK){
                        fansApp.pageInfo = r.data;
                        fansApp.fans = r.data.list;
                        console.log(fansApp.fans)
                    }else {
                        alert(r.message);
                    }
                }
            })
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
        }
    },
    created: function () {
        this.loadFans();
    }
})