# Projekt: Spring Boot + Vue.js

Tento projekt obsahuje dvě části:

- `backend` – Spring Boot aplikace postavená pomocí Gradle
- `frontend` – Vue.js aplikace postavená pomocí Vite
- `docker-compose.yml` – v root složce projektu, slouží ke spuštění databáze

## Požadavky

### Backend
- Java 17 nebo novější
- Gradle (nebo použijte wrapper `./gradlew`)
- Docker

### Frontend
- Node.js (doporučeno 18+)
- npm

---

## Spuštění projektu

### 1. Spusť databázi pomocí Docker Compose

Z root složky projektu spusť:

```bash
docker compose up -d
```

Tím se spustí např. databáze, kterou backend využívá.

> **Poznámka:** Docker Compose je vypnutý v rámci Spring Bootu (`spring.docker.compose.enabled=false`), takže backend se o něj nestará.

---

### 2. Spusť backend

1. Přejdi do složky `backend`:

```bash
cd backend
```

2. Spusť Spring Boot aplikaci:

```bash
./gradlew bootRun
```

Aplikace poběží na adrese: [http://localhost:8080](http://localhost:8080)

---

### 3. Spusť frontend

1. Přejdi do složky `frontend`:

```bash
cd frontend
```

2. Nainstaluj závislosti:

```bash
npm install
```

3. Spusť vývojový server:

```bash
npm run dev
```

Frontend poběží typicky na adrese: [http://localhost:5173](http://localhost:5173)

---

## Poznámky

- Frontend očekává API běžící na `http://localhost:8080`. Pokud to potřebuješ upravit, změň to v `.env` nebo Axios konfiguraci.
- Pro produkční build:
  - Backend: `./gradlew build`
  - Frontend: `npm run build`

---

## Licence

Tento projekt je poskytován pod licencí MIT.
