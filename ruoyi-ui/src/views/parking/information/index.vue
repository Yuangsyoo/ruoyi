<template>
  <div class="app-container">
    <el-form :model="queryParams"  ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="停车场名称" prop="name">
        <el-input

          v-model="queryParams.name"
          placeholder="请输入停车场名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="联系人" prop="contacts">
        <el-input
          v-model="queryParams.contacts"
          placeholder="请输入联系人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="联系电话" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入联系电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="地址" prop="address">
        <el-input
          v-model="queryParams.address"
          placeholder="请输入地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="是否开启月卡购买0开启1关闭" prop="monthlycardpurchase">
        <el-input
          v-model="queryParams.monthlycardpurchase"
          placeholder="请输入是否开启月卡购买0开启1关闭"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否开启临时车限制0开启1关闭" prop="temporaryvehiclerestrictions">
        <el-input
          v-model="queryParams.temporaryvehiclerestrictions"
          placeholder="请输入是否开启临时车限制0开启1关闭"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否开启平台支付0开启1关闭" prop="platformpaymentState">
        <el-input
          v-model="queryParams.platformpaymentState"
          placeholder="请输入是否开启平台支付0开启1关闭"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="超时补费0开启1关闭" prop="overtimecompensation">
        <el-input
          v-model="queryParams.overtimecompensation"
          placeholder="请输入超时补费0开启1关闭"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="无记录离场收起步价0开启1关闭" prop="norecorddeparture">
        <el-input
          v-model="queryParams.norecorddeparture"
          placeholder="请输入无记录离场收起步价0开启1关闭"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否启用支付宝停车缴费0开启1关闭" prop="alipaypaymentState">
        <el-input
          v-model="queryParams.alipaypaymentState"
          placeholder="请输入是否启用支付宝停车缴费0开启1关闭"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否启用微信停车缴费0开启1关闭" prop="wechatpaymentState">
        <el-input
          v-model="queryParams.wechatpaymentState"
          placeholder="请输入是否启用微信停车缴费0开启1关闭"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否启用银联停车缴费0开启1关闭" prop="unionpaypaymentState">
        <el-input
          v-model="queryParams.unionpaypaymentState"
          placeholder="请输入是否启用银联停车缴费0开启1关闭"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否启用ETC停车缴费0开启1关闭" prop="etcpaymentState">
        <el-input
          v-model="queryParams.etcpaymentState"
          placeholder="请输入是否启用ETC停车缴费0开启1关闭"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否启用农信停车缴费0开启1关闭" prop="ruralcreditpaymentState">
        <el-input
          v-model="queryParams.ruralcreditpaymentState"
          placeholder="请输入是否启用农信停车缴费0开启1关闭"
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
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['parking:information:add']"
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
          v-hasPermi="['parking:information:edit']"
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
          v-hasPermi="['parking:information:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['parking:information:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="informationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序列号
" align="center" prop="id" />
      <el-table-column label="名称" align="center" prop="name" />
<!--      <el-table-column label="门面照" align="center" prop="picture" />-->
      <el-table-column label="联系人" align="center" prop="contacts" />
      <el-table-column label="联系电话" align="center" prop="phone" />
      <el-table-column label="QQ号" align="center" prop="qq" />
      <el-table-column label="地址" align="center" prop="address" />
      <el-table-column label="状态" align="center" prop="state" width="50">
        <template scope="scope">
          <span style="color: green" v-if="scope.row.state==0">正常</span>
          <span style="color: red" v-else-if="scope.row.state==1">禁用</span>
        </template>
      </el-table-column>


      <el-table-column label="车位个数" align="center" prop="number" />
      <el-table-column label="剩余车位" align="center" prop="remainingParkingSpace" />
      <el-table-column label="支付离场时间" align="center" prop="payleavingtime" width="100">
        <template scope="scope">
          <span style="color: green" >{{scope.row.payleavingtime}}分钟</span>
        </template>
      </el-table-column>
      <el-table-column label="默认月费" align="center" prop="monthlyfee" />
      <el-table-column label="收费说明" align="center" prop="chargedescription">
        <template scope="scope">
          <span style="color: green" >{{scope.row.chargedescription}}(不参与计算)</span>
        </template>
      </el-table-column>

      <el-table-column label="营业起使时间" align="center" prop="starttime" width="120"/>

      <el-table-column label="营业结束时间" align="center" prop="endtime" width="120"/>


<!--      <el-table-column label="月卡购买最小月数" align="center" prop="minmonths" />
      <el-table-column label="月卡购买最大月数" align="center" prop="maxmonths" />-->
      <el-table-column label="月卡购买" align="center" prop="monthlycardpurchase">
        <template scope="scope">
          <span style="color: green" v-if="scope.row.monthlycardpurchase==0">开启</span>
          <span style="color: red" v-else-if="scope.row.monthlycardpurchase==1">关闭</span>
        </template>
      </el-table-column>
      <el-table-column label="临时车限制" align="center" prop="temporaryvehiclerestrictions" >
        <template scope="scope">
          <span style="color: green" v-if="scope.row.temporaryvehiclerestrictions==0">开启</span>
          <span style="color: red" v-else-if="scope.row.temporaryvehiclerestrictions==1">关闭</span>
        </template>
      </el-table-column>
      <el-table-column label="无记录离场" align="center" prop="norecorddeparture" >
        <template scope="scope">
          <span style="color: green" v-if="scope.row.norecorddeparture==0">开启</span>
          <span style="color: red" v-else-if="scope.row.norecorddeparture==1">关闭</span>
        </template>
      </el-table-column>
      <el-table-column label="场内收费二维码" align="center" prop="onSiteQRCode" width="150px" >
        <template slot-scope="scope">
          <el-image
            style="width: 150px; height: 150px"
            :src="scope.row.onSiteQRCode">
          </el-image>
        </template>
      </el-table-column>
      <el-table-column label="农信无感支付" align="center" prop="platformpaymentState" >
        <template scope="scope">
          <span style="color: green" v-if="scope.row.platformpaymentState==0">开启</span>
          <span style="color: red" v-else-if="scope.row.platformpaymentState==1">关闭</span>
        </template>
      </el-table-column>
<!--
      <el-table-column label="超时补费" align="center" prop="overtimecompensation" >
        <template scope="scope">
          <span style="color: green" v-if="scope.row.overtimecompensation==0">开启</span>
          <span style="color: red" v-else-if="scope.row.overtimecompensation==1">关闭</span>
        </template>
      </el-table-column>

      <el-table-column label="支付宝停车缴费" align="center" prop="alipaypaymentState" >
        <template scope="scope">
          <span style="color: green" v-if="scope.row.alipaypaymentState==0">开启</span>
          <span style="color: red" v-else-if="scope.row.alipaypaymentState==1">关闭</span>
        </template>
      </el-table-column>
      <el-table-column label="微信停车缴费" align="center" prop="wechatpaymentState">
        <template scope="scope">
          <span style="color: green" v-if="scope.row.wechatpaymentState==0">开启</span>
          <span style="color: red" v-else-if="scope.row.wechatpaymentState==1">关闭</span>
        </template>
      </el-table-column>
      <el-table-column label="银联停车缴费" align="center" prop="unionpaypaymentState" >
        <template scope="scope">
          <span style="color: green" v-if="scope.row.unionpaypaymentState==0">开启</span>
          <span style="color: red" v-else-if="scope.row.unionpaypaymentState==1">关闭</span>
        </template>
      </el-table-column>
      <el-table-column label="ETC停车缴费" align="center" prop="etcpaymentState" >
        <template scope="scope">
          <span style="color: green" v-if="scope.row.etcpaymentState==0">开启</span>
          <span style="color: red" v-else-if="scope.row.etcpaymentState==1">关闭</span>
        </template>
      </el-table-column>
      <el-table-column label="农信停车缴费" align="center" prop="ruralcreditpaymentState" >
        <template scope="scope">
          <span style="color: green" v-if="scope.row.ruralcreditpaymentState==0">开启</span>
          <span style="color: red" v-else-if="scope.row.ruralcreditpaymentState==1">关闭</span>
        </template>
      </el-table-column>-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['parking:information:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['parking:information:remove']"
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

    <!-- 添加或修改停车场管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="停车场名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入停车场名称" />
        </el-form-item>
        <el-form-item label="停车场登录密码" prop="name">
          <el-input v-model="form.loginPassword" placeholder="请输入停车场登录密码" />
        </el-form-item>
<!--        <el-form-item label="门面照" prop="picture">
          <el-input v-model="form.picture" placeholder="请输入门面照" />
        </el-form-item>-->
        <el-form-item label="联系人" prop="contacts">
          <el-input v-model="form.contacts" placeholder="请输入联系人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="QQ号" prop="qq">
          <el-input v-model="form.qq" placeholder="请输入QQ号" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>

        <el-form-item label="状态" prop="state">
            <template>
              <el-radio v-model="form.state" label="0">启用</el-radio>
              <el-radio v-model="form.state" label="1">禁用</el-radio>
            </template>
        </el-form-item>
        <el-form-item label="车位个数" prop="number">
          <el-input v-model="form.number" placeholder="请输入车位个数" />
        </el-form-item>
        <el-form-item label="支付离场时间" prop="payleavingtime">
          <el-input v-model="form.payleavingtime" placeholder="请输入支付离场时间" />
        </el-form-item>
        <el-form-item label="默认月费" prop="monthlyfee">
          <el-input v-model="form.monthlyfee" placeholder="请输入默认月费" />
        </el-form-item>
        <el-form-item label="收费说明不参与计算" prop="chargedescription">
          <el-input v-model="form.chargedescription" placeholder="请输入收费说明不参与计算" />
        </el-form-item>
        <el-form-item label="营业起使时间" prop="starttime">
          <el-input v-model="form.starttime" placeholder="请输入正确的时间格式 例:00:00:00"/>

        </el-form-item>
        <el-form-item label="营业结束时间" prop="endtime">
          <el-input v-model="form.endtime" placeholder="请输入正确的时间格式 例:24:00:00"/>

        </el-form-item>
<!--        <el-form-item label="月卡购买最小月数" prop="minmonths">
          <el-input v-model="form.minmonths" placeholder="请输入月卡购买最小月数" />
        </el-form-item>
        <el-form-item label="月卡购买最大月数" prop="maxmonths">
          <el-input v-model="form.maxmonths" placeholder="请输入月卡购买最大月数" />
        </el-form-item>-->
        <el-form-item label="月卡购买" prop="monthlycardpurchase">
          <template>
            <el-radio v-model="form.monthlycardpurchase" label="0">开启</el-radio>
            <el-radio v-model="form.monthlycardpurchase" label="1">关闭</el-radio>
          </template>
        </el-form-item>
        <el-form-item label="临时车限制" prop="temporaryvehiclerestrictions">
          <template>
            <el-radio v-model="form.temporaryvehiclerestrictions" label="0">开启</el-radio>
            <el-radio v-model="form.temporaryvehiclerestrictions" label="1">关闭</el-radio>
          </template>
        </el-form-item>
        <el-form-item label="农信无感支付" prop="platformpaymentState">
          <template>
            <el-radio v-model="form.platformpaymentState" label="0">开启</el-radio>
            <el-radio v-model="form.platformpaymentState" label="1">关闭</el-radio>
          </template>
        </el-form-item>
        <el-form-item label="超时补费" prop="overtimecompensation">
          <template>
            <el-radio v-model="form.overtimecompensation" label="0">开启</el-radio>
            <el-radio v-model="form.overtimecompensation" label="1">关闭</el-radio>
          </template>
        </el-form-item>
        <el-form-item  label="无记录离场" prop="norecorddeparture">
          <template>
            <el-radio v-model="form.norecorddeparture" label="0">开启</el-radio>
            <el-radio v-model="form.norecorddeparture" label="1">关闭</el-radio>
          </template>
        </el-form-item>
        <el-form-item v-show="false" label="支付宝停车缴费" prop="alipaypaymentState">
          <template>
            <el-radio v-model="form.alipaypaymentState" label="0">开启</el-radio>
            <el-radio v-model="form.alipaypaymentState" label="1">关闭</el-radio>
          </template>
        </el-form-item>
        <el-form-item  label="微信停车缴费" prop="wechatpaymentState">
          <template>
            <el-radio v-model="form.wechatpaymentState" label="0">开启</el-radio>
            <el-radio v-model="form.wechatpaymentState" label="1">关闭</el-radio>
          </template>
        </el-form-item>
        <div v-show="this.form.wechatpaymentState==0">
          <el-form-item   label="微信支付商户号" prop="wechatpaymentId">
            <el-input v-model="form.wechatpaymentId" placeholder="请输入微信支付关联id" />
          </el-form-item>
        </div>

        <el-form-item v-show="false" label="银联停车缴费" prop="unionpaypaymentState">
          <template>
            <el-radio v-model="form.unionpaypaymentState" label="0">开启</el-radio>
            <el-radio v-model="form.unionpaypaymentState" label="1">关闭</el-radio>
          </template>
        </el-form-item>
        <el-form-item v-show="false" label="ETC停车缴费" prop="etcpaymentState">
          <template>
            <el-radio v-model="form.etcpaymentState" label="0">开启</el-radio>
            <el-radio v-model="form.etcpaymentState" label="1">关闭</el-radio>
          </template>
        </el-form-item>
        <el-form-item  label="农信停车缴费" prop="ruralcreditpaymentState">
          <template>
            <el-radio v-model="form.ruralcreditpaymentState" label="0">开启</el-radio>
            <el-radio v-model="form.ruralcreditpaymentState" label="1">关闭</el-radio>
          </template>
        </el-form-item>
        <div v-show="this.form.ruralcreditpaymentState==0">
          <el-form-item label="农信支付商户号" prop="ruralcreditpaymentId">
            <el-input v-model="form.ruralcreditpaymentId" placeholder="请输入农信支付关联id" />
          </el-form-item>
        </div>
      <!--        <el-form-item label="平台支付关联id" prop="platformpaymentId">
          <el-input v-model="form.platformpaymentId" placeholder="请输入平台支付关联id" />
        </el-form-item>
        <el-form-item label="支付宝支付关联id" prop="alipaypaymentId">
          <el-input v-model="form.alipaypaymentId" placeholder="请输入支付宝支付关联id" />
        </el-form-item>

        <el-form-item label="银联支付关联id" prop="unionpaypaymentId">
          <el-input v-model="form.unionpaypaymentId" placeholder="请输入银联支付关联id" />
        </el-form-item>
        <el-form-item label="ETC支付关联id" prop="etcpaymentId">
          <el-input v-model="form.etcpaymentId" placeholder="请输入ETC支付关联id" />
        </el-form-item>
        <el-form-item label="备用字段4" prop="numberfour">
          <el-input v-model="form.numberfour" placeholder="请输入备用字段4" />
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
import { listInformation, getInformation, delInformation, addInformation, updateInformation1 } from "@/api/parking/information";

export default {
  name: "Information",
  data() {
    return {
      ww:true,
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
      // 停车场管理表格数据
      informationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        id:null,
        pageNum: 1,
        pageSize: 10,
        name: null,
        picture: null,
        contacts: null,
        phone: null,
        qq: null,
        address: null,
        state: null,
        number: null,
        payleavingtime: null,
        monthlyfee: null,
        chargedescription: null,
        starttime: null,
        endtime: null,
        minmonths: null,
        maxmonths: null,
        monthlycardpurchase: null,
        temporaryvehiclerestrictions: null,
        platformpaymentState: null,
        overtimecompensation: null,
        norecorddeparture: null,
        alipaypaymentState: null,
        wechatpaymentState: null,
        unionpaypaymentState: null,
        etcpaymentState: null,
        ruralcreditpaymentState: null,
        platformpaymentId: null,
        alipaypaymentId: null,
        wechatpaymentId: null,
        unionpaypaymentId: null,
        etcpaymentId: null,
        ruralcreditpaymentId: null,
        remainingParkingSpace:null,
        numberfour: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "停车场名称不能为空", trigger: "blur" }
        ],
        address: [
          { required: true, message: "地址不能为空", trigger: "blur" }
        ],
        state: [
          { required: true, message: "停车场状态0开启1关闭不能为空", trigger: "blur" }
        ],
        number: [
          { required: true, message: "车位个数不能为空", trigger: "blur" }
        ],
        payleavingtime: [
          { required: true, message: "支付离场时间不能为空", trigger: "blur" }
        ],
        monthlycardpurchase: [
          { required: true, message: "是否开启月卡购买0开启1关闭不能为空", trigger: "blur" }
        ],
        freetime:[
          { required: true, message: "免费时常不能为空", trigger: "blur" }
        ],
        temporaryvehiclerestrictions:[
          { required: true, message: "临时车限制不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    if (localStorage.getItem("uu")===0){
      this.ww=true;
    }else {
      this.ww=false;
    }
  },
  methods: {
    /** 查询停车场管理列表 */
    getList() {
      this.loading = true;
      this.queryParams.id=localStorage.getItem("uu")
      listInformation(this.queryParams).then(response => {
        this.informationList = response.rows;
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
        freetime:null,
        loginPassword:"123456",
        name: null,
        picture: null,
        contacts: null,
        phone: null,
        qq: null,
        address: null,
        state: null,
        number: null,
        payleavingtime: null,
        monthlyfee: null,
        chargedescription: null,
        starttime: null,
        endtime: null,
        minmonths: null,
        maxmonths: null,
        monthlycardpurchase: null,
        temporaryvehiclerestrictions: null,
        platformpaymentState: null,
        overtimecompensation: null,
        norecorddeparture: null,
        alipaypaymentState: null,
        wechatpaymentState: null,
        unionpaypaymentState: null,
        etcpaymentState: null,
        ruralcreditpaymentState: null,
        platformpaymentId: null,
        alipaypaymentId: null,
        wechatpaymentId: null,
        unionpaypaymentId: null,
        etcpaymentId: null,
        ruralcreditpaymentId: null,
        idAdress: null,
        numberfour: null
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
      this.title = "添加停车场管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getInformation(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改停车场管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateInformation1(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addInformation(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除停车场管理编号为"' + ids + '"的数据项？').then(function() {
        return delInformation(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('parking/information/export', {
        ...this.queryParams
      }, `information_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
