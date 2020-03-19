/**
 *
 *  @author ZTiger
 *
 */

import { baseUrl } from "./http";
import axios from "axios";
import store from "@/store";
import qs from "qs";
import router from "@/router";
import { Message } from "view-design";

// 请求时间设置;
axios.defaults.timeout = 10000;
// 设置默认请求接口
axios.defaults.baseURL = baseUrl;

/**
 * 封装 get 方法
 */
export function get(url, params = {}) {
    return new Promise((resolve, reject) => {
        axios
            .get(url, {
                params: params
            })
            .then(res => {
                resolve(res.data);
            })
            .catch(err => {
                reject(err);
            });
    });
}

/**
 * 封装 post 方法
 */
export function post_json(url, params = {}) {
    return new Promise((resolve, reject) => {
        axios
            .post(url, params, {
                headers: { "Content-Type": "application/json" }
            })
            .then(res => {
                resolve(res.data);
            })
            .catch(err => {
                reject(err);
            });
    });
}

/**
 *  封装 post form 方法
 */
export function post_string(url, params = {}) {
    return new Promise((resolve, reject) => {
        axios
            .post(url, qs.stringify(params), {
                headers: { "Content-Type": "application/x-www-form-urlencoded" }
            })
            .then(res => {
                resolve(res.data);
            })
            .catch(err => {
                reject(err);
            });
    });
}

/**
 * 封装 post text 方法
 */
export function post_text(url, params = {}) {
    return new Promise((resolve, reject) => {
        axios
            .post(url, params, { headers: { "Content-Type": "text/plain" } })
            .then(res => {
                resolve(res.data);
            })
            .catch(err => {
                reject(err);
            });
    });
}

/**
 *  封装 post 获取进度条 方法
 */
export function post_progress(url, params = {}, callback) {
    return new Promise((resolve, reject) => {
        axios
            .post(url, params, {
                headers: { "Content-Type": "multipart/form-data" },
                onUploadProgress: progressEvent => {
                    if (callback) callback(progressEvent);
                }
            })
            .then(res => {
                resolve(res.data);
            })
            .catch(err => {
                reject(err);
            });
    });
}

/**
 *  封装 patch from 方法
 */
export function patch_string(url, params = {}) {
    return new Promise((resolve, reject) => {
        axios
            .patch(url, qs.stringify(params), {
                headers: { "Content-Type": "application/x-www-form-urlencoded" }
            })
            .then(res => {
                resolve(res.data);
            })
            .catch(err => {
                reject(err);
            });
    });
}

/**
 * 封装 patch json
 */
export function patch_json(url, params = {}) {
    return new Promise((resolve, reject) => {
        axios
            .patch(url, params, {
                headers: { "Content-Type": "application/json;charset=UTF-8" }
            })
            .then(res => {
                resolve(res.data);
            })
            .catch(err => {
                reject(err);
            });
    });
}

/**
 * 封装delete from
 */
export function delete_string(url, params = {}) {
    return new Promise((resolve, reject) => {
        axios
            .delete(url, {
                data: qs.stringify(params),
                headers: { "Content-Type": "application/x-www-form-urlencoded" }
            })
            .then(res => {
                resolve(res.data);
            })
            .catch(err => {
                reject(err);
            });
    });
}

/**
 * 封装delete json
 */
export function delete_json(url, params = {}) {
    return new Promise((resolve, reject) => {
        axios
            .delete(url, {
                data: JSON.stringify(params),
                headers: { "Content-Type": "application/json;" }
            })
            .then(res => {
                resolve(res.data);
            })
            .catch(err => {
                reject(err);
            });
    });
}

/**
 *  封装 put json
 */
export function put_json(url, params = {}) {
    return new Promise((resolve, reject) => {
        axios
            .put(url, JSON.stringify(params), {
                headers: { "Content-Type": "application/json;charset=UTF-8" }
            })
            .then(res => {
                resolve(res.data);
            })
            .catch(err => {
                reject(err);
            });
    });
}

/**
 * 状态码处理函数
 */

const showStatus = err => {
    switch (err) {
        case 400:
            notify("error", "输入的内容错误，请重新输入!");
            break;
        case 401:
            notify("error", "账号或密码错误");
            break;
        case 404:
            notify("error", "登录失效，请重新登录");
            store.commit("delToken");
            router
                .replace({
                    path: "/login"
                })
                .catch(() => {});
            break;
        case 500:
            notify("error", "系统正在维护中,请稍等");
            break;
        case 502:
            notify("error", "网络错误,请稍等");
            break;
        default:
            notify("error", "错误,请稍等");
            break;
    }
};
const notify = (type, msg, duration = 5) => {
    Message[type]({ content: msg, duration: duration });
};

/**
 * 请求拦截
 */
axios.interceptors.request.use(
    // 在发送请求之前做些什么
    config => {
        if (store.state.token) {
            config.headers.Authorization = store.state.token;
        }
        return config;
    },
    // 对请求错误做些什么
    error => {
        return Promise.reject(error);
    }
);
/**
 * 添加响应拦截器
 */
axios.interceptors.response.use(
    response => {
        // 对响应数据做点什么
        const { data } = response;
        if (data.code === 200 && data.msg === "OK") {
            return response;
        } else {
            showStatus();
            return Promise.reject();
        }
    },
    error => {
        showStatus(error.response.status);
        return Promise.reject(error.response);
    }
);
