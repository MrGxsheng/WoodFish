<template>
  <div class="q-pa-md" style="max-width: 350px">
    <q-list bordered class="rounded-borders">
      <!--      <q-expansion-item-->
      <!--        v-for="item in menus"-->
      <!--        v-model="item.isopen"-->
      <!--        expand-separator-->
      <!--        :icon="item.icon"-->
      <!--        :label="item.label"-->
      <!--        @click="handleOpen()"-->
      <!--      >-->
      <!--        <q-card v-for="child in item.children">-->
      <!--          <div class="q-pa-md " style="max-width: 500px">-->
      <!--            <q-list bordered padding class="rounded-borders text-primary ">-->
      <!--              <q-item-->
      <!--                clickable-->
      <!--                v-ripple-->
      <!--                :to="child.link"-->
      <!--                :active="link === child.link"-->
      <!--                active-class="my-menu-link"-->
      <!--              >-->
      <!--                <q-item-section avatar>-->
      <!--                  <q-icon :name="child.icon"/>-->
      <!--                </q-item-section>-->
      <!--                <q-item-section>{{ child.desc }}</q-item-section>-->
      <!--              </q-item>-->
      <!--            </q-list>-->
      <!--          </div>-->
      <!--        </q-card>-->
      <!--      </q-expansion-item>-->

      <!--   解决问题的最好办法就是逃避他   -->
      <template v-for="num in menus.length">
        <q-expansion-item
            v-model="menus[num-1].isopen"
            :icon="menus[num-1].icon"
            :label="menus[num-1].label"
            @click="handleOpen()"
            expand-separator
        >
          <q-card v-for="child in menus[num-1].children">
            <div class="q-pa-md " style="max-width: 500px">
              <q-list bordered padding class="rounded-borders text-primary" style="padding: 4px 0">
                <q-item
                    clickable
                    v-ripple
                    :to="child.link"
                    :class="link === child.link?'my-menu-link':'q-expansion-default'"
                >
                  <q-item-section avatar>
                    <q-icon :name="child.icon"/>
                  </q-item-section>
                  <q-item-section>{{ child.desc }}</q-item-section>
                </q-item>
              </q-list>
            </div>
          </q-card>
        </q-expansion-item>
        <template v-if="miniState&&menus[num-1].isopen">
          <q-expansion-item
              v-for="son in menus[num-1].children"
              v-model="menus[num-1].isopen"
              :icon="son.icon"
              :to="son.link"
              :label="son.desc"
              @click="handleOpen()"
              :class="link===son.link?'q-expansion-active':'q-expansion-default'"
              style="padding: 4px 0"
          >
          </q-expansion-item>
        </template>
      </template>

    </q-list>
  </div>
</template>

<script setup>
import {ref, watch} from 'vue'
import {useStore} from "src/store";
import {useRouter} from "vue-router/dist/vue-router";
import {menu} from "src/components/models";

// 父级的mini状态
const props = defineProps(['miniState'])

let menus = ref(menu)
//监视路由变化，将变化应用到左侧栏
let link = ref()
const $store = useStore()
let $router = useRouter()
watch(() => $router.currentRoute.value.path, (newValue, oldValue) => {
  link.value = $router.currentRoute.value.path.replace("/", '')
}, {immediate: true})


//储存菜单展开或关闭数据
if (!localStorage.getItem('menus')) {
  localStorage.setItem('menus', JSON.stringify(menus))
} else {
  menus.value = JSON.parse(localStorage.getItem('menus'))._value
}


//点击时修改展开数据
function handleOpen() {
  localStorage.setItem('menus', JSON.stringify(menus))
}


</script>

<style scoped>
.q-router-link--active {
  color: rgb(25, 118, 210);
}

.q-pa-md {
  padding: 0;

}

.q-list--bordered {
  border: 0px solid rgba(0, 0, 0, 0.12) !important;
}

.q-item__section {

}

.my-menu-link {
  /*color: white;*/
  color: rgb(25, 118, 210);
  background: #F2C037;
}

.q-expansion-default {
  color: rgb(25, 118, 210)
}


.q-expansion-active {
  padding: 0 !important;
  margin: 4px 0;
  color: white !important;
  background-color: #F2C037;
}


</style>
