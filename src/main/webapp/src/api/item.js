import api from './index';

const header = { headers: { 'Content-Type': 'application/json;charset=UTF-8' } };

// 添加项目
export const itemAdd = (data) => api.post('/item', data, header);
// 获取用户的项目
export const getItem = (data) => api.get('/item', data);
// 获取分享列表
export const getShareItem = (data) => api.get('/item/share', data);
