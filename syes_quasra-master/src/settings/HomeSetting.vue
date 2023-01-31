<template>
  <div class="q-pa-md q-gutter-sm">
    <div class="row">
      <div class="col">
        <!--    按钮    -->
        <div class="header q-pa-md">
          <q-btn class="shadow-1" unelevated color="primary" label="刷新" :loading="loading[0]"
                 @click="simulateProgress(0)" icon="replay"/>
          <q-btn class="shadow-1" unelevated color="secondary" label="新增" @click="windowDisplay=true;"
                 icon=" add_circle_outline"/>
          <q-btn class="shadow-1" unelevated color="red" label="删除" @click="showNotif"
                 icon="delete_forever"/>
        </div>
        <!--    表格    -->
        <q-table
            title="轮播图信息"
            :rows="rows"
            :columns="columns"
            row-key="id"
            :pagination="{rowsPerPage:20}"
            selection="multiple"
            v-model:selected="selected"
            :loading="loadingPage"
        />
        <!--    轮播图    -->
      </div>
      <div class="col q-pa-md">
        <div class="q-pa-md" style="padding:0">
          <q-carousel
              swipeable
              animated
              v-model="slide"
              navigation
              infinite
              :autoplay="autoplay"
              arrows
              transition-prev="slide-right"
              transition-next="slide-left"
              @mouseenter="autoplay = false"
              @mouseleave="autoplay = true"
              height="200px"
          >
            <q-carousel-slide v-for="item in rows" :key="item.id" :name="item.id" :img-src="item.url"/>
          </q-carousel>
        </div>
      </div>
    </div>
    <!--  新增窗口  -->
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
          :form-fields="[{name: 'userid', value: 20201313013}]"
          @uploaded="ImgInfo"
      />
    </q-dialog>
  </div>
</template>

<script setup lang="ts">
import {ref} from "vue";
import {api} from "boot/axios";
import {CommSeccess} from "components/common";
import {useQuasar} from "quasar";

const slide = ref(1)
const autoplay = ref(2000)
let windowDisplay = ref(false)
const selected = ref([])
const uploadUrl = ref()

//获取上传的回调
function ImgInfo(info: any) {
  uploadUrl.value = JSON.parse(info.xhr.response).data.url
  api.get("/Rrollimg/url?url=" + uploadUrl.value).then(res => {
    CommSeccess("上传成功")
    loadPage()
  })
  // api.post('/rollimg/url', {
  //   'url': uploadUrl.value
  // }).then(res => {
  //   console.log(res)
  //   loadPage()
  // })
}


//获取后端数据
let columns = ref([])
let rows = ref([])
let loadingPage = ref(false)


//刷新按钮
let loading = ref([false,])

function simulateProgress(number: number) {
  loading.value[number] = true //这是那个加载动画
  localStorage.removeItem("rollcolumns")
  loadPage()
  setTimeout(() => {
    loading.value[number] = false
    CommSeccess("刷新成功")
  }, 500)

}

//加载页面
loadPage()

function loadPage() {
  loadingPage.value = true
  //获取表格属性
  if (localStorage.getItem("rollcolumns") == null) {
    api.get("/tablemenu/rollimg").then(res => {
      if (columns == undefined) {
        loadPage()
      }
      columns.value = res.data
      columns.value.forEach((item: any) => {
        item.align = "center"
      })
      localStorage.setItem("rollcolumns", JSON.stringify(columns))
    })
  } else {
    // @ts-ignore 不清楚怎么办到的，能跑就行
    const localInfo = JSON.parse(localStorage.getItem("rollcolumns"))._value
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
  api.get("/Rrollimg/").then(res => {
    rows.value = res.data
  })

  setTimeout(() => {
    loadingPage.value = false
  }, 1000)

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
  api.delete("/Rrollimg/" + id).then(res => {
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