# System rekrutacji i zarządzania kandydatami w firmie HR – Plan

##1. Cel projektu
Celem projektu jest stworzenie aplikacji wspierającej proces rekrutacji w firmie HR. System pozwala na publikację ofert
pracy, rejestrowanie się kandydatów (wraz z możliwością podpięcia CV), zarządzanie zgłoszeniami oraz generowanie
raportów dotyczących skuteczności i przebiegu rekrutacji.

## 2. Zakres funkcjonalny

1. Publikacja i modyfikacja ofert pracy.
2. Rejestracja kandydatów oraz uzupełnianie profilu zawodowego (CV, umiejętności).
3. Aplikowanie kandydatów na wybrane oferty pracy.
4. Zarządzanie procesem rekrutacji (status, dodawanie notatek, itp.).
5. Generowanie raportów (liczba aplikacji, skuteczność rekrutacji, źródła pozyskania kandydatów).
6. Rola użytkownika i rola administratora (HR/Rekruter) – zarządzanie dostępem przy pomocy Spring Security.

## 3. Filar technologiczny

1. Język programowania: Java (zalecane minimum Java 11, preferowane Java 17+).
2. Framework: Spring (Spring Boot, Spring Security, Spring Data JPA).
3. Baza danych: PostgreSQL.
4. ORM: Hibernate.
5. Migracje bazy: Flyway lub Liquibase (wymagane).
6. Testy jednostkowe: JUnit (min. 80% pokrycia kodu, zalecany JaCoCo).
7. Dokumentacja REST API: Springdoc OpenAPI / Swagger UI.
8. Kontrola wersji: Git (z czytelną historią commitów).
9. Konteneryzacja: Docker (projekt musi być uruchamialny w kontenerze).
10. Zarządzanie projektem i zależnościami: Maven (standardowa struktura katalogów, plik pom.xml).

## 4. Wymagania dotyczące architektury i jakości

1. Zastosowanie paradygmatu obiektowości oraz zasad SOLID.
2. Polimorfizm i co najmniej jeden wzorzec projektowy w kodzie.
3. Czytelny i dobrze zorganizowany kod.
4. Dwa typy użytkowników (co najmniej):
   a) Użytkownik (Kandydat) – Role: USER
   b) Administrator (Rekruter/HR) – Role: ADMIN
5. Zarządzanie użytkownikami i uprawnieniami z pomocą Spring Security (RBAC).

## 5. Diagram ERD

Projekt musi zawierać diagram ERD przedstawiający strukturę bazy danych, relacje między tabelami (m.in. Kandydat,
Oferta, Aplikacja, itp.).

## 6. Dokumentacja końcowa

1. Plik README.md z opisem uruchomienia aplikacji, technologiami i instrukcjami konfiguracji.
2. Screeny przedstawiające działanie aplikacji (można dołączyć np. z testów funkcjonalnych UI).
3. Opis pokrycia testami jednostkowymi, opis testów integracyjnych

## 7. Planowane iteracje

1. Projekt i konfiguracja nowego repozytorium Git.
2. Przygotowanie środowiska Docker + PostgreSQL.
3. Implementacja początkowej struktury projektu w oparciu o Spring Boot i standard Maven.
4. Opracowanie modelu danych (ERD) oraz aplikacja do bazy z użyciem ORM (Hibernate).
5. Dodanie Spring Security, zarządzanie rolami i uprawnieniami.
6. Implementacja podstawowych funkcjonalności (publikacja ofert, rejestracja użytkowników).
7. Rozszerzenie funkcjonalności (zgłoszenia kandydatów, zmiany statusów).
8. Wprowadzenie raportów na podstawie zebranych danych.
9. Testy jednostkowe (min. 80% pokrycia).
10. Dokumentacja (README.md, screeny, Swagger UI).