<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
<!--      <el-form-item label="停车场" prop="parkinglotinformationid">
        <el-select v-model="queryParams.parkinglotinformationid" clearable  placeholder="请选择">
          <el-option
            v-for="item in parkinglotinformations"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>-->
      <el-form-item label="车牌号" prop="license">
        <el-input
          v-model="queryParams.license"
          placeholder="请输入车牌号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户姓名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入白名单用户姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="地址" prop="adress">
        <el-input
          v-model="queryParams.adress"
          placeholder="请输入地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="操作员" prop="operator">
        <el-input
          v-model="queryParams.operator"
          placeholder="请输入操作员"
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
          v-hasPermi="['parking:whitelist:add']"
        >添加白名单</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['parking:whitelist:edit']"
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
          v-hasPermi="['parking:whitelist:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['parking:whitelist:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
      <el-button
        type="info"
        plain
        icon="el-icon-upload2"
        size="mini"
        @click="handleImport"
        v-hasPermi="['parking:whitelist:import']"
      >导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="whitelistList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序列号" align="center" prop="id" />
      <el-table-column label="停车场" align="center" prop="parkingLotInformation.name" />
      <el-table-column label="车牌号" align="center" prop="license" />
      <el-table-column label="开始时间" align="center" prop="starttime" width="180">
<!--        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.starttime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>-->
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endtime" width="180">
<!--        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endtime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>-->
      </el-table-column>
      <el-table-column label="添加时间" align="center" prop="addtime" width="180">
<!--        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.addtime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>-->
      </el-table-column>
      <el-table-column label="姓名" align="center" prop="name" />
      <el-table-column label="手机号" align="center" prop="phone" />
      <el-table-column label="地址" align="center" prop="adress" />
      <el-table-column label="状态" align="center" prop="state">
        <template scope="scope">
          <span style="color: green" v-if="scope.row.state==0">使用中</span>
          <span style="color: red" v-else-if="scope.row.state==1">已过期</span>
        </template>
      </el-table-column>

      <el-table-column label="操作员" align="center" prop="operator" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['parking:whitelist:edit']"
          >白名单续期</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['parking:whitelist:remove']"
          >白名单删除</el-button>
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
    <!-- 添加或修改停车场白名单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
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
        <el-form-item label="车牌号" prop="license">
          <el-input v-model="form.license" placeholder="请输入车牌号" />
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

        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入白名单用户姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="地址" prop="adress">
          <el-input v-model="form.adress" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="收费" prop="monthlyfee">
          <el-input v-model="form.monthlyfee" placeholder="请输入费用" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 白名单导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport+'&parkingLotInformationId='+parkingLotInformationId"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" /> 是否更新已经存在的用户数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>

</template>

<script>
import { listWhitelist, getWhitelist, delWhitelist, addWhitelist, updateWhitelist } from "@/api/parking/whitelist";
import {getarkinglotinformations} from "@/api/system/user";
import { getToken } from "@/utils/auth";

export default {
  name: "Whitelist",
  data() {
    return {
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/parking/whitelist/importData" // todo
      },
      parkingLotInformationId:'',
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
      // 停车场白名单表格数据
      whitelistList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        parkinglotinformationid: null,
        license: null,
        starttime: null,
        endtime: null,
        addtime: null,
        name: null,
        phone: null,
        adress: null,
        monthlyfee: null,
        state: null,
        operator: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        parkinglotinformationid: [
          { required: true, message: "停车场id不能为空", trigger: "blur" }
        ],
        license: [
          { required: true, message: "车牌号不能为空", trigger: "blur" }
        ],
        monthlyfee: [
          { required: true, message: "费用不能为空", trigger: "blur" }
        ],
      }
    };
  },

  created() {
   this.parkingLotInformationId=localStorage.getItem("uu");
    this.getList();
    this.getarkinglotinformations(localStorage.getItem("uu"));
  },
  methods: {
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "白名单信息导入"; // todo
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('parking/whitelist/importTemplate', {
      }, `stu_base_template_${new Date().getTime()}.xlsx`)  // todo
    },
// 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
// 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
// 提交上传文件
    submitFileForm() {
     this.upload.parkingLotInformationId=localStorage.getItem("uu")
      this.$refs.upload.submit();
    },




    getarkinglotinformations(id){
      getarkinglotinformations(id).then(res=>{
        this.parkinglotinformations=res.data
      })
    },
    /** 查询停车场白名单列表 */
    getList() {
      this.loading = true;
      this.queryParams.parkinglotinformationid=localStorage.getItem("uu")
      listWhitelist(this.queryParams).then(response => {
        this.whitelistList = response.rows;
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
        license: null,
        starttime: null,
        endtime: null,
        addtime: null,
        name: null,
        phone: null,
        adress: null,
        monthlyfee: null,
        state: null,
        operator: null
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
      this.title = "添加白名单";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getWhitelist(id).then(response => {
        this.form = response.data;
        this.form.monthlyfee=null;

        this.open = true;
        this.title = "白名单续期";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateWhitelist(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addWhitelist(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除停车场白名单编号为"' + ids + '"的数据项？').then(function() {
        return delWhitelist(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('parking/whitelist/export', {
        ...this.queryParams
      }, `whitelist_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
