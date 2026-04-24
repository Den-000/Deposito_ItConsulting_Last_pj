# NexTicket
### Sistema di Gestione Eventi e Biglietteria Online
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.x-6DB33F?style=for-the-badge\&logo=spring-boot\&logoColor=white)]
[![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge\&logo=openjdk\&logoColor=white)]
[![MySQL](https://img.shields.io/badge/MySQL-8-4479A1?style=for-the-badge\&logo=mysql\&logoColor=white)]
[![JWT](https://img.shields.io/badge/Auth-JWT-black?style=for-the-badge)]
[![HTML/CSS/JS](https://img.shields.io/badge/Frontend-Vanilla_JS-yellow?style=for-the-badge)]

*Gestisci eventi, acquista biglietti e monitora tutto tramite una dashboard admin completa.*

---

## Introduzione

**NexTicket** è una piattaforma web per la gestione di eventi e biglietti digitali.

Permette agli utenti di:

* Registrarsi e autenticarsi
* Visualizzare eventi disponibili
* Acquistare biglietti digitali
* Effettuare il check-in tramite QR code

Dal lato amministratore, il sistema consente:

* Gestione utenti
* Gestione eventi e location
* Monitoraggio biglietti
* Invio automatico di reminder email

Il progetto è costruito come applicazione full-stack con backend REST in Spring Boot e frontend web statico.

---

## Tech Stack

| Livello        | Tecnologia              | Ruolo               |
| -------------- | ----------------------- | ------------------- |
| **Backend**    | Spring Boot             | API REST            |
| **Linguaggio** | Java                    | Logica applicativa  |
| **ORM**        | Spring Data JPA         | Accesso database    |
| **Database**   | MySQL                   | Persistenza dati    |
| **Sicurezza**  | Spring Security + JWT   | Autenticazione      |
| **Email**      | JavaMailSender          | Invio notifiche     |
| **Scheduler**  | Spring @Scheduled       | Reminder automatici |
| **Frontend**   | HTML + CSS + JavaScript | Interfaccia utente  |
| **Build Tool** | Maven                   | Gestione dipendenze |

---

## Funzionalità

### Gestione Utenti

* Registrazione e login con email e password
* Autenticazione tramite JWT
* Ruoli: USER / ADMIN

### Gestione Eventi

* Creazione eventi con data, location e stato
* Ricerca eventi
* Visualizzazione eventi futuri

### Biglietti

* Acquisto biglietti associati a evento e utente
* Generazione QR code univoco
* Stato biglietto (valido / usato)

### Check-In

* Verifica biglietti tramite QR code
* Impedisce doppio utilizzo

### Dashboard Admin

* Gestione utenti (promozione/eliminazione)
* Gestione eventi
* Gestione location
* Monitoraggio notifiche

### Reminder Automatici

* Invio email automatico 1 giorno prima dell’evento
* Sistema anti-spam con flag `reminderSent`

---

## Architettura Database

```
users
 ├─ id
 ├─ email
 ├─ password
 ├─ role

events
 ├─ id
 ├─ name
 ├─ date
 ├─ status
 ├─ location_id

locations
 ├─ id
 ├─ name
 ├─ city
 ├─ address

tickets
 ├─ id
 ├─ qrCode
 ├─ valid
 ├─ checkedIn
 ├─ reminderSent
 ├─ user_id
 ├─ event_id

payments
 ├─ id
 ├─ amount
 ├─ method
 ├─ status
 ├─ ticket_id
```

---

## Sicurezza

### JWT Authentication

* Token stateless
* Header Authorization: `Bearer token`
* Filtro `JwtAuthenticationFilter` intercetta ogni richiesta

### Password

* Hashing con bcrypt
* Nessuna password salvata in chiaro

---

## Struttura del Progetto

```
src/main/java/com/example/demo

controller/        → REST API
service/           → Logica business
repository/        → Accesso dati
model/             → Entità JPA
dto/               → Data Transfer Objects
security/          → JWT + filtri
```

Frontend:

```
static/
 ├── admin.html
 ├── dashboardUsers.html
 ├── dashboardEvents.html
 ├── dashboardLocations.html
 ├── dashboardNotifications.html
 ├── js/
 ├── css/
```

---

## Deploy

Avvio locale:

```bash
mvn spring-boot:run
```

Oppure:

```bash
mvn clean package
java -jar target/app.jar
```

---

## Pattern Architetturali e Design Pattern

| Pattern                  | Dove                              | Scopo              |
| ------------------------ | --------------------------------- | ------------------ |
| **Layered Architecture** | Controller → Service → Repository | Separazione logica |
| **MVC**                  | REST Controller                   | Gestione richieste |
| **DTO Pattern**          | TicketDTO, NotificationDTO        | Disaccoppiamento   |
| **Repository Pattern**   | JpaRepository                     | Accesso DB         |
| **Dependency Injection** | Spring                            | Modularità         |

---

## Sistema Biglietti e Reminder

### Biglietti

* Generazione QR code univoco
* Collegamento a utente ed evento
* Stato:

  * valid
  * checkedIn

### Reminder Email

Sistema automatico:

```java
@Scheduled(fixedRate = 60000)
```

Logica:

* Cerca eventi di domani
* Recupera ticket validi
* Invia email se `reminderSent = false`
* Imposta `reminderSent = true`

---

## Endpoint API

```
POST   /auth/register          Registrazione
POST   /auth/login             Login

GET    /events/all             Lista eventi
GET    /events/search          Ricerca eventi
GET    /events/{id}            Dettaglio evento

POST   /tickets                Acquisto biglietto
POST   /tickets/check-in       Check-in QR

GET    /tickets/my             Biglietti utente
GET    /tickets/{id}           Dettaglio ticket
GET    /tickets/notifications  Notifiche admin

DELETE /events/{id}            Elimina evento
DELETE /locations/{id}         Elimina location
```

---


## 👨‍💻 Team di Sviluppo

| Nome | Ruolo | GitHub | Linkedin
| Andrea Palumbo | Fullstack Developer | https://github.com/palux17 | https://www.linkedin.com/in/andrea-palumbo-054a233a5/
| Dennis D'Andrea | Fullstack Developer | https://github.com/Den-000| https://it.linkedin.com/in/dennis-d-andrea-2388b5215?trk=people-guest_people_search-card