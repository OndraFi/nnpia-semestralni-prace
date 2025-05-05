<template>
  <nav class="navbar navbar-expand-lg navbar-light">
    <div class="container-md">
      <router-link class="navbar-brand" to="/">Rezervovač</router-link>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
              data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false"
              aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav ms-auto">
          <li class="nav-item">
            <router-link class="nav-link" :class="{'active' : route('/')}" aria-current="page"
                         to="/">Domů
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" :class="{'active' : route('/restaurants')}" aria-current="page"
                         to="/restaurants">Restaurace
            </router-link>
          </li>
          <li class="nav-item" v-if="!authStore.isLoggedIn">
            <router-link class="nav-link" :class="{'active' : route('/login')}" aria-current="page"
                         to="/login">Přihlášení
            </router-link>
          </li>
          <li class="nav-item" v-if="!authStore.isLoggedIn">
            <router-link class="nav-link" :class="{'active' : route('/sign-up')}"
                         aria-current="page" to="/sign-up">Registrace
            </router-link>
          </li>
          <li class="nav-item" v-if="authStore.isLoggedIn">
            <router-link class="nav-link" :class="{'active' : route('/dashboard')}"
                         aria-current="page" to="/dashboard">Moje restaurace
            </router-link>
          </li>
          <li class="nav-item" v-if="authStore.isLoggedIn">
            <router-link class="nav-link" to="/" @click="authStore.logout()">Odhlásit</router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>

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
