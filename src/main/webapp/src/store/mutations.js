/**
*
*  @author ZTiger
*
*/

export default {
  setToken(state, token) {
    state.token = token;
  },
  setUser(state, data) {
    state.user = data;
    state.nickName = JSON.parse(state.user).nickName;
    state.updateTime = JSON.parse(state.user).updateTime;
  },
  setShowType(state, type) {
    state.userShowType = type;
    localStorage.setItem('item_showType', type);
  },
  setSearchHistory(state, history) {
    const historyArray = state.searchHistory;
    if (historyArray.length > 10) {
      historyArray.pop();
    }
    const index = historyArray.indexOf(history);
    if (index !== -1) {
      window.con
      historyArray.splice(index, 1);
    }
    historyArray.splice(0, 0, history);
    state.searchHistory = historyArray;
    localStorage.setItem('search_history', JSON.stringify(historyArray));
  },
  delSearchHistory(state, index) {
    const historyArray = state.searchHistory;
    historyArray.splice(index, 1);
    localStorage.setItem('search_history', JSON.stringify(historyArray));
  },
  delToken(state) {
    state.token = '';
    state.user = '';
    sessionStorage.clear();
  },
  setUserData(state, data) {
    const { user } = data;
    const { token } = data._csrf;
    state.token = token;
    state.user = user;
    sessionStorage.setItem('access_token', token);

  },
  modify(state, data) {
    const user = data;
    state.user = user;
  },
}