<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="车牌号" prop="license">
        <el-input
          v-model="queryParams.license"
          placeholder="请输入车牌号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="支付状态" prop="paystate">
        <el-input
          v-model="queryParams.paystate"
          placeholder="请输入支付状态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
<!--      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['parking:record:add']"
        >新增</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['parking:record:edit']"
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
          v-hasPermi="['parking:record:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['parking:record:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="车牌号" align="center" prop="license" />
      <el-table-column label="停车场" align="center" prop="parkingLotInformation.name" />
      <el-table-column label="入场时间" align="center" prop="admissiontime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.admissiontime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="出场时间" align="center" prop="exittime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.exittime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="车牌颜色" align="center" prop="licensepllatecolor">
        <template slot-scope="scope">
        <span style="color: red" v-if="scope.row.licensepllatecolor==0">未知</span>
        <span style="color: blue" v-if="scope.row.licensepllatecolor==1">蓝色</span>
        <span style="color: yellow" v-if="scope.row.licensepllatecolor==2">黄色</span>
        <span style="color: black" v-if="scope.row.licensepllatecolor==3">白色</span>
        <span style="color: green" v-if="scope.row.licensepllatecolor==4">黑色</span>
        <span style="color: green"  v-else-if="scope.row.licensepllatecolor===5">绿色</span>
        </template>
      </el-table-column>
      <el-table-column label="订单编号" align="center" prop="ordernumber" />
      <el-table-column label="订单状态" align="center" prop="orderstate">
        <template scope="scope">
          <span style="color: green" v-if="scope.row.orderstate==1">已支付</span>
          <span style="color: red" v-else-if="scope.row.orderstate==0">未支付</span>
          <span style="color: red" v-else-if="scope.row.orderstate==2">订单进行中</span>
        </template>
      </el-table-column>
      <el-table-column label="支付状态" align="center" prop="paystate" >
        <template scope="scope">
          <span style="color: green" v-if="scope.row.paystate==1">已支付</span>
          <span style="color: red" v-else-if="scope.row.paystate==0">未支付</span>

        </template>
      </el-table-column>
      <el-table-column label="应付金额" align="center" prop="amountpayable" />
      <el-table-column label="优惠金额" align="center" prop="discountamount" />
      <el-table-column label="实付金额" align="center" prop="money" />
      <el-table-column label="支付方式" align="center" prop="paymentmethod" />
      <el-table-column label="出入口名称" align="center" prop="entranceandexitname" />
      <el-table-column label="支付时间" align="center" prop="payTime" >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.payTime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['parking:record:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['parking:record:remove']"
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

    <!-- 添加或修改停车记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="车牌号" prop="license">
          <el-input v-model="form.license" placeholder="请输入车牌号" />
        </el-form-item>
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
        <el-form-item label="入场时间" prop="admissiontime">
          <el-date-picker clearable
            v-model="form.admissiontime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择入场时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="出场时间" prop="exittime">
          <el-date-picker clearable
            v-model="form.exittime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择出场时间">
          </el-date-picker>
        </el-form-item>
<!--        <el-form-item label="车牌颜色" prop="licensepllatecolor">
          <el-input v-model="form.licensepllatecolor" placeholder="请输入车牌颜色" />
        </el-form-item>-->
        <el-form-item label="订单编号" prop="ordernumber">
          <el-input v-model="form.ordernumber" placeholder="请输入订单编号" />
        </el-form-item>
        <el-form-item label="订单状态" prop="orderstate">
          <template>
            <el-radio v-model="form.orderstate" label="0">未支付</el-radio>
            <el-radio v-model="form.orderstate" label="1">已支付</el-radio>
            <el-radio v-model="form.orderstate" label="2">进行中</el-radio>
          </template>
        </el-form-item>
        <el-form-item label="支付状态" prop="paystate">
          <template>
            <el-radio v-model="form.paystate" label="0">未支付</el-radio>
            <el-radio v-model="form.paystate" label="1">已支付</el-radio>
          </template>
        </el-form-item>
        <el-form-item label="支付金额" prop="money">
          <el-input v-model="form.money" placeholder="请输入支付金额" />
        </el-form-item>
        <el-form-item label="出入口名称" prop="entranceandexitname">
          <el-input v-model="form.entranceandexitname" placeholder="请输入出入口名称" />
        </el-form-item>
        <el-form-item label="支付时间" prop="payTime">
          <el-date-picker clearable
                          v-model="form.payTime"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="请选择出场时间">
          </el-date-picker>
        </el-form-item>
<!--        <el-form-item label="预留字段2" prop="numbertwo">
          <el-input v-model="form.numbertwo" placeholder="请输入预留字段2" />
        </el-form-item>
        <el-form-item label="预留字段3" prop="numberthree">
          <el-input v-model="form.numberthree" placeholder="请输入预留字段3" />
        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRecord, getRecord, delRecord, addRecord, updateRecord } from "@/api/parking/record";
import {getarkinglotinformations} from "@/api/system/user";

export default {
  name: "Record",
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
      // 停车记录表格数据
      recordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        license: null,
        parkinglotinformationid: null,
        admissiontime: null,
        exittime: null,
        licensepllatecolor: null,
        ordernumber: null,
        orderstate: null,
        paystate: null,
        money: null,
        entranceandexitname: null,
        payTime: null,
        numbertwo: null,
        numberthree: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        ordernumber: [
          { required: true, message: "订单编号不能为空", trigger: "blur" }
        ],
      }
    };
  },

  created() {
    console.log('---1111111111----------------,dayin')

    this.getarkinglotinformations(localStorage.getItem("uu"));
    this.getList();

  },
  methods: {


    getarkinglotinformations(id){
      getarkinglotinformations(id).then(res=>{
        this.parkinglotinformations=res.data
        console.log(9)
      })
    },
    /** 查询停车记录列表 */
    getList() {
      this.loading = true;
      this.queryParams.parkinglotinformationid=localStorage.getItem("uu")
      this.queryParams.paystate=0
      listRecord(this.queryParams).then(response => {
        this.recordList = response.rows;

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
        license: null,
        parkinglotinformationid: null,
        admissiontime: null,
        exittime: null,
        licensepllatecolor: null,
        ordernumber: null,
        orderstate: null,
        paystate: null,
        money: null,
        entranceandexitname: null,
        payTime: null,
        numbertwo: null,
        numberthree: null
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
   /* /!** 新增按钮操作 *!/
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加停车记录";
    },*/
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getRecord(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改停车记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateRecord(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRecord(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除停车记录编号为"' + ids + '"的数据项？').then(function() {
        return delRecord(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('parking/record/export', {
        ...this.queryParams
      }, `record_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
