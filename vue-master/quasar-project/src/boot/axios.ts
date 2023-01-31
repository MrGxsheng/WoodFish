import { boot } from "quasar/wrappers";
import axios, { AxiosInstance } from "axios";
import { CommonFail } from "src/components/common";

declare module "@vue/runtime-core" {
  interface ComponentCustomProperties {
    $axios: AxiosInstance;
  }
}

// Be careful when using SSR for cross-request state pollution
// due to creating a Singleton instance here;
// If any client changes this (global) instance, it might be a
// good idea to move this instance creation inside of the
// "export default () => {}" function below (which runs individually
// for each client)
const api = axios.create({ baseURL: "/api" });

export default boot(({ app }) => {
  // for use inside Vue files (Options API) through this.$axios and this.$api

  app.config.globalProperties.$axios = axios;
  // ^ ^ ^ this will allow you to use this.$axios (for Vue Options API form)
  //       so you won't necessarily have to import axios in each vue file

  app.config.globalProperties.$api = api;
  // ^ ^ ^ this will allow you to use this.$api (for Vue Options API form)
  //       so you can easily perform requests against your app's API
  //请求拦截器
  api.interceptors.request.use((config) => {
    // if (localStorage.getItem('token') == null) {
    //   window.location.href = "/login"
    // }
    if (localStorage.getItem("username") == null) {
      window.location.href = "/#/login";
    }
    return config;
  });
  //响应拦截器
  api.interceptors.response.use(
    (res) => {
      if (res.data.code == "200") {
        return res.data;
        //如果再多一层data可能会导致某些res为空
      } else {
        CommonFail("错误:" + res.data.code + "  信息：" + res.data.msg);
      }
    },
    (error) => {
      //错误处理，错误不会有res.data.code，不走上面
      console.log(error.response.data.message);
      CommonFail("请求失败" + "  错误码:" + error.response.status);
    }
  );
});

export { api };
