<template>
  <article class="post post-list">
    <div class="post-entry">
      <h1 class="entry-title">
        <router-link :to="'/Article/'+post.id">
          <p>{{ post.title }}</p>
        </router-link>
      </h1>
      <div class="p-time">
        <div>
          <i class="el-icon-time"></i>
          <span> {{ post.createTimeL | parseTime }}</span>
        </div>
        <div v-show="post.likeCount">
          <i class="el-icon-check"></i>
          <span> {{ post.likeCount }}</span>
        </div>
        <div v-show="post.watchCount">
          <i class="el-icon-view"></i>
          <span> {{ post.watchCount }}</span>
        </div>
      </div>
      <p style="font-size: 13px;margin: 0 0 0 17%;">作者: {{ post.username }}</p>
      <p class="summary">{{ post.preview }}</p>
    </div>
    <hr/>
  </article>
</template>

<script>

export default {
  name: "PrimeArticleListItem.vue",
  props: {
    post: {
      type: Object
    }
  }
}
</script>

<style lang="less" scoped>
.post {
  margin: 0 0 4% 0;
  position: relative;
}

.post-entry {

  .entry-title {
    font-size: 21px;
    font-weight: 600;
    line-height: 50px;
    margin: 0 0 0 17%;
    position: relative;
    z-index: 1;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    width: 70%;
    color: #737373;

    & a:hover {
      color: #8fd0cc;
    }
  }

  .p-time {
    position: absolute;
    right: 0;
    top: 16px;
    font-size: 12px;
    color: #989898;
    letter-spacing: 1px;
    font-family: din, 'Hiragino Sans GB', 'Microsoft Yahei', Arial, sans-serif;
    //display: flex;
    align-items: center;
  }

  p.summary {
    min-height: 60px;
    margin: 0 0 0 17%;
    font-size: 15px;
    color: #6f6f6f;
    letter-spacing: 1px;
    line-height: 30px;
  }

  footer.entry-footer {
    margin: 0 0 0 17%;
    list-style: none;

    .post-more {
      margin-top: 10px;
      text-align: right;
      color: #A0DAD0;

      a:hover {
        color: #737373;
      }
    }

    .info-meta {
      margin-top: 10px;
      font-family: din, 'Hiragino Sans GB', 'Microsoft Yahei', Arial, sans-serif;
      position: absolute;
      top: 20px;
      opacity: 0;
      padding-top: 8px;
      border-top: 1px solid #ddd;
      -webkit-transform: translate3d(-150px, 0, 0);
      transform: translate3d(-150px, 0, 0);
      visibility: hidden;
      transition: .7s all ease;
      -webkit-transition: .6s all ease;
      -moz-transition: .6s all linear;
      -o-transition: .6s all ease;
      -ms-transition: .6s all ease;

      span, a {
        color: #B3B3B3;
        font-size: 12px;
      }

      i {
        margin-top: 3px;
        margin-right: 10px;
        float: left
      }
    }

    .comnum {
      float: left;
    }
  }

}

.post-entry:hover footer.entry-footer .info-meta {
  -webkit-transform: translate3d(-230px, 0, 0);
  transform: translate3d(-230px, 0, 0);
  opacity: 1;
  visibility: visible;
}

.post hr {
  width: 30%;
  height: 1px;
  margin: 0 auto;
  border: 0;
  background: #EFEFEF;
}

/*******/
@media (max-width: 1060px) {
  .info-meta {
    display: none;
  }
}

@media (max-width: 800px) {
  .post-entry {
    .feature img {
      width: 50px;
      height: 50px;
    }

    .entry-title {
      font-size: 16px;
      line-height: 30px;
      margin: 0 0 0 65px;
    }

    .p-time {
      position: relative;
      margin: -15px 0 0 65px;
    }

    p.summary {
      margin: 20px 0 0 65px;
      font-size: 14px;
      height: 30px;
      overflow: hidden;
    }

    .post-more {
      display: none;
    }
  }

  .post-list hr {
    margin-top: 20px;
  }
}

/******/
</style>
