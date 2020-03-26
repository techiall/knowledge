/**
*
*  @author ZTiger
*
*/


<template>
  <div class="rich-text-wrap">
    <vue-tinymce v-if="itemExitFlag" v-model="content" :setting="setting" ref="editor" />
    <div v-else class="NText" v-html="content" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import Prism from 'prismjs';

export default {
  props: [
    'treeNode',
    'showSelectNum',
    'InnerHeight',
    'itemExitFlag',
    'spinShow',
  ],
  data() {
    return {
      getDataFlag: false,
      //设置top高
      TopHeight: 190,
      // 设置
      setting: {
        // 汉化地址
        language_url: '/static/js/zh_CN.js',
        language: 'zh_CN',
        // 调整大小
        resize: false,
        // 高度
        height: '100%',
        // 隐藏右下角
        branding: false,
        // 隐藏状态栏
        statusbar: false,
        // 上传照片函数
        images_upload_handler: this.imagesUpload,
        // 菜单栏配置

        // 插件
        plugins: [
          'lists advlist insertdatetime table image paste wordcount hr print charmap save code media pagebreak searchreplace emoticons fullscreen link codesample preview',
        ],
        toolbar:
          'save |undo redo |  formatselect | bold italic forecolor backcolor | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent |  blockquote | removeformat ',
        // 保存
        save_onsavecallback: this.contentSave,
      },
      // 富文本内容
      content: '',
    };
  },
  computed: {
    ...mapGetters({
      access_token: 'getToken',
    }),
  },
  methods: {
    // 获取数据
    getExitText() {
      if (this.getDataFlag) return;
      this.$emit('update:spinShow', true);
      this.getDataFlag = true;
      this.getExitTextData();
    },
    //获取服务器数据
    getExitTextData() {
      let url = '/storage/text/' + this.treeNode.id;
      this.get(url)
        .then((res) => {
          this.$emit('update:spinShow', false);
          if (res.data) {
            this.content = res.data;
            if (!this.itemExitFlag) {
              this.$nextTick(() => {
                Prism.highlightAll();
              });
            }
          } else if (!this.itemExitFlag) {
            this.content = '作者没有再此节点编辑任何信息。';
          } else {
            this.content = '';
            this.$nextTick(() => {
              this.content = '';
              const iframeId = `#${this.$refs.editor.id}_ifr`;
              document.getElementById(
                iframeId,
              ).contentWindow.document.body.innerText = '';
            });
          }
        })
        .catch((err) => {
          window.console.log(err);
        });
    },
    // 设置上传照片
    imagesUpload(blobInfo, succFun, failFun) {
      let file = blobInfo.blob();
      let data = new FormData();
      let url = '/storage';
      data.append('file', file);
      this.post_progress(url, data)
        .then((res) => {
          succFun(res.link);
        })
        .catch((err) => {
          failFun(err.status);
        });
    },
    // 保存
    contentSave() {
      let url = `storage/text/${this.treeNode.id}`;
      let content = this.content;
      this.post_text(url, content)
        .then(() => {
          this.$Message.success('保存成功');
        })
        .catch(() => {
          this.$Message.error('保存成功失败');
        });
    },
  },
  watch: {
    treeNode: {
      handler(newval, oldval) {
        if (newval === '' || newval.id === oldval.id) return;
        this.getDataFlag = false;
        this.charCount = 0;
        this.moreMisc = false;
        this.content = undefined;
        if (this.showSelectNum === 5) {
          this.getExitText();
        }
      },
      deep: true,
    },
    showSelectNum(val) {
      if (val !== 5) return;
      this.getExitText();
    },
  },
};
</script>
<style scoped>
.rich-text-wrap {
  height: 100%;
}
.NText {
  padding: 20px;
  font-size: 16px;
  letter-spacing: 0.1em;
}
</style>
<style >
.rich-text-wrap .tox-tinymce {
  border: none;
}
</style>