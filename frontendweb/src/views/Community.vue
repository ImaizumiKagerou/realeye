<template>
  <div class="articles">
    <banner></banner>
    <div class="site-content animate">
      <main class="site-main">
        <article class="hentry">
          <!-- 文章头部 -->
          <header class="entry-header">
            <!-- 标题输出 -->
            <h1 class="entry-title">{{ articleData.title }}</h1>
            <hr>
            <div class="breadcrumbs">
              <div id="crumbs">发布时间：{{ articleData.createTime }}</div>
            </div>
            <div class="breadcrumbs">
              <div id="crumbss">作者：{{ articleData.username }}</div>
            </div>
          </header>
          <!-- 正文输出 -->
          <div class="entry-content">
            {{ articleData.content }}
          </div>
          <!-- 文章底部 -->
          <section-title>
            <footer class="post-footer">
              <!-- 阅读次数 -->
              <div class="post-like">
                <i class="iconfont iconeyes"></i>
                <span class="count">{{ articleData.watchCount }}</span>
              </div>
            </footer>
          </section-title>

          <!--评论-->
          <div class="comments">
            <comment v-for="item in comments" :key="item.id" :comment="item">
            </comment>
          </div>
        </article>
      </main>
    </div>
  </div>
</template>

<script>
import Banner from '@/components/banner'
import sectionTitle from '@/components/section-title'
import comment from '@/components/comment'
import menuTree from '@/components/menu-tree'
import {formatDate} from '@/api'
import {CommunityArticleCommentByIdMethod, CommunityArticleInfoByIdMethod} from '@/api/CommunityAPI';

export default {
  name: 'Community',
  data() {
    return {
      articleData: {
        username: undefined,
        title: undefined,
        createTime: undefined,
        content: undefined,
        watchCount: undefined
      },
      showDonate: false,
      comments: []
    }
  },
  components: {
    Banner,
    sectionTitle,
    comment,
    menuTree
  },
  methods: {
    getArticleData() {
      CommunityArticleInfoByIdMethod(this.$route.params.id).then((res) => {
        if (res.data.code === 200) {
          this.articleData.username = res.data.data.username;
          this.articleData.title = res.data.data.title;
          this.articleData.content = res.data.data.content;
          this.articleData.watchCount = res.data.data.watchCount;
          this.articleData.createTime = formatDate(res.data.data.createTime);
        }
      })
    },
    getComment() {
      CommunityArticleCommentByIdMethod(this.$route.params.id).then((res) => {
        if (res.data.data.length === 0) {
          console.log("aaa");
          this.comments = [{
            id: 1,
            content: "暂无评论",
            createTime: undefined
          }]
        } else {
          this.comments = res.data.data || []
        }
      })
    },
    fetchH(arr, left, right) {
      if (right) {
        return arr.filter(item => item.offsetTop > left && item.offsetTop < right)
      } else {
        return arr.filter(item => item.offsetTop > left)
      }
    }
  },
  mounted() {
    this.getArticleData();
  },
  created() {
    this.getComment()
  }
}
</script>
<style lang="less" scoped>
.site-content {
  position: relative;

  .site-main {
    padding: 80px 0 0 0;
  }
}

#article-menus {
  position: sticky;
  top: 0;
  box-shadow: 0 2px 6px rgba(0, 0, 0, .1);
  border-radius: 3px;
  padding: 15px;
  width: 300px;
  transform: translateX(-120%) translateY(150px);
  font-size: 14px;
}

article.hentry {
  .entry-header {
    .entry-title {
      font-size: 23px;
      font-weight: 600;
      color: #737373;
      margin: 0.67em 0;

      &:before {
        content: "#";
        margin-right: 6px;
        color: #d82e16;
        font-size: 20px;
        font-weight: 600;
      }
    }

    hr {
      height: 1px;
      border: 0;
      background: #EFEFEF;
      margin: 15px 0;
    }

    .breadcrumbs {
      font-size: 14px;
      color: #D2D2D2;
      text-decoration: none;
      margin-bottom: 30px;
    }
  }

  .entry-content {
  }

  footer.post-footer {
    width: 100%;
    padding: 20px 10px;
    margin-top: 30px;
    height: 65px;
    position: relative;

    i {
      font-size: 18px;
      margin-right: 5px;
    }

    .post-like {
      float: right;
      margin: 7px 0 0 20px;
    }

    .post-share {
      float: right;
      list-style: none;
      margin-right: 20px;
    }

    .donate {
      float: left;
      line-height: 36px;
      border-radius: 100%;
      -webkit-border-radius: 100%;
      -moz-border-radius: 100%;
      border: 1px solid #2B2B2B;

      &:hover {
        border: 1px solid goldenrod;

        span {
          color: goldenrod;
        }
      }

      span {
        color: #2B2B2B;
        padding: 10px;
        position: relative;
        cursor: pointer;
      }

      .donate_inner {
        display: none;
        margin: 0;
        list-style: none;
        position: absolute;
        left: 80px;
        top: -40px;
        background: #FFF;
        padding: 10px;
        border: 1px solid #ddd;
        box-shadow: 0 2px 6px rgba(0, 0, 0, .08);
        border-radius: 3px;

        &.show {
          display: block;
        }

        li {
          float: left;
        }

        img {
          width: 100px;
        }

        p {
          text-align: center;
          font-size: 15px;
          color: #D2D2D2;
          line-height: 1rem;
        }
      }

      .donate_inner:after, .donate_inner:before {
        content: "";
        position: absolute;
        left: 0;
        bottom: 45%;
        margin-left: -8px;
        border-top: 8px solid transparent;
        border-bottom: 8px solid transparent;
        border-right: 8px solid #fff;
      }

      .donate_inner:before {
        left: -1px;
        border-right: 8px solid #ddd;
      }

    }

    .post-tags {
      margin: 7px 0 0 20px;
      float: left;
      text-transform: uppercase;

      a:hover {
        color: #ff6d6d;
      }
    }
  }

  .open-message {
    margin: 50px 0;
    position: relative;
    background: #2B2B2B;
    padding: 10px 30px;
    border-radius: 3px;
    font-size: 14px;
    color: #fff;

    &:after {
      content: "";
      border-left: 10px solid transparent;
      border-right: 10px solid transparent;
      border-bottom: 10px solid #2B2B2B;
      position: absolute;
      top: -8px;
      left: 48%;
    }

    p {
      margin: 10px 0;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    a {
      color: #A0DAD0;
      padding: 0 5px;
    }
  }
}
</style>
