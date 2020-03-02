/**
*
*  @author ZTiger
*
*/


export default {
    // token
    token: sessionStorage.getItem('access_token') || '',
    // 用户所有信息
    user: '',
    // 高度
    headerHeight: 60,
    // 设置用户视图状态
    userShowType: localStorage.getItem('item_showType') || 'card',
    // 搜索历史
    searchHistory: JSON.parse(localStorage.getItem('search_history')) || [],
}