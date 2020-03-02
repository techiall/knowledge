module.exports = {
    outputDir:'./build/resources/main/static',
    configureWebpack: {
      externals: {
          vue: 'Vue',
          'vue-router': 'VueRouter',
          'vuex': 'Vuex',
          'axios': 'axios',
          'jquery': '$',
          'echarts': 'echarts'
      }
    },
    devServer:{
        proxy:{
            '/api':{
                target:"https://knowledge.pchelper666.com/api",
                changeOrigin: true,
            }
        }
    }
};
