import api from './index';

// 全节点搜索
export const search = (data) => api.get(`/search`, data);
