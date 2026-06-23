# My Bikes - Sistema di Gestione Bike Sharing

**My Bikes** è un'applicazione desktop sviluppata in Java per la gestione di un servizio di bike sharing. Il sistema è basato sul pattern architetturale **MVC (Model-View-Controller)**, utilizza l'interfaccia grafica **Java Swing** per le dashboard di interazione, e si appoggia a un database **MySQL** per la persistenza dei dati con credenziali cifrate in modo sicuro.

Il progetto è stato realizzato come elaborato per l'esame di **Programmazione 3** da **Giuseppe Gautieri**.

---

## Funzionalità Principali

Il sistema prevede due tipologie di utenti con permessi e interfacce dedicate:

### Area Cliente (GUICliente)
* **Registrazione & Login**: Creazione di un account cliente con salvataggio sicuro della password tramite hash.
* **Noleggio Biciclette (Affitti)**: Possibilità di noleggiare una bicicletta specificando il parcheggio di partenza (città origine), il parcheggio di arrivo (città destinazione) e i chilometri previsti del tragitto.
* **Fatturazione e Pagamenti**: Calcolo automatico del costo del noleggio basato sulla tariffa della categoria di bicicletta e la distanza in km. Supporto a diversi metodi di pagamento (es. carta).

### Area Amministratore (GUIAdmin)
* **Registrazione & Login**: Accesso riservato agli amministratori del servizio.
* **Gestione Flotta**: Aggiunta di nuove biciclette nel sistema specificando la categoria e la tariffa oraria/chilometrica.
* **Gestione Equipaggiamento**: Possibilità di aggiungere equipaggiamenti specifici a modelli esistenti (es. seggiolino, navigatore).
* **Aggiornamento Tariffe**: Modifica in tempo reale delle tariffe delle biciclette.
* **Statistiche di Utilizzo**: Visualizzazione della percentuale di utilizzo di ciascuna categoria di bicicletta sul totale dei noleggi effettuati.
* **Storico Completo**: Accesso ai log completi di tutti gli affitti e pagamenti registrati nel sistema.

---

## Struttura del Progetto

Il codice sorgente è strutturato secondo il pattern MVC all'interno della cartella `ProgettoGautieriGiuseppe/src`:

*  **`model`**: Contiene le classi di dominio dell'applicazione.
  * `model.Bike`: Gestione dell'entità bicicletta (`Bike`, `AbstractBike`).
  * `model.Parcheggio`: Rappresenta i punti di stallo/parcheggio nelle città.
  * `model.User`: Gestione dei profili utente (`Cliente`, `Admin`, `AbstractUtente`).
*  **`controller`**: Gestisce la logica di business e l'interazione con il database.
  * `Database.java`: Gestore principale delle connessioni, query SQL e persistenza.
  * `Affitto.java`: Logica legata ai noleggi.
  * `Pagamento.java`: Logica associata ai pagamenti e alla fatturazione.
*  **`view`**: Interfaccia grafica (GUI) realizzata in Swing.
  * `Root.java`: Finestra iniziale di login e accesso alla registrazione.
  * `GUICliente.java` / `RegisterCliente.java`: Dashboard e modulo di registrazione del cliente.
  * `GUIAdmin.java` / `GUIRegAdmin.java`: Dashboard e modulo di registrazione dell'amministratore.
*  **`commons`**: Classi di utilità generale.
  * `DBConstants.java`: Query SQL di inizializzazione e costanti del database.
  * `PasswordUtils.java`: Algoritmo di hashing sicuro delle password.
*  **`exceptions`**: Gestione robusta delle eccezioni personalizzate (es. `UtenteNotFoundException`, `BikeNotFoundException`, `AffittoDuplicatoException`).
*  **`test`**: Contiene scenari di test per la verifica delle funzionalità.
  * `Scenario.java`: Script di test per verificare la connessione ed eseguire inserimenti/ricerche di prova sul database.

---

##  Requisiti di Sistema

* **Java JDK**: Versione 8 o superiore.
* **Database**: MySQL Server (consigliato v8.0 o successive).
* **Driver JDBC**: MySQL Connector/J (incluso nella cartella `lib` del progetto).

---

##  Configurazione del Database

L'applicazione è progettata per configurarsi in modo semi-automatico.

1. Avvia il server **MySQL** (es. tramite XAMPP, Docker o installazione nativa) sulla porta `3306`.
2. Modifica il file `config.properties` situato nella directory principale del progetto per configurare le credenziali di accesso al database:
   ```properties
   db.user=root
   db.password=la_tua_password
   db.serverName=127.0.0.1
   db.port=3306
   db.name=database_biciclette_DEF
   db.uri=jdbc:mysql://127.0.0.1:3306
   ```
3. **Inizializzazione Automatica**: All'avvio dell'applicazione, il controller `Database.java` verificherà la presenza del database e delle tabelle necessarie. Se non esistono, verranno creati automaticamente tramite i comandi DDL definiti in `DBConstants`.

### Schema Relazionale del Database
Il database è composto dalle seguenti tabelle relazionali:
* **`biciclette`**: Memorizza i modelli di biciclette, le loro tariffe e gli equipaggiamenti associati.
* **`clienti`**: Contiene le anagrafiche dei clienti con password cifrate.
* **`admin`**: Contiene gli account degli amministratori del sistema.
* **`affitti`**: Traccia i noleggi attivi e conclusi (collegando clienti, biciclette e dettagli del tragitto).
* **`pagamenti`**: Registra le transazioni finanziarie collegate ad ogni noleggio.

---

##  Come Eseguire l'Applicazione

### Da IDE (Eclipse / IntelliJ IDEA)
1. Importa la cartella `ProgettoGautieriGiuseppe` come progetto Java esistente nel tuo IDE.
2. Assicurati che la libreria JDBC (`mysql-connector-j-*.jar`) all'interno della cartella `lib` sia correttamente aggiunta al **Build Path** del progetto.
3. Esegui la classe **`view.Root.java`** come *Java Application*. Si aprirà la finestra di login dell'interfaccia grafica.

### Verifica del Database (Test Rapido)
Puoi eseguire la classe **`test.Scenario.java`** per verificare che la connessione al database e le query di inserimento funzionino correttamente senza dover avviare l'interfaccia grafica completa.
