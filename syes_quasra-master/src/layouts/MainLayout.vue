<!-- Aside -->
<template>


  <q-layout view="hHh lpr lFr">

    <!-- 顶栏 -->
    <q-header elevated>
      <q-toolbar>
        <q-btn
            flat
            dense
            round
            icon="menu"
            aria-label="Menu"
            @click="toggleLeftDrawer"
        />
        <!--    中间标题    -->
        <q-toolbar-title style="font-size: 14px">
          <q-breadcrumbs>
            <q-breadcrumbs-el :label="positions[0]" icon="home" style="color: white"/>
            <q-breadcrumbs-el :label="positions[1]" icon="widgets" style="color: white"/>
            <q-breadcrumbs-el label="详情" style="color: white"/>
          </q-breadcrumbs>
        </q-toolbar-title>

        <div>{{ UserNow }}
          <q-btn round color="deep-orange" icon="logout" size="sm" @click="logout"/>
        </div>
      </q-toolbar>
    </q-header>

    <!--  侧栏  -->
    <q-drawer
        v-model="leftDrawerOpen"
        show-if-above
        bordered
        :breakpoint="500"

        :mini="miniState"
    >
      <q-list>
        <!--        <q-item-label header class="q-mini-drawer-only">-->
        <!--          后台管理系统-->
        <!--        </q-item-label>-->
        <!--        真正的侧栏-->
        <Aside :miniState="miniState"/>

      </q-list>
    </q-drawer>


    <!--  中间内容 routerView  -->
    <q-page-container>
      <router-view/>
    </q-page-container>
  </q-layout>
</template>

<script setup lang="ts">
import {ref, watch} from 'vue';
import Aside from '../components/Aside.vue';
import {useStore} from "src/store";
import {menu} from "components/models";
import {useRouter} from "vue-router/dist/vue-router";
import {api} from "boot/axios";

const $router = useRouter()
const $store = useStore()

//展开收起侧边栏
const leftDrawerOpen = ref(false)

//已弃用
function toggleLeftDrawer() {
  // leftDrawerOpen.value = !leftDrawerOpen.value
  miniState.value = !miniState.value
}

//默认侧栏开启
let miniState = ref(false)

const UserNow = ref(localStorage.getItem('username'))
const Useravatar = ref(localStorage.getItem('avatar'))

CheckLogin()
//面包屑
let menus = ref(menu)
let positions = ref()
//面包屑寻找数据
watch(() => $router.currentRoute.value.path, (newValue, oldValue) => {
  positions.value = findTitle()
}, {immediate: true})

function findTitle() {
  let position = []
  for (let i = 0; i < menus.value.length; i++) {
    for (let j = 0; j < menus.value[i].children.length; j++) {
      if (menus.value[i].children[j].link === $router.currentRoute.value.path.replace("/", '')) {
        position[0] = menus.value[i].label
        position[1] = menus.value[i].children[j].desc
        return position
      }
    }
  }
}

function logout() {
  $router.push("/login")
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  localStorage.removeItem('avatar')
}

function CheckLogin() {
  api.get('/rootuser/online').then(res => {
    console.log(res.data)
  })
}
</script>
