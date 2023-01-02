<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['parking:charging:add']"
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
          v-hasPermi="['parking:charging:edit']"
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
          v-hasPermi="['parking:charging:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['parking:charging:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="chargingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序列号" align="center" prop="id" />
      <el-table-column label="停车场" align="center" prop="parkingLotInformation.name" />
      <el-table-column label="计费方式" align="center" prop="type" >
        <template scope="scope">
          <span style="color: green" v-if="scope.row.type==0">常规+时段</span>
          <span style="color: green" v-else-if="scope.row.type==1">时长叠加</span>
          <span style="color: green" v-else-if="scope.row.type==2">分时段不同计费规则</span>
          <span style="color: green" v-else-if="scope.row.type==4">按车型计费</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['parking:charging:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['parking:charging:remove']"
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

    <!-- 添加或修改计费规则对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="130px">
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

        <el-form-item label="计费方式" prop="type">

          <template>
            <el-radio v-model="form.type" :label="0">常规+时段</el-radio>
            <el-radio v-model="form.type" :label="1">时长叠加</el-radio>
            <el-radio v-model="form.type" :label="2">分时段不同计费规则</el-radio>
          </template>
        </el-form-item>

        <el-form-item label="起步价" prop="startingprice">
          <el-input v-model="form.startingprice"  placeholder=""/>元
        </el-form-item>
        <el-form-item  label="起步价时长" prop="startingpriceduration">
          <el-input v-model="form.startingpriceduration" placeholder="请输入起步价时长" />分钟
          <div style="color:#F00">注意: 必须是60的倍数，最大不超过1440分钟</div>
        </el-form-item>
        <el-form-item label="每超过1小时加收" prop="increaseincome">
          <el-input v-model="form.increaseincome" placeholder="请输入每超过1小时加收" />元
        </el-form-item>
        <el-form-item v-if="this.form.type!=1" label="单日收费上限" prop="superiorlimit">
          <el-input v-model="form.superiorlimit" placeholder="请输入单日收费上限" />元
        </el-form-item>

        <el-divider content-position="center">计费时间段信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddParkingBillingPeriod">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteParkingBillingPeriod">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="parkingBillingPeriodList" :row-class-name="rowParkingBillingPeriodIndex" @selection-change="handleParkingBillingPeriodSelectionChange" ref="parkingBillingPeriod">
          <el-table-column type="selection" width="50" align="center" />
<!--          <el-table-column label="序号" align="center" prop="index" width="50"/>-->
          <el-table-column v-if="this.form.type==0 || this.form.type==2" label="开始时间" prop="startime" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.startime" placeholder="请输入开始时间" />
            </template>
          </el-table-column>
          <el-table-column v-if="this.form.type==0 || this.form.type==2" label="结束时间" prop="endtime" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.endtime" placeholder="请输入结束时间" />
            </template>
          </el-table-column>
          <el-table-column v-if="this.form.type==1" label="分钟计费（分钟）" prop="minutecharge" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.minutecharge" placeholder="请输入" />
            </template>
          </el-table-column>
          <el-table-column v-if="this.form.type==0 || this.form.type==1 || this.form.type==2" label="收费金额或者每小时加收(元)" prop="addmoney" width="250">
            <template slot-scope="scope">
              <el-input v-model="scope.row.addmoney" placeholder="请输入收费金额或者每小时加收" />
            </template>
          </el-table-column>

          <el-table-column  v-if="this.form.type==2" label="起步价" prop="startingprice" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.startingprice" placeholder="起步价默认时长60分钟" />
            </template>
          </el-table-column>
          <el-table-column  v-if="this.form.type==2" label="起步时长" prop="startingtime" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.startingtime" placeholder="请输入起步时长" />
            </template>
          </el-table-column>

          <el-table-column  v-if="this.form.type==2" label="	收费上限（元）" prop="superiorlimit" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.superiorlimit" placeholder="	请输入收费上限（元）" />
            </template>
          </el-table-column>
          <el-table-column  v-if="this.form.type==3" label="按时记或者一口价" prop="type" width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.type" placeholder="请选择按时记或者一口价">
                <el-option label="按时" value="0" />
                <el-option label="一口价" value="1" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column  v-if="this.form.type==3" label="是否重复收取起步价" prop="repea" width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.repea" placeholder="">
                <el-option label="重复收取起步价" value="0" />
                <el-option label="不重复收取" value="1" />
              </el-select>
            </template>
          </el-table-column>
        </el-table>
        <span v-if="this.form.type==0">
         注意正确的时间格式 例:07:00:00，时间段从低到高。最多可添加24个时间段,时段计费优先其他计费
 <div style="color:#F00">注意: 起步时长必须是60的倍数，最大不超过1440分钟</div>
       </span>
        <span v-if="this.form.type==1">
        注意分钟从低到高;最大值1440（即24小时）。最多可添加24组
 <div style="color:#F00">注意: 起步时长必须是60的倍数，最大不超过1440分钟</div>
       </span>
        <span v-if="this.form.type==2">
        注意时间从低到高,最多可添加5组
 <div style="color:#F00">注意: 起步时长必须是60的倍数，最大不超过1440分钟</div>
       </span>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCharging, getCharging, delCharging, addCharging, updateCharging } from "@/api/parking/charging";
import {getarkinglotinformations} from "@/api/system/user";

export default {
  name: "Charging",

  data() {
    return {
      parkinglotinformations:[],
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedParkingBillingPeriod: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 计费规则表格数据
      chargingList: [],
      // 计费时间段表格数据
      parkingBillingPeriodList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        startingprice: null,
        startingpriceduration: null,
        increaseincome: null,
        superiorlimit: null,
        parkinglotinformationid: null,
        type: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        startingprice: [
          { required: true, message: "起步价不能为空", trigger: "blur" }
        ],
        increaseincome: [
          { required: true, message: "每超过1小时加收不能为空", trigger: "blur" }
        ],
        /*superiorlimit: [
          { required: true, message: "单日收费上限不能为空", trigger: "blur" }
        ],*/
        parkinglotinformationid: [
          { required: true, message: "请选择停车场", trigger: "blur" }
        ],
        type: [
          { required: true, message: "类型不能为空", trigger: "change" }
        ]
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
    /** 查询计费规则列表 */
    getList() {
      this.loading = true;
      this.queryParams.parkinglotinformationid=localStorage.getItem("uu")
      listCharging(this.queryParams).then(response => {
        this.chargingList = response.rows;
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
        startingprice: null,
        startingpriceduration: null,
        increaseincome: null,
        superiorlimit: null,
        parkinglotinformationid: null,
        type: null
      };
      this.parkingBillingPeriodList = [];
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
      this.title = "添加计费规则";
      //this.form.type= 0;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCharging(id).then(response => {
        this.form = response.data;
        this.form.type=response.data.type
        this.parkingBillingPeriodList = response.data.parkingBillingPeriodList;
        this.open = true;
        this.title = "修改计费规则";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.parkingBillingPeriodList = this.parkingBillingPeriodList;
          if (this.form.id != null) {
            updateCharging(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCharging(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除计费规则编号为"' + ids + '"的数据项？').then(function() {
        return delCharging(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
	/** 计费时间段序号 */
    rowParkingBillingPeriodIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 计费时间段添加按钮操作 */
    handleAddParkingBillingPeriod() {
      let obj = {};
      obj.startime = "";
      obj.endtime = "";
      obj.addmoney = "";
      obj.startingprice = "";
      obj.startingtime = "";
      obj.type = "";
      obj.repea = "";
      obj.minutecharge = "";
      this.parkingBillingPeriodList.push(obj);
    },
    /** 计费时间段删除按钮操作 */
    handleDeleteParkingBillingPeriod() {
      if (this.checkedParkingBillingPeriod.length == 0) {
        this.$modal.msgError("请先选择要删除的计费时间段数据");
      } else {
        const parkingBillingPeriodList = this.parkingBillingPeriodList;
        const checkedParkingBillingPeriod = this.checkedParkingBillingPeriod;
        this.parkingBillingPeriodList = parkingBillingPeriodList.filter(function(item) {
          return checkedParkingBillingPeriod.indexOf(item.index) == -1
        });
      }
    },
    /** 复选框选中数据 */
    handleParkingBillingPeriodSelectionChange(selection) {
      this.checkedParkingBillingPeriod = selection.map(item => item.index)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('parking/charging/export', {
        ...this.queryParams
      }, `charging_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
