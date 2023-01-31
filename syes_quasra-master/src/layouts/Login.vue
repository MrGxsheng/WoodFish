<template>
  <div id="building">
    <!-- 顶部提示 -->

    <q-card class="login-card">
      <q-card-section align="center">
        <span class="text-h5 text-weight-bold">登录</span>
      </q-card-section>
      <q-card-section>
        <q-input square v-model="userid" outlined placeholder="用户ID" dense/>
      </q-card-section>
      <q-card-section>
        <q-input square v-model="password" outlined type="password" dense placeholder="密码"/>
      </q-card-section>
      <q-card-section style="margin-top: 15px">
        <q-btn-group>
          <q-btn rounded color="secondary" label="注册"/>
          <q-btn rounded color="primary" label="登录" style="width: 300px" @click="handleLogin"/>
        </q-btn-group>
      </q-card-section>
    </q-card>

  </div>

</template>

<script setup lang="ts">
import {ref} from "vue";
import {api} from "boot/axios";
import {CommFail, CommSeccess} from "components/common";

let userid = ref('')
let password = ref('')

function handleLogin() {
  localStorage.clear()
  api.post("/rootuser/login", {
    "id": userid.value,
    "password": password.value
  }).then(res => {
    if (res.code === "200") {
      localStorage.setItem("token", res.data.token)
      localStorage.setItem("username", res.data.username)
      localStorage.setItem("useravatar", res.data.avatar)
      CommSeccess('登录成功')
      window.location.href = '/'
    } else {
      CommFail(res.msg)
    }
  })
}
</script>

<style scoped>
.login-card {
  position: absolute;
  left: 50%;
  top: 50%;
  width: 440px;
  height: 360px;
  margin: -180px auto auto -220px;
  padding: 24px;
}

#building {
  background: url('../assets/bg_from_MS.svg');
  width: 100%;
  height: 100%;
  position: fixed;
  background-size: cover;
}

</style>
