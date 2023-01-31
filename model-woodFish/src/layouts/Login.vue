<template>
  <div id="building">
    <!-- 顶部提示 -->
    <q-card class="login-card">
      <q-card-section align="center">
        <span class="text-h5 text-weight-bold">登录</span>
      </q-card-section>
      <q-card-section>
        <q-input square v-model="username" outlined placeholder="用户名" dense />
      </q-card-section>
      <q-card-section>
        <q-input square v-model="password" outlined type="password" dense placeholder="密码" />
      </q-card-section>
      <q-card-section style="margin-top: 15px">
        <q-btn-group>
          <q-btn rounded color="secondary" label="注册" @click="handleRegister" />
          <q-btn rounded color="primary" label="登录" style="width: 300px" @click="handleLogin" />
        </q-btn-group>
      </q-card-section>
    </q-card>
  </div>

</template>

<script setup lang="ts">
import { ref } from "vue";
import { CommonSuccess, CommonFail } from 'src/components/common'
import axios from "axios";
import { useRouter } from "vue-router";
let rt = useRouter()
let username = ref('')
let password = ref('')

//登录
function handleLogin() {
  localStorage.clear()
  axios.post("/api/user/login", {
    "username": username.value,
    "password": password.value
  }).then((res: any) => {
    if (res.data.code === 200) {
      localStorage.setItem("score", res.data.data.score)
      localStorage.setItem("username", res.data.data.username)
      // localStorage.setItem("token", res.data.token)
      // localStorage.setItem("useravatar", res.data.avatar)
      CommonSuccess('登录成功')
      rt.push('/')
    } else {
      CommonFail('错误:' + res.data.code + '  信息：' + res.data.msg)
    }
  })
}
//注册
function handleRegister() {
  axios.post("/api/user/reg", {
    "username": username.value,
    "password": password.value
  }).then((res: any) => {
    if (res.data.code === 200) {
      CommonSuccess('注册成功')
      handleLogin()
    } else {
      CommonFail('错误:' + res.data.code + '  信息：' + res.data.msg)
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
