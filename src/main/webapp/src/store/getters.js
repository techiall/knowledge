/**
*
*  @author ZTiger
*
*/

export default {
    getToken: (state) => {
        return state.token;
    },
    getUser: (state) => {
        return state.user || '';
    },
    getTopHeigt: (state) => {
        return state.headerHeight;
    },
    getnickName: (state) => {
        return state.user.nickName || '';
    },
    getuserName: (state) => {
        return state.user.userName || '';
    },
    getImageSrc: (state) => {
        return state.user.images || '';
    },
    getUpdateTime: (state) => {
        return state.user.updateTime || '';
    },
    getShowType: (state) => {
        return state.userShowType;
    },
    getSearcHistory: (state) => {
        return state.searchHistory
    }
}