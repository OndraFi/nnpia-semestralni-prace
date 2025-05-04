<template>
  <div>
    <div class="row">
      <div class="col-md-7">

        <span class="h3">Rezervace</span>
        <span v-if="reservations" class="text-muted small"> ({{ reservations.length }})</span>
      </div>
      <div class="col-md-1">
        <div class="form-check">
          <input class="form-check-input"
                 type="checkbox"
                 v-model="searchAccepted"
                 id="searchAccepted"
                 @change="searchAcceptedReservations">
          <label class="form-check-label" for="searchAccepted">
            schválené
          </label>
        </div>
      </div>
      <div class="col-md-1">
        <div class="form-check">
          <input class="form-check-input"
                 type="checkbox"
                 v-model="searchNotAccepted"
                 id="searchNotAccepted"
                 @change="searchNotAcceptedReservations">
          <label class="form-check-label" for="searchNotAccepted">
            nové
          </label>
        </div>

      </div>
      <div class="col-md-3">
        <input type="text"
               id="searchName"
               class="form-control"
               placeholder="Rezervace pro"
               v-model="searchName"
               @input="searchNameInReservations">
      </div>
    </div>

    <div v-if="reservations" v-for="reservation in reservations"
         class="card my-3 border-bottom border-0">
      <div class="card-body row align-items-center">
        <!-- Stav rezervace jako ikona a badge -->
        <div class="col-md-2 text-center">
          <i
            v-if="reservation.isAccepted"
            class="bi bi-check-circle-fill text-success fs-3"
            title="Schváleno"></i>
          <i
            v-else
            class="bi bi-clock-fill text-primary fs-3"
            title="Nová"></i>
        </div>

        <!-- Základní info o rezervaci -->
        <div class="col-md-7">
          <h5 class="card-title mb-1">
            Rezervace na jméno <strong>{{ reservation.reservationName }}</strong>
          </h5>
          <p class="mb-1">
            Datum: {{ new Date(reservation.startTime).toLocaleDateString() }}<br>
            Čas: {{ new Date(reservation.startTime).toLocaleTimeString() }} –
            {{ new Date(reservation.endTime).toLocaleTimeString() }}
          </p>
          <p class="mb-1">
            <span v-if="reservation.desk">Stůl: {{ reservation.desk.name }}</span>
            <span v-else>
                  Počet osob: {{ reservation.numberOfPeople }}
                  <span v-if="reservation.numberOfPeople === 1">osoba</span>
                  <span v-else-if="reservation.numberOfPeople < 5">osoby</span>
                  <span v-else>osob</span>
                </span>
          </p>
        </div>

        <!-- Kontaktní informace -->
        <div class="col-md-3">
          <p class="mb-1"><i class="bi bi-telephone-fill"></i> {{ reservation.phone }}</p>
          <p class="mb-2"><i class="bi bi-envelope-fill"></i> {{ reservation.email }}</p>

          <div class="d-flex gap-2">
            <button
              v-if="!reservation.isAccepted"
              class="btn btn-sm btn-outline-success"
              @click="doAcceptReservation(reservation)">
              <i class="bi bi-check-circle"></i> Potvrdit
            </button>
            <button
              class="btn btn-sm btn-outline-danger"
              @click="doCancelReservation(reservation)">
              <i class="bi bi-x-circle"></i> Zrušit
            </button>
          </div>
        </div>
      </div>
    </div>
    <div v-if="reservations && reservations.length == 0"
         class="text-center p-3 fw-bold shadow bg-light mt-3">
      <img src="../../assets/img/warning.svg"
           alt="no restaurant found"
           style="height: 50px">
      Nemáte žádné rezervace!
    </div>
  </div>
</template>
<script lang="ts">
import {
  acceptReservation, cancelReservation,
  getReservationsByRestaurantId,
  type Reservation
} from "@/api/reservation.ts";
import type {Restaurant} from "@/api/restaurant.ts";
import {debounce} from "lodash";

export default {
  props: {
    restaurant: {
      type: Object as () => Restaurant,
      required: true
    }
  },
  data() {
    return {
      reservations: null as Array<Reservation> | null,
      searchName: "",
      searchAccepted: false as boolean,
      searchNotAccepted: false as boolean,
    }
  },
  methods: {
    searchAcceptedReservations() {
      this.searchNotAccepted = false;
      this.findReservations();
    },
    searchNotAcceptedReservations() {
      this.searchAccepted = false;
      this.findReservations();
    },
    searchNameInReservations: debounce(function () {
      this.findReservations();
    }, 500),
    async findReservations() {
      if (this.restaurant) {
        this.reservations = null;
        getReservationsByRestaurantId(this.restaurant.id, this.searchName, this.searchAccepted, this.searchNotAccepted).then(response => {
          this.reservations = response;
          console.log("reservations", response);
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
  mounted() {
    this.findReservations()
  },
}
</script>
