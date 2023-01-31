import {RouteRecordRaw} from 'vue-router';

const routes: RouteRecordRaw[] = [
    {
        path: '/',
        component: () => import('layouts/MainLayout.vue'),//侧边栏
        children: [
            {path: '', redirect: 'Dashboard'},  //组件
            {path: 'Dashboard', component: () => import('src/monitors/Dashboard.vue')},
            {path: 'SysInfo', component: () => import('src/monitors/SysInfo.vue')},
            {path: 'SqlInfo', component: () => import('src/monitors/DataInfo.vue')},
            {path: 'LogInfo', component: () => import('src/monitors/LogInfo.vue')},
            {path: 'UserManager', component: () => import('pages/UserManager.vue')},
            {path: 'ItemManager', component: () => import('pages/ItemManager.vue')},
            {path: 'CommentManager', component: () => import('pages/CommentManager.vue')},
            {path: 'PicManager', component: () => import('pages/PicManager.vue')},
            {path: 'OrderManager', component: () => import('pages/OrderManager.vue')},
            {path: 'SortManager', component: () => import('pages/SortManager.vue')},
            {path: 'RollManager', component: () => import('pages/RollManager.vue')},
            {path: 'UserSetting', component: () => import('src/settings/UserSetting.vue')},
            {path: 'HomeSetting', component: () => import('src/settings/HomeSetting.vue')},
            {path: 'About', component: () => import('src/settings/About.vue')},
        ],
    },
    {
        path: '/login',
        component: () => import('../layouts/Login.vue')
    },
    // Always leave this as last one,
    // but you can also remove it
    //404页面
    {
        path: '/:catchAll(.*)*',
        component: () => import('layouts/ErrorNotFound.vue'),
    },
];

export default routes;
