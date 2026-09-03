<template>
  <div>
    <span class="h3">Základní nastavení</span>
    <form v-if="restaurant" @submit.prevent="updateRestaurant">
      <div class="form-check mt-3">
        <input type="checkbox" id="usesDesks" class="form-check-input"
               v-model="restaurant.usesDesks"
               placeholder="Název restaurace">
        <label for="usesDesks">Rezervace na stoly</label>
      </div>
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
      <input type="file" class="form-control" @change="onFileChange">
      <input type="submit"
             class="btn btn-primary w-100 mt-3"
             value="Přidat obrázek">
    </form>
    <div v-if="restaurant && restaurant.imagesIds.length > 0"
         class="d-flex flex-wrap justify-content-start">
      <div
        v-for="image in restaurant.imagesIds"
        :key="image"
        class="card shadow-sm m-2 border-0"
        style="width: 200px;"
      >
        <img
          :src="getRestaurantImageUrl(image)"
          alt="restaurant image"
          class="card-img-top rounded"
          style="height: 150px; object-fit: cover;"
        />
        <div class="card-body text-center p-2">
          <button
            class="btn btn-sm btn-outline-danger"
            @click="deleteImage(image)"
            title="Smazat obrázek"
          >
            <i class="bi bi-trash-fill"></i>
          </button>
        </div>
      </div>
    </div>

  </div>
</template>
<script lang="ts">
import {type Restaurant, updateRestaurantById} from "@/api/restaurant.ts";
import {type AddressRequest, createAddress, updateAddress} from "@/api/address.ts";
import {
  deleteRestaurantImage,
  getRestaurantImageUrl,
  uploadRestaurantImage
} from "@/api/restaurantImage.ts";

export default {
  props: {
    restaurant: {
      type: Object as () => Restaurant,
      required: true
    }
  },
  emits: ['updateRestaurant', 'findRestaurant'],
  data() {
    let addressCreateRequest: AddressRequest = {
      street: "",
      city: "",
      buildingNumber: "",
      zipCode: "",
      country: ""
    }
    return {
      isLoading: false,
      file: null as File | null,
      addressCreateRequest,
    }
  },
  methods: {
    getRestaurantImageUrl,
    async updateRestaurant() {
      this.isLoading = true;
      console.log("updating");
      if (this.restaurant)
        await updateRestaurantById(this.restaurant).then(restaurant => {
          // this.restaurant = restaurant;
          this.$emit('findRestaurant');
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
    async createRestaurantAddress() {
      this.isLoading = true;
      if (this.restaurant)
        await createAddress(this.restaurant.id, this.addressCreateRequest).then(address => {
          this.$emit('findRestaurant')
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
      this.isLoading = true;
      if (this.restaurant && this.restaurant.address)
        await updateAddress(this.restaurant.id, this.restaurant.address).then(address => {
          // this.restaurant.address = address;
          this.$emit('findRestaurant')

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
      this.isLoading = true;
      if (this.restaurant && this.file) {
        const formData = new FormData();
        formData.append('file', this.file);
        await uploadRestaurantImage(this.restaurant.id, formData).then(response => {
          this.$notify({
            type: 'success',
            text: 'Obrázek přidán.'
          })
          this.$emit('findRestaurant', response);
        }).catch(e => {
          this.$notify({
            type: 'error',
            text: 'Nepovedlo se přidat obrázek.'
          })
        })
        this.file = null;
      }
    },
    async deleteImage(uuid: string) {
      await deleteRestaurantImage(uuid).then(response => {
        this.$notify({
          type: 'success',
          text: 'Obrázek odstraněn.'
        })
        this.$emit('findRestaurant', response);
      }).catch(e => {
        this.$notify({
          type: 'error',
          text: 'Nepovedlo se odstranit obrázek.'
        })
        console.log(e);
      })
    },
  },
  mounted() {
    // this.restaurant = JSON.parse(JSON.stringify(this.restaurant_prop));
  }
}
</script>
