<template>
  <!-- 顶部按钮 -->
  <div class="q-pa-md q-gutter-sm">
    <div class="header">
      <q-btn class="shadow-1" unelevated color="primary" label="刷新" :loading="loading[0]"
             @click="simulateProgress(0)" icon="replay"/>
      <q-btn class="shadow-1" unelevated color="secondary" label="新增"
             @click="windowDisplay=true;onReset();buttonStatus='新增用户'"
             icon="add_circle_outline"/>
      <q-btn class="shadow-1" unelevated color="purple" label="修改" @click="checkCounts();buttonStatus='修改用户'"
             icon="edit"/>
      <q-btn class="shadow-1" unelevated color="red" label="删除" @click="showNotif" icon="delete_forever"/>
      <q-btn class="shadow-1" unelevated color="brown-5" label="导出" @click="exportTable" icon="file_download"/>
    </div>

    <!--  表格  -->
    <div class="q-pa-md" style="margin-left:auto">
      <q-table
          title="用户信息管理"
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
          <!--搜索框-->
          <q-input label="学号搜索" v-model="searchtext" :dense=true
                   style="display: inline-block;float: right;margin-right: 20px" debounce="1000">
            <template v-slot:append>
              <q-icon name="search" @click="handlesearch()" class="cursor-pointer"/>
              <q-icon v-if="searchtext !== ''" name="close" @click="handleRest" class="cursor-pointer"/>
            </template>
          </q-input>
        </template>
      </q-table>
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
      <!--新增弹出框-->
      <q-dialog v-model="windowDisplay" position="right">
        <q-card class="column full-height" style="width: 400px">
          <q-card-section class="row items-center q-pb-none ">
            <div class="text-h6">{{ buttonStatus }}</div>
            <q-space/>
            <q-btn icon="close" flat round dense v-close-popup/>
          </q-card-section>
          <div class="q-pa-md" style="max-width: 300px;margin-left: 30px">
            <form @submit.prevent.stop="onSubmit" @reset.prevent.stop="onReset" class="q-gutter-md">
              <q-input
                  ref="userinfo.idRef.value"
                  v-model="userinfo.id.value"
                  label="学号"
                  hint="学生学号"
                  lazy-rules
                  :rules="idRules"
                  :readonly="buttonStatus==='修改用户'"
              />
              <q-input
                  ref="userinfo.infoidRef.value"
                  v-model="userinfo.infoid.value"
                  label="认证id"
                  hint="一般留空"
                  lazy-rules
                  :readonly="buttonStatus==='修改用户'"
              />
              <q-input
                  ref="userinfo.nameRef.value"
                  v-model="userinfo.name.value"
                  label="姓名"
                  hint="输入真实姓名"
                  lazy-rules
                  :rules="nameRules"
              />
              <q-input
                  ref="userinfo.passwordRef.value"
                  v-model="userinfo.password.value"
                  label="密码"
                  hint="请输入密码"
                  lazy-rules
                  :rules="nameRules"
                  :type="userinfo.isPwd.value ? 'password' : 'text'"
              >
                <template v-slot:append>
                  <q-icon
                      :name="userinfo.isPwd.value ? 'visibility_off' : 'visibility'"
                      class="cursor-pointer"
                      @click="userinfo.isPwd.value = !userinfo.isPwd.value"
                  />
                </template>
              </q-input>
              <q-input
                  ref="userinfo.passwordRef.value"
                  v-model="userinfo.repassword.value"
                  label="确认密码"
                  hint="请确认输入密码"
                  lazy-rules
                  :rules="passwordRules"
                  type="password"
              />
              <q-input
                  ref="userinfo.nickname.value"
                  v-model="userinfo.nickname.value"
                  label="昵称"
                  hint="输入昵称，创建后可更改"
                  lazy-rules
                  :rules="nameRules"
              />
              <q-input
                  ref="userinfo.phoneRef.value"
                  v-model="userinfo.phone.value"
                  label="手机号"
                  hint="请输入正确格式手机号"
                  lazy-rules
              />

              <q-toggle v-model="userinfo.accept.value" label="同意许可协议"/>

              <div>
                <q-btn v-if="buttonStatus==='新增用户'" label="提交" type="submit" color="primary"/>
                <q-btn v-if="buttonStatus==='修改用户'" label="提交修改" type="submit" color="primary"/>
                <q-btn v-if="buttonStatus==='新增用户'" label="重置" type="reset" color="primary" flat class="q-ml-sm"/>
              </div>
            </form>
          </div>
        </q-card>
      </q-dialog>
    </div>
  </div>
</template>

<script setup lang="ts">
import {exportFile, useQuasar} from 'quasar'
import {ref, Ref} from "vue";
import {api} from "boot/axios";
import {Userinfo} from "components/models";
import {CommFail, CommSeccess, CommWarn} from "components/common";

//插件初始化
const $q = useQuasar()
let searchtext = ref('');
//刷新按钮
let loading = ref([false,])

function simulateProgress(number: number) {
  loading.value[number] = true //这是那个加载动画
  localStorage.removeItem("usercolumns")
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
const currentPage = ref(1); //当前页面
let Pagecount = ref(1)  //页数
const PageItem: number = 10   //页面数据数量
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
  if (localStorage.getItem("usercolumns") == null) {
    api.get("/tablemenu/user").then(res => {
      if (columns == undefined) {
        loadPage()
      }
      columns.value = res.data
      columns.value.forEach((item: any) => {
        item.align = "center"
      })
      localStorage.setItem("usercolumns", JSON.stringify(columns))
    })
  } else {
    // @ts-ignore 不清楚怎么办到的，能跑就行
    const localInfo = JSON.parse(localStorage.getItem("usercolumns"))._value
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
  api.get("/Ruser/page?" + "pagesize=" + PageItem + "&currentpage=" + currentPage.value + "&searchtext=" + searchtext.value).then(res => {
    rows.value = res.data.data
    Pagecount.value = Math.ceil(res.data.total / PageItem)
  })

  setTimeout(() => {
    loadingPage.value = false
  }, 1000)

}

//搜索
function handlesearch() {
  loadPage()
}

function handleRest() {
  searchtext.value = "";

  loadPage()
}

//删除
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
          deleteUsers_ById(idlist)

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
function deleteUsers_ById(idlist: any) {
  idlist.value.forEach((item: string) => {
    // 先用枚举删除将就一下
    deleteUserById(item)
  })
}

// 根据id删除单个用户
function deleteUserById(id: string) {
  api.delete("Ruser/" + id).then(res => {
    if (res.code == "200") {
      CommSeccess('成功删除')
    }
  })
}


//新增用户
const userinfo = new Userinfo();
let windowDisplay = ref(false)
let buttonStatus: string = '新增用户'

//表格规则
let accept = ref(false)
//@ts-ignore 我不知道为什么，但是它能跑
let passwordRules = ref([(val: Ref<string>) => (val == userinfo.password.value) || '两次输入密码不一致'])
let nameRules = ref([(val: string | any[]) => (val && val.length > 0) || '输入值为空'])

let ageRules = ref([
  (val: string | null) => (val !== null && val !== '') || '请输入年龄',
  (val: number) => (val > 0 && val < 100) || '数字格式不正确'
])
let idRules = ref([
  (val: number) => (val > 20191111111 && val < 20229999999) || '请输入正确的学号'
])

//新增用户提交
function onSubmit() {
  if (userinfo.accept.value == true) {

    if (userinfo.name.value != '' && userinfo.id.value != '' && userinfo.password.value != '' && userinfo.nickname.value != '' && userinfo.password.value == userinfo.repassword.value && userinfo.phone.value != '') {
      if (buttonStatus === '新增用户') {

        api.post("/Ruser/", {
          "realname": userinfo.name.value,
          "password": userinfo.password.value,
          "nickname": userinfo.nickname.value,
          "phone": userinfo.phone.value,
          "id": userinfo.id.value
        }).then(res => {
          if (res.status == 200) {
            $q.notify({
              icon: 'done',
              color: 'positive',
              message: '提交成功',
              position: 'top'
            })
            loadPage()
          }
        })
      }
      if (buttonStatus === '修改用户') {

        api.put("/Ruser/", {
          "realname": userinfo.name.value,
          "password": userinfo.password.value,
          "nickname": userinfo.nickname.value,
          "phone": userinfo.phone.value,
          "id": userinfo.id.value
        }).then(res => {
          if (res.status == 200) {
            CommSeccess("提交成功")
            loadPage()
          }
        })
      }
      userinfo.accept.value = false
      windowDisplay.value = false
      onReset()
      loadPage()
    } else {
      CommFail('请检查格式是否正确')
    }
  } else {
    CommFail('请同意协议')
  }
}

function onReset() {
  userinfo.clearall()
  accept.value = false
}

//修改用户
function checkCounts() {
  buttonStatus = '修改用户'
  if (selected.value.length != 1) {
    CommWarn("请选择一个用户进行修改")
  } else {//@ts-ignore
    userinfo.id.value = selected.value[0].id//@ts-ignore
    userinfo.name.value = selected.value[0].realname//@ts-ignore
    userinfo.nickname.value = selected.value[0].nickname//@ts-ignore
    userinfo.phone.value = selected.value[0].phone//@ts-ignore
    userinfo.infoid.value = selected.value[0].infoid
    windowDisplay.value = true
  }
}


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
.header .q-btn {
  margin-right: 15px;
}

</style>
