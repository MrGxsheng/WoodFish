<template>
  <div class="q-pa-md q-gutter-sm row ">
    <CommCard :info="info1" color="primary" title="概览"></CommCard>
    <CommCard :info="infoSpringBoot" color="accent" title="SpringBoot信息"></CommCard>
    <CommCard :info="infoSoftware" color="positive" title="软件信息"></CommCard>
    <CommCard :info="infoSchedule" color="red" title="定时任务"></CommCard>
    <CommCard :info="infoJVM" color="warning" title="JVM监控"></CommCard>
    <CommCard :info="infoProgress" color="blue-grey" title="进程监控"></CommCard>
    <CommCard :info="infoSys" color="info" title="系统监测"></CommCard>
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
          {label: '关', value: 99999999},
          {label: '5s', value: 5},
          {label: '20s', value: 20},
          {label: '60s', value: 60},
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
  </div>
</template>

<script lang="ts" setup>
import CommCard from '/src/components/CommCard.vue'
import {InfoKV} from "components/models";
import axios from "axios";
import {diffDate} from "components/common";
import {ref} from "vue";

let actuatorHealth: any = {}
let actuatorEnv: any = {}
let actuatorSchedule: any = {}
let info1 = new InfoKV() //概览
let infoSpringBoot = new InfoKV();//SpringBoot信息
let infoSoftware = new InfoKV()//软件信息
let infoSchedule = new InfoKV()//定时任务
let infoJVM = new InfoKV()//JVM监测
let infoProgress = new InfoKV()//进程检测
let infoSys = new InfoKV()
//一些其他单位
let JvmMemoryUsage = 0
let JvmMemoryMax = 1
let SysDiskUsage = 0
let SysDiskMax = 1
let timeCount = 1
const Reload = ref(20)

let timer = setInterval(() => {
  timeCount++;
  if (timeCount % (Reload.value) == 0) {
    loadPage()
  }
}, 1000)


loadPage()

function loadPage() {
  //这个运行最慢
  axios.get('http://192.168.199.99:8000/actuator/health').then(res => {
    actuatorHealth = res.data
    InfoData()
    SoftwareData()
    SysData()
  })
  axios.get('http://192.168.199.99:8000/actuator/env').then(res => {
    actuatorEnv = res.data
    SpringBootData()
  })
  axios.get('http://192.168.199.99:8000/actuator/scheduledtasks').then(res => {
    actuatorSchedule = res.data.cron
    ScheduleData()
  })
  JVMData()
  ProgressData()

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

//SpringBoot信息
function SpringBootData() {
  infoSpringBoot.list.value.splice(0, info1.list.value.length)//清空旧数据
  infoSpringBoot.addList('后台端口', actuatorEnv.propertySources[4].properties['my.server.name'].value + ':' + actuatorEnv.propertySources[0].properties['local.server.port'].value)
  infoSpringBoot.addList('JDK版本', actuatorEnv.propertySources[2].properties['java.runtime.version'].value)
  infoSpringBoot.addList('操作系统', actuatorEnv.propertySources[2].properties['os.name'].value)
  infoSpringBoot.addList('文件编码', actuatorEnv.propertySources[2].properties['file.encoding'].value)
  infoSpringBoot.addList('日志文件', actuatorEnv.propertySources[2].properties['LOG_FILE'].value)
  infoSpringBoot.addList('日志等级', actuatorEnv.propertySources[4].properties['logging.level.root'].value)
}

//软件信息
function SoftwareData() {
  infoSoftware.list.value.splice(0, info1.list.value.length)//清空旧数据
  infoSoftware.addList("Mysql主机", actuatorEnv.propertySources[4].properties['spring.datasource.url'].value.slice(13, 32))
  infoSoftware.addList("驱动", actuatorEnv.propertySources[4].properties['spring.datasource.driver-class-name'].value)
  infoSoftware.addList("Redis主机", actuatorEnv.propertySources[4].properties['spring.redis.host'].value + ':' + actuatorEnv.propertySources[4].properties['spring.redis.port'].value)
  infoSoftware.addList("Redis版本", actuatorHealth.components.redis.details.version)
  infoSoftware.addList("RabbitMQ主机", actuatorEnv.propertySources[4].properties['spring.rabbitmq.host'].value + ':' + actuatorEnv.propertySources[4].properties['spring.rabbitmq.port'].value)
  infoSoftware.addList("RabbitMQ版本", actuatorHealth.components.rabbit.details.version)
}

//定时任务
function ScheduleData() {
  infoSchedule.list.value.splice(0, info1.list.value.length)//清空旧数据
  actuatorSchedule.forEach((item: any, index: any) => {
    infoSchedule.addList("任务" + index + 1, item.runnable.target.slice(34, 80))
    infoSchedule.addList("任务" + index + 1 + "周期", item.expression)
  })
}

//JVM信息
function JVMData() {
  infoJVM.list.value.splice(0, info1.list.value.length)//清空旧数据

  axios.get('http://192.168.199.99:8000/actuator/metrics/process.start.time').then(res => {
    infoJVM.addList("开启时间", new Date(res.data.measurements[0].value * 1000).toLocaleDateString().replace(/\//g, "-") + " " + new Date(res.data.measurements[0].value * 1000).toTimeString().substr(0, 8))
    infoJVM.addList("运行时间", diffDate(res.data.measurements[0].value * 1000))
  })
  axios.get('http://192.168.199.99:8000/actuator/metrics/jvm.threads.daemon').then(res => {
    infoJVM.addList("JVM后台线程", res.data.measurements[0].value)
  })
  axios.get('http://192.168.199.99:8000/actuator/metrics/jvm.threads.live').then(res => {
    infoJVM.addList("JVM存活线程", res.data.measurements[0].value)
  })
  axios.get('http://192.168.199.99:8000/actuator/metrics/jvm.threads.peak').then(res => {
    infoJVM.addList("JVM排队线程", res.data.measurements[0].value)
  })

}

//进程监测
function ProgressData() {
  infoProgress.list.value.splice(0, info1.list.value.length)//清空旧数据
  axios.get('http://192.168.199.99:8000/actuator/metrics/process.cpu.usage').then(res => {
    infoProgress.addProgress("CPU占用", (res.data.measurements[0].value).toFixed(2), true)
    axios.get('http://192.168.199.99:8000/actuator/metrics/jvm.memory.used').then(res => {
      JvmMemoryUsage = res.data.measurements[0].value / 1024 / 1024 / 8
      JvmMemoryUsage.toFixed(2)
      //没办法，阻塞函数
      axios.get('http://192.168.199.99:8000/actuator/metrics/jvm.memory.max').then(res => {
        JvmMemoryMax = res.data.measurements[0].value / 1024 / 1024 / 8
        JvmMemoryMax.toFixed(2)
        infoProgress.addProgress("内存占用", (JvmMemoryUsage / JvmMemoryMax).toFixed(2), true)
        axios.get('http://192.168.199.99:8000/actuator/metrics/system.cpu.count').then(res => {
          infoProgress.addList("系统内核", res.data.measurements[0].value)
          infoProgress.addList("内存总量", '16GB')
        })
      })
    })
  })
}

//系统监测
function SysData() {
  infoSys.list.value.splice(0, info1.list.value.length)//清空旧数据
  axios.get('http://192.168.199.99:8000/actuator/metrics/system.cpu.usage').then(res => {
    infoSys.addProgress("CPU占用", (res.data.measurements[0].value).toFixed(2), true)
    SysDiskUsage = parseInt((actuatorHealth.components.diskSpace.details.free / 1024 / 1024 / 1024).toFixed())
    SysDiskMax = parseInt((actuatorHealth.components.diskSpace.details.total / 1024 / 1024 / 1024).toFixed())
    infoSys.addProgress("硬盘占用", (1 - SysDiskUsage / SysDiskMax).toFixed(2), true)
    infoSys.addList("可用空间", SysDiskUsage + "GB")
    infoSys.addList("全部空间", SysDiskMax + "GB")
  })
}
</script>

<style scoped>
.my-card {
  width: 100%;
  max-width: 350px;
  max-height: 450px;
}
</style>

