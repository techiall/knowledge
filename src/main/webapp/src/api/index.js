/**
 * @author zTiger
 *
 */

import axios from 'axios';
import showStatus from '@/utils/messages.js';

const api = axios.create({
    baseURL: process.env.VUE_APP_API_URL,
    timeout: 20000,
});

// 设置默认请求头
api.defaults.headers['Content-Type'] = 'application/x-www-form-urlencoded';

/**
 * 请求拦截
 */
api.interceptors.request.use(
    (config) => {
        const configure = config;
        configure.headers.AUTHORIZATION = sessionStorage.getItem('access_token');
        // configure.headers['if-non-match'] =
        return configure;
    },
    (error) => Promise.reject(error),
);

/**
 * 响应拦截
 */
api.interceptors.response.use(
    (response) => {
        // 对响应数据做点什么
        const {data, code, msg, link} = response.data;

        if (code === 200 && msg === 'OK') {
            return data || link;
        } else {
            showStatus();
            return Promise.reject(2004);
        }
    },
    (error) => {
        const status = error.response.status;
        showStatus(status);
        return Promise.reject(status);
    },
);

// 处理 get 请求
const get = (url, params, config = {}) => api.get(url, {...config, params});
// 处理 post 请求
const post = (url, params, config = {}) => api.post(url, params, config);
// 处理 delete 请求，为了防止和关键词delete冲突，方法名定义为deletes
const deletes = (url, params, config = {}) => api.delete(url, {...config, data: params});
// 处理 put 请求
const put = (url, params, config = {}) => api.put(url, params, config);
// 处理 patch 请求
const patch = (url, params, config = {}) => api.patch(url, params, config);

export default {
    get,
    post,
    deletes,
    put,
    patch,
};
