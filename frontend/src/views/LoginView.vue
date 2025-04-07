<template>
  <Default>
    <div class="container-md">
      <div class="row">
        <div class="col-md-3"></div>
        <form @submit.prevent="submit" class="col-md-6 pt-5 p-3 shadow">
          <h1>Přihlášení</h1>
          <div class="form-floating">
            <input type="email" id="email" class="form-control" placeholder="email" v-model="email">
            <label for="email">Email</label>
          </div>
          <div class="form-floating mt-3">
            <input type="password" id="password" class="form-control" placeholder="heslo" v-model="password">
            <label for="password">Heslo</label>
          </div>
          <input class="btn btn-primary mt-3" type="submit" value="PŘIHLÁSIT">
        </form>
        <div class="col-md-3"></div>
      </div>
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
