<template>
  <div class="home">
    <div class="site-content animate">
      <!--文章列表-->
      <main :class="{'search':hideSlogan}" class="site-main">
        <span style="top: 100px;">
          <el-button v-if="$store.state.isLogin" @click="showCommunitiesDialog()">发帖</el-button>
        </span>
        <template v-for="item in postList">
          <CommunityListItem :key="item.id" :post="item"></CommunityListItem>
        </template>
      </main>

      <!--加载更多-->
      <div v-show="hasNextPage" class="more">
        <div class="more-btn" @click="loadMore">More</div>
      </div>
    </div>

    <el-dialog :visible.sync="dialogVisible" title="发送新帖" width="50%">
      <el-form ref="form" :model="dialog" label-width="70px">
        <el-form-item label="标题">
          <el-input v-model="dialog.title" required></el-input>
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="dialog.content" required type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
          <span class="dialog-footer">
              <el-button @click="cancelFatie">取 消</el-button>
              <el-button type="primary" @click="saveFatie">确 定</el-button>
          </span>
      </template>
    </el-dialog>

  </div>
</template>

<script>
import Feature from '@/components/feature'
import sectionTitle from '@/components/section-title'
import Post from '@/components/post'
import SmallIco from '@/components/small-ico'
import Quote from '@/components/quote'
import CommunityListItem from '@/components/CommunityListItem'
import {AddCommunity, CommunityListMethod} from '@/api/CommunityAPI'

export default {
  name: 'Communities',
  props: ['cate', 'words'],
  data() {
    return {
      postList: [],
      currPage: 1,
      hasNextPage: false,
      dialogVisible: false,
      dialog: {
        title: undefined,
        content: undefined
      }
    }
  },
  components: {
    Feature,
    sectionTitle,
    Post,
    SmallIco,
    Quote,
    CommunityListItem
  },
  computed: {
    searchWords() {
      return this.$route.params.words
    },
    category() {
      return this.$route.params.cate
    },
    hideSlogan() {
      return this.category || this.searchWords
    },
    notice() {
      return this.$store.getters.notice
    }
  },
  methods: {
    saveFatie() {
      const _self = this;
      AddCommunity(this.dialog.title, this.dialog.content).then((res) => {
        if (res.data.code === 200) {
          _self.$message.success("发送成功");
          _self.cancelFatie();
        } else {
          _self.$message.error(res.data.message);
        }
      })
    },
    cancelFatie() {
      this.dialogVisible = false;
      this.dialog.title = undefined;
      this.dialog.content = undefined;
    },
    showCommunitiesDialog() {
      this.dialogVisible = true;
    },
    fetchList() {
      CommunityListMethod(this.currPage, 5).then((res) => {
        this.postList = res.data.data.data || [];
        this.currPage = res.data.data.currPage;
        this.hasNextPage = res.data.data.hasNextPage;
      })
    },
    loadMore() {
      CommunityListMethod(this.currPage + 1, 5, this.$route.params.words).then((res) => {
        this.postList = this.postList.concat(res.data.data.data || []);
        this.currPage = res.data.data.currPage;
        this.hasNextPage = res.data.data.hasNextPage;
      })
    }
  },
  mounted() {
    this.fetchList();
  }
}
</script>
<style lang="less" scoped>

el-button {
  position: fixed;
  top: 100px;
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

.top-feature {
  width: 100%;
  height: auto;
  margin-top: 30px;

  .feature-content {
    margin-top: 10px;
    display: flex;
    justify-content: space-between;
    position: relative;

    .feature-item {
      width: 32.9%;
    }
  }
}

.site-main {
  padding-top: 80px;

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

/******/
@media (max-width: 800px) {
  .top-feature {
    display: none;
  }

  .site-main {
    padding-top: 40px;
  }

  .site-content {
    .notify {
      margin: 30px 0 0 0;
    }

    .search-result {
      margin-bottom: 20px;
      font-size: 16px;
    }
  }
}

/******/
</style>
