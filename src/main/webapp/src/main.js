/**
 *
 * @author ZTiger
 *
 */


import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import {
    delete_json,
    delete_string,
    get,
    patch_json,
    patch_string,
    post_json,
    post_progress,
    post_string,
    post_text,
    put_json
} from './request/api'

Vue.prototype.get = get;
Vue.prototype.post_json = post_json;
Vue.prototype.post_string = post_string;
Vue.prototype.post_text = post_text;
Vue.prototype.post_progress = post_progress;
Vue.prototype.patch_string = patch_string;
Vue.prototype.patch_json = patch_json;
Vue.prototype.delete_string = delete_string;
Vue.prototype.delete_json = delete_json;
Vue.prototype.put_json = put_json;


Vue.config.productionTip = false;

Vue.directive('title', {
    inserted(el, binding) {
        document.title = binding.expression;
    }
})

new Vue({
    render: h => h(App),
    store,
    router
}).$mount('#app');
