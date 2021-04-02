<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades"></i> 用户管理
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
<!--        <el-button-->
<!--            class="handle-del mr10"-->
<!--            icon="el-icon-delete"-->
<!--            type="primary"-->
<!--            @click="delAllSelection"-->
<!--        >批量删除-->
<!--        </el-button>-->
        <el-select v-model="query.activeStatus" class="handle-select mr10" placeholder="停/启用">
          <el-option key="1" label="启用" value="1"></el-option>
          <el-option key="2" label="禁用" value="0"></el-option>
        </el-select>
        <el-input v-model="query.name" class="handle-input mr10" placeholder="用户名"></el-input>
        <el-button icon="el-icon-search" type="primary" @click="handleSearch">搜索</el-button>
      </div>
      <el-table
          ref="multipleTable"
          :data="tableData"
          border
          class="table"
          header-cell-class-name="table-header"
          @selection-change="handleSelectionChange"
      >
<!--        <el-table-column align="center" type="selection" width="55"></el-table-column>-->
        <el-table-column align="center" label="ID" prop="id" width="55"></el-table-column>
        <el-table-column label="用户名" prop="username"></el-table-column>
        <!--                <el-table-column label="账户余额">-->
        <!--                    <template #default="scope">￥{{ scope.row.money }}</template>-->
        <!--                </el-table-column>-->
        <!--                <el-table-column label="头像(查看大图)" align="center">-->
        <!--                    <template #default="scope">-->
        <!--                        <el-image-->
        <!--                            class="table-td-thumb"-->
        <!--                            :src="scope.row.thumb"-->
        <!--                            :preview-src-list="[scope.row.thumb]"-->
        <!--                        ></el-image>-->
        <!--                    </template>-->
        <!--                </el-table-column>-->
        <!--                <el-table-column prop="address" label="地址"></el-table-column>-->
        <el-table-column align="center" label="状态">
          <template #default="scope">
            <el-tag
                :type="
                                scope.row.activeStatus === true
                                    ? 'success'
                                    : scope.row.activeStatus === false
                                    ? 'danger'
                                    : ''
                            "
            >{{ scope.row.activeStatus ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column :formatter="formatDate" label="注册时间" prop="createTime"></el-table-column>
        <el-table-column :formatter="formatDate" label="上一次登录时间" prop="lastLoginTime"></el-table-column>
        <el-table-column align="center" label="操作" width="180">
          <template #default="scope">
            <el-button @click="changeActiveStatus(scope.row.id)"
                :type="
                                scope.row.activeStatus === false
                                    ? 'success'
                                    : scope.row.activeStatus === true
                                    ? 'danger'
                                    : ''
                            "
            >
              {{ scope.row.activeStatus ? '禁用' : '启用' }}
            </el-button>
<!--            <el-button-->
<!--                icon="el-icon-edit"-->
<!--                type="text"-->
<!--                @click="handleEdit(scope.$index, scope.row)"-->
<!--            >编辑-->
<!--            </el-button>-->
<!--            <el-button-->
<!--                class="red"-->
<!--                icon="el-icon-delete"-->
<!--                type="text"-->
<!--                @click="handleDelete(scope.$index, scope.row)"-->
<!--            >删除-->
<!--            </el-button>-->
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
    <el-dialog v-model="editVisible" title="编辑" width="30%">
      <el-form ref="form" :model="form" label-width="70px">
        <el-form-item label="用户名">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
                <span class="dialog-footer">
                    <el-button @click="editVisible = false">取 消</el-button>
                    <el-button type="primary" @click="saveEdit">确 定</el-button>
                </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import {formatDate} from "@/api";
import {getUserManageData,changeActiveStatusById} from "@/api/UserAPI";

export default {
  name: "UserManage",
  data() {
    return {
      query: {
        activeStatus: undefined,
        name: undefined,
        pageIndex: 1,
        pageSize: 2
      },
      tableData: [],
      multipleSelection: [],
      delList: [],
      editVisible: false,
      pageTotal: 0,
      form: {},
      idx: -1,
      id: -1
    };
  },
  created() {
    this.getData();
  },
  methods: {
    formatDate,
    changeActiveStatus(id){
      changeActiveStatusById(id);
      this.getData();
    },
    // 获取 easy-mock 的模拟数据
    getData() {
      getUserManageData(this.query.pageIndex, this.query.pageSize, this.query.activeStatus, this.query.name).then(res => {
        console.log(res);
        this.tableData = res.data.data.records;
        this.pageTotal = res.data.data.total;
      });
    },
    // 触发搜索按钮
    handleSearch() {
      this.query.pageIndex = 1;
      this.getData();
    },
    // 删除操作
    handleDelete(index) {
      // 二次确认删除
      this.$confirm("确定要删除吗？", "提示", {
        type: "warning"
      })
          .then(() => {
            this.$message.success("删除成功");
            this.tableData.splice(index, 1);
          })
          .catch(() => {
          });
    },
    // 多选操作
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    delAllSelection() {
      const length = this.multipleSelection.length;
      let str = "";
      this.delList = this.delList.concat(this.multipleSelection);
      for (let i = 0; i < length; i++) {
        str += this.multipleSelection[i].name + " ";
      }
      this.$message.error(`删除了${str}`);
      this.multipleSelection = [];
    },
    // 编辑操作
    handleEdit(index, row) {
      this.idx = index;
      this.form = row;
      this.editVisible = true;
    },
    // 保存编辑
    saveEdit() {
      this.editVisible = false;
      this.$message.success(`修改第 ${this.idx + 1} 行成功`);
      this.$set(this.tableData, this.idx, this.form);
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
