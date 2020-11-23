import api from './index';
import qs from 'qs';

const header = {headers: {'Content-Type': 'application/json;charset=UTF-8'}};


// 创建节点
export const createNode = (data) => api.post('/node', data, header);
// 删除节点
export const delNode = (id, data) => api.deletes(`/node/${id}`, qs.stringify(data));
// 删除多节点
export const delNodes = (id, data) => api.deletes(`/node?itemId=${id}`, JSON.stringify(data), header);
// 根节点列表
export const getRootNode = (data) => api.get('/node', data);
// 获得当前节点的子节点和父节点
export const getChildNode = (id) => api.get(`/node/${id}/child`);
// 根据 id 获得节点数据
export const getNode = (id) => api.get(`/node/${id}`);
// 更新节点
export const updateNode = (id, data) => api.put(`/node/${id}`, data, header);
// 模糊搜索节点
export const queryNode = (data) => api.get('/node/name', data);
// 获取力导图
export const getForce = (id) => api.get(`/node/${id}/graph`);
// 获取树图
export const getTree = (id) => api.get(`/node/${id}/link`);
// 获得节点的操作记录
export const getRecord = (id, data) => api.get(`/record/node/${id}`, data);
