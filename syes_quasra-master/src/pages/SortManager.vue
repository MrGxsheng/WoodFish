<template>
  <div class="q-pa-md q-gutter-sm">
    <div class="header">
      <q-btn class="shadow-1" unelevated color="primary" label="刷新" :loading="loading[0]"
             @click="simulateProgress(0)" icon="replay"/>
      <q-btn class="shadow-1" unelevated color="primary" label="清空筛选" :loading="loading[0]"
             @click="loadPage();loadPage2();" icon="replay"/>
      <q-btn class="shadow-1" unelevated color="secondary" label="新增"
             @click="windowDisplay=true;onReset();buttonStatus='新增'"
             icon="add_circle_outline"/>
      <q-btn class="shadow-1" unelevated color="purple" label="修改" @click="checkCounts();buttonStatus='修改'"
             icon="edit"/>
      <q-btn class="shadow-1" unelevated color="red" label="删除" @click="showNotif" icon="delete_forever"/>
    </div>
    <div class="row">
      <div class="col q-pa-md">
        <q-table
            style="max-height: 800px"
            title="父级菜单"
            :rows="Prows"
            :columns="Pcolumns"
            row-key="id"
            selection="multiple"
            :pagination="{rowsPerPage: 20}"
            v-model:selected="selected"
            :loading="loadingPage"
        >
          <template v-slot:body-cell-psort="props">
            <q-td style="text-align: center">
              <q-btn color="primary" label="筛选" size="sm" @click="handleCheck(props.row.id)"/>
            </q-td>
          </template>
        </q-table>
      </div>
      <div class="col">
        <div class="col q-pa-md">
          <q-table
              style="max-height: 800px"
              virtual-scroll
              title="子集菜单"
              :rows="Nrows"
              :columns="Ncolumns"
              row-key="id"
              :pagination="{rowsPerPage: 20}"
              selection="multiple"
              v-model:selected="selected"
              :loading="loadingPage"
          />
        </div>
      </div>
    </div>


    <q-dialog v-model="windowDisplay" position="right">
      <q-card class="column full-height" style="width: 400px">
        <q-card-section class="row items-center q-pb-none ">
          <div class="text-h6">{{ buttonStatus === '新增' ? '新增' : '修改' }}</div>
          <q-space/>
          <q-btn icon="close" flat round dense v-close-popup/>
        </q-card-section>
        <div class="q-pa-md" style="max-width: 300px;margin-left: 30px">
          <form @submit.prevent.stop="onSubmit()" @reset.prevent.stop="onReset()" class="q-gutter-md">
            <q-input
                v-model="sort.name.value"
                label="分类名称"
                lazy-rules
            />
            <q-input
                ref="order.useridRef.value"
                v-model="sort.icon.value"
                label="图标"
            />
            <q-input
                ref="order.useridRef.value"
                v-model="sort.pid.value"
                label="父级id"
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

import {api} from "boot/axios";
import {ref} from "vue";
import {CommSeccess, CommWarn} from "components/common";
import {Sort} from "components/models";
import {useQuasar} from "quasar";

let loadingPage = ref(false)
let Pcolumns = ref([])
let Prows = ref([])
let Ncolumns = ref([])
let Nrows = ref([])
let loading = ref([false,])
let selected = ref([])
let windowDisplay = ref(false)
let sort = new Sort()
let checkid = ref('')
let buttonStatus = ref('')

//加载动画
function simulateProgress(number: number) {
  checkid.value = ''
  loading.value[number] = true //这是那个加载动画
  localStorage.removeItem("psortcolumns")
  localStorage.removeItem("nsortcolumns")
  loadPage();
  loadPage2();
  setTimeout(() => {
    loading.value[number] = false

    // CommSeccess("刷新成功")
  }, 500)

}

//重置
function onReset() {
  sort.clearall()
}

//新增提交
function onSubmit() {
  if (buttonStatus.value === '新增') {
    api.post("/Rsort/", {
      "name": sort.name.value,
      "icon": sort.icon.value,
      "pid": sort.pid.value
    }).then(res => {
      if (res.code === "200") {
        CommSeccess("提交成功")
      }

      windowDisplay.value = false
      loadPage()
      loadPage2()
    })
  }
  if (buttonStatus.value === '修改') {
    api.put("/Rsort/", {
      "id": sort.id.value,
      "name": sort.name.value,
      "icon": sort.icon.value,
      "pid": sort.pid.value
    }).then(res => {
      if (res.code === "200") {
        CommSeccess("操作成功")
      }

      windowDisplay.value = false
      loadPage()
      loadPage2()
    })
  }
}

//修改
function checkCounts() {
  if (selected.value.length != 1) {
    CommWarn("请选择一个数据进行修改")
  } else {//@ts-ignore
    sort.id.value = selected.value[0].id//@ts-ignore
    sort.name.value = selected.value[0].name//@ts-ignore
    sort.icon.value = selected.value[0].icon//@ts-ignore
    sort.pid.value = selected.value[0].pid//@ts-ignore
    windowDisplay.value = true
  }
}

//加载页面
loadPage()

function loadPage() {
  loadingPage.value = true
  //获取表格属性
  if (localStorage.getItem("psortcolumns") == null || localStorage.getItem("psortcolumns") == undefined) {
    api.get("/tablemenu/psort").then(res => {
      // console.log('刷新表格') //握草怪死了，改成 刷新了表格 就会报错
      if (Pcolumns == undefined) {
        loadPage()
        console.log('重新加载')
      }
      Pcolumns.value = res.data
      Pcolumns.value.forEach((item) => {
        //@ts-ignore
        item.align = "center"
      })
      localStorage.setItem("psortcolumns", JSON.stringify(Pcolumns))
    })
  } else {
    // @ts-ignore 不清楚怎么办到的，能跑就行
    const localInfo = JSON.parse(localStorage.getItem("psortcolumns"))._value
    if (localInfo == undefined) {
      setTimeout(() => {
        simulateProgress(0)
      }, 500)
      console.log('刷新快了');
    } else {
      Pcolumns = localInfo
    }
  }

//获取分页数据
  api.get("/Rsort/p").then(res => {
    Prows.value = res.data
  })

  setTimeout(() => {
    loadingPage.value = false
  }, 1000)

}

loadPage2()

function loadPage2() {
  if (localStorage.getItem("nsortcolumns") == null || localStorage.getItem("nsortcolumns") == undefined) {
    api.get("/tablemenu/nsort").then(res => {
      // console.log('刷新表格') //握草怪死了，改成 刷新了表格 就会报错
      if (Ncolumns == undefined) {
        loadPage()
      }
      Ncolumns.value = res.data
      Ncolumns.value.forEach((item) => {
        //@ts-ignore
        item.align = "center"
      })
      localStorage.setItem("nsortcolumns", JSON.stringify(Ncolumns))
    })
  } else {
    // @ts-ignore 不清楚怎么办到的，能跑就行
    const localInfo = JSON.parse(localStorage.getItem("nsortcolumns"))._value
    if (localInfo == undefined) {
      setTimeout(() => {
        simulateProgress(0)
      }, 500)
      console.log('刷新快了');
    } else {
      Ncolumns = localInfo
    }
  }
  api.get("/Rsort/n?checkid=" + checkid.value).then(res => {
    Nrows.value = res.data
  })
}

//筛选
function handleCheck(props: any) {
  checkid.value = props
  loadPage()
  loadPage2()
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
            loadPage2()
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
  api.delete("/Rsort/" + id).then(res => {
    if (res.code == "200") {
      CommSeccess('成功删除')
    }
  })
  loadPage()
  loadPage2()
}
</script>

<style scoped>
.header .q-btn {
  margin-right: 15px;
}
</style>
