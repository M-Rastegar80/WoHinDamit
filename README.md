# WoHinDamit? — Shared project

The shared application is in `backend/`, based on the local Noushin backend snapshot from 12 October 2026. The active frontend is `backend/src/main/resources/static/`, served by Spring Boot at http://localhost:8080/.

Questions and recommendations come from the backend API. Mani's green background and typography are retained with responsive forms. Original frontend files are preserved in `reference/mani-frontend/`. The previous technical preview remains at `/preview/index.html`.

- [Persian comparison and next steps](docs/comparison-fa.md)
- [Project handoff for Chat, Work and future sessions](docs/PROJECT-HANDOFF-2026-10-15.md)
- [Setup, password prompt and optional demo data](backend/docs/local-preview.md)

## Start

Open PowerShell in `backend/` and follow the password prompt and `mvnw.cmd spring-boot:run` instructions in the setup guide. Requires a compatible JDK, PostgreSQL on localhost:5432, the local development database `wohin_damit`, and `DB_PASSWORD` entered locally. The application uses Hibernate `ddl-auto=update` and targets a development database.

Open http://localhost:8080/. Categories load automatically. Select a category, answer the questions and request a recommendation. GitHub Pages or opening the HTML directly does not run the Java backend.

## Scope and provenance

Integrated: categories, dynamic questions/options, typed answers, backend decisions, loading/error/empty states. Pending: item search, item-specific decisions, action-aware suitable locations, maps and verified Vienna data. Both demo questions remain required by the backend contract.

Frontend base: `6d4f19f` in M-Rastegar80/WoHinDamit. Backend original source: https://github.com/n00shin/WoHinDamit at local base `4a1f1e3`. Local preview/demo additions: `727dd37d0a25b26f4c7b90857f864351f46b6ce4`, previously published on https://github.com/M-Rastegar80/WoHinDamit/tree/preview/local-api-demo.

Backend source is copied into `backend/`; its original Git history remains on the preview branch. This branch retains the frontend Git history. No changes are pushed to Noushin's repository.

## Original project introduction

Smart Recycling Vienna
WoHinDamit? ist eine Webanwendung, die Nutzerinnen und Nutzern in Wien hilft, Gegenstände sinnvoll witer zu erwenden, zu reparieren, zu spenden oder richtig zu entsorgen.

## Projekt

Diplomprojekt / Abschlussprojekt
HTL Wien West - Kolleg für Informatik

## Team

- Mohamad Rastegar Moghadam
- Noushin Azimi

## Status

Projektstart: Oktober 2026
