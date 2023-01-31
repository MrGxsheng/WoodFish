<template>
  <q-card class="my-card2 q-ma-md">
    <q-card-section :class="'bg-'+props.color">
      <div class="text-h6 text-white">{{ props.title }}</div>
    </q-card-section>

    <q-separator/>

    <q-card-actions class="q-pa-md  column" vertical>
      <div class="col">
        <div class="row" v-for="(item,index) in props.info.list.value">
          <div class="col">
            <div v-if="index<2">
              <q-circular-progress
                  show-value
                  font-size="10px"
                  class="q-ma-md"
                  :value="parseFloat(item.v*100)"
                  size="60px"
                  :thickness="0.1"
                  :color="props.color"
                  track-color="grey-3"
              >
                <q-avatar size="60px">
                  <q-icon name="memory" :color="props.color"></q-icon>
                </q-avatar>
              </q-circular-progress>
              <span>{{ item.k + "  " + item.v * 100 + '%' }} </span></div>
          </div>
        </div>
      </div>
      <div class="col " v-for="(item,index) in props.info.list.value">
        <div v-if="index>=2" class="q-ma-md">
          <span class="float-left">{{ item.k }}</span>
          <span :class="'float-right   '+'text'+props.color">{{ item.v * 100 }}%</span>
          <q-linear-progress stripe :color="props.color" size="10px" :value="parseFloat(item.v)"/>
        </div>
      </div>
    </q-card-actions>
  </q-card>
</template>

<script lang="ts" setup>
const props = defineProps(['info', 'color', 'title'])
</script>

<style scoped>
.my-card2 {
  width: 100%;
  max-width: 38vw;
  max-height: 450px;
}
</style>

