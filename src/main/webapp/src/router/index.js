/**
 *
 *  @author ZTiger
 *
 */


import Vue from 'vue'
import vueRouter from 'vue-router';

/**
 *  引入 view UI
 */
import ViewUI from 'view-design';
import 'view-design/dist/styles/iview.css';

/**
 * 引入 vuex
 */
import Vuex from 'vuex';

/**
 *  引入 axios
 */
import axios from 'axios';

/**
 * 导入自定义 ui
 */
import '@/assets/css/index.css';
/**
 * 引入路由组件
 */


Vue.use(Vuex);
Vue.use(ViewUI);
Vue.use(vueRouter);
Vue.prototype.axios = axios;
/**
 * 路由 跳转
 */
let router = new vueRouter({
	mode: 'history',
	routes: [{
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
			requireAuth: true
		}
	},
	{
		path: '/project',
		name: 'project',
		component: () => import('@/page/project/project.vue'),
		children: [
			{
				path: '/',
				name: 'home',
				component: () => import('@/page/project/projectmain/projectHome.vue'),
			},
			{
				path: 'list',
				name: 'list',
				component: () => import('@/page/project/projectmain/projectList.vue'),
			},
			{
				path: 'share',
				name: 'share',
				component: () => import('@/page/project/projectmain/projectShare.vue'),
			},
		],
		meta: {
			requireAuth: true
		}
	},
	{
		path: '/user',
		name: 'user',
		component: () => import('@/page/user/user.vue'),
		meta: {
			requireAuth: true
		}
	},
	{
		path: '/node/:id/:name',
		name: 'nodeshow',
		component: () => import('@/page/nodeshow/nodeshow.vue'),
	},
	{
		path: '*',
		name: 'nofound',
		component: () => import('@/page/noFound/nofound.vue')
	}
	]
})


/**
 *  路由 守卫 函数
 */
router.beforeEach((to, from, next) => {
	if (to.path === '/') {
		next();
	} else {
		let token = sessionStorage.getItem('access_token');
		if (to.matched.some(r => r.meta.requireAuth)) {
			if (token) {
				next();
			} else {
				next('/login');
			}
		} else {
			if (to.name === 'search') {
				if (!to.query.q) {
					next('/')
				} else {
					next();
				}
			} else {
				next();
			}
		}
	}
})


export default router