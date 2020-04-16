/**
*
*  @author ZTiger
*
*/


<template>
  <div>
    <div class="main-content-msg-title">
      <div class="user-main-header-name">个人资料</div>
      <div class="user-main-header-title">知识图谱构建平台</div>
    </div>
    <div class="main-content-msg-row">
      <div class="msg-row-content dis-flex" @click.stop="headPortrait">
        <div class="dis-flex-left">
          <div class="msg-row-name">头像</div>
          <div class="msg-row-title msg-row-title-tip">更改照片可帮助您个性化您的帐号</div>
        </div>
        <div class="dis-flex-right dis-flex-img">
          <img :src="images" class="msg-row-image" v-if="images" />
          <div class="msg-row-Nimage" v-else>{{nickName.charAt(0).toUpperCase()}}</div>
          <div class="msg-row-image-camera">
            <Icon type="ios-camera" size="20" color="#c5c8ce" />
          </div>
        </div>
      </div>
    </div>
    <div class="main-content-msg-row">
      <div class="msg-row-content">
        <div class="msg-row-name">用户名</div>
        <div class="msg-row-title">{{user.userName}}</div>
      </div>
    </div>
    <div class="main-content-msg-row">
      <router-link to="user/nickname" class="msg-row-content dis-flex">
        <div class="dis-flex-left">
          <div class="msg-row-name">昵称</div>
          <div class="msg-row-title">{{nickName}}</div>
        </div>
        <div class="dis-flex-right">
          <Icon type="ios-arrow-forward" size="20" />
        </div>
      </router-link>
    </div>
    <div class="main-content-msg-row">
      <div class="msg-row-content">
        <div class="msg-row-name">创建时间</div>
        <div class="msg-row-title">{{TimeConversion(user.createTime)}}</div>
      </div>
    </div>
    <div class="main-content-msg-row">
      <div class="msg-row-content">
        <div class="msg-row-name">更新时间</div>
        <div class="msg-row-title">{{TimeConversion(updateTime)}}</div>
      </div>
    </div>
    <div class="main-content-msg-row">
      <router-link to="/user/password" class="msg-row-content dis-flex" @click="selectRow(3)">
        <div class="dis-flex-left">
          <div class="msg-row-name">密码</div>
          <div class="msg-row-title">••••••••</div>
        </div>
        <div class="dis-flex-right">
          <Icon type="ios-arrow-forward" size="20" />
        </div>
      </router-link>
    </div>
    <upload-images ref="uploadImages" />
  </div>
</template>


<script>
// 点击上传照片
import uploadImages from '@/components/uploadImages';
import { mapGetters } from 'vuex';

export default {
  components: { uploadImages },
  data() {
    return {};
  },
  computed: {
    ...mapGetters({
      user: 'getUser',
      images: 'getImageSrc',
      nickName: 'getnickName',
      updateTime: 'getUpdateTime',
    }),
  },
  methods: {
    // 时间转换函数
    TimeConversion(time) {
      const date = new Date(time);
      const completion = (num) => {
        return num.toString().padStart(2, '0');
      };
      return (
        date.getFullYear() +
        '年' +
        completion(date.getMonth() + 1) +
        '月' +
        completion(date.getDate()) +
        '日'
      );
    },
    // 选择头像
    headPortrait() {
      this.$refs.uploadImages.clickStatus(3);
    },
  },
};
</script>


<style scoped>
.user-main-header-name {
  font-size: 23px;
  color: #202124;
}
.user-main-header-title {
  margin: 10px 0 0px 0;
  font-size: 12px;
  font-weight: 520;
  color: #5f6368;
}
.main-content-msg-title {
  padding: 20px;
}
.main-content-msg-row {
  cursor: pointer;
  font-size: 0;
}
.main-content-msg-row:hover,
.main-content-msg-row:hover + .main-content-msg-row {
  border-top: 1px solid #dadce0;
}
.main-content-msg-row:hover {
  background: #f5f5f5;
}

.main-content-msg-row:active {
  background: #e1e1e1;
  transform-origin: center right;
  transition: all 1s;
}
.main-content-msg-row:hover .msg-row-content,
.main-content-msg-row:hover + .main-content-msg-row .msg-row-content {
  border-width: 0;
}
.msg-row-content {
  padding: 20px 20px 20px 0;
  margin: 0 0 0 20px;
  border-top: 1px solid #dadce0;
  color: #515a6e;
}
.main-content-msg-row:hover:nth-of-type(2),
.main-content-msg-row:nth-of-type(2) .msg-row-content {
  border-width: 0px;
}
.msg-row-name,
.msg-row-title {
  display: inline-block;
}
.msg-row-name {
  width: 150px;
  height: 25px;
  line-height: 25px;
  font-size: 12px;
  letter-spacing: 0.1em;
}
.msg-row-title {
  height: 25px;
  line-height: 25px;
  font-size: 16px;
  color: #202124;
}
.msg-row-title-tip {
  color: #5f6368;
  font-size: 12px;
}
.msg-row-image,
.msg-row-Nimage {
  height: 60px;
  width: 60px;
}
.msg-row-Nimage {
  font-family: Georgia;
  font-size: 18px;
  text-align: center;
  line-height: 60px;
  color: #dff6f0;
  background: #4d80e4;
}
.dis-flex-img {
  position: relative;
  border-radius: 50%;
  overflow: hidden;
}
.msg-row-image-camera {
  position: absolute;
  left: 0;
  bottom: 0px;
  width: 60px;
  text-align: center;
  background: rgba(32, 33, 36, 0.6);
}
.dis-flex {
  display: flex;
  align-items: center;
}
.dis-flex-left {
  flex-grow: 1;
}
</style>