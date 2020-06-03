/**
*
*  @author ZTiger
*
*/


<template>
  <div>
    <Modal v-model="ModalFalg" width="650" :styles="{top: '50px'}" :footer-hide="true">
      <p slot="header" class="header center">
        <span>项目设置</span>
      </p>
      <div class="item-modal-main scroll">
        <div class="item-modal-title">项目封面</div>
        <div class="g-upload-wrap">
          <img class="g-upload-img" :src="submitMsg.image" />
          <span class="g-upload-button" v-if="status||user.userName === submitMsg.userName">
            <input type="file" title ref="FileMsgRef" @change="uploadImage" />
            <span>上传新封面</span>
          </span>
        </div>
        <div class="item-modal-title">项目作者</div>
        <div class="item-border">{{submitMsg.nickName}}</div>
        <div class="item-modal-title">项目名称</div>
        <Input
          ref="NameI"
          placeholder="项目名称"
          maxlength="20"
          v-if="status||user.userName === submitMsg.userName"
          v-model="submitMsg.name"
          @on-change="changeEvent"
        />
        <div v-else class="item-border">{{submitMsg.name}}</div>
        <div class="item-modal-title">项目描述</div>
        <Input
          class="textarea-i"
          type="textarea"
          maxlength="200"
          placeholder="项目描述..."
          v-if="status||user.userName === submitMsg.userName"
          v-model="submitMsg.description"
          show-word-limit
          :autosize="{minRows: 2,maxRows: 6}"
          @on-change="changeEvent"
        />
        <div v-else class="item-border">{{submitMsg.description||'无'}}</div>
        <div class="item-modal-title">项目公开性</div>
        <Select
          v-if="status||user.userName === submitMsg.userName"
          v-model="submitMsg.share"
          @on-change="changeEvent"
        >
          <Option v-for="item in shareList" :value="item.value" :key="item.value">{{ item.label }}</Option>
        </Select>
        <div v-else class="item-border">公开项目 (所有成员可见，仅项目成员可编辑)</div>
        <div class="item-modal-title">项目创建时间</div>
        <div>{{submitMsg.createTime|TimeConversion}}</div>
        <div class="item-modal-title">项目最近修改时间</div>
        <div>{{submitMsg.updateTime|TimeConversion}}</div>
      </div>
      <div class="item-modal-footer">
        <Button type="text" @click="ModalFalg = false">取消</Button>
        <Button
          v-if="status||user.userName === submitMsg.userName"
          type="primary"
          :disabled="allowFlag"
          :loading="serveLoadFlag"
          @click="keepSetting"
        >
          <span v-if="!serveLoadFlag">保存</span>
          <span v-else>保存中</span>
        </Button>
        <Button type="primary" @click="ModalFalg = false" v-else>确定</Button>
      </div>
    </Modal>
    <item-image v-model="itemImageFlag" :imgSrc.sync="ImgResult" @on-upload="handleUpload" />
  </div>
</template>


<script>
import { keepItem } from '@/api/item';
import { mapState } from 'vuex';
import itemImage from './itemImage.vue';
import imagePath from '@/assets/images/itembg.jpg';

export default {
  components: { itemImage },
  filters: {
    TimeConversion(time) {
      const date = new Date(time);
      const completion = num => {
        return num.toString().padStart(2, '0');
      };
      return (
        date.getFullYear() +
        ' 年 ' +
        completion(date.getMonth() + 1) +
        ' 月 ' +
        completion(date.getDate()) +
        ' 日 '
      );
    },
  },
  data() {
    return {
      // 模态框展示标志位
      ModalFalg: false,
      // 允许发送标志位
      allowFlag: true,
      shareList: [
        {
          value: 'false',
          label: '私有项目 (仅项目成员可查看和编辑)',
        },
        {
          value: 'true',
          label: '公开项目 (所有成员可见，仅项目成员可编辑)',
        },
      ],
      // 向服务器上传信息
      submitMsg: {
        share: '',
        name: '',
        description: '',
        updateTime: '',
        createTime: '',
        id: '',
        image: '',
      },
      // 检测数据是否一样
      oldMsg: {
        share: '',
        name: '',
        description: '',
        image: '',
      },
      // 设置查看状态还是分享状态
      status: '',
      // 向服务器发送 标志位
      serveLoadFlag: false,
      // 照片连接
      ImgResult: '',
      // 照片裁剪标志为
      itemImageFlag: false,
    };
  },
  computed: {
    ...mapState(['user']),
  },
  methods: {
    // chang 事件触发的函数
    changeEvent() {
      let flag = true;
      if (this.submitMsg.image !== this.oldMsg.image) {
        flag = false;
      }
      if (this.submitMsg.name !== this.oldMsg.name) {
        flag = false;
      }
      if (this.submitMsg.share !== this.oldMsg.share) {
        flag = false;
      }
      if (this.submitMsg.description !== this.oldMsg.description) {
        flag = false;
      }
      this.allowFlag = flag;
    },
    // 展示 设置modal
    showView(val, status) {
      this.ModalFalg = true;
      this.status = status === 'true';
      this.submitMsg = {
        name: val.name,
        share: val.share ? 'true' : 'false',
        description: val.description,
        createTime: val.createTime,
        updateTime: val.updateTime,
        nickName: val.author.nickName,
        userName: val.author.userName,
        id: val.id,
        image: val.image || imagePath,
      };
      this.oldMsg = {
        name: val.name,
        share: val.share ? 'true' : 'false',
        description: val.description,
        image: val.image || imagePath,
      };
    },
    // 保存设置 并向服务器发送数据
    async keepSetting() {
      const params = {
        share: this.submitMsg.share === 'true',
        name: this.submitMsg.name,
        description: this.submitMsg.description,
        image: this.submitMsg.image,
      };
      this.serveLoadFlag = true;
      try {
        const data = await keepItem(this.submitMsg.id, params);
        this.$Message.success('保存成功');
        this.oldMsg = {
          name: data.name,
          share: data.share ? 'true' : 'false',
          description: data.description,
          image: data.image,
        };
        this.allowFlag = true;
        this.$emit('updataItem', data);
        this.serveLoadFlag = false;
      } catch (err) {
        this.serveLoadFlag = false;
      }
    },
    // 上传图片
    uploadImage() {
      const file = this.$refs.FileMsgRef.files[0];
      const reg = /\.(png|jpg|jpeg)$/;
      if (!file || reg.test(file.type)) {
        return;
      }
      const reader = new FileReader();
      reader.onload = e => {
        this.ImgResult = e.target.result;
        this.itemImageFlag = true;
      };
      reader.readAsDataURL(file);
    },
    // 处理上传后的图片
    handleUpload(link) {
      this.submitMsg.image = link;
      this.changeEvent();
    },
  },
};
</script>


<style scoped>
.header {
  color: #262626;
  font-size: 18px;
  font-weight: 600;
  letter-spacing: 0.1em;
}
.item-modal-title:first-of-type {
  margin: 0px 0 5px;
}
.item-modal-title {
  margin: 20px 0 5px;
  font-weight: 600;
  letter-spacing: 0.1em;
}
.main-img {
  width: 100px;
  height: 100px;
  user-select: none;
}

.ivu-radio-default {
  margin-right: 20px;
}
.item-modal-footer {
  text-align: right;
}
.item-modal-footer button {
  margin-left: 20px;
}
.item-modal-footer button:last-of-type {
  width: 88px;
}
.item-modal-main {
  max-height: calc(100vh - 200px);
  overflow: auto;
}
.item-border {
  padding: 5px 10px;
  min-height: 32px;
  border: 1px solid #dadce0;
  border-radius: 4px;
}
.g-upload-wrap {
  font-size: 0;
  height: 120px;
}
.g-upload-img {
  width: 240px;
  height: 120px;
  float: left;
  border-radius: 4px;
}
.g-upload-button {
  position: relative;
  display: inline-block;
  margin: 0 0 0 40px;
  height: 38px;
  width: 96px;
  text-align: center;
  line-height: 38px;
  font-size: 14px;
  color: #2d8cf0;
  border: 1px solid #2d8cf0;
  border-radius: 4px;
  user-select: none;
}
.g-upload-button:hover {
  background: #2d8cf0;
  color: #fff;
}
.g-upload-button input {
  position: absolute;
  left: 0;
  top: 0;
  opacity: 0;
  width: 100%;
  height: 100%;
  cursor: pointer;
  font-size: 0;
}
</style>
