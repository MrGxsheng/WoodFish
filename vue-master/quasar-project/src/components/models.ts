import { ref } from "vue";

export interface Todo {
  id: number;
  content: string;
}

export interface Meta {
  totalCount: number;
}

export let menus = [
  { link: "home", icon: "home", desc: "主页", id: 1 },
  { link: "item", icon: "category", desc: "道具", id: 2 },
  { link: "top", icon: "leaderboard", desc: "排行榜", id: 3 },
  { link: "costlist", icon: "leaderboard", desc: "花费列表", id: 4 },
  { link: "about", icon: "info", desc: "关于", id: 5 },
];
