<script setup lang="ts">
import {RouterLink, RouterView} from 'vue-router'
import {useAuthStore} from "@/stores/authStore.ts";
import AOS from 'aos'
import 'aos/dist/aos.css'
import {Notifications} from "@kyvg/vue3-notification";

console.log(import.meta.env.VITE_API_URL);
const authStore = useAuthStore();
console.log(authStore.isLoggedIn);
AOS.init();
</script>

<template>
  <!--  <header>-->
  <!--    <div class="wrapper">-->
  <!--      <nav>-->
  <!--        <RouterLink to="/">Home</RouterLink>-->
  <!--        <RouterLink to="/about">About</RouterLink>-->
  <!--      </nav>-->
  <!--    </div>-->
  <!--  </header>-->
  <RouterView/>
  <notifications position="bottom right"/>
</template>

<style scoped>
</style>
