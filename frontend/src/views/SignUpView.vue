<template>
  <Default>

    <img src="../assets/img/deviders/devider-2.svg" class="mt-5" alt="devider">
    <div class="bg-light">
      <div class="container-md">
        <div class="row">
          <form @submit.prevent="submit" class="col-md-6 p-3 pt-5">
            <h1 class="text-center mb-5">Registrace</h1>
            <div class="form-floating">
              <input type="email" id="email" class="form-control" placeholder="email" v-model="email">
              <label for="email">Email</label>
            </div>
            <div class="form-floating mt-3">
              <input type="password" id="password" class="form-control" placeholder="heslo" v-model="password">
              <label for="password">Heslo</label>
            </div>
            <input class="mt-3 btn btn-primary w-100" type="submit">
          </form>
          <div class="col-md-6 d-none d-md-block">
            <img src="../assets/img/sign-up.svg"
                 alt="Ilustrace registrace"
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

export default {
  components: {Default},
  data() {
    return {
      email: '',
      password: '',
      authStore: useAuthStore()
    }
  },
  methods: {
    async submit() {
      this.authStore.signUpUser(this.email, this.password).then(response => {
        console.log(response);
        this.$notify({
          type: 'success',
          text: 'Váš účet byl vytvořen.'
        })
        this.$router.push({name: 'login'});
      }).catch(e => {
        this.$notify({
          type: 'error',
          text: 'Účet se nepovedlo vytvořit.'
        })
        console.log(e);
      })
    }
  }
}
</script>
<style scoped>

</style>
