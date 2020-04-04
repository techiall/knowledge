/**
 *
 *  @author ZTiger
 *
 */

import Vue from 'vue';
import vueRouter from 'vue-router';
import routes from './routes';
/**
 * 引入 TinyMCE
 */
import VueTinymce from '@packy-tang/vue-tinymce';
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
 * 导入自定义 ui
 */
import '@/assets/css/index.css';
/**
 * 引入路由组件
 */
Vue.use(Vuex);
Vue.use(ViewUI);
Vue.use(vueRouter);
Vue.use(VueTinymce);
/**
 * 路由 跳转
 */
const router = new vueRouter({
  mode: 'history',
  routes,
});

/**
 *  路由 守卫 函数
 */
router.beforeEach((to, from, next) => {
  let token = sessionStorage.getItem('access_token');
  if (to.name === 'login') {
    if (!token) next();
    else next('/project');
  } else {
    if (to.matched.some((r) => r.meta.requireAuth)) {
      if (token) {
        next();
      } else {
        next(`/login?redirect=${to.path}`);
      }
    } else {
      if (to.name === 'search') {
        if (!to.query.q) {
          next('/');
        } else {
          next();
        }
      } else {
        next();
      }
    }
  }
});

export default router;
