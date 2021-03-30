<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-share"></i> APIKey管理
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
        <el-select v-model="query.activeStatus" class="handle-select mr10" placeholder="停/启用">
          <el-option key="1" label="启用" value="1"></el-option>
          <el-option key="2" label="禁用" value="0"></el-option>
        </el-select>
        <el-input v-model="query.name" class="handle-input mr10" placeholder="用户名"></el-input>
        <el-input v-model="query.apikey" class="handle-input mr10" placeholder="Key"></el-input>
        <el-button icon="el-icon-search" type="primary" @click="handleSearch">搜索</el-button>
        <el-button icon="el-icon-plus" type="primary" @click="handleAdd">新增Key</el-button>
      </div>
      <el-table
          ref="multipleTable"
          :data="tableData"
          border
          class="table"
          header-cell-class-name="table-header"
      >
        <el-table-column label="用户名" prop="username"></el-table-column>
        <el-table-column label="Key" prop="apikey"></el-table-column>
        <el-table-column align="center" label="状态">
          <template #default="scope">
            <el-tag
                :type="
                                new Date(scope.row.expireTime) > new Date()
                                    ? 'success'
                                    : !(new Date(scope.row.expireTime) > new Date())
                                    ? 'danger'
                                    : ''
                            "
            >{{ new Date(scope.row.expireTime) > new Date() ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column :formatter="formatDate" label="申请时间" prop="createTime"></el-table-column>
        <el-table-column :formatter="formatDate" label="过期时间" prop="expireTime"></el-table-column>
        <el-table-column align="center" label="操作" width="280">
          <template #default="scope">
            <el-button type="primary">
              重置Key
            </el-button>
            <el-button type="primary">
              修改过期时间
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

    <!-- 编辑弹出框 -->
    <el-dialog v-model="addVisible" title="新增" width="30%">
      <el-form ref="form" :model="form" label-width="70px">
        <el-form-item label="用户名">
          <el-input v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item label="过期时间">
          <el-date-picker v-model="form.expireTime" :shortcuts="shortcuts" placeholder="选择日期"
                          type="date"></el-date-picker>
        </el-form-item>
      </el-form>
      <template #footer>
                <span class="dialog-footer">
                    <el-button @click="cancelAdd">取 消</el-button>
                    <el-button type="primary" @click="saveAdd">确 定</el-button>
                </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import {formatDate} from "@/api";
import {addKey, getKeyList} from "@/api/KeyAPI"

export default {
  name: "APIKeyManage",
  data() {
    return {
      query: {
        activeStatus: undefined,
        name: undefined,
        apikey: undefined,
        pageIndex: 1,
        pageSize: 2
      },
      tableData: [],
      multipleSelection: [],
      delList: [],
      addVisible: false,
      pageTotal: 0,
      form: {},
      idx: -1,
      id: -1,
      shortcuts: [
        {
          text: '今天',
          value: new Date(),
        },
        {
          text: '一周后',
          value: (() => {
            const date = new Date()
            date.setTime(date.getTime() + 3600 * 1000 * 24 * 7)
            return date
          })(),
        },
        {
          text: '30天后',
          value: (() => {
            const date = new Date()
            date.setTime(date.getTime() + 3600 * 1000 * 24 * 30)
            return date
          })(),
        }
      ]
    }
  },
  created() {
    this.getData();
  },
  methods: {
    formatDate,
    // 获取 easy-mock 的模拟数据
    getData() {
      getKeyList(this.query.pageIndex, this.query.pageSize, this.query.activeStatus, this.query.name, this.query.apikey).then(res => {
        this.tableData = res.data.data.records;
        this.pageTotal = res.data.data.total;
      });
    },
    // 触发搜索按钮
    handleSearch() {
      this.query.pageIndex = 1;
      this.getData();
    },
    // 新增操作
    handleAdd() {
      this.addVisible = true;
    },
    // 取消新增
    cancelAdd() {
      this.addVisible = false;
      this.form = {};
    },
    // 保存新增
    saveAdd() {
      let _self = this;
      addKey(this.form.username, this.form.expireTime).then((res) => {
        if (res.data.code === 200) {
          _self.$message.success(`修改成功`);
          this.addVisible = false;
        } else {
          _self.$message.success(res.data.message);
        }
      })
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
