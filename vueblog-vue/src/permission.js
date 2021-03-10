import router from "./router";

// 路由判断登陆, 根据配置文件的参数
router.beforeEach((to, from, next) => {
    if (to.matched.some(record => record.meta.requireAuth)) {
        // 判断路由是否需要登陆权限
        const token = localStorage.getItem("token");
        console.log("--------" + token);

        if(token) {
            if (to.path === '/login') {

            } else {
                next();
            }
        } else {
            next({
                path: "/login"
            })
        }
    } else {
        next();
    }
})
