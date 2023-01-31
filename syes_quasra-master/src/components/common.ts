import {Notify} from 'quasar'

//一般类型操作成功
export function CommSeccess(message: any) {
    Notify.create({
        icon: 'done',
        color: 'positive',
        message: message,
        position: 'top',
        group: false,
    })
}

export function CommFail(message: any) {
    Notify.create({
        icon: 'error',
        color: 'negative',
        message: message,
        position: 'top',
        group: false,
    })
}

export function CommWarn(message: any) {
    Notify.create({
        icon: 'error',
        type: 'warning',
        message: message,
        position: 'top',
        group: false,
    })
}

export function diffDate(dateTime: number) {
    let dateNow = new Date();  // 获取当前时间
    let timeDiff = dateNow.getTime() - dateTime;  // 时间差的毫秒数
    // timeDiff = 时间戳差值
    let days = Math.floor(timeDiff / (24 * 3600 * 1000)); // 计算出天数
    let leavel1 = timeDiff % (24 * 3600 * 1000); // 计算天数后剩余的时间
    let hours = Math.floor(leavel1 / (3600 * 1000)); // 计算天数后剩余的小时数
    let leavel2 = timeDiff % (3600 * 1000); // 计算剩余小时后剩余的毫秒数
    let minutes = Math.floor(leavel2 / (60 * 1000)); // 计算剩余的分钟数
    let leavel3 = timeDiff % (60 * 1000); // 计算剩余分钟后剩余的毫秒数
    let seconds = Math.floor(leavel3 / 1000); // 计算剩余的秒数
    return days + "天" + hours + "小时" + minutes + "分" + seconds + "秒";
}