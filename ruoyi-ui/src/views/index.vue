<template>
  <section class="chart-container">
    <el-row>
      <el-col :span="12">

        <div id="chartLine" style="width:100%; height:400px;"></div>
      </el-col>
      <el-col :span="12">
        <div id="chartPie" style="width:100%; height:400px;"></div>
      </el-col>
      <el-col :span="12">
        <div id="chartColumn" style="width:100%; height:400px;"></div>
      </el-col>
      <el-col :span="12"  >
        <div  id="chartBar" style="width:100%; height:400px;"></div>
      </el-col>
      <div  id="chartBarid" style="width:100%; height:400px;"></div>
      <el-col :span="24">
        <a href="http://echarts.baidu.com/examples.html" target="_blank" style="float: right;">more>></a>
      </el-col>
    </el-row>
  </section>
</template>

<script>
import echarts from 'echarts'
import { listInformation,getOccupiedParkingSpace,getSumAllListAndCoupon,getDailyInformation,getMoney, getParkingLots,getInformation, delInformation, addInformation, updateInformation } from "@/api/parking/information";
export default {
  data() {
    return {
      chartColumn: null,
      chartBar: null,
      chartLine: null,
      chartPie: null,
      chartBarid:null,
    }
  },

  methods: {
    drawColumnChart() {
      if (localStorage.getItem("uu")!=0){
        getMoney(localStorage.getItem("uu")).then(res=> {

          console.log(res.data)
          var months=res.data.month
          var moneys=res.data.money
          this.chartColumn = echarts.init(document.getElementById('chartColumn'));
          this.chartColumn.setOption({
            title: {text: '每月收入'},
            tooltip: {},
            xAxis: {
              data: months
            },
            yAxis: {},
            series: [{
              name: '今年每月收入',
              type: 'bar',
              data: moneys
            }]
          });
        })
      }

    },

    drawLineChart() {
      if (localStorage.getItem("uu")!=0) {
        getDailyInformation(localStorage.getItem("uu")).then(res => {
          let data = res.data;
          var names = data.name
          var DailyInformations = data.parkingLots
          this.chartLine = echarts.init(document.getElementById('chartLine'));
          this.chartLine.setOption({

            title: {
              text: '',
              subtext: '',
              x: 'center'
            },
            tooltip: {
              trigger: 'item',
              formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
              orient: 'vertical',
              left: 'left',
              data: names
            },
            series: [
              {
                name: '访问来源',
                type: 'pie',
                radius: '55%',
                center: ['50%', '60%'],
                data: DailyInformations,
                itemStyle: {
                  emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                  }
                }
              }
            ]
          });
        });
      }
    },
    drawPieChart() {
      if (localStorage.getItem("uu")!=0) {
        getParkingLots(localStorage.getItem("uu")).then(res => {
          var names = res.data.name
          var ParkingLotsVos = res.data.parkingLots
          this.chartPie = echarts.init(document.getElementById('chartPie'));
          this.chartPie.setOption({
            title: {
              text: '车位信息',
              subtext: '',
              x: 'center'
            },
            tooltip: {
              trigger: 'item',
              formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
              orient: 'vertical',
              left: 'left',
              data: names
            },
            series: [
              {
                name: '访问来源',
                type: 'pie',
                radius: '55%',
                center: ['50%', '60%'],
                data: ParkingLotsVos,
                itemStyle: {
                  emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                  }
                }
              }
            ]
          });
        })
      }
    },
    drawBarChart() {
      if (localStorage.getItem("uu")!=0) {
        getSumAllListAndCoupon(localStorage.getItem('uu')).then(res => {
          var count = res.data.count
          var names = res.data.name
          this.chartBar = echarts.init(document.getElementById('chartBar'));
          this.chartBar.setOption({
            title: {text: '数量'},
            tooltip: {},
            xAxis: {
              data: names
            },
            yAxis: {},
            series: [{
              name: '名单数量',
              type: 'bar',
              data: count
            }]
          });
        })
      }
    },
    drawBarChartid() {
      if (localStorage.getItem("uu")==0) {
        getOccupiedParkingSpace(localStorage.getItem("uu")).then(res => {
          if (res.code===200){
            var name = res.data.name
            var number = res.data.value
            this.chartBarid = echarts.init(document.getElementById('chartBarid'));
            this.chartBarid.setOption({
              title: {text: '数量'},
              tooltip: {},
              xAxis: {
                data: name
              },
              yAxis: {},
              series: [{
                name: '名单数量',
                type: 'bar',
                data: number
              }]
            });
          }

        })
      }
    },




    drawCharts() {

      this.drawBarChartid()
        this.drawLineChart()

        this.drawColumnChart()

        this.drawPieChart()

      this.drawBarChart()


    },
  },
/*created() {
  this.drawCharts()
},*/
  mounted(){
    this.drawCharts()
  },
  updated: function () {
    this.drawCharts()
  }
}
</script>

<style scoped>
.chart-container {
  width: 100%;
  float: left;
}
/*.chart div {
    height: 400px;
    float: left;
}*/

.el-col {
  padding: 30px 20px;
}
</style>

