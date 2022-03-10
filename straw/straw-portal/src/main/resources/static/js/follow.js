let followApp = new Vue({
    el: "#followApp",
    data: {
        fans: [],
        pageInfo: {}
    },
    methods: {
        loadFollow: function (pageNum){
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
                url: "/v1/users/selectFollow",
                method: "get",
                data: {pageNum: pageNum,username: username},
                success: function (r){
                    if (r.code === OK){
                        followApp.pageInfo = r.data;
                        followApp.fans = r.data.list;
                        console.log(followApp.fans)
                    }else {
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
        this.loadFollow();
    }
})