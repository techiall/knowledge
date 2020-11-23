import api from './index';
import qs from 'qs';

const header = {headers: {'Content-Type': 'application/json;charset=UTF-8'}};

// 获取当前用户信息
export const getUserMe = () => api.get('/user/me');
// 删除自己的账号
export const deleteUser = () => api.deletes('/user/me');
// 修改自己密码
export const patchPassword = (data) => api.put('/user/me/password', qs.stringify(data));
// 修改昵称 / 头像
export const patchUser = (data) => api.put('/user/me', data, header);
// 登录
export const userLogin = (data) => api.post('/user/login', qs.stringify(data));
// 注册
export const userRegister = (data) => api.post('/register', data, header);
// 判断用户名是否存在
export const userRegisterQuery = (data) => api.get('/register/query', data);
// 注销
export const userLogout = () => api.post('/user/logout');
