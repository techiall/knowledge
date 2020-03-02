/**
*
*  @author ZTiger
*
*/


<template>
  <div id="froala-editor">
    <div v-if="!itemExitFlag" class="NText" v-html="getText"></div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import FroalaEditor from 'froala-editor';
import { baseUrl } from '../../../../../request/http';
require('froala-editor/js/languages/zh_cn');
require('froala-editor/css/froala_editor.pkgd.min.css');
require('froala-editor/js/plugins/align.min');
require('froala-editor/js/plugins/char_counter.min');
require('froala-editor/js/plugins/align.min.js');
require('froala-editor/js/plugins/code_beautifier.min.js');
require('froala-editor/js/plugins/colors.min');
require('froala-editor/js/plugins/font_family.min');
require('froala-editor/js/plugins/font_size.min');
require('froala-editor/js/plugins/fullscreen.min');
require('froala-editor/js/plugins/image.min');
require('froala-editor/js/plugins/inline_class.min');
require('froala-editor/js/plugins/line_height.min');
require('froala-editor/js/plugins/paragraph_format.min');
require('froala-editor/js/plugins/print.min');
require('froala-editor/js/plugins/quick_insert.min');
require('froala-editor/js/plugins/table.min');
require('froala-editor/js/plugins/video.min');
require('froala-editor/js/plugins/help.min');
import $ from '../../../../../assets/jquery-vendor';

export default {
  props: ['treeNode', 'showSelectNum', 'InnerHeight', 'itemExitFlag'],
  data() {
    return {
      getDataFlag: false,
      //设置top高
      TopHeight: 190,
      //上传的内容
      editor: '',
      // 获取文本内容
      getText: '',
      // 字数统计
      charCount: '',
      // 点击moreMisc 标志位
      moreMisc: false,
      // 在小型设备
      toolbarButtonsMD: {
        moreText: {
          buttons: [
            'fontFamily',
            'fontSize',
            'bold',
            'italic',
            'underline',
            'strikeThrough',
            'textColor',
            'backgroundColor',
          ],
          buttonsVisible: 1,
        },
        moreParagraph: {
          buttons: [
            'alignLeft',
            'alignCenter',
            'formatOLSimple',
            'alignRight',
            'alignJustify',
            'formatOL',
            'formatUL',
            'paragraphFormat',
            'paragraphStyle',
            'lineHeight',
            'outdent',
            'indent',
            'quote',
          ],
          buttonsVisible: 1,
        },
        moreRich: {
          buttons: ['insertImage', 'insertVideo', 'insertTable', 'insertHR'],
          buttonsVisible: 1,
        },
        moreMisc: {
          buttons: [
            'saveSelection',
            'undo',
            'redo',
            'fullscreen',
            'print',
            'spellChecker',
            'selectAll',
            'html',
            'help',
          ],
          align: 'right',
          buttonsVisible: 1,
        },
      },
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
      this.getDataFlag = true;
      $('.fr-element.fr-view').empty();
      $('.fr-wrapper').css('height', this.InnerHeight - this.TopHeight + 'px');
      this.getExitTextData();
    },
    //创建 富文本
    createExitText() {
      this.editor = new FroalaEditor('#froala-editor', {
        height: this.InnerHeight - this.TopHeight,
        language: 'zh_cn',
        placeholder: '输入内容',
        toolbarButtons: {
          moreText: {
            buttons: [
              'fontFamily',
              'fontSize',
              'bold',
              'italic',
              'underline',
              'strikeThrough',
              'textColor',
              'backgroundColor',
            ],
            buttonsVisible: 8,
          },
          moreParagraph: {
            buttons: [
              'alignLeft',
              'alignCenter',
              'formatOLSimple',
              'alignRight',
              'alignJustify',
              'formatOL',
              'formatUL',
              'paragraphFormat',
              'paragraphStyle',
              'lineHeight',
              'outdent',
              'indent',
              'quote',
            ],
            buttonsVisible: 2,
          },
          moreRich: {
            buttons: ['insertImage', 'insertVideo', 'insertTable', 'insertHR'],
            buttonsVisible: 4,
          },
          moreMisc: {
            buttons: [
              'saveSelection',
              'undo',
              'redo',
              'fullscreen',
              'print',
              'spellChecker',
              'selectAll',
              'html',
              'help',
            ],
            align: 'right',
            buttonsVisible: 3,
          },
        },
        toolbarButtonsMD: this.toolbarButtonsMD,
        toolbarButtonsSM: this.toolbarButtonsMD,
        toolbarButtonsXS: this.toolbarButtonsMD,
        fontFamilySelection: true,
        fontFamily: {
          'Arial,Helvetica,sans-serif': 'Arial',
          'Impact,Charcoal,sans-serif': 'Impact',
          'Tahoma,Geneva,sans-serif': 'Tahoma',
          SimSun: '宋体',
          SimHei: '黑体',
          'Microsoft Yahei': '微软雅黑',
          KaiTi: '楷体',
          NSimSun: '新宋体',
          STHeiti: '华文黑体',
          STSong: '华文宋体',
          STXinwei: '华文新魏',
          STLiti: '华文隶书',
          STXingkai: '华文行楷',
          YouYuan: '幼圆',
          LiSu: '隶书',
        },
        shortcutsHint: false,
        imageUploadRemoteUrls: false,
        imageUploadURL: baseUrl + 'storage',
        videoUploadURL: baseUrl + 'storage',
        requestHeaders: {
          'X-XSRF-TOKEN': this.access_token,
        },
        events: {
          contentChanged: () => {
            if (this.editor.charCounter.count() !== this.charCount) {
              let $uploadId = $('#uploadFroalaExit');
              $uploadId.removeClass('Froalachange');
            }
          },
          'commands.before': (cmd) => {
            if (cmd === 'moreMisc') {
              if (this.moreMisc) {
                this.moreMisc = false;
                $('.fr-wrapper').css(
                  'height',
                  this.InnerHeight - this.TopHeight + 'px',
                );
              } else {
                this.moreMisc = true;
                $('.fr-wrapper').css(
                  'height',
                  this.InnerHeight - this.TopHeight - 49 + 'px',
                );
              }
            }
          },
        },
      });
    },
    // 自定义按钮
    customButton() {
      // 设置上传按键
      FroalaEditor.DefineIconTemplate(
        'material_upload',
        '<i id="uploadFroalaExit" class="ivu-icon ivu-icon-md-cloud-upload Froalachange"></i>',
      );
      FroalaEditor.DefineIcon('saveSelection', {
        NAME: 'upload',
        template: 'material_upload',
      });
      FroalaEditor.RegisterCommand('saveSelection', {
        title: '保存',
        focus: true,
        undo: false,
        refreshAfterCallback: false,
        callback: () => {
          let $uploadId = $('#uploadFroalaExit');
          if (
            $uploadId.hasClass('Froalachange') ||
            $uploadId.hasClass('uploadColor')
          ) {
            return;
          }
          let url = 'storage/text/' + this.treeNode.id;
          let getHtmlEdit = this.editor.html.get();
          let Reg = /(<p\s?data-f-id="pbf".+<\/p>)/g;
          getHtmlEdit = getHtmlEdit.replace(Reg, '');
          this.post_text(url, getHtmlEdit)
            .then(() => {
              this.uploadSuccesss();
            })
            .catch(() => {});
        },
      });
    },
    //获取服务器数据
    getExitTextData() {
      let url = 'storage/text/' + this.treeNode.id;
      this.get(url)
        .then((res) => {
          this.spinShow = true;
          if (res.data) {
            if (this.itemExitFlag) {
              this.editor.html.insert(res.data);
              this.charCount = this.editor.charCounter.count();
            } else {
              this.getText = res.data;
            }
          } else {
            this.getText = '作者没有再此节点编辑任何信息。';
          }
        })
        .catch(() => {
          this.spinShow = true;
        });
    },
    //设置上传成功
    uploadSuccesss() {
      let $uploadId = $('#uploadFroalaExit');
      $uploadId.removeClass('ivu-icon-md-cloud-upload');
      $uploadId.addClass('ivu-icon-md-cloud-done');
      $uploadId.addClass('uploadColor');
      setTimeout(() => {
        let $uploadId = $('#uploadFroalaExit');
        $uploadId.removeClass('ivu-icon-md-cloud-done');
        $uploadId.removeClass('uploadColor');
        $uploadId.addClass('ivu-icon-md-cloud-upload');
        $uploadId.addClass('Froalachange');
        this.charCount = this.editor.charCounter.count();
      }, 2000);
    },
  },
  watch: {
    treeNode: {
      handler(newval, oldval) {
        if (newval === '' || newval.id === oldval.id) return;
        this.getDataFlag = false;
        let $uploadId = $('#uploadFroalaExit');
        $uploadId.addClass('Froalachange');
        this.charCount = 0;
        this.moreMisc = false;
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
    itemExitFlag(val) {
      if (val) {
        this.customButton();
        this.createExitText();
      }
    },
  },
};
</script>
<style scoped>
.NText {
  padding: 20px;
  font-size: 16px;
  letter-spacing: 0.1em;
}
</style>
<style>
.Froalachange {
  color: #c5c8ce;
}
.fr-box.fr-basic .fr-element {
  padding: 10px;
  min-height: 0 !important;
}
.fr-box.fr-basic.fr-top .fr-wrapper {
  transition: height 0.5s;
  box-sizing: border-box;
}
.fr-box.fr-basic.fr-top .fr-wrapper::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}
.fr-box.fr-basic.fr-top .fr-wrapper::-webkit-scrollbar-thumb {
  background-color: #c5c8ce;
  border-radius: 5px;
}
.fr-command.fr-btn.fr-active + .fr-dropdown-menu::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}
.fr-command.fr-btn.fr-active + .fr-dropdown-menu::-webkit-scrollbar-thumb {
  background-color: #c5c8ce;
  border-radius: 5px;
}
#uploadFroalaExit {
  font-size: 20px;
}
.uploadColor {
  color: #2d8cf0;
}
.fr-wrapper > div[style*='z-index: 9999'] {
  position: absolute;
  top: -10000px;
  opacity: 0;
}
.fr-box.fr-basic .fr-element {
  margin-top: -41px;
}
.fr-toolbar {
  border: none;
}
.fr-toolbar.fr-ltr.fr-desktop.fr-top.fr-basic.fr-sticky-off {
  border: none;
}
.fr-box.fr-basic .fr-wrapper,
.second-toolbar {
  border: none;
}
</style>