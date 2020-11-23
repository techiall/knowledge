/**
 *
 * @author ZTiger
 *
 */

import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';

Vue.config.productionTip = false;

Vue.directive('title', {
    inserted(el, binding) {
        document.title = binding.expression;
    },
});

new Vue({
    render: (h) => h(App),
    store,
    router,
}).$mount('#app');
