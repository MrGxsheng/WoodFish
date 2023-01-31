<template>
  <!-- 顶部提示 -->
  <div class="q-pa-md q-gutter-sm">
    <div class="header">
      <q-btn class="shadow-1" unelevated color="primary" label="刷新" :loading="loading[0]"
             @click="simulateProgress(0)" icon="replay"/>
      <q-btn class="shadow-1" unelevated color="secondary" label="新增"
             @click="windowDisplay=true;buttonStatus='新增评论';onReset()"
             icon="add_circle_outline"/>
      <q-btn class="shadow-1" unelevated color="purple" label="修改" @click="checkCounts();buttonStatus='修改评论'"
             icon="edit"/>
      <q-btn class="shadow-1" unelevated color="red" label="删除" @click="showNotif" icon="delete_forever"/>
      <q-btn class="shadow-1" unelevated color="brown-5" label="导出" @click="exportTable" icon="file_download"/>
    </div>
    <!--  表格  -->
    <div class="q-pa-md" style="margin-left:auto">
      <q-table
          title="用户评论管理"
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
          <q-input label="学号或物品ID" v-model="searchtext" :dense=true
                   style="display: inline-block;float: right;margin-right: 20px" debounce="1000">
            <template v-slot:append>
              <q-icon name="search" @click="handlesearch()" class="cursor-pointer"/>
              <q-icon v-if="searchtext !== ''" name="close" @click="handleRest" class="cursor-pointer"/>
            </template>
          </q-input>
        </template>
        <!--    加载动画    -->
        <q-inner-loading showing color="primary" label="加载..."/>
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
          <div class="text-h6">{{ buttonStatus }}</div>
          <q-space/>
          <q-btn icon="close" flat round dense v-close-popup/>
        </q-card-section>
        <div class="q-pa-md" style="max-width: 300px;margin-left: 30px">
          <form @submit.prevent.stop="onSubmit" @reset.prevent.stop="onReset()" class="q-gutter-md">
            <q-input
                v-if="buttonStatus==='修改评论'"
                ref="commentinfo.idRef.value"
                v-model="commentinfo.id.value"
                label="编号"
                hint="评论编号"
                lazy-rules
                :readonly="buttonStatus==='修改评论'"
                :rules="contentRules"
            />
            <q-input
                ref="commentinfo.itemidRef.value"
                v-model="commentinfo.itemid.value"
                label="物品Id"
                hint="输入正确物品id"
                lazy-rules
                :rules="contentRules"
                :readonly="buttonStatus==='修改评论'"
            />
            <q-input
                ref="commentinfo.useridRef.value"
                v-model="commentinfo.userid.value"
                label="用户id"
                hint="输入正确用户id"
                lazy-rules
                :rules="contentRules"
                :readonly="buttonStatus==='修改评论'"
            />
            <q-input
                ref="commentinfo.contentRef.value"
                v-model="commentinfo.content.value"
                label="内容"
                hint="请输入评论内容"
                lazy-rules
                :rules="contentRules"
            />
            <q-input
                ref="commentinfo.tocommentidRef.value"
                v-model="commentinfo.fromcommentid.value"
                label="父级评论id"
                hint="请输入父级评论id"
                lazy-rules
                :readonly="buttonStatus==='修改评论'"
            />
            <div>
              <q-btn v-if="buttonStatus==='新增评论'" label="提交" type="submit" color="primary"/>
              <q-btn v-if="buttonStatus==='修改评论'" label="提交修改" type="submit" color="primary"/>
              <q-btn v-if="buttonStatus==='新增评论'" label="重置" type="reset" color="primary" flat class="q-ml-sm"/>
            </div>
          </form>
        </div>
      </q-card>
    </q-dialog>
  </div>
</template>

<script setup lang="ts">
//表格多选框
import {ref} from "vue";
import {api} from "boot/axios";
import {CommFail, CommSeccess, CommWarn} from "components/common";
import {Commentinfo} from "components/models";
import {exportFile, useQuasar} from "quasar";


//刷新按钮
let loading = ref([false,])
let searchtext = ref('');

function simulateProgress(number: number) {
  loading.value[number] = true //这是那个加载动画
  localStorage.removeItem("commentcolumns")
  loadPage()
  setTimeout(() => {
    loading.value[number] = false
    CommSeccess("刷新成功")
  }, 500)

}

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

let loadingPage = ref(false)
loadPage()

function loadPage() {
  loadingPage.value = true
  //获取表格属性
  if (localStorage.getItem("commentcolumns") == null || localStorage.getItem("commentcolumns") == undefined) {
    api.get("/tablemenu/comment").then(res => {
      if (columns == undefined) {
        loadPage()
      }
      columns.value = res.data
      columns.value.forEach((comment) => {
        //@ts-ignore
        comment.align = "center"
      })
      localStorage.setItem("commentcolumns", JSON.stringify(columns))
    })
  } else {
    // @ts-ignore 不清楚怎么办到的，能跑就行
    const localInfo = JSON.parse(localStorage.getItem("commentcolumns"))._value
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
  api.get("/Rcomment/page?" + "pagesize=" + PageItem + "&currentpage=" + currentPage.value + "&searchtext=" + searchtext.value).then(res => {
    rows.value = res.data.data
    Pagecount.value = Math.ceil(res.data.total / PageItem)
  })
  setTimeout(() => {
    loadingPage.value = false
  }, 1000)

}

//新增评论
const commentinfo = new Commentinfo();
let windowDisplay = ref(false)
let buttonStatus: string = '新增评论'
//规则
let contentRules = ref([(val: string | any[]) => (val && val.length > 0) || '输入值为空'])


//清空
function onReset() {
  commentinfo.clearall()
}

//修改评论
//修改评论
function checkCounts() {
  buttonStatus = '修改评论'
  if (selected.value.length != 1) {
    CommWarn("请选择一个数据进行修改")
  } else {//@ts-ignore
    commentinfo.id.value = selected.value[0].id//@ts-ignore
    commentinfo.itemid.value = selected.value[0].itemid//@ts-ignore
    commentinfo.userid.value = selected.value[0].userid//@ts-ignore
    commentinfo.content.value = selected.value[0].content//@ts-ignore
    commentinfo.fromcommentid.value = selected.value[0].fromcommentid//@ts-ignore
    windowDisplay.value = true
  }
}

//提交新增或修改
function onSubmit() {
  if (buttonStatus === '新增评论') {
    if (commentinfo.itemid.value != '' && commentinfo.userid.value != '' && commentinfo.content.value != '') {
      api.post("/Rcomment/", {
        "id": commentinfo.id.value,
        "userid": commentinfo.userid.value,
        "itemid": commentinfo.itemid.value,
        "content": commentinfo.content.value,
        "fromcommentid": commentinfo.fromcommentid.value
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
  if (buttonStatus === '修改评论') {
    if (commentinfo.id.value != '' && commentinfo.itemid.value != '' && commentinfo.userid.value != '' && commentinfo.content.value != '') {
      api.put("/Rcomment/", {
        "id": commentinfo.id.value,
        "userid": commentinfo.userid.value,
        "itemid": commentinfo.itemid.value,
        "content": commentinfo.content.value,
        "fromcommentid": commentinfo.fromcommentid.value
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
  api.delete("/Rcomment/" + id).then(res => {
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

//搜索
function handlesearch() {
  loadPage()
}

function handleRest() {
  searchtext.value = "";
  console.log("重置了按钮")
  loadPage()
}

</script>

<style scoped>
.header .q-btn {
  margin-right: 15px;
}
</style>
