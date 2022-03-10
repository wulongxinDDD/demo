let blankApp = new Vue({
    el: "#blankApp",
    data: {
        fans: [],
        pageInfo: {}
    },
    methods: {
        loadBlank: function (pageNum){
            if (pageNum == null) {
                pageNum = 1;
            }
            $.ajax({
                url: "/v1/users/selectBlank",
                method: "get",
                data: {pageNum: pageNum},
                success: function (r){
                    if (r.code === OK){
                        blankApp.pageInfo = r.data;
                        blankApp.fans = r.data.list;
                        console.log(blankApp.fans)
                    }else {
                        alert(r.message);
                    }
                }
            })
        },
        deleteBlank: function (blank) {
            let username = blank;
            $.ajax({
                url: "/v1/users/deleteBlank/" + username,
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
        }
    },
    created: function () {
        this.loadBlank();
    }
})