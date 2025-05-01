<script setup lang="ts">
import Default from "@/layouts/Default.vue";
</script>

<template>
  <Default>
    <div class="container py-5">
      <div class="text-center mb-5">
        <h1 class="display-4 fw-bold text-dark">Moderní rezervační systém</h1>
        <p class="lead text-muted">Efektivní správa rezervací pro restaurace, kavárny a podniky všeho druhu.</p>
        <router-link to="/sign-up" class="btn btn-primary btn-lg shadow mt-3">Vyzkoušet zdarma</router-link>
      </div>

      <div class="row g-4">
        <div class="col-md-6 col-lg-3">
          <div class="card h-100 shadow rounded-4 border-0">
            <div class="card-body text-center">
              <i class="bi bi-calendar3 fs-1 text-primary mb-3"></i>
              <h5 class="fw-bold">Přehled rezervací</h5>
              <p class="text-muted">Okamžitý přehled nad všemi rezervacemi na daný den.</p>
            </div>
          </div>
        </div>
        <div class="col-md-6 col-lg-3">
          <div class="card h-100 shadow rounded-4 border-0">
            <div class="card-body text-center">
              <i class="bi bi-pc-display fs-1 text-success mb-3"></i>
              <h5 class="fw-bold">Snadná integrace</h5>
              <p class="text-muted">Vložení widgetu na váš web zabere méně než 5 minut.</p>
            </div>
          </div>
        </div>
        <div class="col-md-6 col-lg-3">
          <div class="card h-100 shadow rounded-4 border-0">
            <div class="card-body text-center">
              <i class="bi bi-envelope-paper-heart fs-1 text-info mb-3"></i>
              <h5 class="fw-bold">Notifikace e-mailem</h5>
              <p class="text-muted">Zákazník i restaurace dostanou potvrzení a možnost zrušení.</p>
            </div>
          </div>
        </div>
        <div class="col-md-6 col-lg-3">
          <div class="card h-100 shadow rounded-4 border-0">
            <div class="card-body text-center">
              <i class="bi bi-person-check fs-1 text-danger mb-3"></i>
              <h5 class="fw-bold">Bez registrace</h5>
              <p class="text-muted">Uživatel nemusí vytvářet účet – pohodlné a rychlé.</p>
            </div>
          </div>
        </div>
      </div>
    </div>


  </Default>
</template>

