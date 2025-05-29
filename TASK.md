#System rekrutacji i zarządzania kandydatami w firmie HR – Zadania
]
Dodatkowe kontrolery REST API:
- wyswietlenie ofert pracy dla danej firmy (companyName)
- wyswietlenie ofert pracy dla konkretnej lokacji (location)
- wyswietlenie ofert pracy utworzonych przez konkretnego uzytkownika (createdBy)

Candidate.getApplications() - wyswietlenie aplikacji do ofert pracy dla danego kandydata

jobOffer.getApplications() - wyswietlenie aplikacji dla danej oferty pracy

Dodać logikę blokującą możliwość tworzenia ofert pracy przez Usera 
(lub dać użytkownikowi wybór przy tworzeniu konta czy chce być rekrutantem czy aplikującym)

========================================================================================================================

1. OGÓLNA STRUKTURA PROJEKTU

1.1. Warstwa prezentacji (Controllers):  
    • AuthController (do rejestracji i logowania)  
    • UserController (zarządzanie danymi użytkowników – głównie przez rolę ADMIN/HR)  
    • JobOfferController (zarządzanie ofertami pracy)  
    • ApplicationController (zarządzanie aplikacjami kandydatów)  
    • CandidateController (opcjonalnie, jeśli chcesz osobno obsługiwać encję Candidate)

1.2. Warstwa logiki biznesowej (Services):  
   • AuthService (logika rejestracji i logowania)  
   • UserService (obsługa CRUD użytkowników)  
   • JobOfferService (obsługa CRUD ofert pracy)  
   • ApplicationService (obsługa aplikowania i przeglądu aplikacji)  
   • CandidateService (obsługa CRUD kandydata)

1.3. Warstwa dostępu do danych (Repositories):  
   • UserRepository  
   • JobOfferRepository  
   • ApplicationRepository  
   • CandidateRepository

1.4. Warstwa modelu/encji (Entities) + ewentualne DTO:  
   • User, Candidate, JobOffer, Application, (UserRole i ApplicationStatus jako Enum).  
   • Alternatywnie: do komunikacji przez kontroler możesz używać odrębnych klas DTO (np. UserRequestDTO, UserResponseDTO), aby uniknąć bezpośredniej ekspozycji encji.

1.5. Konfiguracja Spring Security:  
   • Klasa konfiguracyjna: SecurityConfig – z @EnableWebSecurity lub Spring Boot starter.  
   • Mechanizm autentykacji (np. JWT lub session-based).  
   • Kontrola dostępu (Role-Based Access Control) na poziomie metod lub endpointów.

1.6. Baza danych:  
   • PostgreSQL (uruchomiony w Dockerze).  
   • Konfiguracja w application.properties + migracje bazy w Flyway/Liquibase.

1.7. Dokumentacja:  
   • OpenAPI/Swagger (Springdoc).  
   • README.md z opisem uruchomienia, screenami i informacją o coverage testów.

1.8. Testy:  
   • JUnit + Mokito (testy jednostkowe i integracyjne).  
   • Uruchamiane przez Maven (np. mvn test).  
   • JaCoCo (lub inny) do raportowania pokrycia testami (co najmniej 80%).


2. PRZEPŁYW LOGIKI – WYBRANE OPERACJE

2.1. Tworzenie konta (rejestracja)  
   • W AuthController udostępniasz endpoint POST /api/auth/register.  
   • Przyjmuje np. AuthRegisterRequestDTO z polami (username, password, loginEmail, role).  
   • AuthService rejestruje nowego użytkownika:  
   – Walidacja (np. Bean Validation).  
   – Sprawdzenie, czy user o podanym email/username już istnieje.  
   – Hashowanie hasła (BCryptPasswordEncoder).  
   – Zapis w tabeli users.  
   – (opcjonalnie) Jeśli role=CANDIDATE, możesz od razu utworzyć powiązanego kandydata.  
   • Zwraca np. UserResponseDTO (ID, username, role, itp.).

2.2. Logowanie  
   • W AuthController endpoint POST /api/auth/login.  
   • AuthLoginRequestDTO (loginEmail i password).  
   • AuthService sprawdza dane, jeśli poprawne – wydaje JWT lub tworzy sesję.  
   • W odpowiedzi np. token JWT (AuthLoginResponseDTO).

2.3. Tworzenie ofert pracy (dostępne dla roli HR)  
   • W JobOfferController endpoint POST /api/job-offers.  
   • Ścieżka chroniona (np. @PreAuthorize("hasRole('HR')")).  
   • JobOfferDTO zawiera dane oferty (tytuł, opis, location, itp.).  
   • W kodzie JobOfferService:  
   – Tworzysz obiekt JobOffer.  
   – Ustawiasz createdBy = aktualnie zalogowany user (pobierasz go z SecurityContextHolder lub tokena JWT).  
   – jobOfferRepository.save(…)  
   • Zwracasz ID oferty czy całą ofertę.

2.4. Aplikowanie na oferty pracy (dostępne dla roli CANDIDATE)  
   • W ApplicationController endpoint POST /api/applications.  
   • Przykładowo ApplicationDTO zawiera jobOfferId i ewentualnie notatkę.  
   • Serwis ApplicationService:  
   – Pobiera z bazy usera (aktualnie zalogowanego, rola CANDIDATE).  
   – Powiązaną encję Candidate (o ile w Twoim modelu tak to jest rozwiązane).  
   – Pobiera jobOffer z bazy – sprawdza, czy isActive = true i czy data nie jest przeterminowana.  
   – Tworzy Application (status = APPLIED, appliedAt = LocalDateTime.now() itd.).  
   – Zapisuje w bazie.

2.5. Przegląd aplikacji  

(1) Dla kandydata:  
   • GET /api/applications?candidateId=… (lub /api/candidates/{id}/applications).  
   – Zwracasz listę Application, ewentualnie sparametryzowaną (np. status=INTERVIEW).

(2) Dla HR:  
   • GET /api/job-offers/{offerId}/applications – pozwala rekruterowi podejrzeć wszystkie zgłoszenia na daną ofertę.  
   (lub /api/applications?jobOfferId=…)

Można też dodać metody do zmiany statusu aplikacji (np. z APPLIED na REJECTED lub HIRED), zapewniając że tylko HR to może zrobić.


3. JAK ZAPEWNIĆ WALIDACJĘ I POPRAWNE WYŚWIETLANIE DANYCH

3.1. Walidacja danych  
   • Bean Validation (Hibernate Validator) + adnotacje @NotNull, @Size, @Email, itp. w DTO.  
   • Np. w AuthRegisterRequestDTO:

   public class AuthRegisterRequestDTO {
   @NotNull
   @Size(min=3, max=50)
   private String username;
       @NotNull
       @Size(min=6, max=100)
       private String password;

       @NotNull
       @Email
       private String loginEmail;

       @NotNull
       private UserRole role;

       // get/set
   }

• Gdy walidacja się nie powiedzie, Spring wyrzuci MethodArgumentNotValidException. 
  Można podpiąć @ControllerAdvice, żeby ładnie sformatować błąd w JSON.

3.2. Poprawne wyświetlanie i unikanie pętli w JSON (relacje w JPA)  
   • Możesz użyć adnotacji z Jacksona: @JsonIgnore, @JsonManagedReference, @JsonBackReference, @JsonIdentityInfo.  
   • Alternatywnie – i to jest polecane w projektach – zamiast oddawać w Controllerze bezpośrednio encje JPA, używasz DTO.  
   – Np. UserResponseDTO (zawiera id, username, role, etc.) bez listy JobOffer.  
   – JobOfferResponseDTO (bez usera w stylu Cyclic JSON).  
   • Dzięki temu unikniesz problemów z serializacją i pętlą Cyclic reference.

4. ZASTOSOWANIE ZASAD SOLID I WZORCÓW PROJEKTOWYCH
    - Single Responsibility (każda warstwa i każda klasa ma jasno określony cel: np. serwis AuthService tylko do rejestracji/logowania).
    - Open/Closed (kod jest otwarty na rozszerzenia, a zamknięty na modyfikacje – np. możesz wprowadzać kolejne role, stany aplikacji, nie zmieniając fundamentów).
    - Liskov Substitution (jeśli stworzysz np. abstrakcję IUser i klika implementacji).
    - Interface Segregation (DTO i serwisy nie powinny narzucać zależności, których nie potrzebują).
    - Dependency Inversion (używasz serwisów + interfejsów repository i wstrzykujesz je do kontrolerów).

   Z uwagi na wymaganie polimorfizmu i przynajmniej jednego wzorca projektowego:
   • Polimorfizm – np. w warstwie serwisu możesz mieć różne implementacje jednej logiki w zależności od roli (CANDIDATE vs. HR).  
   • Możesz też wprowadzić dziedziczenie w encjach (User -> Candidate i HRUser) – to przykładowy polimorfizm w JPA, choć bywa dyskusyjny.  
   • Popularny wzorzec projektowy:  
   – „Factory method” przy tworzeniu obiektów (np. ApplicationFactory, która tworzy Application z parametrami).  
   – Strategia (np. przy różnych politykach liczenia statystyk rekrutacyjnych).  
   – Builder (jeśli masz obiekty z wieloma polami, np. JobOfferBuilder).

   Dobrym przykładem będzie chociażby builder w prostych modelach DTO/Entity lub strategia do obsługi różnych wariantów rejestracji/logowania (np. rejestracja z potwierdzeniem maila vs. rejestracja uproszczona).

5. DOCKER
   • Dockerfile dla aplikacji Spring Boot (jar).  
   FROM openjdk:17-jdk-alpine  
   COPY target/TwojaAplikacja.jar app.jar  
   ENTRYPOINT ["java","-jar","/app.jar"]  
   • docker-compose.yml, w którym uruchamiasz:  
   – Aplikację (z Dockerfile)  
   – PostgreSQL (łącznie z siecią i zmiennymi POSTGRES_DB, POSTGRES_USER, POSTGRES_PASSWORD).  
   • Możesz też w Dockerfile uruchomić migracje Flyway, jeśli chcesz (lub w samej aplikacji, automatycznie przy starcie).

6. MIGRACJE BAZY (FLYWAY/LIQUIBASE)
   • W pliku application.properties:  
   spring.flyway.locations=classpath:db/migration  
   • Każda migracja w stylu V1__Create_tables.sql, V2__Add_index_user_email.sql, itd.  
   • Przy starcie aplikacji Flyway wykona wszelkie niezbędne skrypty SQL.

7. TESTY (JUnit/Mockito)  
   ────────────────────────────────────────────────────────────────────────
   • Testy jednostkowe warstwy serwisu (np. AuthServiceTest, UserServiceTest).  
   • Testy integracyjne (z wykorzystaniem @SpringBootTest i wpiętej bazy testowej, np. testcontainers).  
   • W raporcie JaCoCo monitorujesz, czy osiągasz >80% coverage.

8. READMe + DIAGRAM BAZY
   • W pliku README.md:  
   – Opis instalacji i uruchomienia (docker-compose up).  
   – Opis endpointów (lub link do swaggera: /swagger-ui.html czy /swagger-ui/index.html).  
   – Screeny z działania (np. testy w Postmanie, SwaggerUI).  
   – Instrukcja kontrybucji, doc info.  
   • Diagram ERD:  
   – Czytelny rysunek przedstawiający tabele (users, candidates, job_offers, applications) i ich relacje.


────────────────────────────────────────────────────────────────────────
PODSUMOWANIE
────────────────────────────────────────────────────────────────────────
Proponowana architektura i przepływ powinny zaspokoić wymogi:  
• Dwa typy użytkowników (CANDIDATE, HR), co spełnia RBAC.  
• Polimorfizm (np. implementacje strategii lub rozdzielenie na różne role).  
• Wzorzec projektowy (np. Builder, Factory Method).  
• Docker (aplikacja + db).  
• Spring Boot + Security (logowanie, rejestracja, role).  
• Swagger (dokumentacja, testy endpointów).  
• JPA z PostgreSQL, migracje (Flyway/Liquibase).  
• Testy z pokryciem >80%.  
• Czytelny kod + SOLID.

W takiej formie użytkownik będzie mógł:
1) Zarejestrować się (AuthController).
2) Zalogować do aplikacji (AuthController) – otrzymać JWT/sesję.
3) Jeśli jest HR, tworzyć i edytować oferty (JobOfferController).
4) Jeśli jest CANDIDATE, aplikować na oferty (ApplicationController).
5) Administrator/HR może weryfikować listę aplikacji, zmieniać ich status itp.
6) Jednocześnie mamy osobny endpoint do pobierania listy użytkowników (UserController) lub usuwania kont (deleteUser).

Całość spina się w jedną, dobrze uporządkowaną aplikację, którą można rozwijać o kolejne funkcjonalności (np. obsługa CV, wysyłanie maili, integracja z zewnętrznymi serwisami).

