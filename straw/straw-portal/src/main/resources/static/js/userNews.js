let userNewsApp = new Vue({
    el: "#userNewsApp",
    data: {
        user: {},
        news: [],
        pageInfo: {},
        total: ""
    },
    methods: {
        loadUser: function () {
            $.ajax({
                url: "/v1/users/me",
                method: "get",
                success: function (r) {
                    if (r.code == OK) {
                        console.log(r.data)
                        userNewsApp.user = r.data
                    } else {
                        alert(r.message);
                    }
                }
            })
        },
        loadNews: function (){
            $.ajax({
                url: "/v1/questions/news",
                method: "get",
                success: function (r){
                    if (r.code == OK){
                        userNewsApp.news = r.data;
                        console.log(r.data)
                        userNewsApp.total = userNewsApp.news.length
                    } else {
                        alert(r.message);
                    }
                }
            })
        },
    },

    created: function () {
        this.loadUser();
        this.loadNews();
    }
})