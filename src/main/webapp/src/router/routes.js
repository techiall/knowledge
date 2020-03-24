export default [
    {
        path: "/",
        name: "home",
        component: () => import("@/page/home/home.vue")
    },
    {
        path: "/search",
        name: "search",
        component: () => import("@/page/search/search.vue")
    },
    {
        path: "/login",
        name: "login",
        component: () => import("@/page/login/main.vue")
    },
    {
        path: "/manage/:user/:itemId",
        name: "manage",
        component: () => import("@/page/Backstage/manage.vue"),
        meta: {
            requireAuth: true
        }
    },
    {
        path: "/project",
        name: "project",
        component: () => import("@/page/project/project.vue"),
        children: [
            {
                path: "/",
                name: "home",
                component: () =>
                    import("@/page/project/projectmain/projectHome.vue")
            },
            {
                path: "list",
                name: "list",
                component: () =>
                    import("@/page/project/projectmain/projectList.vue")
            },
            {
                path: "share",
                name: "share",
                component: () =>
                    import("@/page/project/projectmain/projectShare.vue")
            }
        ],
        meta: {
            requireAuth: true
        }
    },
    {
        path: "/user",
        name: "user",
        component: () => import("@/page/user/user.vue"),
        meta: {
            requireAuth: true
        }
    },
    {
        path: "/node/:id/",
        name: "nodeshow",
        component: () => import("@/page/nodeshow/nodeshow.vue")
    },
    {
        path: "*",
        name: "nofound",
        component: () => import("@/page/noFound/nofound.vue")
    }
];
