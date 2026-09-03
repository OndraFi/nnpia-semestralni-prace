<template>
  <Default>

    <img src="../assets/img/deviders/devider-2.svg" class="mt-5" alt="devider">
    <div class="bg-light">
      <div class="container-md">
        <div class="row">
          <form @submit.prevent="submit" class="col-md-6 pt-5 p-3">
            <h1 class="mb-5 text-center">Přihlášení</h1>
            <div class="form-floating">
              <input type="email" id="email" class="form-control" placeholder="email" v-model="email">
              <label for="email">Email</label>
            </div>
            <div class="form-floating mt-3">
              <input type="password" id="password" class="form-control" placeholder="heslo" v-model="password">
              <label for="password">Heslo</label>
            </div>
            <input v-if="!isLoading" class="btn btn-primary mt-3 w-100" type="submit" value="PŘIHLÁSIT">
            <div v-else class="text-center mt-3"><div class="spinner-border text-primary" role="status"><span class="visually-hidden">Loading...</span></div></div>
          </form>
          <div class="col-md-6 d-none d-md-block">
            <img src="../assets/img/login.svg"
                 alt="Ilustrace přihlášení"
                 class="img-fluid">
          </div>
        </div>
      </div>
    </div>
    <img src="../assets/img/deviders/devider-6.svg" alt="devider" width="100%">
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
      isLoading: false,
      email: '',
      password: '',
      authStore: useAuthStore(),
    }
  },
  methods:{
    async submit(){
      this.isLoading = true;
      this.authStore.loginUser(this.email, this.password).then(response=>{
        console.log(response);
        this.$notify({
          type: 'success',
          text: 'Byl jste úspěšně přihlášen.'
        })
        this.$router.push({name: 'dashboard'});
      }).catch(e=>{
        this.$notify({
          type: 'error',
          text: 'Přihášení se nezdařilo.'
        })
        console.log(e);
      }).finally(() => {
        this.isLoading = false;
      })
    }
  }
}
</script>
<style scoped>

</style>
