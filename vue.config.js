const path = require('path')

const resolve = dir => {
  return path.join(__dirname, dir)
}

module.exports = {
<<<<<<< HEAD
  outputDir: './build/resources/main/static',
  devServer: {
    proxy: {
      '/api': {
        target: "https://knowledge.pchelper666.com/",
        changeOrigin: true,
      }
    }
  },
  configureWebpack: config => {
    config.externals = {
      vue: 'Vue',
      'vue-router': 'VueRouter',
      'vuex': 'Vuex',
      'axios': 'axios',
      'jquery': '$',
      'echarts': 'echarts'
=======
    chainWebpack: config => {
        config.plugins.delete('prefetch');
    },
    outputDir: './build/resources/main/static',
    devServer: {
        proxy: {
            '/api': {
                target: "https://knowledge.pchelper666.com/api",
                changeOrigin: true,
            }
        }
    },
    configureWebpack: config => {
        config.externals = {
            vue: 'Vue',
            'vue-router': 'VueRouter',
            'vuex': 'Vuex',
            'axios': 'axios',
            'jquery': '$',
            'echarts': 'echarts'
        }
        config.entry.app = './src/main/webapp/src/main.js';
        config.resolve.alias = {
            '@': resolve('./src/main/webapp/src')
        }
    },
    pages: {
        index: {
            entry: './src/main/webapp/src/main.js',
            template: './src/main/webapp/public/index.html',
            favicon: './src/main/webapp/public/favicon.ico',
            // 在 dist/index.html 的输出
            filename: 'index.html',
        },
>>>>>>> 70677c27101deb7aca0e37f8482053118da734a0
    }
    config.entry.app = './src/main/webapp/src/main.js';
    config.resolve.alias = {
      '@': resolve('./src/main/webapp/src')
    }
  },
  chainWebpack: config => {
    // 移除 prefetch 插件
    config.plugins.delete('prefetch');
  },
  pages: {
    index: {
      entry: './src/main/webapp/src/main.js',
      template: './src/main/webapp/public/index.html',
      favicon: './src/main/webapp/public/favicon.ico',
      // 在 dist/index.html 的输出
      filename: 'index.html',
    },
  }
};
