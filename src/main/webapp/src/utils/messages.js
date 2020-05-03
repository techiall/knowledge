import store from '@/store';
import router from '@/router';
import { Message } from 'view-design';

const notify = (type, msg, duration = 5) => {
  Message[type]({ content: msg, duration: duration });
};

/**
 * 状态码处理函数
 */
export default (err) => {
  switch (err) {
    case 400:
      notify('error', '输入的内容错误，请重新输入!');
      break;
    case 401:
      notify('error', '账号或密码错误');
      break;
    case 404:
      notify('error', '登录失效，请重新登录');
      store.commit('delToken');
      router
        .replace({
          path: '/login',
        })
        .catch(() => {});
      break;
    case 500:
      notify('error', '系统正在维护中,请稍等');
      break;
    case 502:
      notify('error', '网络错误,请稍等');
      break;
    default:
      notify('error', '错误,请稍等');
      break;
  }
};
