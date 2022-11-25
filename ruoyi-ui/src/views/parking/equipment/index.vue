<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="停车场id" prop="parkinglotinformationid">
        <el-input
          v-model="queryParams.parkinglotinformationid"
          placeholder="请输入停车场id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入设备名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="摄象机序列号" prop="cameraserialnumber">
        <el-input
          v-model="queryParams.cameraserialnumber"
          placeholder="摄象机序列号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="进出口闸门" prop="direction">
        <el-input
          v-model="queryParams.direction"
          placeholder="0代表进口闸门1代表出口闸门"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="控制板" prop="controlplate">
        <el-input
          v-model="queryParams.controlplate"
          placeholder="0红1黄2蓝3模拟摄像机"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>


      <el-form-item label="设备状态" prop="state">
        <el-input
          v-model="queryParams.state"
          placeholder="请输入0代表正常1代表异常"
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
          v-hasPermi="['parking:equipment:add']"
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
          v-hasPermi="['parking:equipment:edit']"
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
          v-hasPermi="['parking:equipment:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['parking:equipment:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="equipmentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="停车场id" align="center" prop="parkinglotinformationid" />
      <el-table-column label="设备名称" align="center" prop="name" />
      <el-table-column label="摄象机序列号" align="center" prop="cameraserialnumber" />
      <el-table-column label="主板序列号" align="center" prop="motherboardserialnumber" />
      <el-table-column label="0代表进口闸门1代表出口闸门" align="center" prop="direction" />
      <el-table-column label="控制板" align="center" prop="controlplate" />
      <el-table-column label="一行显示" align="center" prop="onedisplay" />
      <el-table-column label="二行显示" align="center" prop="twodisplay" />
      <el-table-column label="三行显示" align="center" prop="threedisplay" />
      <el-table-column label="四行显示" align="center" prop="fourdisplay" />
      <el-table-column label="二维码" align="center" prop="qrcode" />
      <el-table-column label="0代表正常1代表异常" align="center" prop="state" />
      <el-table-column label="ip地址" align="center" prop="ipadress" />
      <el-table-column label="无记录离场0开启1关闭" align="center" prop="departurewithoutrecords" />
      <el-table-column label="车牌防伪0开启1关闭" align="center" prop="licenseplatanticounterfeiting" />
      <el-table-column label="特定车辆出0开启1关闭" align="center" prop="specificvehicleexit" />
      <el-table-column label="余位屏0开启1关闭" align="center" prop="residualscreen" />
      <el-table-column label="音量范围0-10" align="center" prop="volume" />
      <el-table-column label="预留字段" align="center" prop="numberone" />
      <el-table-column label="预留字段2" align="center" prop="numbertwo" />
      <el-table-column label="预留字段3" align="center" prop="numberthree" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['parking:equipment:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['parking:equipment:remove']"
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

    <!-- 添加或修改停车场设备管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="停车场id" prop="parkinglotinformationid">
          <el-input v-model="form.parkinglotinformationid" placeholder="请输入停车场id" />
        </el-form-item>
        <el-form-item label="设备名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入设备名称" />
        </el-form-item>
        <el-form-item label="摄象机序列号" prop="cameraserialnumber">
          <el-input v-model="form.cameraserialnumber" placeholder="请输入摄象机序列号" />
        </el-form-item>
        <el-form-item label="主板序列号" prop="motherboardserialnumber">
          <el-input v-model="form.motherboardserialnumber" placeholder="请输入主板序列号" />
        </el-form-item>
        <el-form-item label="0代表进口闸门1代表出口闸门" prop="direction">
          <el-input v-model="form.direction" placeholder="请输入0代表进口闸门1代表出口闸门" />
        </el-form-item>
        <el-form-item label="控制板" prop="controlplate">
          <el-input v-model="form.controlplate" placeholder="请输入控制板" />
        </el-form-item>
        <el-form-item label="一行显示" prop="onedisplay">
          <el-input v-model="form.onedisplay" placeholder="请输入一行显示" />
        </el-form-item>
        <el-form-item label="二行显示" prop="twodisplay">
          <el-input v-model="form.twodisplay" placeholder="请输入二行显示" />
        </el-form-item>
        <el-form-item label="三行显示" prop="threedisplay">
          <el-input v-model="form.threedisplay" placeholder="请输入三行显示" />
        </el-form-item>
        <el-form-item label="四行显示" prop="fourdisplay">
          <el-input v-model="form.fourdisplay" placeholder="请输入四行显示" />
        </el-form-item>
        <el-form-item label="二维码" prop="qrcode">
          <el-input v-model="form.qrcode" placeholder="请输入二维码" />
        </el-form-item>
        <el-form-item label="0代表正常1代表异常" prop="state">
          <el-input v-model="form.state" placeholder="请输入0代表正常1代表异常" />
        </el-form-item>
        <el-form-item label="ip地址" prop="ipadress">
          <el-input v-model="form.ipadress" placeholder="请输入ip地址" />
        </el-form-item>
        <el-form-item label="无记录离场0开启1关闭" prop="departurewithoutrecords">
          <el-input v-model="form.departurewithoutrecords" placeholder="请输入无记录离场0开启1关闭" />
        </el-form-item>
        <el-form-item label="车牌防伪0开启1关闭" prop="licenseplatanticounterfeiting">
          <el-input v-model="form.licenseplatanticounterfeiting" placeholder="请输入车牌防伪0开启1关闭" />
        </el-form-item>
        <el-form-item label="特定车辆出0开启1关闭" prop="specificvehicleexit">
          <el-input v-model="form.specificvehicleexit" placeholder="请输入特定车辆出0开启1关闭" />
        </el-form-item>
        <el-form-item label="余位屏0开启1关闭" prop="residualscreen">
          <el-input v-model="form.residualscreen" placeholder="请输入余位屏0开启1关闭" />
        </el-form-item>
        <el-form-item label="音量范围0-10" prop="volume">
          <el-input v-model="form.volume" placeholder="请输入音量范围0-10" />
        </el-form-item>
        <el-form-item label="预留字段" prop="numberone">
          <el-input v-model="form.numberone" placeholder="请输入预留字段" />
        </el-form-item>
        <el-form-item label="预留字段2" prop="numbertwo">
          <el-input v-model="form.numbertwo" placeholder="请输入预留字段2" />
        </el-form-item>
        <el-form-item label="预留字段3" prop="numberthree">
          <el-input v-model="form.numberthree" placeholder="请输入预留字段3" />
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
import { listEquipment, getEquipment, delEquipment, addEquipment, updateEquipment } from "@/api/parking/equipment";

export default {
  name: "Equipment",
  data() {
    return {
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
      // 停车场设备管理表格数据
      equipmentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        parkinglotinformationid: null,
        name: null,
        cameraserialnumber: null,
        motherboardserialnumber: null,
        direction: null,
        controlplate: null,
        onedisplay: null,
        twodisplay: null,
        threedisplay: null,
        fourdisplay: null,
        qrcode: null,
        state: null,
        ipadress: null,
        departurewithoutrecords: null,
        licenseplatanticounterfeiting: null,
        specificvehicleexit: null,
        residualscreen: null,
        volume: null,
        numberone: null,
        numbertwo: null,
        numberthree: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "设备名称不能为空", trigger: "blur" }
        ],
        cameraserialnumber: [
          { required: true, message: "摄象机序列号不能为空", trigger: "blur" }
        ],
        direction: [
          { required: true, message: "0代表进口闸门1代表出口闸门不能为空", trigger: "blur" }
        ],
        qrcode: [
          { required: true, message: "二维码不能为空", trigger: "blur" }
        ],
        ipadress: [
          { required: true, message: "ip地址不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询停车场设备管理列表 */
    getList() {
      this.loading = true;
      listEquipment(this.queryParams).then(response => {
        this.equipmentList = response.rows;
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
        name: null,
        cameraserialnumber: null,
        motherboardserialnumber: null,
        direction: null,
        controlplate: null,
        onedisplay: null,
        twodisplay: null,
        threedisplay: null,
        fourdisplay: null,
        qrcode: null,
        state: null,
        ipadress: null,
        departurewithoutrecords: null,
        licenseplatanticounterfeiting: null,
        specificvehicleexit: null,
        residualscreen: null,
        volume: null,
        numberone: null,
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加停车场设备管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getEquipment(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改停车场设备管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateEquipment(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addEquipment(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除停车场设备管理编号为"' + ids + '"的数据项？').then(function() {
        return delEquipment(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('parking/equipment/export', {
        ...this.queryParams
      }, `equipment_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
