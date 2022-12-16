<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">

      <el-form-item label="区域" prop="region">
        <el-input
          v-model="queryParams.region"
          placeholder="请输入区域"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车位号" prop="parkingspacenumber">
        <el-input
          v-model="queryParams.parkingspacenumber"
          placeholder="请输入车位号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="业主姓名" prop="nameofemployer">
        <el-input
          v-model="queryParams.nameofemployer"
          placeholder="请输入业主姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="电话" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车牌" prop="license">
        <el-input
          v-model="queryParams.license"
          placeholder="请输入绑定车牌"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['parking:fixedparkingspace:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['parking:fixedparkingspace:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['parking:fixedparkingspace:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['parking:fixedparkingspace:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="fixedparkingspaceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序列号" align="center" prop="id" />
      <el-table-column label="停车场" align="center" prop="parkinglotinformationid" />
      <el-table-column label="区域" align="center" prop="region" />
      <el-table-column label="车位号" align="center" prop="parkingspacenumber" />
      <el-table-column label="业主姓名" align="center" prop="nameofemployer" />
      <el-table-column label="电话" align="center" prop="phone" />
      <el-table-column label="绑定车牌" align="center" prop="license" />
      <el-table-column label="有效期起" align="center" prop="starttime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.starttime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="有效期止" align="center" prop="endtime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endtime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="占用状态" align="center" prop="state">
        <template scope="scope">
          <span style="color: green" v-if="scope.row.state==0">未占用</span>
          <span style="color: rebeccapurple" v-else-if="scope.row.state==1">已占用</span>
        </template>
      </el-table-column>
      <el-table-column label="占用车牌" align="center" prop="occupationoflicenseplate" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['parking:fixedparkingspace:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['parking:fixedparkingspace:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改停车场固定车位对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="停车场" prop="parkinglotinformationid">
            <el-select v-model="form.parkinglotinformationid" clearable  placeholder="请选择">
              <el-option
                v-for="item in parkinglotinformations"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="区域" prop="region">
          <el-input v-model="form.region" placeholder="请输入区域" />
        </el-form-item>
        <el-form-item label="车位号" prop="parkingspacenumber">
          <el-input v-model="form.parkingspacenumber" placeholder="请输入车位号" />
        </el-form-item>
        <el-form-item label="业主姓名" prop="nameofemployer">
          <el-input v-model="form.nameofemployer" placeholder="请输入业主姓名" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="绑定车牌" prop="license">
          <el-input v-model="form.license" placeholder="请输入绑定车牌" />
        </el-form-item>
        <el-form-item label="有效期起" prop="starttime">
          <el-date-picker clearable
            v-model="form.starttime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择有效期起">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="有效期止" prop="endtime">
          <el-date-picker clearable
            v-model="form.endtime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择有效期止">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listFixedparkingspace, getFixedparkingspace, delFixedparkingspace, addFixedparkingspace, updateFixedparkingspace } from "@/api/parking/fixedparkingspace";
import {getarkinglotinformations} from "@/api/system/user";

export default {
  name: "Fixedparkingspace",
  data() {
    return {
      parkinglotinformations:[],
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 停车场固定车位表格数据
      fixedparkingspaceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        parkinglotinformationid: null,
        region: null,
        parkingspacenumber: null,
        nameofemployer: null,
        phone: null,
        license: null,
        starttime: null,
        endtime: null,
        state: null,
        occupationoflicenseplate: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        parkinglotinformationid: [
          { required: true, message: "停车场不能为空", trigger: "blur" }
        ],
        region: [
          { required: true, message: "区域不能为空", trigger: "blur" }
        ],
        parkingspacenumber: [
          { required: true, message: "车位号不能为空", trigger: "blur" }
        ],
        license: [
          { required: true, message: "绑定车牌不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getarkinglotinformations(localStorage.getItem("uu"));
  },
  methods: {
    getarkinglotinformations(id){
      getarkinglotinformations(id).then(res=>{
        this.parkinglotinformations=res.data
      })
    },
    /** 查询停车场固定车位列表 */
    getList() {
      this.loading = true;
      this.queryParams.parkinglotinformationid=localStorage.getItem("uu")
      listFixedparkingspace(this.queryParams).then(response => {
        this.fixedparkingspaceList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        parkinglotinformationid: null,
        region: null,
        parkingspacenumber: null,
        nameofemployer: null,
        phone: null,
        license: null,
        starttime: null,
        endtime: null,
        state: null,
        occupationoflicenseplate: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加停车场固定车位";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getFixedparkingspace(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改停车场固定车位";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateFixedparkingspace(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addFixedparkingspace(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除停车场固定车位编号为"' + ids + '"的数据项？').then(function() {
        return delFixedparkingspace(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('parking/fixedparkingspace/export', {
        ...this.queryParams
      }, `fixedparkingspace_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
