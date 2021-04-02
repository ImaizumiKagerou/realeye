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
        <el-input v-model="query.name" class="handle-input mr10" placeholder="用户名"></el-input>
        <el-button icon="el-icon-search" type="primary" @click="handleSearch">搜索</el-button>
      </div>
      <el-table
          ref="multipleTable"
          :data="tableData"
          border
          class="table"
          header-cell-class-name="table-header"
      >
        <el-table-column :formatter="formatTitleType" align="center" label="类型" prop="title"
                         width="55"></el-table-column>
        <el-table-column label="楼主" prop="username" width="120"></el-table-column>
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

        <el-table-column label="标题" prop="title" width="200"></el-table-column>
        <el-table-column label="内容" prop="content"></el-table-column>
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

  </div>
</template>

<script>
import {formatDate} from "@/api";
import {ChangeActive, GetCommunityData} from '@/api/CommunityAPI';

export default {
  name: "CommunityManage",
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
    };
  },
  created() {
    this.getData();
  },
  methods: {
    formatTitleType(row, column) {
      let data = row[column.property]
      if (data === null) {
        return null
      }
      return data === '' ? '回复' : '楼主';
    },
    formatDate,
    changeActiveStatus(id) {
      ChangeActive(id).then((res) => {
        this.getData();
      });
    },
    // 获取 easy-mock 的模拟数据
    getData() {
      GetCommunityData(this.query.pageIndex, this.query.pageSize, this.query.activeStatus).then(res => {
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
