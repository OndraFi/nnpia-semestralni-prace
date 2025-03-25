
<template>
<div>
  <Default>
    <h1>Sign up</h1>
    <form @submit.prevent="submit">
      <input type="email" placeholder="email" v-model="email">
      <input type="password" placeholder="heslo" v-model="password">
      <input type="submit">
    </form>
  </Default>
</div>
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
      authStore: useAuthStore()
    }
  },
  methods:{
    async submit(){
      this.authStore.signUpUser(this.email, this.password).then(response=>{
        console.log(response);
        this.$router.push({name: 'login'});
      }).catch(e=>{
        console.log(e);
      })
    }
  }
}
</script>
<style scoped>

</style>
