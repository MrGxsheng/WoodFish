<template>
  <!-- 顶部提示 -->
  <div class="q-pa-md q-gutter-sm">
    <q-banner class="bg-primary text-white">
      这个是关于页面
      <template v-slot:action>
        <q-btn flat color="white" label="Dismiss"/>
        <q-btn flat color="white" label="Update Credit Card"/>
      </template>
    </q-banner>
  </div>
</template>

<script>
export default {
  name: "About"
}
</script>

<style scoped>

</style>
