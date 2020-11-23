export default [
    {
        path: '/',
        name: 'home',
        component: () => import('@/page/home/home.vue'),
    },
    {
        path: '/search',
        name: 'search',
        component: () => import('@/page/search/search.vue'),
    },
    {
        path: '/login',
        name: 'login',
        component: () => import('@/page/login/main.vue'),
    },
    {
        path: '/manage/:user/:itemId',
        name: 'manage',
        component: () => import('@/page/Backstage/manage.vue'),
        meta: {
            requireAuth: true,
        },
    },
    {
        path: '/project',
        name: 'project',
        component: () => import('@/page/project/project.vue'),
        children: [
            {
                path: '/',
                name: 'projectHome',
                component: () => import('@/page/project/projectmain/projectHome.vue'),
            },
            {
                path: 'list',
                name: 'projectList',
                component: () => import('@/page/project/projectmain/projectList.vue'),
            },
            {
                path: 'share',
                name: 'projectShare',
                component: () => import('@/page/project/projectmain/projectShare.vue'),
            },
        ],
        meta: {
            requireAuth: true,
        },
    },
    {
        path: '/user',
        name: 'user',
        component: () => import('@/page/user/user.vue'),
        meta: {
            requireAuth: true,
        },
        children: [
            {
                path: '/',
                name: 'userHome',
                component: () => import('@/page/user/userMain/userModify/userHome.vue'),
            },
            {
                path: 'nickname',
                name: 'nickName',
                component: () => import('@/page/user/userMain/userModify/nameModify.vue'),
            },
            {
                path: 'password',
                name: 'password',
                component: () => import('@/page/user/userMain/userModify/passwordModify.vue'),
            },
            {
                path: 'deleteaccount',
                name: 'deleteAccount',
                component: () => import('@/page/user/userMain/userModify/deleteaccount.vue'),
            },
        ],
    },
    {
        path: '/node/:id/',
        name: 'nodeshow',
        component: () => import('@/page/nodeshow/nodeshow.vue'),
    },
    {
        path: '*',
        name: 'nofound',
        component: () => import('@/page/noFound/nofound.vue'),
    },
];
