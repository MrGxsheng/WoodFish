<template>
  <div class="q-pt-md q-pl-md">
    <q-btn color="purple" label="刷新" icon="autorenew" @click="flashTop" />
  </div>
  <div class="q-pa-md" style="max-width: 800px">
    <q-table
      title="花费记录"
      :rows="tops"
      :columns="columns"
      row-key="name"
      rows-per-page-label="每页显示"
      :pagination="InitialPagination"
    />
  </div>
</template>
<script setup lang="ts">
import { api } from "src/boot/axios";
import { ref } from "vue";

let tops = ref([]);
let InitialPagination: any = {
  rowsPerPage: 20,
};
const columns: any = [
  {
    name: "userId",
    required: true,
    label: "用户ID",
    align: "left",
    field: "userId",
    sortable: true,
  },
  {
    name: "cost",
    align: "center",
    label: "cost",
    field: "cost",
    sortable: true,
  },
  {
    name: "itemID",
    align: "center",
    label: "道具ID",
    field: "itemId",
    sortable: true,
  },
  {
    name: "platformId",
    align: "center",
    label: "platformID",
    field: "platformId",
    sortable: true,
  },
  {
    name: "createTime",
    label: "创建日期",
    field: "createTime",
    sortable: true,
  },
];
function flashTop() {
  api.get("/cost/list").then((res: any) => {
    tops.value = res.data.data;
  });
}
flashTop();
</script>
