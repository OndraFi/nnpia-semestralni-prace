<template>
  <footer class="py-3 my-4">
    <ul class="nav justify-content-center border-bottom pb-3 mb-3">
      <li class="nav-item">
        <router-link class="nav-link px-2 text-body-secondary" to="/">Domů</router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link px-2 text-body-secondary" to="/restaurants">Restaurace</router-link>
      </li>
      <li class="nav-item" v-if="!authStore.isLoggedIn">
        <router-link class="nav-link px-2 text-body-secondary" to="/login">Login</router-link>
      </li>
      <li class="nav-item" v-if="!authStore.isLoggedIn">
        <router-link class="nav-link px-2 text-body-secondary" to="/sign-up">Sign up</router-link>
      </li>
    </ul>
    <p class="text-center text-body-secondary">© {{ new Date().getFullYear() }} Rezervovač</p>
  </footer>
</template>
<script lang="ts">
import {useAuthStore} from "@/stores/authStore.ts";

export default {
  data() {
    return {
      authStore: useAuthStore()
    }
  },
  methods: {
    route(url: string): boolean {
      return this.$route.path === url;
    }
  }
}
</script>
