<template>
  <!-- 顶部提示 -->
  <div class="q-pa-md q-gutter-sm">
    <div class="header">
      <q-btn class="shadow-1" unelevated color="primary" label="刷新" :loading="loading[0]"
             @click="simulateProgress(0)" icon="replay"/>
      <q-btn class="shadow-1" unelevated color="secondary" label="上传"
             @click="windowDisplay=true;"
             icon="add_circle_outline"/>
      <q-btn class="shadow-1" unelevated color="red" label="删除" @click="showNotif" icon="delete_forever"/>
    </div>
    <!--  表格  -->
    <div class="q-pa-md" style="margin-left:auto">
      <q-table
          title="图片管理"
          :rows="rows"
          :columns="columns"
          row-key="name"
          hide-pagination
          :pagination="pagination"
          :selected-rows-label="getSelectedString"
          selection="multiple"
          v-model:selected="selected"
          :loading="loadingPage"
          grid
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
        <template v-slot:item="props">
          <div
              class="q-pa-xs col-xs-12 col-sm-6 col-md-4 col-lg-3 grid-style-transition"
              :style="props.selected ? 'transform: scale(0.95);' : ''"
          >
            <q-card :class="props.selected ? 'bg-grey-2' : ''">
              <q-card-section>
                <q-checkbox dense v-model="props.selected" :label="props.row.name"/>
              </q-card-section>
              <q-card-section>
                <q-img :src="props.row.url" style="max-height: 230px;max-width: 100%">
                </q-img>
              </q-card-section>
              <q-separator/>
              <q-expansion-item
                  v-model="openall"
                  icon="camera"
                  :label="'用户: '+props.row.userid"
                  :caption="' '+props.row.createtime"
              >
                <q-item v-for="col in props.cols.filter(col => col.name !== 'desc')"
                        :key="col.name">
                  <q-item-section>
                    <!--        获取了全部属性            -->
                    <q-item-label>{{ col.label }}</q-item-label>
                  </q-item-section>
                  <q-item-section side>
                    <!--        显示属性值            -->
                    <q-item-label caption v-if="col.label!=='可用状态'">{{ col.value }}
                    </q-item-label>
                    <q-item-label caption v-if="col.label==='可用状态'">
                      <div @click="switchbutton(props.row)" style="text-align: center">
                        <q-btn v-if="col.value===false" color="primary" label="可用" size="sm"/>
                        <q-btn v-if="col.value===true" color="red" label="已删除" size="sm"/>
                      </div>
                    </q-item-label>
                  </q-item-section>
                </q-item>
              </q-expansion-item>
            </q-card>
          </div>
        </template>
      </q-table>
    </div>
    <!--  分页  -->
    <div class=" q-pa-lg flex flex-center
                      ">
      <q-pagination
          v-model="currentPage"
          :max="Pagecount"
          direction-links
          @click="handlePage()"
          style="min-width: 2em"
      />
    </div>

    <!--文件上传-->
    <q-dialog v-model="windowDisplay" position="top">
      <q-uploader
          url="http://localhost:8000/file/upload"
          label="图片上传"
          multiple
          color="teal"
          style="max-width: 300px"
          max-file-size="1048576"
          max-files="10"
          auto-upload
          field-name="file"
          :form-fields="[{name: 'userid', value: userid.value}]"
      />
    </q-dialog>

  </div>
</template>

<script lang="ts" setup>
//刷新按钮
import {ref} from "vue";
import {CommSeccess} from "components/common";
import {api} from "boot/axios";
import {useQuasar} from "quasar";

let loading = ref([false,])
let searchtext = ref('');

function simulateProgress(number: number) {
  loading.value[number] = true //这是那个加载动画
  localStorage.removeItem("piccolumns")
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
const PageItem = 8   //页面数据数量
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
  if (localStorage.getItem("piccolumns") == null || localStorage.getItem("piccolumns") == undefined) {
    api.get("/tablemenu/picdto").then(res => {
      if (columns == undefined) {
        loadPage()
      }
      columns.value = res.data
      columns.value.forEach((pic) => {
        //@ts-ignore
        pic.align = "center"
      })
      localStorage.setItem("piccolumns", JSON.stringify(columns))
      // console.log(localStorage.getItem("piccolumns"))
      // console.log(JSON.parse(localStorage.getItem("piccolumns")))
    })
  } else {
    // @ts-ignore 不清楚怎么办到的，能跑就行
    const localInfo = JSON.parse(localStorage.getItem("piccolumns"))._value
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
  api.get("/Rfile/page?" + "pagesize=" + PageItem + "&currentpage=" + currentPage.value + "&searchtext=" + searchtext.value).then(res => {

    rows.value = res.data.data
    Pagecount.value = Math.ceil(res.data.total / PageItem)
  })
  setTimeout(() => {
    loadingPage.value = false
  }, 1000)

}

//图片整体展开或关闭
let openall = ref(false)
//文件上传
let windowDisplay = ref(false)
let userid = ref(20201313013)

//切换按钮状态
function switchbutton(value: { id: string; is_delete: any; }) {

  api.get("/Rfile/status?id=" + value.id + "&status=" + !value.is_delete).then(() => {
    loadPage()
    CommSeccess("操作成功")
  })
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
  api.delete("/Rfile/" + id).then(res => {
    if (res.code == "200") {
      CommSeccess('成功删除')
    }
  })
}


</script>

<style scoped>
.header .q-btn {
  margin-right: 15px;
}
</style>
<style lang="sass">
.grid-style-transition
  transition: transform .28s, background-color .28s
</style>
