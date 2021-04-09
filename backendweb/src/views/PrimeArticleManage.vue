<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades"></i> 社区管理
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
        <el-select v-model="query.activeStatus" class="handle-select mr10" placeholder="停/启用">
          <el-option key="1" label="启用" value="1"></el-option>
          <el-option key="2" label="禁用" value="0"></el-option>
        </el-select>
        <el-input v-model="query.name" class="handle-input mr10" placeholder="标题"></el-input>
        <el-button icon="el-icon-search" type="primary" @click="handleSearch">搜索</el-button>
        <el-button icon="el-icon-plus" type="primary" @click="handleAdd">新增推荐文章</el-button>
      </div>
      <el-table
          ref="multipleTable"
          :data="tableData"
          border
          class="table"
          header-cell-class-name="table-header"
      >
        <el-table-column label="作者" prop="username" width="120"></el-table-column>
        <el-table-column align="center" label="状态" width="120">
          <template #default="scope">
            <el-tag
                :type="
                                scope.row.active === true
                                    ? 'success'
                                    : scope.row.active === false
                                    ? 'danger'
                                    : ''
                            "
            >{{ scope.row.active ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="标题" prop="title" width="150"></el-table-column>
        <el-table-column label="简介" prop="preview" width="150"></el-table-column>
        <el-table-column label="内容" prop="content" width="100">
          <template #default="scope">
            <el-button @click="showPreview(scope.row.title,scope.row.preview,scope.row.content)">预览</el-button>
          </template>
        </el-table-column>
        <el-table-column :formatter="formatDate" label="发布时间" prop="createTime"></el-table-column>
        <el-table-column align="center" label="操作" width="180">
          <template #default="scope">
            <el-button :type="
                                scope.row.active === false
                                    ? 'success'
                                    : scope.row.active === true
                                    ? 'danger'
                                    : ''
                            "
                       @click="changeActiveStatus(scope.row.id)"
            >
              {{ scope.row.active ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
            :current-page="query.pageIndex"
            :page-size="query.pageSize"
            :total="pageTotal"
            background
            layout="total, prev, pager, next"
            @current-change="handlePageChange"
        ></el-pagination>
      </div>
    </div>

    <!-- 新增弹出框 -->
    <el-dialog v-model="dialog.visible" :title="dialog.type?'预览':'新增'" width="70%">
      <el-form v-if="!dialog.type" ref="form" label-width="70px">
        <el-form-item label="标题">
          <el-input v-model="dialog.title"></el-input>
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="dialog.preview"></el-input>
        </el-form-item>
        <el-form-item label="内容">
          <quill-editor v-model:value="dialog.content"/>
        </el-form-item>
      </el-form>
      <el-form v-else ref="form" label-width="70px">
        <el-form-item label="标题">
          <el-input v-model="dialog.title" readonly></el-input>
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="dialog.preview" readonly></el-input>
        </el-form-item>
        <el-form-item label="内容">
          <div style="margin-left: 20px" v-html="dialog.content"></div>
        </el-form-item>
      </el-form>
      <template #footer>
        <span v-if="!dialog.type" class="dialog-footer">
            <el-button @click="cancelDialog">取 消</el-button>
            <el-button type="primary" @click="saveAdd">确 定</el-button>
        </span>
        <span v-else class="dialog-footer">
            <el-button @click="cancelDialog">确 定</el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>

<script>
import {formatDate} from "@/api";
import {AddPrimeArticle, ChangePrimeActive, GetPrimeArticleData} from '@/api/CommunityAPI';

export default {
  name: "PrimeArticleManage",
  data() {
    return {
      query: {
        activeStatus: undefined,
        name: undefined,
        pageIndex: 1,
        pageSize: 10
      },
      tableData: [],
      pageTotal: 0,
      dialog: {
        // true 预览 false 新增
        type: true,
        visible: false,
        title: undefined,
        preview: undefined,
        content: undefined
      }
    };
  },
  created() {
    this.getData();
  },
  methods: {
    handleAdd() {
      this.dialog.visible = true;
      this.dialog.type = false;
    },
    saveAdd() {
      AddPrimeArticle(this.dialog.title, this.dialog.content, this.dialog.preview).then((res) => {
        if (res.data.code === 200) {
          this.$message.success("添加成功");
          this.cancelDialog();
        } else {
          this.$message.error(res.data.message);
        }
      })
    },
    cancelDialog() {
      this.dialog.visible = false;
      this.dialog.title = undefined;
      this.dialog.preview = undefined;
      this.dialog.content = undefined;
    },
    showPreview(title, preview, content) {
      this.dialog.type = true;
      this.dialog.visible = true;
      this.dialog.title = title;
      this.dialog.preview = preview;
      this.dialog.content = content;
    },
    formatDate,
    changeActiveStatus(id) {
      ChangePrimeActive(id).then((res) => {
        this.getData();
      });
    },
    // 获取 easy-mock 的模拟数据
    getData() {
      GetPrimeArticleData(this.query.pageIndex, this.query.pageSize, this.query.activeStatus).then(res => {
        this.tableData = res.data.data.records;
        this.pageTotal = res.data.data.total;
      });
    },
    // 触发搜索按钮
    handleSearch() {
      this.query.pageIndex = 1;
      this.getData();
    },
    // 分页导航
    handlePageChange(val) {
      this.query.pageIndex = val;
      this.getData();
    }
  }
};
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}

.handle-select {
  width: 120px;
}

.handle-input {
  width: 300px;
  display: inline-block;
}

.table {
  width: 100%;
  font-size: 14px;
}

.red {
  color: #ff0000;
}

.mr10 {
  margin-right: 10px;
}

.table-td-thumb {
  display: block;
  margin: auto;
  width: 40px;
  height: 40px;
}
</style>
