let myApp = new Vue({
    el: "#myApp",
    data: {
        mys: [],
        pageInfo: {}
    },
    methods: {
        loadMy: function (pageNum){
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
                url: "/v1/questions/selectMy",
                method: "get",
                data: {pageNum: pageNum,usernameU: username},
                success: function (r){
                    if (r.code === OK){
                        myApp.pageInfo = r.data;
                        myApp.mys = r.data.list;
                        console.log(myApp.mys)
                    }else {
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
        deleteCollect: function (id){
            $.ajax({
                url: "/v1/users/deleteCollect/" + id,
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
    },
    created: function () {
        this.loadMy();
    }
})