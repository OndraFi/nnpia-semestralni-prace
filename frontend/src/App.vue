<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import {useAuthStore} from "@/stores/authStore.ts";
console.log(import.meta.env.VITE_API_URL);
const authStore = useAuthStore();
console.log(authStore.isLoggedIn);
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
<RouterView />
</template>

<style scoped>
</style>
