import {Ref, ref, UnwrapRef} from "vue";

export interface Todo {
    id: number;
    content: string;
}

export interface Meta {
    totalCount: number;
}

//卡片组件的信息
export class InfoKV {
    //这是啥类型我不知道，但是能跑
    list: Ref<UnwrapRef<any[]>> = ref([])

    addList(k: string, v: string | number) {
        // @ts-ignore
        this.list.value.push({k: k, v: v})
    }

    //这是带进度条的
    addProgress(k: string, v: string | number, b: boolean) {
        // @ts-ignore
        this.list.value.push({k: k, v: v, b: b})
    }

    setList(k: string, v: string | number) {
        this.list.value.forEach(item => {
            item.k == k ? item.v = v : ''
        })
    }
}

//菜单数据
export let menu = ref([
    {
        icon: 'home', label: '主页', id: 1, isopen: true, children: [
            {link: 'Dashboard', icon: 'donut_large', desc: '仪表盘', id: 4,},
            {link: 'SysInfo', icon: 'hub', desc: '系统监控', id: 15},
            {link: 'SqlInfo', icon: 'vertical_split', desc: '数据监控', id: 17},
            {link: 'LogInfo', icon: 'view_timeline', desc: '日志监控', id: 16},
        ]
    },
    {
        icon: 'perm_identity', label: '管理', isopen: true, id: 2, children: [
            {link: 'UserManager', icon: 'person', desc: '用户管理', id: 6},
            {link: 'ItemManager', icon: 'shopping_bag', desc: '物品管理', id: 7},
            {link: 'SortManager', icon: 'category', desc: '分类管理', id: 13},
            {link: 'CommentManager', icon: 'chat', desc: '评论管理', id: 10},
            {link: 'PicManager', icon: 'image', desc: '图片管理', id: 11},
            {link: 'OrderManager', icon: 'list_alt', desc: '订单管理', id: 12},
            {link: 'RollManager', icon: 'motion_photos_on', desc: '抽卡管理', id: 18},
        ]
    },
    {
        icon: 'settings', label: '设置', id: 3, isopen: true, children: [
            {link: 'HomeSetting', icon: 'phonelink_setup', desc: '首页设置', id: 14},

            {link: 'About', icon: 'info', desc: '关于', id: 9},
        ]
    }
])

export class Order {
    userid = ref()
    useridRef = ref()
    itemid = ref()
    itemidRef = ref()

    SetItem(value: Order) {
        this.userid = value.userid
        this.useridRef = value.useridRef
        this.itemid = value.itemid
        this.itemidRef = value.itemid
    }

    clearall() {
        this.itemid = ref('')
        this.itemidRef = ref('')
        this.userid = ref('')
        this.useridRef = ref('')
    }
}

export class Sort {
    id = ref()
    name = ref()
    icon = ref()
    pid = ref()

    SetItem(value: Sort) {
        this.id = value.id
        this.name = value.name
        this.icon = value.icon
        this.pid = value.pid
    }

    clearall() {
        this.id = ref('')
        this.name = ref('')
        this.icon = ref('')
        this.pid = ref('')
    }
}

export class RollInfo {
    id = ref()
    name = ref()
    ranks = ref()
    sort = ref()
    level = ref()
    enable = ref()
    p = ref()
    idRef = ref()
    nameRef = ref()
    ranksRef = ref()
    sortRef = ref()
    levelRef = ref()
    enableRef = ref()
    pRef = ref()

    clearall() {
        this.id.value = ''
        this.ranks.value = ''
        this.sort.value = ''
        this.level.value = ''
        this.name.value = ''
        this.enable.value = ''
        this.p.value = ''
    }
}

export class Iteminfo {
    id = ref()
    idRef = ref()
    title = ref()
    titleRef = ref()
    description = ref()
    descriptionRef = ref()
    price = ref()
    priceRef = ref()
    userid = ref()
    useridRef = ref()
    sort = ref()
    accept = ref(false)

    SetItem(value: Iteminfo) {
        Object.assign(this, value)
        this.id = value.id
        this.idRef = value.idRef
        this.title = value.title
        this.titleRef = value.titleRef
        this.description = value.description
        this.descriptionRef = value.descriptionRef
        this.price = value.price
        this.priceRef = value.priceRef
        this.userid = value.userid
        this.useridRef = value.useridRef
        this.sort = value.sort
    }

    clearall() {
        this.id.value = ''
        this.idRef.value = ''
        this.title.value = ''
        this.titleRef.value = ''
        this.description.value = ''
        this.descriptionRef.value = ''
        this.price.value = ''
        this.priceRef.value = ''
        this.userid.value = ''
        this.useridRef.value = ''
        this.accept.value = false
        this.sort.value = ''
    }
}

export class Commentinfo {
    id = ref('')
    idRef = ref('')
    itemid = ref('')
    itemRef = ref('')
    userid = ref('')
    useridRef = ref('')
    content = ref('')
    contentRef = ref('')
    tocommentid = ref('')
    fromcommentid = ref('')
    tocommentidRef = ref('')
    fromcommentidRef = ref('')

    Setitem(value: Commentinfo) {
        Object.assign(value)
    }

    clearall() {
        this.id.value = ''
        this.idRef.value = ''
        this.itemid.value = ''
        this.itemRef.value = ''
        this.userid.value = ''
        this.useridRef.value = ''
        this.content.value = ''
        this.contentRef.value = ''
        this.tocommentid.value = ''
        this.fromcommentid.value = ''
        this.tocommentidRef.value = ''
        this.fromcommentidRef.value = ''
    }
}

export class Userinfo {
    name = ref()
    nameRef = ref()
    age = ref()
    ageRef = ref()
    id = ref()
    idRef = ref()
    infoid = ref()
    infoidRef = ref()
    nickname = ref()
    nicknameRef = ref()
    phone = ref()
    phoneRef = ref()
    password = ref()
    passwordRef = ref()
    isPwd = ref()
    repassword = ref()
    repasswordRef = ref()
    accept = ref(false)

    //ts的构造器只能有一个
    SetItem(values: any) {
        this.name = values.name
        this.nameRef = values.nameRef
        // this.age = values.age
        // this.ageRef = values.ageRef
        this.id = values.id
        this.idRef = values.idRef
        this.nickname = values.nickname
        this.nicknameRef = values.nicknameRef
        this.phone = values.phone
        this.phoneRef = values.phoneRef
        this.password = values.password
        this.passwordRef = values.passwordRef
        this.isPwd = values.isPwd
        this.repassword = values.repassword
        this.repasswordRef = values.repasswordRef
    }

    clearall() {
        this.name.value = ''
        this.age.value = ''
        this.id.value = ''
        this.idRef.value = ''
        this.nicknameRef.value = ''
        this.nickname.value = ''
        this.passwordRef.value = ''
        this.password.value = ''
        this.repassword.value = ''
        this.repasswordRef.value = ''
        this.phoneRef.value = ''
        this.phone.value = ''
        this.nameRef.value = ''
        this.ageRef.value = ''
        this.accept.value = false
    }
}

