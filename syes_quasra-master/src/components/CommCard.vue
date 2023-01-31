<template>
  <q-card class="my-card q-ma-md">
    <q-card-section :class="'bg-'+props.color">
      <div class="text-h6 text-white">{{ props.title }}</div>
    </q-card-section>

    <q-separator/>

    <q-card-actions class="q-pa-md " vertical>
      <div class="q-pa-md" v-for="item in props.info.list.value">
        <span class="float-left">{{ item.k }}</span>
        <span v-if="!item.b" :class="'float-right '+ 'text-'+props.color"> {{ item.v }}</span>
        <span v-if="item.b" :class="'float-right '+ 'text-'+props.color">{{ item.v * 100 }}%</span>
        <q-linear-progress v-if="item.b" stripe :color="props.color" size="10px" :value="parseFloat(item.v)"/>
      </div>
    </q-card-actions>
  </q-card>
</template>

<script lang="ts" setup>
const props = defineProps(['info', 'color', 'title'])
</script>

<style scoped>
.my-card {
  width: 100%;
  max-width: 350px;
  max-height: 450px;
}
</style>

