<template>
  <div class="home">
    <div class="site-content animate">
      <!--通知栏-->
      <div class="notify">
        <div v-if="hideSlogan" class="search-result">
          <span v-if="searchWords">搜索结果："{{ searchWords.replaceAll("+", " ") }}" 相关文章</span>
          <span v-else-if="category">分类 "{{ category }}" 相关文章</span>
        </div>
        <quote v-else>{{ notice }}</quote>
      </div>

      <!--文章列表-->
      <main :class="{'search':hideSlogan}" class="site-main">
        <section-title v-if="!hideSlogan">推荐</section-title>
        <template v-for="item in postList">
          <SearchResultListItem :key="item.id" :post="item"></SearchResultListItem>
        </template>
      </main>

      <!--加载更多-->
      <div v-show="hasNextPage" class="more">
        <div class="more-btn" @click="loadMore">More</div>
      </div>
    </div>
  </div>
</template>

<script>
import Feature from '@/components/feature'
import sectionTitle from '@/components/section-title'
import Post from '@/components/post'
import SmallIco from '@/components/small-ico'
import Quote from '@/components/quote'
import SearchResultListItem from '@/components/SearchResultListItem'
import {SearchAPIMethod} from '@/api/SearchAPI';

export default {
  name: 'Search',
  props: ['cate', 'words'],
  data() {
    return {
      postList: [],
      currPage: 1,
      hasNextPage: false
    }
  },
  components: {
    Feature,
    sectionTitle,
    Post,
    SmallIco,
    Quote,
    SearchResultListItem
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
    fetchList() {
      SearchAPIMethod(1, 5, this.$route.params.words).then((res) => {
        console.log(res);
        this.postList = res.data.data.data || [];
        this.currPage = res.data.data.currPage;
        this.hasNextPage = res.data.data.hasNextPage;
      })
    },
    loadMore() {
      SearchAPIMethod(this.currPage + 1, 5, this.$route.params.words).then(res => {
        this.postList = this.postList.concat(res.data.data.data || [])
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
