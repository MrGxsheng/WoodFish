<template>
  <div class="q-pa-md q-gutter-sm row ">
    <CommCard :info="ListMysql" title="Mysql概览" color="secondary"></CommCard>
    <CommCard :info="ListWeb" title="Web检测" color="purple"></CommCard>
    <CommCard :info="ListImp" title="重要SQL检测" color="positive"></CommCard>


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
    <div class="q-pa-sm" style="width: 39vw">
      <q-table
          title="URL监控"
          :rows="UrlRows"
          :columns="UrlColumns"
          row-key="name"
          color="primary"
      />
    </div>
    <div class="q-pa-sm q-ml-md" style="width: 39vw">
      <q-table
          title="Session监控"
          :rows="SessionRows"
          :columns="SessionColumns"
          row-key="name"
          color="primary"
      />
    </div>

  </div>
</template>

<script lang="ts" setup>
import axios from "axios";
import {InfoKV} from "components/models";
import CommCard from "components/CommCard.vue";
import {ref} from "vue";
import {api} from "boot/axios";

const Reload = ref(20)
const ListMysql = new InfoKV()
const ListWeb = new InfoKV()
const ListImp = new InfoKV()
let DataDataSource = []
let DataWeb = []
let DataImp = []
let timeCount = 1
let UrlColumns = ref([])
let UrlRows = ref([])
let SessionColumns = ref([])
let SessionRows = ref([])
//定时器
let timer = setInterval(() => {
  timeCount++;
  if (timeCount % (Reload.value) == 0) {
    loadPage()
  }
}, 1000)
loadPage()

function loadPage() {
  CardList()
  WebList()
  ImpList()
  URLList()
  SessionList()
}

//Mysql概览
function CardList() {
  ListMysql.list.value.splice(0, ListMysql.list.value.length)
  axios.get('http://192.168.199.99:8000/druid/datasource.json').then(res => {
    console.log(res)
    DataDataSource = res.data.Content[0]
    ListMysql.addList("获取连接时检测", DataDataSource.TestOnBorrow === true ? '开启' : '关闭')
    ListMysql.addList("空闲时检测", DataDataSource.TestWhileIdle === true ? '开启' : '关闭')
    ListMysql.addList("保持连接", DataDataSource.KeepAlive === true ? '开启' : '关闭')
    ListMysql.addList("初始化大小", DataDataSource.InitialSize)
    ListMysql.addList("最大连接大小", DataDataSource.MaxActive)
    ListMysql.addList("最长等待时间", DataDataSource.MaxWait)
  })
}

//Web检测
function WebList() {
  ListWeb.list.value.splice(0, ListWeb.list.value.length)
  axios.get('http://192.168.199.99:8000/druid/webapp.json').then(res => {
    DataWeb = res.data.Content[0]
    ListWeb.addList("正在执行", DataWeb.RunningCount)
    ListWeb.addList("最大并发", DataWeb.ConcurrentMax)
    ListWeb.addList("请求次数", DataWeb.RequestCount)
    ListWeb.addList("更新行数", DataWeb.JdbcUpdateCount)
    ListWeb.addList("读取行数", DataWeb.JdbcFetchRowCount)
    ListWeb.addList("JDBC执行数", DataWeb.JdbcExecuteCount)
  })
}

//重要SQL检测
function ImpList() {
  ListImp.list.value.splice(0, ListImp.list.value.length)
  axios.get('http://192.168.199.99:8000/druid/weburi.json').then(res => {
        DataImp = res.data.Content
        //这样是为了保证顺序
        ListImp.addList("登录请求", 0)
        ListImp.addList("首页浏览", 0)
        ListImp.addList("物品浏览", 0)
        ListImp.addList("新增/修改物品", 0)
        ListImp.addList("新增/修改评论", 0)
        ListImp.addList("新增/修改订单", 0)
        DataImp.forEach((data: any) => {
          data.URI == '/user/login' ? ListImp.setList("登录请求", data.RequestCount) : ''
          data.URI == '/rollimg/' ? ListImp.setList("首页浏览", data.RequestCount) : ''
          data.URI == '/item/id' ? ListImp.setList("物品浏览", data.RequestCount) : ''
          data.URI == '/file/id' ? ListImp.setList("新增/修改物品", data.RequestCount) : ''
          data.URI == '/comment/p' ? ListImp.setList("新增/修改评论", data.RequestCount) : ''
          data.URI == '/order/u' ? ListImp.setList("新增/修改订单", data.RequestCount) : ''
        })
      }
  )
}

//URL检测
function URLList() {
  api.get('/tablemenu/URL').then(res => {
    UrlColumns.value.splice(0, UrlColumns.value.length)
    res.data.forEach((data: any) => {
      data.sortable = true
      data.align = 'center'
      //@ts-ignore
      UrlColumns.value.push(data)
    })
  })
  axios.get('http://192.168.199.99:8000/druid/weburi.json').then(res => {
    UrlRows.value.splice(0, UrlRows.value.length)
    res.data.Content.forEach((data: any) => {
      //@ts-ignore
      UrlRows.value.push(data)
    })
  })
}

//Session
function SessionList() {
  api.get('/tablemenu/Session').then(res => {
    SessionColumns.value.splice(0, SessionColumns.value.length)
    res.data.forEach((data: any) => {
      data.sortable = true
      data.align = 'center'
      //@ts-ignore
      SessionColumns.value.push(data)
    })
  })
  axios.get('http://192.168.199.99:8000/druid/websession.json').then(res => {
    SessionRows.value.splice(0, SessionRows.value.length)
    res.data.Content.forEach((data: any) => {
      //@ts-ignore
      SessionRows.value.push(data)
    })
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

