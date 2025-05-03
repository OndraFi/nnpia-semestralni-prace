<template>
  <Default>

    <div class="container-md">
      <span class="h3">Základní nastavení</span>
      <form v-if="restaurant" @submit.prevent="updateRestaurant">
        <div class="form-floating mt-3">
          <input type="text" id="name" class="form-control" v-model="restaurant.name"
                 placeholder="Název restaurace">
          <label for="name">Název restaurace</label>
        </div>
        <div class="form-floating mt-3">
          <input type="number" id="capacity" class="form-control" min="0"
                 v-model="restaurant.capacity" placeholder="Kapacita restaurace">
          <label for="capacity">Kapacita restaurace</label>
        </div>
        <div class="form-floating mt-3">
          <input type="time" id="openTime" class="form-control" v-model="restaurant.openTime"
                 placeholder="Open time">
          <label for="openTime">Otevírací doba</label>
        </div>
        <div class="form-floating mt-3">
          <input type="time" id="closeTime" class="form-control" v-model="restaurant.closeTime"
                 placeholder="Close time">
          <label for="closeTime">Zavírací doba</label>
        </div>
        <input class="btn btn-primary w-100 mt-3" type="submit" value="Upravit Restauraci">
      </form>
      <hr>
      <span class="h3">Adresa</span>
      <form v-if="!(restaurant?.address)" @submit.prevent="createRestaurantAddress">
        <div class="form-floating">
          <input type="text"
                 class="form-control mt-3"
                 id="country"
                 v-model="addressCreateRequest.country"
                 required
                 placeholder="country">
          <label for="country">Země</label>
        </div>
        <div class="form-floating">
          <input type="text"
                 class="form-control mt-3"
                 id="city"
                 v-model="addressCreateRequest.city"
                 required
                 placeholder="city">
          <label for="city">Město</label>
        </div>
        <div class="form-floating">
          <input type="text"
                 class="form-control mt-3"
                 id="street"
                 v-model="addressCreateRequest.street"
                 required
                 placeholder="street">
          <label for="street">Ulice</label>
        </div>
        <div class="form-floating">
          <input type="text"
                 class="form-control mt-3"
                 id="buildingNumber"
                 v-model="addressCreateRequest.buildingNumber"
                 required
                 placeholder="building Number">
          <label for="buildingNumber">Číslo popisné</label>
        </div>
        <div class="form-floating">
          <input type="text"
                 class="form-control mt-3"
                 id="zip"
                 v-model="addressCreateRequest.zipCode"
                 required
                 placeholder="zip">
          <label for="zip">PSČ</label>
        </div>
        <input class="btn btn-primary w-100 mt-3" value="Upravit adresu" type="submit">
      </form>
      <form v-if="restaurant?.address" @submit.prevent="updateRestaurantAddress">
        <div class="form-floating">
          <input type="text"
                 class="form-control mt-3"
                 id="country"
                 v-model="restaurant.address.country"
                 required
                 placeholder="country">
          <label for="country">Země</label>
        </div>
        <div class="form-floating">
          <input type="text"
                 class="form-control mt-3"
                 id="city"
                 v-model="restaurant.address.city"
                 required
                 placeholder="city">
          <label for="city">Město</label>
        </div>
        <div class="form-floating">
          <input type="text"
                 class="form-control mt-3"
                 id="street"
                 v-model="restaurant.address.street"
                 required
                 placeholder="street">
          <label for="street">Ulice</label>
        </div>
        <div class="form-floating">
          <input type="text"
                 class="form-control mt-3"
                 id="buildingNumber"
                 v-model="restaurant.address.buildingNumber"
                 required
                 placeholder="building Number">
          <label for="buildingNumber">Číslo popisné</label>
        </div>
        <div class="form-floating">
          <input type="text"
                 class="form-control mt-3"
                 id="zip"
                 v-model="restaurant.address.zipCode"
                 required
                 placeholder="zip">
          <label for="zip">PSČ</label>
        </div>
        <input class="btn btn-primary w-100 mt-3" value="Upravit adresu" type="submit">
      </form>

      <hr>
      <span class="h3">Obrázky</span>
      <br>
      <span
        class="small text-muted">První obrázek bude použit jako náhledový v seznamu restaurací</span>
      <form @submit.prevent="uploadImage">
        <input type="file" @change="onFileChange">
        <input type="submit" value="Přidat obrázek">
      </form>
      <div v-if="restaurant && restaurant.imagesIds.length > 0">
        <img
          v-for="image in restaurant.imagesIds"
          :key="image"
          :src="getRestaurantImageUrl(image)"
          alt="restaurant image"
          style="max-width: 200px; margin: 0.5rem;"
        />
      </div>

      <hr>
      <span class="h-3">Widget</span>
      <div v-if="restaurant">
        <router-link :to="`/restaurant/${restaurant.id}/widget`"
                     class="btn btn-primary">
          Widget
        </router-link>
      </div>

      <!--      REZERVACE-->
      <hr>
      <span class="h3">Rezervace</span>
      <div v-if="reservations" class="row shadow my-3" v-for="reservation in reservations">
        <p class="position-relative">
          <span
            class="position-absolute top-0 start-0 translate-middle badge rounded-pill bg-danger"
            v-if="!reservation.isAccepted">Nová</span>
          <span
            class="position-absolute top-0 start-0 translate-middle badge rounded-pill bg-success"
            v-else>Schválená</span>
        </p>
        id: {{ reservation.id }}
        <br>
        uuid: {{ reservation.uuid }}
        <br>
        <b>začátek rezervace:</b> {{ reservation.startTime }}
        <br>
        konec rezervacec: {{ reservation.endTime }}
        <br>
        počet osob: {{ reservation.numberOfPeople }}
        <br>
        na jméno: {{ reservation.reservationName }}
        <br>
        telefon: {{ reservation.phone }}
        <br>
        email: {{ reservation.email }}
        <br>
        <button
          v-if="!reservation.isAccepted"
          class="btn btn-primary"
          @click="doAcceptReservation(reservation)">
          Potvrdit rezervaci
        </button>
        <button
          class="btn btn-danger"
          @click="doCancelReservation(reservation)">
          Zrušit rezervaci
        </button>
      </div>
      <div v-if="reservations && reservations.length == 0"
           class="text-center p-3 fw-bold shadow bg-light mt-3">
        <img src="../../assets/img/warning.svg"
             alt="no restaurant found"
             style="height: 50px">
        Nemáte žádné rezervace!
      </div>
    </div>
  </Default>
</template>
<script lang="ts">
import Default from "@/layouts/Default.vue";
import {findRestaurantById, type Restaurant, updateRestaurantById} from "@/api/restaurant.ts";
import {
  type Address,
  type AddressRequest,
  createAddress,
  findAddress,
  updateAddress
} from "@/api/address.ts"
import {
  getRestaurantImageUrl,
  uploadRestaurantImage
} from "@/api/restaurantImage.ts";
import {
  acceptReservation, cancelReservation,
  getReservationsByRestaurantId,
  type Reservation
} from "@/api/reservation.ts";

export default {
  components: {Default},
  data() {
    let addressCreateRequest: AddressRequest = {
      street: "",
      city: "",
      buildingNumber: "",
      zipCode: "",
      country: ""
    }
    return {
      restaurant: null as Restaurant | null,
      file: null as File | null,
      addressCreateRequest,
      reservations: null as Array<Reservation> | null,
    }
  },
  methods: {
    acceptReservation,
    getRestaurantImageUrl,
    async findRestaurant() {
      await findRestaurantById(this.$route.params.id).then(response => {
        this.restaurant = response;
        console.log(response);
      }).catch(e => {
        console.log(e);
      })
    },
    async updateRestaurant() {
      console.log("updating");
      if (this.restaurant)
        await updateRestaurantById(this.restaurant).then(restaurant => {
          this.restaurant = restaurant;
          this.$notify({
            type: 'success',
            text: 'Úprava proběhla v pořádku.'
          })
          console.log("updated");
        }).catch(e => {
          this.$notify({
            type: 'error',
            text: 'Nepovedlo se uložit změny.'
          })
          console.log(e)
        })
    },
    async findRestaurantAddress() {
      console.log("finding restaurant address");
      if (this.restaurant) {
        await findAddress(this.restaurant.id).then(address => {
          console.log(address);
          this.restaurant.address = address;
        }).catch(e => {
          console.log(e);
        })
      }
    },
    async createRestaurantAddress() {
      await createAddress(this.restaurant.id, this.addressCreateRequest).then(address => {
        this.restaurant.address = address;
        this.$notify({
          type: 'success',
          text: 'Adresa upravena.'
        })
      }).catch(e => {
        this.$notify({
          type: 'error',
          text: 'Nepovedlo se upravit adresu.'
        })
        console.log(e);
      })
    },
    async updateRestaurantAddress() {
      await updateAddress(this.restaurant.id, this.restaurant.address).then(address => {
        this.restaurant.address = address;
        this.$notify({
          type: 'success',
          text: 'Adresa upravena.'
        })
      }).catch(e => {
        this.$notify({
          type: 'error',
          text: 'Nepovedlo se upravit adresu.'
        })
        console.log(e);
      })
    },
    onFileChange(event: Event) {
      const target = event.target as HTMLInputElement;
      if (target.files && target.files.length > 0) {
        this.file = target.files[0];
      }
    },
    async uploadImage() {
      if (this.restaurant && this.file) {
        const formData = new FormData();
        formData.append('file', this.file);
        await uploadRestaurantImage(this.restaurant.id, formData).then(response => {
          this.$notify({
            type: 'success',
            text: 'Obrázek přidán.'
          })
        }).catch(e=>{
          this.$notify({
            type: 'error',
            text: 'Nepovedlo se přidat obrázek.'
          })
        })
        this.file = null;
      }
    },
    async findReservations() {
      if (this.restaurant) {
        getReservationsByRestaurantId(this.restaurant.id).then(response => {
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
    // await this.findRestaurantAddress();
  }
}
</script>
