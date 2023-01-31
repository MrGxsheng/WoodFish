<template>
  <!-- 顶部提示 -->
  <div class="q-pa-md q-gutter-sm row">
    <q-card class="my-card q-ma-md">
      <q-card-section class="bg-orange">
        <div class="text-h6 text-white">自动更新/网页跳转</div>
      </q-card-section>
      <q-separator/>
      <q-card-actions class="q-pa-md " vertical>
        <div class="q-pa-md">
          <q-btn-toggle
              v-model="Reload"
              spread
              class="my-custom-toggle"
              no-caps
              rounded
              toggle-color="orange"
              color="white"
              text-color="orange"
              :options="[
          {label: '关', value: 9999999},
          {label: '5s', value: 10},
          {label: '20s', value: 20},
          {label: '1min', value: 60},
        ]"
          />
        </div>
        <div class="q-pa-md">
          <span class="q-mr-md">Druid控制台</span>
          <q-btn color="orange-5" size="sm" class="float-right" label="点击跳转"/>
        </div>
        <div class="q-pa-md">
          <span class="q-mr-md">SpringActuator</span>
          <q-btn color="orange-5" size="sm" class="float-right" label="点击跳转"/>
        </div>
        <div class="q-pa-md">
          <span class="q-mr-md">前台</span>
          <q-btn color="orange-5" size="sm" class="float-right" label="点击跳转"/>
        </div>
      </q-card-actions>
    </q-card>
    <CommCard :info="info1" color="secondary" title="概览"></CommCard>
    <WideCard :info="infoProgress" color="primary" title="实时监控"></WideCard>
    <div class="q-ma-md shadow-3 q-pa-md" style="width: 38vw">
      <VueApexCharts type="line" height="330" :options="chartOptions" :series="series"></VueApexCharts>
    </div>
    <div class="q-ma-md shadow-3 q-pa-md" style="width: 38.5vw">
      <VueApexCharts type="line" height="330" :options="chartOptions" :series="series"></VueApexCharts>
    </div>

  </div>
</template>

<script lang="ts" setup>
import CommCard from '/src/components/CommCard.vue'
import {ref} from "vue";
import {InfoKV} from "components/models";
import axios from "axios";
import WideCard from "/src/components/WideCard.vue";
import VueApexCharts from "vue3-apexcharts";

const Reload = ref(20)
let actuatorHealth: any = {}
let info1 = new InfoKV() //概览
let infoProgress = new InfoKV()//进程检测
let timeCount = 1
let SysDiskUsage = 0
let SysDiskMax = 1
let JvmMemoryUsage = 0
let JvmMemoryMax = 1
const columns = [
  {
    name: 'name',
    required: true,
    label: 'Dessert (100g serving)',
    align: 'left',
    field: (row: { name: any; }) => row.name,
    format: (val: any) => `${val}`,
    sortable: true
  },
  {name: 'calories', align: 'center', label: 'Calories', field: 'calories', sortable: true},
  {name: 'fat', label: 'Fat (g)', field: 'fat', sortable: true},
  {name: 'carbs', label: 'Carbs (g)', field: 'carbs'},
  {name: 'protein', label: 'Protein (g)', field: 'protein'},
  {name: 'sodium', label: 'Sodium (mg)', field: 'sodium'},
  {
    name: 'calcium',
    label: 'Calcium (%)',
    field: 'calcium',
    sortable: true,
    sort: (a: string, b: string) => parseInt(a, 10) - parseInt(b, 10)
  },
  {
    name: 'iron',
    label: 'Iron (%)',
    field: 'iron',
    sortable: true,
    sort: (a: string, b: string) => parseInt(a, 10) - parseInt(b, 10)
  }
]
const rows = [
  {
    name: 'Frozen Yogurt',
    calories: 159,
    fat: 6.0,
    carbs: 24,
    protein: 4.0,
    sodium: 87,
    calcium: '14%',
    iron: '1%'
  },
  {
    name: 'Ice cream sandwich',
    calories: 237,
    fat: 9.0,
    carbs: 37,
    protein: 4.3,
    sodium: 129,
    calcium: '8%',
    iron: '1%'
  },
  {
    name: 'Eclair',
    calories: 262,
    fat: 16.0,
    carbs: 23,
    protein: 6.0,
    sodium: 337,
    calcium: '6%',
    iron: '7%'
  },
  {
    name: 'Cupcake',
    calories: 305,
    fat: 3.7,
    carbs: 67,
    protein: 4.3,
    sodium: 413,
    calcium: '3%',
    iron: '8%'
  },
  {
    name: 'Gingerbread',
    calories: 356,
    fat: 16.0,
    carbs: 49,
    protein: 3.9,
    sodium: 327,
    calcium: '7%',
    iron: '16%'
  },
  {
    name: 'Jelly bean',
    calories: 375,
    fat: 0.0,
    carbs: 94,
    protein: 0.0,
    sodium: 50,
    calcium: '0%',
    iron: '0%'
  },
  {
    name: 'Lollipop',
    calories: 392,
    fat: 0.2,
    carbs: 98,
    protein: 0,
    sodium: 38,
    calcium: '0%',
    iron: '2%'
  },
  {
    name: 'Honeycomb',
    calories: 408,
    fat: 3.2,
    carbs: 87,
    protein: 6.5,
    sodium: 562,
    calcium: '0%',
    iron: '45%'
  },
  {
    name: 'Donut',
    calories: 452,
    fat: 25.0,
    carbs: 51,
    protein: 4.9,
    sodium: 326,
    calcium: '2%',
    iron: '22%'
  },
  {
    name: 'KitKat',
    calories: 518,
    fat: 26.0,
    carbs: 65,
    protein: 7,
    sodium: 54,
    calcium: '12%',
    iron: '6%'
  }
]
//定时器
let timer = setInterval(() => {
  timeCount++;
  if (timeCount % (Reload.value) == 0) {
    // loadPage()
    loadPage()
  }
}, 1000)
loadPage()

function loadPage() {
  //系统概览
  axios.get('http://192.168.199.99:8000/actuator/health').then(res => {
    actuatorHealth = res.data
    InfoData()
    ProgressData()
  })
}

//概览
function InfoData() {
  info1.list.value.splice(0, info1.list.value.length)//清空旧数据
  actuatorHealth.status === 'UP' ? info1.addList("系统状态", "正常") : info1.addList("系统状态", "异常")
  actuatorHealth.components.db.status === 'UP' ? info1.addList("数据库状态", "正常") : info1.addList("数据库状态", "异常")
  actuatorHealth.components.redis.status === 'UP' ? info1.addList("缓存数据库状态", "正常") : info1.addList("缓存数据库状态", "异常")
  actuatorHealth.components.rabbit.status === 'UP' ? info1.addList("消息队列状态", "正常") : info1.addList("消息队列状态", "异常")
  actuatorHealth.components.ping.status === 'UP' ? info1.addList("延迟状态", "正常") : info1.addList("延迟状态", "异常")
  actuatorHealth.components.diskSpace.status === 'UP' ? info1.addList("硬盘状态", "正常") : info1.addList("硬盘状态", "异常")
}

//进程监测
function ProgressData() {
  infoProgress.list.value.splice(0, info1.list.value.length)//清空旧数据
  axios.get('http://192.168.199.99:8000/actuator/metrics/process.cpu.usage').then(res => {
    infoProgress.addProgress("CPU占用", (res.data.measurements[0].value).toFixed(2), true)
    //没办法，阻塞函数
    axios.get('http://192.168.199.99:8000/actuator/metrics/jvm.memory.max').then(res => {
      JvmMemoryMax = res.data.measurements[0].value / 1024 / 1024 / 8
      JvmMemoryMax.toFixed(2)
      infoProgress.addProgress("内存占用", (JvmMemoryUsage / JvmMemoryMax).toFixed(2), true)
      axios.get('http://192.168.199.99:8000/actuator/metrics/system.cpu.usage').then(res => {
        infoProgress.addProgress("系统CPU占用", (res.data.measurements[0].value).toFixed(2), true)
        SysDiskUsage = parseInt((actuatorHealth.components.diskSpace.details.free / 1024 / 1024 / 1024).toFixed())
        SysDiskMax = parseInt((actuatorHealth.components.diskSpace.details.total / 1024 / 1024 / 1024).toFixed())
        infoProgress.addProgress("系统硬盘占用", (1 - SysDiskUsage / SysDiskMax).toFixed(2), true)
      })
    })
  })
}

const series = [
  {
    name: "High - 2013",
    data: [28, 29, 33, 36, 32, 32, 33]
  },
  {
    name: "Low - 2013",
    data: [12, 11, 14, 18, 17, 13, 13]
  }]
const chartOptions = {
  chart: {
    height: 350,
    type: 'line',
    dropShadow: {
      enabled: true,
      color: '#000',
      top: 18,
      left: 7,
      blur: 10,
      opacity: 0.2
    },
    toolbar: {
      show: false
    }
  },
  colors: ['#77B6EA', '#545454'],
  dataLabels: {
    enabled: false,
  },
  stroke: {
    curve: 'straight'
  },
  title: {
    text: '前台监控',
    align: 'left'
  },
  grid: {
    borderColor: '#e7e7e7',
    row: {
      colors: ['#f3f3f3', 'transparent'], // takes an array which will be repeated on columns
      opacity: 0.5
    },
  },
  markers: {
    size: 5
  },
  xaxis: {
    categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul'],
    title: {
      text: 'Month'
    }
  },
  yaxis: {
    title: {
      text: 'Temperature'
    },
    min: 5,
    max: 40
  },
  legend: {
    position: 'top',
    horizontalAlign: 'right',
    floating: true,
    offsetY: -25,
    offsetX: -5
  },
}

</script>
<style scoped>
.my-card {
  width: 100%;
  max-width: 350px;
  max-height: 450px;
}


</style>
