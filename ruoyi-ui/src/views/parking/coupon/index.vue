<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="优惠卷名称" prop="name" label-width="70">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入优惠卷名称"
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
          v-hasPermi="['parking:coupon:add']"
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
          v-hasPermi="['parking:coupon:edit']"
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
          v-hasPermi="['parking:coupon:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['parking:coupon:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="couponList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="id" />
      <el-table-column label="停车场" align="center" prop="parkingLotInformation.name" />
      <el-table-column label="优惠卷" align="center" prop="type" >
        <template scope="scope">
          <span style="color: green" v-if="scope.row.type==0">次卷</span>
          <span style="color: green" v-else-if="scope.row.type==1">小时卷</span>
          <span style="color: green" v-else-if="scope.row.type==2">金额卷</span>
        </template>
      </el-table-column>
      <el-table-column label="优惠卷名称" align="center" prop="name" />
      <el-table-column label="数量" align="center" prop="count" />
      <el-table-column label="优惠卷使用时间期限" align="center" prop="usetype">
        <template scope="scope">
          <span style="color: green" v-if="scope.row.usetype==0">当天</span>
          <span style="color: green" v-else-if="scope.row.usetype==1">二维码结束期限</span>

        </template>
      </el-table-column>
      <el-table-column label="二维码开始时间" align="center" prop="qrcodestarttime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.qrcodestarttime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="二维码结束时间" align="center" prop="qrcodeendtime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.qrcodeendtime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column  label="领取数量" align="center" prop="receiveccount" />
      <el-table-column label="状态" align="center" prop="state" >
        <template scope="scope">
          <span style="color: green" v-if="scope.row.state==0">活动进行中</span>
          <span style="color: green" v-else-if="scope.row.state==1">活动已结束</span>
        </template>
      </el-table-column>
      <el-table-column label="二维码" align="center" prop="qrcode">
        <template slot-scope="scope">
          <el-image
            style="width: 150px; height: 150px"
            :src="scope.row.qrcode">
          </el-image>
        </template>
      </el-table-column>
      <el-table-column  label="优惠面值" align="center" prop="preferentialfacevalue" >
        <template scope="scope">
          <span style="color: green" v-if="scope.row.type==0">次</span>
          <span style="color: green" v-if="scope.row.type==1">{{ scope.row.preferentialfacevalue }}小时</span>
          <span style="color: green" v-if="scope.row.type==2">{{ scope.row.preferentialfacevalue }}元</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['parking:coupon:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['parking:coupon:remove']"
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

    <!-- 添加或修改优惠卷对话框 -->
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
        <el-form-item label="优惠卷类型" prop="type">
          <template>
            <el-radio v-model="form.type" :label="0">次卷</el-radio>
            <el-radio v-model="form.type" :label="1">小时卷</el-radio>
            <el-radio v-model="form.type" :label="2">金额卷</el-radio>
          </template>
        </el-form-item>
        <el-form-item label="优惠卷有效期" prop="usetype">
          <template>
            <el-radio v-model="form.usetype" :label="0">当天有效</el-radio>
            <el-radio v-model="form.usetype" :label="1">与二维码有效时间相同</el-radio>
          </template>
        </el-form-item>
        <el-form-item label="优惠卷名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入优惠卷名称" />
        </el-form-item>
        <el-form-item label="数量" prop="count">
          <el-input v-model="form.count" placeholder="请输入数量" />
        </el-form-item>
        <el-form-item label="二维码开始时间" prop="qrcodestarttime">
          <el-date-picker clearable
            v-model="form.qrcodestarttime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择二维码开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="二维码结束时间" prop="qrcodeendtime">
          <el-date-picker clearable
            v-model="form.qrcodeendtime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择二维码结束时间">
          </el-date-picker>
        </el-form-item>

        <el-form-item v-if="form.type==1" label="优惠面值 " prop="preferentialfacevalue">
          <el-input  v-model="form.preferentialfacevalue" placeholder="请输入优惠面值 单位小时" />小时
        </el-form-item>
        <el-form-item v-if="form.type==2" label="优惠金额 " prop="preferentialfacevalue">
          <el-input  v-model="form.preferentialfacevalue" placeholder="请输入优惠金额 单位元" />元
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
import { listCoupon, getCoupon, delCoupon, addCoupon, updateCoupon } from "@/api/parking/coupon";
import {getarkinglotinformations} from "@/api/system/user";

export default {
  name: "Coupon",
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
      // 优惠卷表格数据
      couponList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        parkinglotinformationid: null,
        type: null,
        name: null,
        count: null,
        usetype: null,
        qrcodestarttime: null,
        qrcodeendtime: null,
        receiveccount: null,
        state: null,
        qrcode: null,
        preferentialfacevalue: null,
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
    this.getarkinglotinformations(localStorage.getItem("uu"));
  },
  methods: {
    getarkinglotinformations(id){
      getarkinglotinformations(id).then(res=>{
        this.parkinglotinformations=res.data
      })
    },
    /** 查询优惠卷列表 */
    getList() {
      this.loading = true;
      this.queryParams.parkinglotinformationid=localStorage.getItem("uu")
      listCoupon(this.queryParams).then(response => {
        this.couponList = response.rows;
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
        type: null,
        name: null,
        count: null,
        usetype: null,
        qrcodestarttime: null,
        qrcodeendtime: null,
        receiveccount: null,
        state: null,
        qrcode: null,
        preferentialfacevalue: null,

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
      this.title = "添加优惠卷";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCoupon(id).then(response => {
        this.form = response.data;
       /* this.form.type= String.valueOf(response.data.type)
        this.form.usetype=String.valueOf(response.data.usetype)*/
        this.open = true;

        this.title = "修改优惠卷";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCoupon(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCoupon(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除优惠卷编号为"' + ids + '"的数据项？').then(function() {
        return delCoupon(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('parking/coupon/export', {
        ...this.queryParams
      }, `coupon_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
