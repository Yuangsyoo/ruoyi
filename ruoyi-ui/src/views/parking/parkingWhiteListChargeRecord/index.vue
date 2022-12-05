<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="车牌号码" prop="license">
        <el-input
          v-model="queryParams.license"
          placeholder="请输入车牌号码"
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
          v-hasPermi="['parking:parkingWhiteListChargeRecord:add']"
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
          v-hasPermi="['parking:parkingWhiteListChargeRecord:edit']"
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
          v-hasPermi="['parking:parkingWhiteListChargeRecord:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['parking:parkingWhiteListChargeRecord:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="parkingWhiteListChargeRecordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="停车场id" align="center" prop="parkingLotInformation.name" />
      <el-table-column label="缴费时间" align="center" prop="paymenttime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.paymenttime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="缴费方式" align="center" prop="paymentmethod" />
      <el-table-column label="车牌号码" align="center" prop="license" />
      <el-table-column label="开始时间" align="center" prop="starttime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.starttime,'{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endtime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endtime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="实收金额" align="center" prop="money" />
      <el-table-column label="收费员" align="center" prop="operator" />
      <el-table-column label="操作类型" align="center" prop="operationtype" />
      <el-table-column label="备注" align="center" prop="remarks" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['parking:parkingWhiteListChargeRecord:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['parking:parkingWhiteListChargeRecord:remove']"
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

    <!-- 添加或修改白名单收费记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="停车场id" prop="parkinglotinformationid">
          <el-select v-model="form.parkinglotinformationid" clearable  placeholder="请选择">
            <el-option
              v-for="item in parkinglotinformations"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="缴费时间" prop="paymenttime">
          <el-date-picker clearable
            v-model="form.paymenttime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择缴费时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="缴费方式" prop="paymentmethod">
          <el-input v-model="form.paymentmethod" placeholder="请输入缴费方式" />
        </el-form-item>
        <el-form-item label="车牌号码" prop="license">
          <el-input v-model="form.license" placeholder="请输入车牌号码" />
        </el-form-item>
        <el-form-item label="开始时间" prop="starttime">
          <el-date-picker clearable
            v-model="form.starttime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endtime">
          <el-date-picker clearable
            v-model="form.endtime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="实收金额" prop="money">
          <el-input v-model="form.money" placeholder="请输入实收金额" />
        </el-form-item>
        <el-form-item label="收费员" prop="operator">
          <el-input v-model="form.operator" placeholder="请输入收费员" />
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="form.remarks" placeholder="请输入备注" />
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
import { listParkingWhiteListChargeRecord, getParkingWhiteListChargeRecord, delParkingWhiteListChargeRecord, addParkingWhiteListChargeRecord, updateParkingWhiteListChargeRecord } from "@/api/parking/parkingWhiteListChargeRecord";
import {getarkinglotinformations} from "@/api/system/user";

export default {
  name: "ParkingWhiteListChargeRecord",
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
      // 白名单收费记录表格数据
      parkingWhiteListChargeRecordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        parkinglotinformationid: null,
        paymenttime: null,
        paymentmethod: null,
        license: null,
        starttime: null,
        endtime: null,
        money: null,
        operator: null,
        operationtype: null,
        remarks: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        parkinglotinformationid: [
          { required: true, message: "停车场id不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getarkinglotinformations(id){
      getarkinglotinformations(id).then(res=>{
        this.parkinglotinformations=res.data
      })
    },
    /** 查询白名单收费记录列表 */
    getList() {
      this.loading = true;
      this.queryParams.parkinglotinformationid=localStorage.getItem("uu")
      listParkingWhiteListChargeRecord(this.queryParams).then(response => {
        this.parkingWhiteListChargeRecordList = response.rows;
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
        paymenttime: null,
        paymentmethod: null,
        license: null,
        starttime: null,
        endtime: null,
        money: null,
        operator: null,
        operationtype: null,
        remarks: null
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
      this.getarkinglotinformations(localStorage.getItem("uu"));
      this.open = true;
      this.title = "添加白名单收费记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getParkingWhiteListChargeRecord(id).then(response => {
        this.form = response.data;
        this.getarkinglotinformations(localStorage.getItem("uu"));
        this.open = true;
        this.title = "修改白名单收费记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateParkingWhiteListChargeRecord(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addParkingWhiteListChargeRecord(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除白名单收费记录编号为"' + ids + '"的数据项？').then(function() {
        return delParkingWhiteListChargeRecord(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('parking/parkingWhiteListChargeRecord/export', {
        ...this.queryParams
      }, `parkingWhiteListChargeRecord_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
