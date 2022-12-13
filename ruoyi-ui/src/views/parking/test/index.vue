<template>
  <div class="app-container">

   <div style="margin-top:10px;">
     <span>最近离场缴费信息</span>
   </div>

    <el-table style="margin-top: 20px;" v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">
      <el-table-column v-if="false" label="id" align="center" prop="id" />
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
<!--      <el-table-column label="订单编号" align="center" prop="ordernumber" />-->
      <el-table-column label="订单状态" align="center" prop="orderstate">
        <template scope="scope">
          <span style="color: green" v-if="scope.row.orderstate==1">已支付</span>
          <span style="color: red" v-else-if="scope.row.orderstate==0">未支付</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="支付状态" v-if="false" align="center" prop="paystate" />-->
<!--      <el-table-column label="应付金额" align="center" prop="amountpayable" />
      <el-table-column label="优惠金额" align="center" prop="discountamount" />-->
      <el-table-column label="实付金额" align="center" prop="money" />
      <el-table-column label="支付方式" align="center" prop="paymentmethod" />
      <el-table-column label="出入口名称" align="center" prop="entranceandexitname" />
            <el-table-column label="进口照片" align="center" prop="numbertwo">
              <template slot-scope="scope">
                <el-image
                  style="width: 100px; height: 100px"
                  :src="scope.row.numbertwo">
                </el-image>
              </template>
            </el-table-column>
            <el-table-column label="出口照片" align="center" prop="numberthree" >
                <template slot-scope="scope">
                  <el-image
                    style="width: 100px; height: 100px"
                    :src="scope.row.numberthree"
                    :preview-src-list="scope.row.numberthree"
                  ></el-image>
                </template>
            </el-table-column>
<!--      <el-table-column label="支付时间" align="center" prop="payTime" >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.payTime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column>-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-show="scope.row.orderstate===2"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['parking:record:edit']"
          >现金</el-button>
          <el-button v-show="scope.row.orderstate===2"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="updateToRecordFromCoupon(scope.row)"
          >优惠卷</el-button>
        </template>
      </el-table-column>
    </el-table>



    <div  style="margin-top:150px;">离场待缴费信息</div>
    <el-table style="margin-top: 20px;" v-loading="false" :data="tableData" @selection-change="handleSelectionChange">
      <el-table-column v-if="false" label="id" align="center" prop="id" />
      <el-table-column label="车牌号" align="center" prop="license" />
      <el-table-column label="订单号" align="center" prop="ordernumber" />
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
<!--      <el-table-column label="车牌颜色" align="center" prop="licensepllatecolor">
        <template slot-scope="scope">
          <span style="color: red" v-if="scope.row.licensepllatecolor==0">未知</span>
          <span style="color: blue" v-if="scope.row.licensepllatecolor==1">蓝色</span>
          <span style="color: yellow" v-if="scope.row.licensepllatecolor==2">黄色</span>
          <span style="color: black" v-if="scope.row.licensepllatecolor==3">白色</span>
          <span style="color: green" v-if="scope.row.licensepllatecolor==4">黑色</span>
          <span style="color: green"  v-else-if="scope.row.licensepllatecolor===5">绿色</span>
        </template>
      </el-table-column>-->
      <!--      <el-table-column label="订单编号" align="center" prop="ordernumber" />-->
      <el-table-column label="订单状态" align="center" prop="orderstate">
        <template scope="scope">
          <span style="color: green" v-if="scope.row.orderstate==1">已支付</span>
          <span style="color: red" v-else-if="scope.row.orderstate==0">未支付</span>
          <span style="color: red" v-else-if="scope.row.orderstate==2">订单进行中</span>
        </template>
      </el-table-column>
      <!--      <el-table-column label="支付状态" v-if="false" align="center" prop="paystate" />-->
      <el-table-column label="应付金额" align="center" prop="amountpayable" />
      <el-table-column label="优惠金额" align="center" prop="discountamount" />
      <el-table-column label="实付金额" align="center" prop="money" />
<!--      <el-table-column label="支付方式" align="center" prop="paymentmethod" />-->
<!--      <el-table-column label="出入口名称" align="center" prop="entranceandexitname" />-->
<!--      <el-table-column label="进口照片" align="center" prop="numbertwo">
        <template slot-scope="scope">
          <el-image
            style="width: 100px; height: 100px"
            :src="scope.row.numbertwo">
          </el-image>
        </template>
      </el-table-column>
      <el-table-column label="出口照片" align="center" prop="numberthree" >
        <template slot-scope="scope">
          <el-image
            style="width: 100px; height: 100px"
            :src="scope.row.numberthree"
            :preview-src-list="scope.row.numberthree"
          ></el-image>
        </template>
      </el-table-column>-->
      <!--   <el-table-column label="支付时间" align="center" prop="payTime" >
       <template slot-scope="scope">
           <span>{{ parseTime(scope.row.payTime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
         </template>
       </el-table-column>-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
                    <el-button
                      size="mini"
                      type="text"
                      icon="el-icon-edit"
                      @click="handleUpdate(scope.row)"
                      v-hasPermi="['parking:record:edit']"
                    >现金</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="updateToRecordFromCoupon(scope.row)"
          >优惠卷</el-button>
        </template>
      </el-table-column>
    </el-table>
<!--    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />-->

    <!-- 添加或修改停车记录对话框 -->
<!--   <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
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
        <el-form-item label="车牌颜色" prop="licensepllatecolor">
          <el-input v-model="form.licensepllatecolor" placeholder="请输入车牌颜色" />
        </el-form-item>
        <el-form-item label="订单编号" prop="ordernumber">
          <el-input v-model="form.ordernumber" placeholder="请输入订单编号" />
        </el-form-item>
        <el-form-item label="订单状态0代表未支付1代表已支付" prop="orderstate">
          <el-input v-model="form.orderstate" placeholder="请输入订单状态0代表未支付1代表已支付" />
        </el-form-item>
        <el-form-item label="支付状态0代表未支付1代表已支付" prop="paystate">
          <el-input v-model="form.paystate" placeholder="请输入支付状态0代表未支付1代表已支付" />
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
    </el-dialog>-->
  </div>
</template>


<script>
import {
  listRecord,
  getRecord,
  getPayRecord,
  delRecord,
  addRecord,
  updateRecord,
  updateToRecord,
  updateToRecordFromCoupon
} from "@/api/parking/record";
import {getarkinglotinformations} from "@/api/system/user";

export default {
  name: "RecentDeparturePayment",
  data() {
    return {
      tableData:[],
      websock:null,
      lockReconnect: false, //是否真正建立连接
      timeout: 28 * 1000, //保持websocket的连接
      timeoutObj: null, //心跳心跳倒计时
      serverTimeoutObj: null, //心跳倒计时
      timeoutnum: null, //断开 重连倒计时

      parkinglotinformations:[],
      // 遮罩层
      loading: false,
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

    // 初始化websocket
    this.initWebSocket();

    //this.getPayRecord(localStorage.getItem("uu"));

    this.setInterval1();
  },
  destroyed() {
    //销毁
    this.websocketclose();
  },

  methods: {

    setInterval1(){

       setInterval(()=>{
              this.getPayRecordOrder();

      },5000)
    },
    getPayRecordOrder(){
      this.queryParams.parkinglotinformationid=localStorage.getItem("uu")
      this.queryParams.orderstate=2
      listRecord(this.queryParams).then(res=>{
        this.tableData=res.rows
      })
    },

    initWebSocket() {

      //建立websocket连接
      if ("WebSocket" in window){
        //连接服务端访问的url，我这里配置在了env中，就是上面在线测试工具中的地址,下面放了实例
        let ws ="ws://localhost:8080/websocket/"+JSON.parse(JSON.parse(localStorage.getItem("name")).data).username
        this.websock = new WebSocket(ws);
        this.websock.onopen = this.websocketonopen;
        this.websock.onerror = this.websocketonerror;
        this.websock.onmessage = this.websocketonmessage;
        this.websock.onclose = this.websocketclose;
      }
    },
    //重新连接
    reconnect() {
      var that = this;
      if (that.lockReconnect) {
        return;
      }
      that.lockReconnect = true;
      //没连接上会一直重连，设置延迟避免请求过多
      that.timeoutnum && clearTimeout(that.timeoutnum);
      that.timeoutnum = setTimeout(function () {
        //新连接
        that.initWebSocket();
        that.lockReconnect = false;
      }, 5000);
    },
    //重置心跳
    reset() {
      var that = this;
      //清除时间
      clearTimeout(that.timeoutObj);
      clearTimeout(that.serverTimeoutObj);
      //重启心跳
      that.start();
    },
    //开启心跳
    start() {
      var self = this;
      self.timeoutObj && clearTimeout(self.timeoutObj);
      self.serverTimeoutObj && clearTimeout(self.serverTimeoutObj);
      self.timeoutObj = setTimeout(function () {
        //这里发送一个心跳，后端收到后，返回一个心跳消息，
        if (self.websock.readyState == 1) {
          //连接正常

        } else {
          //否则重连
          self.reconnect();
        }
        self.serverTimeoutObj = setTimeout(function () {
          //超时关闭
          self.websock.close();
        }, self.timeout);
      }, self.timeout);
    },

    websocketonopen: function () {
       console.log("连接成功",)

      //可以通过send方法来向服务端推送消息，推送的消息在onMessage中可以打印出来查看并做一些业务处理。
      //this.websock.send("向服务端推送的消息内容")
    },
    websocketonerror: function (e) {
       console.log("连接失败",)
      this.reconnect();
    },
    websocketonmessage: function (e) {//JSON.parse(e.data); //这个是收到后端主动推送的值
      console.log("服务端消息的内容",e.data)
      if (e){
        let parse = JSON.parse(e.data) //将json字符串转为对象
        this.recordList=parse
        if (parse.type === 'add'){ //消息个数+1
          this.total = this.total + 1
          if (this.$route.path !== '/system_manager/system_manager_message'){
            this.mess = this.$message({
              showClose: true,
              message: '你有一条新的系统通知待查看',
              type: 'success',
              center:true,
              duration:0
            });
          }else {
            vue.$emit('flush',true)
          }
        } else if(parse.type === 'del'){
          this.total = this.total - 1
        }
        if (parse.type === 'message'){
          //判断是外厂还是内厂操作 跳转的route不同
          if(parse.maint === 'outSide'){ //外厂
            this.jumpRouter = API.backFlowApproveOutSidePath
            //如果当前页面就是将要跳转的页面，直接刷新当前页
            if (this.$route.path === API.backFlowApproveOutSidePath){
              vue.$emit('flush',true)
            }
          }else { //内厂
            this.jumpRouter = API.backFlowApprovePath
            //如果当前页面就是将要跳转的页面，直接刷新当前页
            if (this.$route.path === API.backFlowApprovePath){
              vue.$emit('flush',true)
            }
          }
          let notification = document.getElementsByClassName('el-notification')
          if (notification.length === 0){
            let that = this
            this.notifi = this.$notify({
              title: '维保流程消息提醒',
              message: parse.message,
              offset: 100,
              type:'success',
              duration: 0,
              onClick:function(){
                that.$router.push({
                  path: that.jumpRouter
                });
                //关闭消息通知弹窗
                that.notifi.close();
              }
            });
          }
        }
        //收到服务器信息，心跳重置
        this.reset();
      }
    },
    websocketclose: function (e) {
      // console.log("连接关闭",)
      //重连
      this.reconnect();
    },


    getarkinglotinformations(id){
      getarkinglotinformations(id).then(res=>{
        this.parkinglotinformations=res.data
      })
    },
    /** 查询停车记录列表 */
    getPayRecord(id){
      getPayRecord(id).then(res=>{

        this.recordList=res.data;
      })
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;

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
   /* handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加停车记录";
    },*/
    /**现金按钮操作 */
    handleUpdate(row) {
      console.log(row.id)
      updateToRecord(row.id).then(res=>{

        this.getPayRecordOrder();
      })
    },
    /** 优惠卷按钮 */
    updateToRecordFromCoupon(row) {
      console.log(row.id)
      updateToRecordFromCoupon(row.id).then(res=>{

        this.getPayRecordOrder();
      })
    },
    /** 提交按钮 */
  /*  submitForm() {
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
    },*/

    /** 导出按钮操作 */
   /* handleExport() {
      this.download('parking/record/export', {
        ...this.queryParams
      }, `record_${new Date().getTime()}.xlsx`)
    }*/
  }
};

</script>
