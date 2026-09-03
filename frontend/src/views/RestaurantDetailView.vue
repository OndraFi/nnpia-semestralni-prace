<template>
  <Default>
    <h1 v-if="restaurant" class="text-center my-5 display-4">{{ restaurant.name }}</h1>
    <img src="../assets/img/deviders/devider-2.svg" alt="devider">

    <div class="bg-light pb-3 pt-5">
      <div class="container-md p-0 bg-white shadow-sm rounded border"
           style="margin-top: -170px; position: relative; z-index: 10;"
           v-if="restaurant">
        <restaurant-reservation />
      </div>
    </div>
    <img src="../assets/img/deviders/devider-6.svg" alt="devider" width="100%">

  </Default>
</template>
<script lang="ts">
import Default from "@/layouts/Default.vue";
import {
  type Reservation,
  type ReservationTimeRange
} from "@/api/reservation.ts";
import {findRestaurantById, type Restaurant} from "@/api/restaurant.ts";
import ReservationTimePicker from "@/components/ReservationTimePicker.vue";
import RestaurantReservation from "@/components/RestaurantReservation.vue";

export default {
  components: {RestaurantReservation, ReservationTimePicker, Default},
  data() {
    return {
      reservationDate: new Date().toISOString().split('T')[0],
      numberOfPeople: 1,
      phone: '',
      email: '',
      reservationName: '',
      availableTimes: null as Array<ReservationTimeRange> | null,
      restaurant: null as Restaurant | null,
      selectedTime: null as ReservationTimeRange | null,
      reservation: null as Reservation | null,
    }
  },
  methods: {
    findRestaurant() {
      findRestaurantById(Number(this.$route.params.id)).then(restaurant => {
        this.restaurant = restaurant;
      })
    },
  },
  mounted() {
    this.findRestaurant();
  }
}
</script>
<style scoped>

</style>
