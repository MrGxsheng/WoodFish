<template>
  <div class="q-pt-md q-pl-md">
    <q-btn color="purple" label="刷新" icon="autorenew" @click="flashTop" />
  </div>
  <div class="q-pa-md" style="max-width: 800px">
    <q-table
      title="排行榜"
      :rows="tops"
      :columns="columns"
      row-key="name"
      hide-bottom
      rows-per-page-label="每页显示"
      :pagination="InitialPagination"
    >
      <template v-slot:body-cell-god="props">
        <q-td :props="props">
          <q-btn
            color="purple"
            label="上帝"
            size="sm"
            @click="clearScore(props)"
          />
        </q-td>
      </template>

      <template v-slot:body-cell-handle="props">
        <q-td :props="props">
          <q-btn
            color="purple"
            label="删除"
            size="sm"
            @click="handleUse(props)"
          />
        </q-td>
      </template>
    </q-table>
  </div>
</template>
<script setup lang="ts">
import { api } from "src/boot/axios";
import { ref } from "vue";
import { CommonSuccess } from "src/components/common";
let username = localStorage.getItem("username");
let score = localStorage.getItem("score");
let tops = ref([]);
let InitialPagination: any = {
  rowsPerPage: 20,
};
const columns: any = [
  {
    name: "username",
    required: true,
    label: "用户名",
    align: "left",
    field: "username",
    sortable: true,
  },
  {
    name: "score",
    align: "center",
    label: "score",
    field: "score",
    sortable: true,
  },
  {
    name: "createtime",
    align: "center",
    label: "创建日期",
    field: "createtime",
    sortable: true,
  },

  {
    name: "regip",
    align: "center",
    label: "临时Ip",
    field: "regip",
    sortable: false,
  },

  {
    name: "god",
    align: "center",
    label: "上帝",
    field: "god",
    sortable: false,
  },

  {
    name: "handle",
    align: "center",
    label: "操作",
    field: "handle",
    sortable: false,
  },
];

function flashTop() {
  api.get("/user/list").then((res: any) => {
    tops.value = res.data.data;
  });
}
flashTop();

function handleUse(props: any) {
  api.delete("/user/" + props.row.id).then((res: any) => {
    CommonSuccess("删除成功");
    flashTop();
  });
}

function clearScore(props: any) {
  api
    .put("/user", {
      id: props.row.id,
      username: username?.valueOf,
      score: 0,
    })

    .then((res: any) => {
      CommonSuccess("清除成功");
      flashTop();
    });
}
</script>
