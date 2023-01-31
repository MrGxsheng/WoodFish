<template>
  <div>
    <q-list dark padding class="rounded-borders text-primary">
      <q-item clickable v-ripple :active="link === item.link" v-for="(item, index) in menus" :key="index"
        @click="link = item.link" :to="item.link" active-class="my-menu-link">
        <q-item-section avatar>
          <q-icon :name="item.icon" />
        </q-item-section>
        <q-item-section>{{ item.desc }}</q-item-section>
      </q-item>
    </q-list>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { menus } from 'components/models.ts'
const CurrentLink = defineProps(['link'])
const link = ref(CurrentLink.link)
</script>

<style lang="css">
.my-menu-link {
  color: white;
  background: rgb(156, 39, 176)
}
</style>
