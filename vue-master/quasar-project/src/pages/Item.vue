<template>
  <div class="q-pa-md">
    <q-btn label="添加道具" color="primary" @click="prompt = true" />
    <!-- 取消任意点击关闭悬浮窗 加上  persistent -->
    <q-dialog v-model="prompt">
      <q-card style="width: 700px; max-width: 80vw">
        <q-card-section>
          <div class="text-h6">道具名称</div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          <q-input
            dense
            v-model="name"
            autofocus
            @keyup.enter="prompt = false"
          />
        </q-card-section>

        <q-card-section>
          <div class="text-h6">道具描述</div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          <q-input
            dense
            v-model="description"
            autofocus
            @keyup.enter="prompt = false"
          />
        </q-card-section>

        <q-card-section>
          <div class="text-h6">道具花费</div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          <q-input
            dense
            v-model="cost"
            autofocus
            @keyup.enter="prompt = false"
          />
        </q-card-section>
        <q-card-actions align="right" class="text-primary">
          <q-btn flat label="取消" v-close-popup />
          <q-btn flat label="确定" v-close-popup @click="addItem" />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>

  <div class="q-pa-md" style="max-width: 800px">
    <q-table
      title="道具"
      :rows="items"
      :columns="columns"
      row-key="name"
      hide-bottom
    >
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
import { QTable, QTd, QBtn, Dialog } from "quasar";
import { api } from "src/boot/axios";
import { CommonFail, CommonSuccess, DialogAlert } from "src/components/common";
import { ref } from "vue";

let items: any = ref([]);
const prompt = ref(false);
const name = ref("");
const cost = ref("");
const description = ref("");

const columns: any = [
  { name: "id", align: "left", label: "ID", field: "id", sortable: true },
  {
    name: "name",
    label: "道具名称",
    align: "center",
    field: "name",
    sortable: true,
  },
  {
    name: "description",
    label: "描述",
    field: "description",
    align: "center",
    sortable: true,
  },
  { name: "cost", label: "花费", field: "cost", sortable: true },
  {
    name: "createtime",
    label: "创建时间",
    field: "createtime",
    sortable: true,
  },
  { name: "handle", label: "操作", field: "handle", sortable: false },
];

function loadPage() {
  api.get("/item/list").then((res) => {
    items.value = res.data.data;
  });
}
loadPage();
// 删除道具
function handleUse(props: any) {
  api.delete("/item/" + props.row.id).then((res: any) => {
    CommonSuccess("删除成功");
    loadPage();
  });
}

function addItem() {
  api
    .post("/item", {
      name: name.value,
      description: description.value,
      cost: cost.value,
    })
    .then((res: any) => {
      if (res.data.code === 200) {
        CommonSuccess("添加成功");
        loadPage();
        name.value = "";
        description.value = "";
        cost.value = "";
      } else {
        CommonFail(res.data.msg);
      }
    });
}
</script>
