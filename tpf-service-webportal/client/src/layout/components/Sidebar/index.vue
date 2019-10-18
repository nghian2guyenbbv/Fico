<template>
  <div :class="{'has-logo':showLogo}">
    <logo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="variables.menuBg"
        :text-color="variables.menuText"
        :unique-opened="false"
        :active-text-color="variables.menuActiveText"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item
          v-for="route in permission_routes"
          :key="route.path"
          :item="route"
          :base-path="route.path"
        />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import Logo from "./Logo";
import SidebarItem from "./SidebarItem";
import variables from "@/styles/variables.scss";
import * as cookie from '@/utils/cookie'
import router from '@/router'

export default {
  components: { SidebarItem, Logo },
  data() {
    return {
      permission_routes: []
    };
  },
  created() {
    console.log(this.fnCookie())
    this.permission_routes = this.generateRoutes()
  },
  computed: {
    ...mapGetters(["permission_routes", "sidebar"]),
    activeMenu() {
      const route = this.$route;
      const { meta, path } = route;
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu;
      }
      return path;
    },
    showLogo() {
      return this.$store.state.settings.sidebarLogo;
    },
    variables() {
      return variables;
    },
    isCollapse() {
      return !this.sidebar.opened;
    }
  },
  methods: {
    hasPermission(url) {
      if (route.meta && route.meta.roles) {
        return roles.some(role => route.meta.roles.includes(role));
      } else {
        return true;
      }
    },

    filterAsyncRoutes(url) {
      const res = [];

      routes.forEach(route => {
        const tmp = { ...route };
        if (hasPermission(roles, tmp)) {
          if (tmp.children) {
            tmp.children = filterAsyncRoutes(tmp.children, roles);
          }
          res.push(tmp);
        }
      });

      return res;
    },

    generateRoutes() {
      let roles = cookie.getRoles()
      console.log(roles)
      console.log(router.options.routes)
      return router.options.routes
      // if (roles.includes("admin")) {
      //   accessedRoutes = asyncRoutes || [];
      // } else {
      //   accessedRoutes = filterAsyncRoutes(asyncRoutes, roles);
      // }
      // commit("SET_ROUTES", accessedRoutes);
    }
  }
};
</script>
