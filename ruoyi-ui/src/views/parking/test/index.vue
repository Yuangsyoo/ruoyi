<template>
  <div class="app-container">

    <!-- 入场照片 -->
    <div class="tupian">
      <el-image class="zhaopian" :src="recordList[0].numbertwo">
          </el-image>
      <div class="zhaopianxia">入场识别机</div>
      <div class="zhaopianshijian">入场时间：{{ recordList[0].admissiontime }}</div>
    </div>
    <!-- 出场照片 -->
    <div class="tupian tupian2">
      <el-image class="zhaopian" :src="recordList[0].numberthree">
          </el-image>
      <div class="zhaopianxia">出场识别机</div>
      <div class="zhaopianshijian">出场时间：{{ recordList[0].exittime }}</div>
    </div>

    <div class="tupian_1" style="margin-top:2%">
      <div style="line-height: 60px;font-size:26px;font-weight: 550;letter-spacing:2px;">出口车辆记录</div>
      <div style="border: 1px #457ef9 solid;">
        <!-- 表头 -->
        <div style="color: white;background-color: #1b75d5;line-height: 40px;">
          <div class="biaotou biaotou1">车牌号</div>
          <div class="biaotou biaotou2">入场时间</div>
          <div class="biaotou biaotou2">出场时间</div>
          <div class="biaotou biaotou3">金额</div>
          <div class="biaotou biaotou1">订单状态</div>
        </div>
        <!-- 表体 -->
        <div style="line-height: 40px;" v-for="(item,index) in tableData">
          <div class="biaotou biaotou1">{{ item.license }}</div>
          <div class="biaotou biaotou2">{{ item.admissiontime }}</div>
          <div class="biaotou biaotou2">{{ item.exittime }}</div>
          <div class="biaotou biaotou3">{{ item.money }}</div>
          <div class="biaotou biaotou1">
            <span style="color: green" v-if="item.orderstate == 1">已支付</span>
            <span style="color: red" v-else-if="item.orderstate == 0">未支付</span>
            <span style="color: red" v-else-if="item.orderstate == 2">订单进行中</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 最近的离场缴费信息 -->
    <div class="tupian_1 tupian2" style="margin-top:2%">
      <div style="line-height: 60px;font-size:26px;font-weight: 550;letter-spacing:2px;">收费信息</div>
      <div class="shoufeixinxi">
        <div style="width: 90%;margin: auto;">
          <span class="shoufeixinxi_span">停车时长：{{recordList[0].date}}</span>
          <span class="shoufeixinxi_span">收费金额：{{ recordList[0].money }}元</span>
        </div>
        <div style="width: 90%;margin: auto;">
          <div class="shoufeixinxi_span">
            <span>车牌颜色：</span>
            <span style="color: red" v-if="recordList[0].licensepllatecolor == 0">未知</span>
            <span style="color: blue" v-if="recordList[0].licensepllatecolor == 1">蓝色</span>
            <span style="color: yellow" v-if="recordList[0].licensepllatecolor == 2">黄色</span>
            <span style="color: black" v-if="recordList[0].licensepllatecolor == 3">白色</span>
            <span style="color: green" v-if="recordList[0].licensepllatecolor == 4">黑色</span>
            <span style="color: green" v-else-if="recordList[0].licensepllatecolor === 5">绿色</span>
          </div>
          <div class="shoufeixinxi_span">
            <span>订单状态：</span>
            <span style="color: green" v-if="recordList[0].orderstate == 2">订单进行中</span>
            <span style="color: green" v-if="recordList[0].orderstate == 1">已支付</span>
            <span style="color: red" v-else-if="recordList[0].orderstate == 0">未支付</span>
          </div>
        </div>
        <div style="width: 90%;margin: auto;">
          <span class="shoufeixinxi_span">车牌号码：{{ recordList[0].license }}</span>
          <span class="shoufeixinxi_span">支付方式：{{ recordList[0].paymentmethod }}</span>
        </div>
        <div style="width: 90%;margin: auto;">
           <button class="shoufeixinxi_button" type="submit" @click="entranceOpening">入口开闸</button>
          <button class="shoufeixinxi_button" v-show="recordList[0].orderstate == 2" type="submit" @click="handleUpdate(recordList[0])">现金放行</button>
          <button class="shoufeixinxi_button" v-show="recordList[0].orderstate == 2" type="submit" @click="updateToRecordFromCoupon(recordList[0])">票卷放行</button>
           <button class="shoufeixinxi_button" type="submit" @click="exitOpening">出口开闸</button>
        </div>
      </div>
    </div>

    <!-- <div style="margin-top:10px;">
      <span>最近离场缴费信息</span>
    </div>

    <el-table style="margin-top: 20px;" v-loading="loading" :data="recordList"
      @selection-change="handleSelectionChange">
      <el-table-column v-if="false" label="id" align="center" prop="id" />
      <el-table-column label="车牌号" align="center" prop="license" />
      <el-table-column label="入场时间" align="center" prop="admissiontime" width="180">
      </el-table-column>
      <el-table-column label="出场时间" align="center" prop="exittime" width="180">
      </el-table-column>
      <el-table-column label="车牌颜色" align="center" prop="licensepllatecolor">
        <template slot-scope="scope">
          <span style="color: red" v-if="scope.row.licensepllatecolor == 0">未知</span>
          <span style="color: blue" v-if="scope.row.licensepllatecolor == 1">蓝色</span>
          <span style="color: yellow" v-if="scope.row.licensepllatecolor == 2">黄色</span>
          <span style="color: black" v-if="scope.row.licensepllatecolor == 3">白色</span>
          <span style="color: green" v-if="scope.row.licensepllatecolor == 4">黑色</span>
          <span style="color: green" v-else-if="scope.row.licensepllatecolor === 5">绿色</span>
        </template>
      </el-table-column>
      <el-table-column label="订单状态" align="center" prop="orderstate">
        <template scope="scope">
          <span style="color: green" v-if="scope.row.orderstate == 2">订单进行中</span>
          <span style="color: green" v-if="scope.row.orderstate == 1">已支付</span>
          <span style="color: red" v-else-if="scope.row.orderstate == 0">未支付</span>
        </template>
      </el-table-column>
      <el-table-column label="实付金额" align="center" prop="money" />
      <el-table-column label="支付方式" align="center" prop="paymentmethod" />
      <el-table-column label="出入口名称" align="center" prop="entranceandexitname" />
      <el-table-column label="进口照片" align="center" prop="numbertwo">
        <template slot-scope="scope">
          <el-image style="width: 200px; height: 200px" :src="scope.row.numbertwo"
            :preview-src-list="[scope.row.numbertwo]">
          </el-image>
        </template>
      </el-table-column>
      <el-table-column label="出口照片" align="center" prop="numberthree">
        <template slot-scope="scope">
          <el-image style="width: 200px; height: 200px" :src="scope.row.numberthree"
            :preview-src-list="[scope.row.numberthree]"></el-image>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-show="scope.row.orderstate == 2" size="mini" type="text" icon="el-icon-edit"
            @click="handleUpdate(scope.row)" v-hasPermi="['parking:record:edit']">现金</el-button>
          <el-button v-show="scope.row.orderstate == 2" size="mini" type="text" icon="el-icon-delete"
            @click="updateToRecordFromCoupon(scope.row)">优惠卷</el-button>
        </template>
      </el-table-column>
    </el-table>



    <div style="margin-top:150px;">离场待缴费信息</div>
    <el-table style="margin-top: 20px;" v-loading="false" :data="tableData" @selection-change="handleSelectionChange">
      <el-table-column v-if="false" label="序列号" align="center" prop="id" />
      <el-table-column label="车牌号" align="center" prop="license" />
      <el-table-column label="订单号" align="center" prop="ordernumber" />
      <el-table-column label="停车场" align="center" prop="parkingLotInformation.name" />
      <el-table-column label="入场时间" align="center" prop="admissiontime" width="180">
      </el-table-column>
      <el-table-column label="出场时间" align="center" prop="exittime" width="180">
      </el-table-column>
      <el-table-column label="订单状态" align="center" prop="orderstate">
        <template scope="scope">
          <span style="color: green" v-if="scope.row.orderstate == 1">已支付</span>
          <span style="color: red" v-else-if="scope.row.orderstate == 0">未支付</span>
          <span style="color: red" v-else-if="scope.row.orderstate == 2">订单进行中</span>
        </template>
      </el-table-column>
      <el-table-column label="应付金额" align="center" prop="amountpayable" />
      <el-table-column label="优惠金额" align="center" prop="discountamount" />
      <el-table-column label="实付金额" align="center" prop="money" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate1(scope.row)"
            v-hasPermi="['parking:record:edit']">现金</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete"
            @click="updateToRecordFromCoupon1(scope.row)">优惠卷</el-button>
        </template>
      </el-table-column>
    </el-table> -->
  </div>
</template>


<script>
import {
  listRecord,
  listRecord1,
  getRecord2,
  getRecord,
  getPayRecord,
  getPayRecord1,
  delRecord,
  addRecord,
  updateRecord,
  updateToRecord,
  updateToRecordFromCoupon,
  updateToRecord1,
  updateToRecordFromCoupon1
} from "@/api/parking/record";
import { getarkinglotinformations } from "@/api/system/user";
import { addOpening } from "@/api/parking/opening";
export default {
  name: "RecentDeparturePayment",
  data() {
    return {
      //开闸对象
      switchingOff:{},
      tableData: [],
      websock: null,
      lockReconnect: false, //是否真正建立连接
      timeout: 28 * 1000, //保持websocket的连接
      timeoutObj: null, //心跳心跳倒计时
      serverTimeoutObj: null, //心跳倒计时
      timeoutnum: null, //断开 重连倒计时

      parkinglotinformations: [],
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
        numberthree: null,
        parkingeqid: null
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
    //this.initWebSocket();

    this.getPayRecord1();
    this.setInterval2();
    this.setInterval1();
  },
  destroyed() {
    //销毁
   // this.websocketclose();
  },

  methods: {
    //查询前10条信息
    getList() {
      this.queryParams.parkinglotinformationid = localStorage.getItem("uu")
      listRecord1(this.queryParams).then(res => {
        this.tableData = res.data
      })
    },

    setInterval1() {
      setInterval(() => {
        console.log(1)
        this.getList();
      }, 15000)
    },
    setInterval2() {
      setInterval(() => {
        console.log(2)
        this.getPayRecordOrder1();
      }, 5000)
    },

    //入口开闸
    entranceOpening(){
      if (localStorage.getItem("bb")!=null){
        this.switchingOff.parkinglotinformationid=localStorage.getItem("uu")
        this.switchingOff.parkinglotequipmentid=localStorage.getItem("bb")
        addOpening(this.switchingOff).then(res=>{
          this.$modal.msgSuccess("开杆成功");
        })
      }

    },
    exitOpening(){
      if (localStorage.getItem("aa")!=null){
        this.switchingOff.parkinglotinformationid=localStorage.getItem("uu")
        this.switchingOff.parkinglotequipmentid=localStorage.getItem("aa")
        addOpening(this.switchingOff).then(res=>{
          this.$modal.msgSuccess("开杆成功");
        })
      }
    },

    getPayRecordOrder1() {
      var a= localStorage.getItem("aa")
      getRecord2(a).then(res => {
       let msg = res.msg;
       if (msg!==null){
         let parse = JSON.parse(msg) //将json字符串转为对象
         this.recordList = parse
       }

      })
    },


    getarkinglotinformations(id) {
      getarkinglotinformations(id).then(res => {
        this.parkinglotinformations = res.data
      })
    },
    /** 查询停车记录列表 */
    getPayRecord1() {
      getPayRecord1(localStorage.getItem("uu"), localStorage.getItem("aa")).then(res => {

        this.recordList = res.data;
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
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /**现金按钮操作 */
    handleUpdate(row) {
      console.log(row.id)
      updateToRecord(row.id).then(res => {
      //  this.recordList = this.getPayRecordOrder();
       // this.getPayRecord(localStorage.getItem("uu"));
      })
    },

    /** 优惠卷按钮 */
    updateToRecordFromCoupon(row) {
      console.log(row.id)
      updateToRecordFromCoupon(row.id).then(res => {
        //this.getPayRecord(localStorage.getItem("uu"));
       // this.recordList = this.getPayRecordOrder();
      })
    },

    /*  getPayRecordOrder() {
        this.queryParams.parkinglotinformationid = localStorage.getItem("uu")
        this.queryParams.orderstate = 2
        listRecord(this.queryParams).then(res => {
          this.tableData = res.dataTable.rows
        })
      },*/
    /* /!**现金按钮操作 *!/
   handleUpdate1(row) {
     console.log(row.id)
     updateToRecord1(row.id).then(res => {
       this.recordList = this.getPayRecordOrder();
       this.getPayRecord(localStorage.getItem("uu"));
     })
   },*/
    /*/!** 优惠卷按钮 *!/
    updateToRecordFromCoupon1(row) {
      console.log(row.id)
      updateToRecordFromCoupon1(row.id).then(res => {
        this.getPayRecord(localStorage.getItem("uu"));
        this.recordList = this.getPayRecordOrder();
      })
    },*/
    /* initWebSocket() {

   //建立websocket连接
   if ("WebSocket" in window) {
     //连接服务端访问的url47.108.163.36
     let ws = "ws://47.108.163.36:7399/websocket/" + localStorage.getItem("aa")
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
   console.log("服务端消息的内容111", e.data)
   if (e) {
     let parse = JSON.parse(e.data) //将json字符串转为对象
     this.recordList = parse
     if (parse.type === 'add') { //消息个数+1
       this.total = this.total + 1
       if (this.$route.path !== '/system_manager/system_manager_message') {
         this.mess = this.$message({
           showClose: true,
           message: '你有一条新的系统通知待查看',
           type: 'success',
           center: true,
           duration: 0
         });
       } else {
         vue.$emit('flush', true)
       }
     } else if (parse.type === 'del') {
       this.total = this.total - 1
     }
     if (parse.type === 'message') {
       //判断是外厂还是内厂操作 跳转的route不同
       if (parse.maint === 'outSide') { //外厂
         this.jumpRouter = API.backFlowApproveOutSidePath
         //如果当前页面就是将要跳转的页面，直接刷新当前页
         if (this.$route.path === API.backFlowApproveOutSidePath) {
           vue.$emit('flush', true)
         }
       } else { //内厂
         this.jumpRouter = API.backFlowApprovePath
         //如果当前页面就是将要跳转的页面，直接刷新当前页
         if (this.$route.path === API.backFlowApprovePath) {
           vue.$emit('flush', true)
         }
       }
       let notification = document.getElementsByClassName('el-notification')
       if (notification.length === 0) {
         let that = this
         this.notifi = this.$notify({
           title: '维保流程消息提醒',
           message: parse.message,
           offset: 100,
           type: 'success',
           duration: 0,
           onClick: function () {
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
   console.log("连接关闭",)
   //重连
   this.reconnect();
 },
*/
  }
};

</script>

<style>
  .tupian{
    display: inline-block;
    width: 40%;
    height: 40%;
    background-color: #e9f0f9;
    margin-left: 10%;
    border-radius: 10px;
  }

  .tupian_1{
    display: inline-block;
    width: 40%;
    height: 40%;
    margin-left: 10%;
    vertical-align:top;
  }

  .tupian2{
    margin-left: 50px;
  }

  .zhaopian{
    width: 96%;
    height: 96%;
    margin-top: 2%;
    margin-left: 2%;
  }

  .zhaopianxia{
    line-height: 40px;
    margin-left: 10px;
    font-weight: 550;
    display: inline-block;
  }

  .zhaopianshijian{
    line-height: 40px;
    font-weight: 550;
    display: inline-block;
    float: right;
    margin-right: 10px;
    color: #457ef9;
  }

  .biaotou{
    display: inline-block;
    text-align: center;
  }

  .biaotou1{
    width: 15%;
  }

  .biaotou2{
    width: 30%;
  }

  .biaotou3{
    width: 10%;
  }

  .shoufeixinxi{
    background-color: #e9f0f9;

  }

  .shoufeixinxi_span{
    display: inline-block;
    width: 50%;
    line-height: 50px;
    font-size: 24px;
  }

  .shoufeixinxi_button{
    width: 25%;
    margin: 20px 4%;
    font-size: 28px;
    background-color: #457ef9;
    color: white;
  }
</style>
