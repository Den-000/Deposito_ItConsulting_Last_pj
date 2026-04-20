# Event Ticketing System (Backend - Spring Boot)

Sistema backend per la gestione d'eventi e ticketing con QR Code, progettato seguendo principi d'architettura pulita e best practice enterprise.

---

## Descrizione

Quest'applicazione permette agli organizzatori di creare eventi ed agli utenti di acquistare biglietti digitali con QR Code univoci.
Include funzionalità avanzate come:

* gestione eventi con posti limitati
* acquisto biglietti con diversi tipi (VIP, Standard)
* generazione QR Code univoci
* check-in digitale
* invio email automatico
* scheduler per promemoria eventi
* gestione pagamenti simulati
* audit accessi

---

## Architettura

Il progetto segue una struttura **layered + clean architecture inspired**:

```
controller → service → repository → model
                ↓
               dto
```

### Struttura progetto

```
com.example.ticketing
│
├── controller
├── dto
├── model
├── repository
├── security
├── service
└── scheduler
```

---

## Modello Dati

### Entità principali:

* **MyUser** → utenti e admin
* **Event** → eventi
* **TicketType** → tipologie di biglietti (VIP, Standard)
* **Ticket** → biglietti acquistati
* **Payment** → pagamenti associati ai ticket
* **Location** → luogo evento
* **Notification** → log notifiche email
* **CheckInLog** → storico accessi

### Relazioni principali:

* Un evento ha più ticket
* Un utente può acquistare più ticket
* Ogni ticket ha un pagamento associato
* Ogni ticket può avere più log di check-in

---

## Tecnologie utilizzate

* Java 17+
* Spring Boot
* Spring Data JPA (Hibernate)
* Spring Mail
* MySQL
* Maven
* Lombok
* ZXing (QR Code)

---

## Funzionalità principali

### Gestione Eventi

* Creazione eventi
* Gestione posti disponibili
* Supporto multi-ticket pricing

### Ticketing

* Acquisto biglietti
* Generazione QR Code univoci
* Validazione ticket
* Check-in

### Notifiche

* Email di conferma d'acquisto
* Reminder automatici per eventi imminenti

### Scheduler

* Invio promemoria giornaliero
* Invalidazione ticket per eventi passati

### Pagamenti

* Simulazione pagamento (CARD, PAYPAL)
* Stato pagamento (PAID, FAILED)

---

## Sicurezza

* Autenticazione utenti
* Ruoli: USER / ADMIN
* Integrazione JWT

---

## Obiettivo del progetto

Dimostrare competenze in:

* progettazione backend
* modellazione database
* architettura software
* gestione processi asincroni
* sviluppo API REST

---

## Autore

Progetto sviluppato a scopo didattico / portfolio.
By D.D.

---
