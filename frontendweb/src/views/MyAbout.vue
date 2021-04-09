<template>
  <div class="about">
    <div class="site-content">
      <div class="content-warp">
        <div class="about-site about-info">
          <div>
            <section-title>
              <i class="el-icon-user"></i>
              {{ userInfo.username }}
              <br/>
              <br/>
              <div style="font-size: 14px;">
                <div style="display: inline-block;">
                  <p v-if="userInfo.email">绑定邮箱 : {{ userInfo.email }}</p>
                  <el-button v-else @click="showEmailDialog">绑定邮箱</el-button>
                </div>
                <div style="float: right;display: inline-block;">
                  <i class="el-icon-watch"></i>
                  上次登陆时间: {{ userInfo.lastLoginTime }}
                </div>
              </div>
            </section-title>
          </div>
        </div>
        <div class="about-me about-info">
          <section-title id="Guestbook">API</section-title>
          <div class="info-card">
            <div v-if="userInfo.apikey">
              <p>APIKey: {{ userInfo.apikey }}</p>
              <p v-show="!('申请中...'===userInfo.apikey)">过期时间为: {{ userInfo.expireTime }}</p>
            </div>
            <el-button v-else @click="applyKey">申请APIKey</el-button>
            <a :href="$store.state.base_url+'api.docx'" class="el-button el-button--default">API文档下载</a>
          </div>
        </div>
      </div>
    </div>
    <div class="site-content">
      <main class="site-main">
        <span style="top: 100px;">
          <el-select v-model="value" clearable placeholder="请选择" @change="getPostList">
            <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            >
            </el-option>
          </el-select>
        </span>

        <template v-for="item in postList">
          <CommunityListItem :key="type+item.id+item.content" :post="item"></CommunityListItem>
        </template>
      </main>

      <!--加载更多-->
      <div v-show="hasNextPage" class="more">
        <div class="more-btn" @click="loadMore">More</div>
      </div>
    </div>

    <el-dialog :visible.sync="emailDialogVisible" title="绑定邮箱" width="50%">
      <el-form label-width="70px">
        <el-form-item label="Email">
          <el-input v-model="email" required></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
          <span class="dialog-footer">
              <el-button @click="cancelEmailDialog">取 消</el-button>
              <el-button type="primary" @click="bindEmail">确 定</el-button>
          </span>
      </template>
    </el-dialog>

  </div>
</template>
<script>
import sectionTitle from '@/components/section-title'
import {BindEmailMethod, GetUserInfo, MyAboutList} from '@/api/UserAPI';
import {formatDate, formatDate2Date} from '@/api';
import CommunityListItem from '@/components/CommunityListItem'

export default {
  name: "MyAbout",
  data() {
    return {
      value: "我的帖子",
      options: [{
        value: 'community',
        label: '我的帖子'
      }, {
        value: 'comment',
        label: '我的评论'
      }],
      postList: [],
      currPage: 1,
      hasNextPage: false,
      email: undefined,
      emailDialogVisible: false,
      apikeyDialogVisible: false,
      type: undefined,
      userInfo: {
        username: undefined,
        email: undefined,
        apikey: undefined,
        lastLoginTime: undefined,
        expireTime: undefined
      }
    }
  },
  components: {
    sectionTitle,
    CommunityListItem
  },
  methods: {
    getPostList(val) {
      console.log('abc');
      this.type = val;
      MyAboutList(this.currPage, 5, this.type).then((res) => {
        this.postList = res.data.data.data || [];
        this.currPage = 1;
        this.hasNextPage = res.data.data.hasNextPage;
      })
    },
    loadMore() {
      console.log('ccc');
      MyAboutList(this.currPage + 1, 5, this.type).then((res) => {
        this.postList = this.postList.concat(res.data.data.data || []);
        this.currPage = res.data.data.currPage;
        this.hasNextPage = res.data.data.hasNextPage;
      })
    },
    applyKey() {
      this.$confirm(
          "确定要申请吗？",
          "提示",
          {
            type: "warning"
          })
          .then(() => {
            this.userInfo.apikey = "申请中...";
            this.$message.success("删除成功");
          })
    },
    bindEmail() {
      const _self = this;
      BindEmailMethod(this.email).then((res) => {
        if (res.data.code === 200) {
          _self.$message.success("绑定成功");
          _self.getUserData();
          this.cancelEmailDialog();
        } else {
          _self.$message.error(res.data.message);
        }
      })
    },
    getUserData() {
      GetUserInfo().then((res) => {
        this.userInfo.username = res.data.data.username;
        this.userInfo.email = res.data.data.email;
        this.userInfo.apikey = res.data.data.apikey;
        this.userInfo.lastLoginTime = formatDate(res.data.data.lastLoginTime);
        this.userInfo.expireTime = formatDate2Date(res.data.data.expireTime);
      })
    },
    showEmailDialog() {
      this.emailDialogVisible = true;
    },
    cancelEmailDialog() {
      this.emailDialogVisible = false;
      this.email = undefined;
    }
  },
  mounted() {
    this.getUserData();
    this.getPostList('community');
  }
}
</script>
<style lang="less" scoped>
.about {
  padding-top: 40px;
}

.content-warp {
  margin-top: 80px;

  .about-info {
    margin: 30px 0;

    span {
      color: red;
      margin-right: 10px;
    }

    .info-card {
      min-height: 100px;
      padding: 20px;
      border-radius: 3px;
      margin: 30px 0 50px 0;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

      p {
        line-height: 1.7rem;
      }
    }
  }

  .contactForm {
    width: 100%;
    padding: 20px;

    .form-item {
      align-items: center;
      display: flex;

      &:not(:last-child) {
        margin-bottom: 20px;
      }

      label {
        width: 80px;
      }

      .v {
        min-height: 40px;
        line-height: 20px;
        border-radius: 3px;
        padding: 2px 10px;
        outline: none;
        border: 1px solid #8fd0cc;
        width: 100%;
        resize: vertical;
      }

      button {
        width: 100px;
        height: 40px;
        border-radius: 3px;
        outline: 0;
        border-style: none;
        cursor: pointer;
        background-color: #409eff;
        color: white;

        &:hover {
          opacity: 0.8;
        }
      }
    }
  }
}

.site-content {
  .notify {
    margin: 60px 0;
    border-radius: 3px;

    & > div {
      padding: 20px;
    }
  }

  .search-result {
    padding: 15px 20px;
    text-align: center;
    font-size: 20px;
    font-weight: 400;
    border: 1px dashed #ddd;
    color: #828282;
  }
}

.site-main {
  padding-top: 20px;

  &.search {
    padding-top: 0;
  }
}

.more {
  margin: 50px 0;

  .more-btn {
    width: 100px;
    height: 40px;
    line-height: 40px;
    text-align: center;
    color: #ADADAD;
    border: 1px solid #ADADAD;
    border-radius: 20px;
    margin: 0 auto;
    cursor: pointer;

    &:hover {
      color: #8fd0cc;
      border: 1px dashed #8fd0cc;
    }
  }
}

/*******/
@media (max-width: 800px) {
  .content-warp {
    margin-top: 0;
  }
}
</style>
