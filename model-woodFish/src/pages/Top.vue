<template>
  <div class="q-pt-md q-pl-md">
    {{ '用户名 :'+username + '分数： ' + score }}
  </div>
  <div class="q-pt-md q-pl-md">
    <q-btn color="purple" label="刷新" icon="autorenew" @click="flashTop" />
  </div>
  <div class="q-pa-md" style="max-width: 800px;">
    <q-table title="排行榜" :rows="tops" :columns="columns" row-key="name" rows-per-page-label="每页显示"
      :pagination="InitialPagination" />
  </div>
</template>
<script setup lang="ts">
import { api } from 'src/boot/axios';
import { ref } from 'vue';
let username = localStorage.getItem('username')
let score = localStorage.getItem('score')
let tops = ref([])
let InitialPagination: any = {
  rowsPerPage: 20
}
const columns: any = [
  { name: 'username', required: true, label: '用户名', align: 'left', field: 'username', sortable: true },
  { name: 'score', align: 'center', label: 'score', field: 'score', sortable: true },
  { name: 'createTime', label: '创建日期', field: 'createTime', sortable: true },
]
function flashTop() {
  api.get('/user/top').then((res: any) => {
    tops.value = res.data
  })
}
flashTop()
</script>
