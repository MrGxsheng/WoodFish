<template>
  <div class="q-pt-md q-pl-md">
    {{ '用户名 :'+username + '分数： ' + score }}
  </div>
  <div class="q-pa-md" style="max-width: 800px;">
    <q-table title="道具" :rows="items" :columns="columns" row-key="name" hide-bottom>
      <template v-slot:body-cell-handle="props">
        <q-td :props="props">
          <q-btn color="purple" label="使用" size="sm" @click="handleUse(props)" />
        </q-td>
      </template>
    </q-table>
  </div>
</template>
<script setup lang="ts" >
import { QTable, QTd, QBtn } from 'quasar';
import { api } from 'src/boot/axios';
import { DialogAlert } from 'src/components/common';
import { ref } from 'vue';
let items: any = ref([])
const columns: any = [
  { name: 'id', align: 'left', label: 'ID', field: 'id', sortable: true },
  { name: 'name', label: '道具名称', align: 'center', field: 'name', sortable: true },
  { name: 'description', label: '描述', field: 'description', align: 'center', sortable: true },
  { name: 'cost', label: '花费', field: 'cost', sortable: true },
  { name: 'handle', label: '操作', field: 'handle', sortable: false },
]
let username = localStorage.getItem('username')
let score: any = localStorage.getItem('score')
function loadPage() {
  api.get('/item/getAll').then(res => {
    items.value = res.data
  })
}
loadPage()
//用户消费
function handleUse(props: any) {
  api.get('/cost/one?username=' + username + '&cost=' + props.row.cost + '&itemid=' + props.row.id + '&palt=2').then((res: any) => {
    if (res.code == 200) {
      if (props.row.id == 1) {
        DialogAlert('佛不在乎')
      }
      if (props.row.id == 2) {
        DialogAlert('韩明不在乎')
      }
      if (props.row.id == 3) {
        DialogAlert('宝不在乎')
      }
      score = score - props.row.cost
      localStorage.setItem('score', score)
    }
  })
}

</script>
