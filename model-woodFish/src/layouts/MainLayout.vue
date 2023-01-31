<template>
  <q-layout view="hHh Lpr fFf">
    <q-header elevated>
      <q-toolbar class="bg-purple">
        <q-btn flat dense round icon="menu" aria-label="Menu" @click="toggleLeftDrawer" />

        <q-toolbar-title>
          <div class="text-body1">
            {{ CurrentMemu.desc }}
          </div>
        </q-toolbar-title>

        <div>用户名:{{ getUserInfo() }}
          &nbsp;
          &nbsp;
          <q-btn color="red" label="退出" size="sm" @click="handelLogOut" />
        </div>
      </q-toolbar>
    </q-header>

    <q-drawer v-model="leftDrawerOpen" show-if-above bordered>
      <q-list>
        <Aside :link="CurrentMemu.link" />
      </q-list>
    </q-drawer>

    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import Aside from 'src/components/Aside.vue';
import { menus } from 'src/components/models';
import { RouterView, useRouter } from 'vue-router';
import { CommonWarn, getLocalItem } from 'src/components/common';
import { QLayout, QHeader, QToolbar, QBtn, QToolbarTitle, QDrawer, QList, QPageContainer } from 'quasar';
const $router = useRouter()
const leftDrawerOpen = ref(false)
let CurrentMemu: any = ref([])
function loadPage() {
  updateBreadcrumbs()
}
loadPage()

//获取用户名
function getUserInfo(): any { //any这下好了吧
  if (getLocalItem('username') == null) {
    CommonWarn('用户未登录')
    $router.push('/login')
  } else {
    return getLocalItem('username')
  }
}
//左上角抽屉
function toggleLeftDrawer() {
  leftDrawerOpen.value = !leftDrawerOpen.value
}
//监听面包屑
watch(() => $router.currentRoute.value.path, (oldValue, newValue) => {
  updateBreadcrumbs()
})
//面包屑
function updateBreadcrumbs() {
  menus.forEach((element: any) => {
    if (element.link == $router.currentRoute.value.path.replace('/', '')) {
      CurrentMemu.value = element
    }
  });
}
//登出
function handelLogOut() {
  localStorage.clear()
  $router.push('/login')
}
</script>
<style>
.body--dark {
  background: #000
}
</style>
