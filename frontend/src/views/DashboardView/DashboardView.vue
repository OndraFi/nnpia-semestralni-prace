<template>
  <Default>
    <div class="container-md text-center">
    <h1>Vaše restaurace</h1>
      <button  class="btn btn-primary w-100"
               type="button"
               data-bs-toggle="collapse"
               data-bs-target="#createRestaurantCollapse"
               aria-expanded="false"
               aria-controls="createRestaurantCollapse">Přidat restauraci</button>
      <div class="collapse" id="createRestaurantCollapse">
        <form @submit.prevent="submitCreateForm">
          <div class="form-floating mt-3">
            <input type="text"
                   minlength="4"
                   id="restaurantName"
                   class="form-control"
                   required
                   placeholder="název restaurace"
                   v-model="restaurantName">
            <label for="restaurantName">Název restaurace</label>
          </div>
          <div class="form-floating mt-3">
            <input type="number"
                   id="capacity"
                   class="form-control"
                   required
                   min="0"
                   placeholder="Kapacita restaurace"
                   v-model="capacity">
            <label for="capacity">Kapacita restaurace</label>
          </div>
          <div class="form-floating mt-3">
            <input type="time"
                   required
                   id="openTime"
                   class="form-control"
                   placeholder="otevírací doba"
                   v-model="openTime">
            <label for="openTime">Otevýrací doba</label>
          </div>
          <div class="form-floating mt-3">
            <input type="time"
                   id="closeTime"
                   required
                   placeholder="zavírací doba"
                   class="form-control"
                   v-model="closeTime">
            <label for="closeTime">Čas zavření</label>
          </div>
          <input v-if="!isLoading" class="btn btn-primary w-100 mt-3" type="submit" value="Přidat">
<div v-else class="text-center mt-3"><div class="spinner-border text-primary" role="status"><span class="visually-hidden">Loading...</span></div></div>
        </form>
      </div>
    <restaurant-card :restaurants="restaurants" :dashboard="true"/>
      <div v-if="restaurants && restaurants.length == 0" class="text-center p-3 fw-bold shadow bg-light mt-3">
        <img src="../../assets/img/warning.svg"
             alt="no restaurant found"
             style="height: 50px">
        Nemáte žádné restaurace!
      </div>
    </div>
  </Default>
</template>
<script lang="ts">
import Default from "@/layouts/Default.vue";
import {createRestaurant, findRestaurantsByUser, type RestaurantRequest, type Restaurant} from "@/api/restaurant.ts";
import RestaurantCard from "@/components/RestaurantCard.vue";

export default {
  components: {RestaurantCard, Default},
  data() {
    const restaurants : Restaurant[] = [];
    return {
      isLoading: false,
      restaurantName: '',
      capacity: 0,
      openTime: '',
      closeTime: '',
      restaurants
    }
  },
  methods: {
    async submitCreateForm() {
      this.isLoading = true;
      try {
      if (this.restaurantName.length > 3 && this.capacity > 0){
        const restaurant : RestaurantRequest = {
          name: this.restaurantName,
          usesDesks: false,
          capacity: this.capacity,
          openTime: this.openTime,
          closeTime: this.closeTime
        }
        createRestaurant(restaurant).then(response => {
          console.log(response);
          this.restaurantName = "";
          this.capacity = 0;
          this.openTime = '';
          this.closeTime = '';
          this.$notify({
            type: 'success',
            text: 'Restaurace byla vytvořena.'
          })
          this.findUserRestaurants();
        }).catch(e => {
          console.log(e);
          this.$notify({
            type: 'error',
            text: 'Restauraci se nepovedlo vytvořit.'
          })
        })
      } else
        console.log("Název restaurace musí mít alespoň 3 znaky.");
      } finally {
        this.isLoading = false;
      }
    },
    findUserRestaurants() {
      findRestaurantsByUser().then(response => {
        console.log(response);
        this.restaurants = response;
      })
    }
  },
  mounted() {
    this.findUserRestaurants();
  }
}
</script>
<style scoped>

</style>
