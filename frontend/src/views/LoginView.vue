<template>
  <Default>
    <div>
      LOGIN
      <form @submit.prevent="submit">
        <input type="email" placeholder="email" v-model="email">
        <input type="password" placeholder="heslo" v-model="password">
        <input type="submit">
      </form>
    </div>
  </Default>
</template>
<script lang="ts">
import Default from "@/layouts/Default.vue";
import {useAuthStore} from "@/stores/authStore.ts";
import {useRouter} from "vue-router";

export default {
  components: {Default},
  data(){
    return {
      email: '',
      password: '',
      authStore: useAuthStore(),
    }
  },
  methods:{
    async submit(){
      this.authStore.loginUser(this.email, this.password).then(response=>{
        console.log(response);
        this.$router.push({name: 'dashboard'});
      }).catch(e=>{
        console.log(e);
      })
    }
  }
}
</script>
<style scoped>

</style>
