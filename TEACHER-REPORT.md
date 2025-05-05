# NNPIA – Report semestrální práce - Rezervační systém

**Jméno a přijmení:** Fialka Ondřej Bc.

## Organizační požadavky
✅ **Povinná konzultace:** nejpozději do **6. týdne** semestru.

✅ **Termín odevzdání:** minimálně **2 dny před termínem zápočtu**, který si student zvolí.

✅ **Odevzdání repozitáře:** GitHub Classroom.

✅ **Dokumentace:** Součástí práce musí být stručná **README.md** dokumentace, která popisuje projekt, použitou technologii a návod na spuštění.

---

### Backend (Spring Boot)

### 1. Použití **Spring Boot** jako backendového frameworku.

#### ✅ Splněno

### 2. **Minimálně 3 datové entity** včetně vizualizace datových entit. Schéma může být vygenerováno pomocí IDE na základě již existujícího kódu.

#### ✅ Splněno

### 3. **Spring JPA** pro práci s databází.

#### ✅ Splněno

### 4. **Spring Security** pro autentizaci a autorizaci pomocí JWT tokenů.

#### ✅ Splněno

```Připomínky učitele
SecurityConfiguration.java obsahuje endpointy vyloučené z autentifikace u kterých bych očekával, že budou chráněny. Např. /api/v1/restaurants/{id}/reservations/available-times/{deskId} a /api/v1/restaurants/{id}/reservations/{uuid}.
Student obhájil tento postup. Endpointy slouží pro správu rezervace přes email. UUID zajištuje jedinečný přístup.
```

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(HttpMethod.POST,"/api/v1/auth/**").permitAll()
                    .requestMatchers(HttpMethod.GET,"/api/v1/restaurants").permitAll()
                    .requestMatchers(HttpMethod.GET,"/api/v1/restaurants/{id}").permitAll()
                    .requestMatchers(HttpMethod.GET,"/api/v1/restaurants/{id}/reservations/available-times").permitAll()
                    .requestMatchers(HttpMethod.GET,"/api/v1/restaurants/{id}/reservations/available-times/{deskId}").permitAll()
                    .requestMatchers(HttpMethod.DELETE,"/api/v1/restaurants/{id}/reservations/{uuid}").permitAll()
                    .requestMatchers(HttpMethod.POST,"/api/v1/restaurants/{id}/reservations").permitAll()
                    .requestMatchers(HttpMethod.GET,"/api/v1/restaurant-images/{uuid}").permitAll()
                    .requestMatchers(HttpMethod.GET,"/api/v1/restaurants/{id}/desks").permitAll()
                    .anyRequest().authenticated()
            );
    return http.build();
}
```

### 5. **Vícevrstvá architektura** (Model, DAO/Repository, Service, Controller).

#### ✅ Splněno

---

## Frontend

### 1. Použití **React.js** nebo jiné knihovny pro tvorbu single page aplikace.

#### ✅ Splněno

### 2. Aplikace musí obsahovat **minimálně 7 komponent**, a ideálně hooky.

#### ✅ Splněno

### 3. Implementace alespoň jedné **znovupoužitelné komponenty** (např. data-grid, tabulka, filtr, modal apod.).

#### ✅ Splněno

---

## Dodatečná netriviální funkcionalita

1. Vue.js pro realizaci frontendu
2. Napojemní na emailovou bránu, možnost spravovat rezervace přes email.


#### ✅ Splněno

```Připomínky učitele
Student využil pro realizaci frontendu Vue.js oproti React.js. Student seznámil učitele s Vue.js a jeho výhodami oproti React.js.
```

---

## Obecné požadavky

### 1. Dodržování **principů čistého kódu** (čitelnost, struktura, názvy, formátování).

#### ⚠️ Splněno s připomínkami

```Připomínky učitele
Některé service třídy využívají k přístupu do databáze přímo Repositories jiných entit. Například DeskService využívá 
restaurantRepository a zároveň restaurantService. Pro zachování logiky bych doporučuoval aby jednotlivé service
používali výhradně repository pouze pro entitu kterou reprezentují (např. DeskService by měl používat pouze DeskRepository).
Pro práci s ostatními entitami by měli využívat service třídy (např. restaurantService).
```

```java
public class DeskService {

    private final DeskRepository deskRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantService restaurantService;
```

### 2. Semestrální práce musí splňovat single page application rendering pattern.

#### ✅ Splněno

### 3. Studenti si sami vybírají vhodný typ databáze (relační, grafová, dokumentová). Výběr by měl odpovídat potřebám aplikace.

#### ✅ Splněno

---


## Testování

### 1. **Minimálně 2 unit testy** na netriviální logiku/algoritmus.

#### ✅ Splněno

```Připomínky učitele
Jednoduché unit testy. byli nahrazeny větším množstvím testů controlleru.
```

### 2. **Minimálně 2 unit testy** controlleru (např. s využitím **MockMvc**).

#### ✅ Splněno

### 3. **Minimálně 1 integrační test**, kde spolupracují alespoň 2 třídy servisní vrstvy (datová vrstva není mockovaná).

#### ✅ Splněno

---

## Závěr

### ✅ Semestrální práce obhájena

```Připomínky učitele
Student dokázal obhájil semestrální práci v předtermínu. Kód semestrální práce neobsahuje žádné závažné chyby nebo nedostatky.

Student si vyhrál s UI, pěkné a přehledné. Možnost Widgetu. Napojení na emialovou bránu. Možnost spravovat rezervace přes email.

Líbí se mi že využil jiný frontendový framework, je znát že je v této oblasti odborníkem.

Průběžně docházel na cvičení a konzultace.
```