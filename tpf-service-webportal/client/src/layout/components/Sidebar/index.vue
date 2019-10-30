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
import Logo from "./Logo";
import SidebarItem from "./SidebarItem";
import variables from "@/styles/variables.scss";
import router from '@/router'

export default {
  components: { SidebarItem, Logo },
  data() {
    return {
      permission_routes: []
    };
  },
  created() {
    this.permission_routes = this.generateRoutes()
  },
  computed: {
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
      return this.state.settings.sidebarLogo;
    },
    variables() {
      return variables;
    },
    isCollapse() {
      return !this.state.app.sidebar.opened;
    }
  },
  methods: {
    hasPermission(roles, route) {
      if (route.meta && route.meta.roles) {
        return roles.some(role => route.meta.roles.includes(role));
      } else {
        return true;
      }
    },

    filterAsyncRoutes(routes, roles) {
      const res = [];

      routes.forEach(route => {
        const tmp = { ...route };
        if (this.hasPermission(roles, tmp)) {
          if (tmp.children) {
            tmp.children = this.filterAsyncRoutes(tmp.children, roles);
          }
          res.push(tmp);
        }
      });

      return res;
    },

    generateRoutes() {
      let roles = this.fnCookie().getRoles()
      let accessedRoutes = []
      let tmpRoutes
      if (roles.includes("admin")) {
        tmpRoutes = router.options.routes
      } else {
        tmpRoutes = this.filterAsyncRoutes(router.options.routes, roles);
      }
      
      for (let i in tmpRoutes) {
        if (tmpRoutes[i].children && tmpRoutes[i].children.length != 0) {
          accessedRoutes.push(tmpRoutes[i])
        }
      }
      
      return accessedRoutes
    }
  }
};
</script>
