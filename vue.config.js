const path = require('path')

const resolve = dir => {
    return path.join(__dirname, dir)
}

module.exports = {
    chainWebpack: config => {
        config.plugins.delete('prefetch');
    },
    outputDir: './build/resources/main/static',
    devServer: {
        proxy: {
            '/api': {
                target: "https://knowledge.pchelper666.com/",
                changeOrigin: true,
            },
            '/static': {
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
    }
};
