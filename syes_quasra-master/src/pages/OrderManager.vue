<template>
  <div class="q-pa-md q-gutter-sm">
    <div class="header">
      <q-btn class="shadow-1" unelevated color="primary" label="刷新" :loading="loading[0]"
             @click="simulateProgress(0)" icon="replay"/>
      <q-btn class="shadow-1" unelevated color="secondary" label="新增"
             @click="windowDisplay=true;onReset()"
             icon="add_circle_outline"/>
      <q-btn class="shadow-1" unelevated color="red" label="删除" @click="showNotif" icon="delete_forever"/>
      <q-btn class="shadow-1" unelevated color="brown-5" label="导出" @click="exportTable" icon="file_download"/>
    </div>

    <!--  表格  -->
    <div class="q-pa-md" style="margin-left:auto">
      <q-table
          title="订单信息管理"
          :rows="rows"
          :columns="columns"
          row-key="id"
          hide-pagination
          :pagination="pagination"
          :selected-rows-label="getSelectedString"
          selection="multiple"
          v-model:selected="selected"
          :loading="loadingPage"
      >
        <template v-slot:top-right>
          <q-input label="学号搜索" v-model="searchtext" :dense=true
                   style="display: inline-block;float: right;margin-right: 20px" debounce="1000">
            <template v-slot:append>
              <q-icon name="search" @click="handlesearch()" class="cursor-pointer"/>
              <q-icon v-if="searchtext !== ''" name="close" @click="handleRest" class="cursor-pointer"/>
            </template>
          </q-input>
        </template>
        <!--    加载动画    -->

        <q-inner-loading showing color="primary" label="加载..."/>
        <!--        自定义表格属性-->
        <template v-slot:body-cell-enable="props">
          <q-td>
            <div @click="switchbutton(props)" style="text-align: center">
              <q-btn v-if="props.row.enable===1" color="primary" label="可用" size="sm"/>
              <q-btn v-if="props.row.enable===0" color="red" label="取消" size="sm"/>
            </div>
          </q-td>
        </template>
        <!--        自定义表格属性-->
        <template v-slot:body-cell-status="props">
          <q-td style="text-align: center">
            <q-btn-group>
              <q-btn-toggle
                  v-model="props.row.status"
                  toggle-color="primary"
                  size="sm"
                  @click="switchstatus(props)"
                  :options="[
                    {label: '确认', value: 1},
                    {label: '交易', value: 2},
                    {label: '完成', value: 3},
                    // {label: '交易完成', value: 5}
                  ]"
              />
            </q-btn-group>
          </q-td>
        </template>
      </q-table>
    </div>
    <!--  分页  -->
    <div class="q-pa-lg flex flex-center">
      <q-pagination
          v-model="currentPage"
          :max="Pagecount"
          direction-links
          @click="handlePage()"
          style="min-width: 2em"
      />
    </div>


    <!--  新增，修改  -->
    <!--新增弹出框-->
    <q-dialog v-model="windowDisplay" position="right">
      <q-card class="column full-height" style="width: 400px">
        <q-card-section class="row items-center q-pb-none ">
          <div class="text-h6">新增订单</div>
          <q-space/>
          <q-btn icon="close" flat round dense v-close-popup/>
        </q-card-section>
        <div class="q-pa-md" style="max-width: 300px;margin-left: 30px">
          <form @submit.prevent.stop="onSubmit" @reset.prevent.stop="onReset()" class="q-gutter-md">
            <q-input
                ref="order.titleRef.value"
                v-model="order.itemid.value"
                label="物品id"
                hint="输入正确物品id"
                lazy-rules
                :rules="contentRules"
            />
            <q-input
                ref="order.useridRef.value"
                v-model="order.userid.value"
                label="用户id"
                hint="请输入用户id"
                lazy-rules
                :rules="idRules"
            />
            <div>
              <q-btn label="提交" type="submit" color="primary"/>
            </div>
          </form>
        </div>
      </q-card>
    </q-dialog>
  </div>
</template>

<script setup lang="ts">
//插件初始化
import {ref} from "vue";
import {api} from "boot/axios";
import {CommFail, CommSeccess} from "components/common";
import {Order} from "components/models";
import {exportFile, useQuasar} from "quasar";


//刷新按钮
let loading = ref([false,])

function simulateProgress(number: number) {
  loading.value[number] = true //这是那个加载动画
  localStorage.removeItem("ordercolumns")
  loadPage()
  setTimeout(() => {
    loading.value[number] = false
    CommSeccess("刷新成功")
  }, 500)

}


//表格多选框
const selected = ref([])

function getSelectedString() {
  return selected.value.length === 0 ? '' : `已选择${selected.value.length}项${selected.value.length > 1 ? '' : ''}`
}


//分页
const currentPage = ref(1) //当前页面
let Pagecount = ref(1)  //页数
const PageItem = 10   //页面数据数量
const pagination = ref({rowsPerPage: 10}) //表格显示的最大数量
function handlePage() {
  loadPage();
}

//获取后端数据
let columns = ref([])
let rows = ref([])
let searchtext = ref('');
let loadingPage = ref(false)
loadPage()

function loadPage() {
  loadingPage.value = true
  //获取表格属性
  if (localStorage.getItem("ordercolumns") == null || localStorage.getItem("ordercolumns") == undefined) {
    api.get("/tablemenu/order").then(res => {
      // console.log('刷新表格') //握草怪死了，改成 刷新了表格 就会报错
      if (columns == undefined) {
        loadPage()
      }
      columns.value = res.data
      columns.value.forEach((item) => {
        //@ts-ignore
        item.align = "center"
      })
      localStorage.setItem("ordercolumns", JSON.stringify(columns))
    })
  } else {
    // @ts-ignore 不清楚怎么办到的，能跑就行
    const localInfo = JSON.parse(localStorage.getItem("ordercolumns"))._value
    if (localInfo == undefined) {
      setTimeout(() => {
        simulateProgress(0)
      }, 500)
      console.log('刷新快了');
    } else {
      columns = localInfo
    }
  }
//获取分页数据
  api.get("/Rorder/page?" + "pagesize=" + PageItem + "&currentpage=" + currentPage.value + "&searchtext=" + searchtext.value).then(res => {
    rows.value = res.data.data
    Pagecount.value = Math.ceil(res.data.total / PageItem)
  })
  setTimeout(() => {
    loadingPage.value = false
  }, 1000)

}

//切换可用情况
function switchbutton(value: { row: { id: string; }; value: any; }) {
  // console.log(value)
  api.put("/Rorder/", {
    "id": value.row.id,//@ts-ignore
    "enable": value.row.enable == 1 ? 0 : 1
  }).then(() => {
    loadPage()
    CommSeccess("操作成功")
  })
}

//切换订单状态
function switchstatus(props: any) {
  api.get("/Rorder/status/order?id=" + props.row.id + "&status=" + props.row.status).then(() => {
    loadPage()
    CommSeccess("操作成功")
  })

}

//新增物品
const order = new Order();
let windowDisplay = ref(false)
//规则
let contentRules = ref([(val: string | any[]) => (val && val.length > 0) || '输入值为空'])
let idRules = ref([
  (val: number) => (val > 20191111111 && val < 20229999999) || '请输入正确的学号'
])

//清空
function onReset() {
  order.clearall()
}


//提交新增或修改
function onSubmit() {
  if (order.userid.value != '' && order.itemid.value != '') {
    api.post("/Rorder/", {
      "userid": order.userid.value,
      "itemid": order.itemid.value,
    }).then(res => {
      if (res.status === 200) {
        CommSeccess("提交成功")
      }
      windowDisplay.value = false
      loadPage()
    })
  } else {
    CommFail("请检查输入格式是否正确")
  }
}

//搜索
function handlesearch() {
  loadPage()
}

function handleRest() {
  searchtext.value = "";
  console.log("重置了按钮")
  loadPage()
}

//删除
const $q = useQuasar()

function showNotif() {
  $q.notify({
    message: '确定要删除所选项目吗？',
    type: 'negative',
    position: 'top',
    actions: [
      {
        label: '确定', color: 'yellow', handler: () => {
          const idlist: any = ref([])
          selected.value.forEach((item: any, index) => {
            idlist.value.push(item.id)
          })
          // 删除用户
          deleteItems_ById(idlist)
          // 刷新页面
          setTimeout(() => {
            loadPage()
          }, 500)
        }
      },
      {
        label: '取消', color: 'white', handler: () => { /* ... */
        }
      }
    ]
  })
}

// 根据id删除多个用户
function deleteItems_ById(idlist: any) {
  idlist.value.forEach((item: string) => {
    // 先用枚举删除将就一下
    deleteItemById(item)
  })
}

// 根据id删除单个用户
function deleteItemById(id: string) {
  api.delete("/Rorder/" + id).then(res => {
    if (res.code == "200") {
      CommSeccess('成功删除')
    }
  })
}

//导出
//导出数据
function wrapCsvValue(val: any, formatFn: ((arg0: any, arg1: any) => any) | undefined, row: any) {
  let formatted = formatFn !== void 0
      ? formatFn(val, row)
      : val

  formatted = formatted === void 0 || formatted === null
      ? ''
      : String(formatted)

  formatted = formatted.split('"').join('""')
  return `"${formatted}"`
}

function exportTable() {
  //@ts-ignore
  const content = [columns.map(col => wrapCsvValue(col.label))].concat(
      //@ts-ignore
      rows.value.map(row => columns.map(col => wrapCsvValue(
          typeof col.field === 'function'
              ? col.field(row)
              : row[col.field === void 0 ? col.name : col.field],
          col.format,
          row
      )).join(','))
  ).join('\r\n')

  const status = exportFile(
      'table-export.csv',
      content,
      'text/csv'
  )


  if (status !== true) {
    $q.notify({
      message: 'Browser denied file download...',
      color: 'negative',
      icon: 'warning'
    })
  }
}
</script>

<style scoped>

.my-table-details {
  font-size: 0.85em;
  font-style: italic;
  max-width: 200px;
  white-space: normal;
  color: #555;
  margin-top: 4px;
}

.header .q-btn {
  margin-right: 15px;
}

.active {
  color: red;
  background-color: red;
}
</style>
