<template>
  <div class="text-center">
    Rušení rezervace...
    <br>
    {{message}}
  </div>
</template>
<script lang="ts">
  import {cancelReservation} from "@/api/reservation.ts";

  export default {
    data(){
      return {
        message: ""
      }
    },
    created() {
      const restaurantId = this.$route.params.id
      const reservationUuid = this.$route.params.uuid;
      cancelReservation(restaurantId,reservationUuid).then(response=>{
        console.log(response);
        this.message = "Vaše rezervace byla úspěšně zrušena."
      }).catch(e => {
        console.log(e);
      })
    }
  }
</script>
<style scoped>

</style>
