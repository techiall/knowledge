module.exports = {
    externals: {
      'vue': 'Vue', // 左侧vue是我们自己引入时候要用的，右侧是开发依赖库的主人定义的不能修改
      'vue-router': 'VueRouter',
      'vuex': 'Vuex',
      'axios': 'axios',
    }
};
