<script setup lang="ts">
import Default from "@/layouts/Default.vue";
</script>

<template>
  <Default>
    <div class="">
      <div class="container py-5">
        <div class="row align-items-center mb-5">
          <!-- Text -->
          <div class="col-lg-6 text-center text-lg-start">
            <h1 class="display-4 fw-bold text-dark">Moderní rezervační systém</h1>
            <p class="lead text-muted">Efektivní správa rezervací pro restaurace, kavárny a další Vaše skvělé podniky</p>
            <router-link to="/sign-up"
                         class="btn btn-primary btn-lg shadow mt-3"
                         data-aos="zoom-in">Vyzkoušet zdarma
            </router-link>
          </div>

          <!-- Obrázek -->
          <div class="col-lg-6 text-center mt-4 mt-lg-0">
            <img src="../assets/img/hp.svg"
                 alt="Ilustrace rezervací"
                 class="img-fluid"
                 style="max-height: 300px;">
          </div>
        </div>
      </div>
    </div>
    <div class="bg-light">
      <img src="../assets/img/deviders/devider-2.svg" alt="devider">
      <div class="container-md">
        <div class="row g-4 pb-5">
          <div class="col-md-6 col-lg-3">
            <div class="card h-100 shadow rounded-4 border-0"
                 data-aos="flip-left">
              <div class="card-body text-center">
                <i class="bi bi-calendar3 fs-1 text-primary mb-3"></i>
                <h5 class="fw-bold">Přehled rezervací</h5>
                <p class="text-muted">Okamžitý přehled nad všemi rezervacemi na daný den.</p>
              </div>
            </div>
          </div>
          <div class="col-md-6 col-lg-3">
            <div class="card h-100 shadow rounded-4 border-0"
                 data-aos="flip-left"
                 data-aos-delay="100">
              <div class="card-body text-center">
                <i class="bi bi-pc-display fs-1 text-success mb-3"></i>
                <h5 class="fw-bold">Snadná integrace</h5>
                <p class="text-muted">Vložení widgetu na váš web zabere méně než 5 minut.</p>
              </div>
            </div>
          </div>
          <div class="col-md-6 col-lg-3">
            <div class="card h-100 shadow rounded-4 border-0"
                 data-aos="flip-left"
                 data-aos-delay="200">
              <div class="card-body text-center">
                <i class="bi bi-envelope-paper-heart fs-1 text-info mb-3"></i>
                <h5 class="fw-bold">Notifikace e-mailem</h5>
                <p class="text-muted">Zákazník i restaurace dostanou potvrzení a možnost zrušení.</p>
              </div>
            </div>
          </div>
          <div class="col-md-6 col-lg-3">
            <div class="card h-100 shadow rounded-4 border-0"
                 data-aos="flip-left"
                 data-aos-delay="300">
              <div class="card-body text-center">
                <i class="bi bi-person-check fs-1 text-danger mb-3"></i>
                <h5 class="fw-bold">Bez registrace</h5>
                <p class="text-muted">Uživatel nemusí vytvářet účet – pohodlné a rychlé.</p>
              </div>
            </div>
          </div>
        </div>

      </div>
    </div>
    <img src="../assets/img/deviders/devider-6.svg" alt="devider" width="100%">
  </Default>
</template>

