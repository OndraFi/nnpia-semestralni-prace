<template>
  <Default>
    <div class="container-md border-start border-end border-bottom shadow-sm pb-5 px-5 pt-2 rounded">
      <div class="row mb-5 border-bottom" style="cursor: pointer;">
        <div class="col-3" @click="menu='rezervace'"
             :class="{ 'border-top border-primary border-3': menu === 'rezervace', 'text-muted bg-light' : menu !== 'rezervace' }">
          <p class="text-center mt-3">
            <i class="bi bi-calendar-check-fill me-2"></i> Rezervace
          </p>
        </div>
        <div class="col-3" @click="menu='nastaveni'"
             :class="{ 'border-top border-primary border-3': menu === 'nastaveni', 'text-muted bg-light' : menu !== 'nastaveni' }">
          <p class="text-center mt-3">
            <i class="bi bi-gear-fill me-2"></i> Nastavení
          </p>
        </div>
        <div class="col-3" @click="menu='stoly'"
             :class="{ 'border-top border-primary border-3': menu === 'stoly', 'text-muted bg-light' : menu !== 'stoly' }">
          <p class="text-center mt-3">
            <i class="bi bi-person-fill me-2"></i>
            <br class="d-md-none">
            Stoly
          </p>
        </div>
        <div class="col-3" @click="menu='widget'"
             :class="{ 'border-top border-primary border-3': menu === 'widget', 'text-muted bg-light' : menu !== 'widget' }">
          <p class="text-center mt-3">
            <i class="bi bi-bookmark-check-fill me-2"></i> Widget
          </p>
        </div>
      </div>


      <!--      REZERVACE-->
      <div v-if="restaurant"
           v-show="menu === 'rezervace'">
        <reservations :restaurant="restaurant" ></reservations>
      </div>

      <!--      NASTAVENÍ-->
      <div v-if="restaurant"
           v-show="menu === 'nastaveni'">
        <settings
          :restaurant="restaurant"
          @find-restaurant="findRestaurant"/>
      </div>

      <div v-if="restaurant" v-show="menu === 'widget'">
        <p>
          Na stránku vložte kód:
          <pre class="border rounded pt-3">
            &lt;iframe src="http://localhost:5173/restaurant/{{ restaurant.id }}/widget" width="100%" height="700px" /&gt;
          </pre>
        </p>
        <restaurant-reservation/>
      </div>

      <div v-if="restaurant" v-show="menu === 'stoly'">
        <desks :restaurant="restaurant"></desks>
      </div>

    </div>
  </Default>
</template>
<script lang="ts">
import Default from "@/layouts/Default.vue";
import {findRestaurantById, type Restaurant, updateRestaurantById} from "@/api/restaurant.ts";
import {
  type AddressRequest,
  createAddress,
  updateAddress
} from "@/api/address.ts"
import {
  deleteRestaurantImage,
  getRestaurantImageUrl,
  uploadRestaurantImage
} from "@/api/restaurantImage.ts";
import {
  acceptReservation, cancelReservation,
  getReservationsByRestaurantId,
  type Reservation
} from "@/api/reservation.ts";
import Desks from "@/components/dashboard/desks.vue";
import RestaurantReservation from "@/components/RestaurantReservation.vue";
import Settings from "@/components/dashboard/settings.vue";
import Reservations from "@/components/dashboard/reservations.vue";

export default {
  components: {Reservations, Settings, RestaurantReservation, Desks, Default},
  data() {
    return {
      restaurant: null as Restaurant | null,
      reservations: null as Array<Reservation> | null,
      menu: 'rezervace',
    }
  },
  methods: {
    deleteRestaurantImage,
    acceptReservation,
    getRestaurantImageUrl,
    async findRestaurant() {
      await findRestaurantById(Number(this.$route.params.id)).then(response => {
        this.restaurant = response;
        console.log(response);
      }).catch(e => {
        console.log(e);
      })
    },
    async findReservations() {
      if (this.restaurant) {
        getReservationsByRestaurantId(this.restaurant.id, "", true, true).then(response => {
          this.reservations = response;
          console.log(response);
        }).catch(e => {
          console.log(e);
        })
      }
    },
    async doAcceptReservation(reservation: Reservation) {
      if (this.restaurant) {
        acceptReservation(this.restaurant.id, reservation.id).then(response => {
          this.findReservations();
          this.$notify({
            type: 'success',
            text: 'Rezervace potvrzena.'
          })
        }).catch(e => {
          this.$notify({
            type: 'error',
            text: 'Nepovedlo se potvrdit rezervaci.'
          })
          console.log(e);
        })
      }
    },
    async doCancelReservation(reservation: Reservation) {
      if (this.restaurant) {
        cancelReservation(this.restaurant.id, reservation.uuid).then(response => {
          this.findReservations();
          this.$notify({
            type: 'success',
            text: 'Rezervace zrušena.'
          })
        }).catch(e => {
          this.$notify({
            type: 'error',
            text: 'Nepovedlo se zrušit rezervaci.'
          })
          console.log(e);
        })
      }
    }
  },
  async created() {
    await this.findRestaurant();
    await this.findReservations();
  }
}
</script>
