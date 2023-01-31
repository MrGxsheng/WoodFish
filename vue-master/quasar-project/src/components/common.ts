import { Dialog, Notify } from "quasar";

export function getLocalItem(item: string) {
  return localStorage.getItem(item)
}
// 一般成功消息
export function CommonSuccess(msg: string) {
  Notify.create({
    type: 'positive',
    message: msg,
    position: 'top',
    group: false
  })
}
export function CommonWarn(msg: string) {
  Notify.create({
    type: 'warning',
    message: msg,
    position: 'top',
    group: false
  })
}
export function CommonFail(msg: string) {
  Notify.create({
    type: 'negative',
    message: msg,
    position: 'top',
    group: false
  })
}
export function CommonInfo(msg: string) {
  Notify.create({
    type: 'info',
    message: msg,
    position: 'top',
    group: false
  })
}
//加载
export function loading() {
  Notify.create({
    type: 'ongoing',
    message: '加载中',
    position: 'top'
  })
}
export function loadedSuucess(fun: Function) {
  fun({
    icon: 'done',
    color: 'positive',
    type: 'positive',
    message: "加载成功",
    timeout: 1000
  })
}
export function loadFail(fun: Function) {
  fun({
    icon: 'done',
    color: 'negative',
    type: 'negative',
    message: "加载失败",
    timeout: 1000
  })
}
//对话框
export function DialogAlert(message: string) {
  Dialog.create({
    title: '消息',
    message: message,
    persistent: true
  }).onOk(() => {
    CommonWarn('此处应该有音频')
  })
}
