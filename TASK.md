#System rekrutacji i zarządzania kandydatami w firmie HR – Zadania

## 1. Konfiguracja wstępna

Stworzyć repozytorium Git (np. na GitHub, GitLab) i zainicjować projekt w strukturze Maven.
Dodać plik .gitignore, zawierający m.in. katalog target/, pliki IDE itp.
Przygotować plik README.md z krótkim opisem projektu.
Skonfigurować Dockera (Dockerfile + docker-compose) do uruchamiania aplikacji wraz z bazą PostgreSQL.

## 2. Projekt struktury bazy danych

Opracować model danych (ERD) z uwzględnieniem encji: Kandydat, Oferta, Aplikacja (lub inne potrzebne).
Zdefiniować klucze główne, obce, relacje (1:1, 1:N, N:M – w zależności od potrzeb).
Zapewnić migracje bazy danych (Flyway/Liquibase).

##3. Warstwa aplikacji (Spring Boot)
Skonfigurować plik pom.xml z niezbędnymi zależnościami (Spring Web, Spring Data JPA, Spring Security, PostgreSQL driver,
Springdoc OpenAPI, Flyway/Liquibase itp.).
Stworzyć pakietową strukturę katalogów (controller, service, repository, configuration, model/entity itp.).
Zapewnić hierarchię klas zgodną z zasadami SOLID i paradygmatem OOP.

## 4. Zarządzanie użytkownikami

Skonfigurować Spring Security z obsługą ról (USER, ADMIN).
Zaimplementować rejestrację/logowanie użytkowników (z wykorzystaniem np. JWT lub bazowego mechanizmu session).
Zapewnić dostęp do poszczególnych funkcji w zależności od roli użytkownika.

## 5. Funkcjonalności dla Kandydatów (ROLE_USER)

Rejestracja konta kandydata, uzupełnienie profilu (CV, umiejętności, kontakt).
Przeglądanie dostępnych ofert pracy.
Aplikowanie na wybrane oferty pracy.
Podgląd statusu aplikacji.

## 6. Funkcjonalności dla Rekruterów (ROLE_ADMIN)

Dodawanie/edycja/usuwanie ofert pracy.
Przeglądanie wszystkich aplikacji kandydata.
Dodawanie notatek do aplikacji i zmiana statusu rekrutacji.
Generowanie raportów (np. liczba aplikacji, skuteczność rekrutacji, źródła kandydatów).

## 7. Polimorfizm i wzorzec projektowy

Wybrać i opisać miejsce zastosowania polimorfizmu (np. różne typy raportów, różne implementacje interfejsów).
Zaimplementować co najmniej jeden wzorzec projektowy (np. Strategy, Factory, Observer).

## 8. Testowanie

Napisać testy jednostkowe JUnit dla najważniejszych komponentów (service, controller).
Konfigurować pomiar pokrycia testowego (np. JaCoCo) i potwierdzić osiągnięcie min. 80%.

## 9. Dokumentacja

Udokumentować REST API za pomocą Springdoc OpenAPI (Swagger UI).
Uzupełnić README.md o instrukcje uruchomienia, opis technologii i konfiguracji.
Dołączyć screeny z działania aplikacji (wywołanie endpointów, widok docker-compose, itp.).

## 10. Finalizacja i weryfikacja

Upewnić się, że wszystkie wymogi formalne zostały spełnione (SOLID, rolę użytkownika, polimorfizm, wzorzec projektowy,
Docker, migracje bazy, testy 80%+).
Stworzyć prezentację wynikową (opcjonalnie).