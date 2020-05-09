import api from './index';

const textHeader = { headers: { 'Content-Type': 'text/plain' } };
const fileHeader = { headers: { 'Content-Type': 'multipart/form-data' } };

// 获得节点文本
export const getNodeText = (id) => api.get(`/storage/text/${id}`);
// 节点文本上传
export const updateNodeText = (id, data) => api.post(`/storage/text/${id}`, data, textHeader);
// 上传文件
export const uploadFile = (data, config) => api.post(`/storage`, data, { ...config, ...fileHeader });
