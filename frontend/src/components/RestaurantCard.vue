<template>
  <div v-for="restaurant in restaurants" class="p-4 my-4 shadow restaurant-card border-bottom border-primary border-4">
      <component class="row text-decoration-none text-black" :is="dashboard ? 'div' : 'router-link'" :to="`/restaurant/${restaurant.id}`">
        <div class="col-md-6">
          <router-link :to="`/dashboard/restaurant/${restaurant.id}`" v-if="dashboard">
            <span>id:{{ restaurant.id }}</span>
          </router-link>
          <br>
          <span>name:{{ restaurant.name }}</span>
          <br>
          <span>capacity:{{ restaurant.capacity }}</span>
        </div>
        <div class="col-md-6" v-if="restaurant.address">
          <span>Země: {{ restaurant.address.country }}</span>
          <br>
          <span>město: {{ restaurant.address.city }}</span>
          <br>
          <span>ulice: {{ restaurant.address.street }}</span>
          <br>
          <span>číslo popisné: {{ restaurant.address.buildingNumber }}</span>
          <br>
          <span>psč: {{ restaurant.address.zipCode }}</span>
        </div>
        <div class="col-md-6" v-else> #</div>
      </component>
  </div>
</template>
<script lang="ts">
import {type Restaurant} from "@/api/restaurant.ts";

export default {
  props: {
    restaurants: {
      type: Array<Restaurant>,
      required: true,
    },
    dashboard: {
      type: Boolean,
      default: false,
    }
  }
}
</script>
<style scoped>

</style>
