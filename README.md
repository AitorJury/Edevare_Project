# Edevare 🎓 | Fullstack Educational Ecosystem

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)

**Edevare** es una plataforma integral diseñada para optimizar la productividad académica y conectar a la comunidad educativa. Mediante una arquitectura **Fullstack moderna**, la aplicación ofrece herramientas de gestión (Pomodoro, Flashcards, Task Manager) y un ecosistema de comunicación en tiempo real entre alumnos y profesores particulares.

---

## 🏗️ Arquitectura del Sistema

El proyecto destaca por su alta modularidad y reutilización de código mediante una estructura multinivel:

* **`backend`**: API REST robusta desarrollada en **Java 17 con Spring Boot**. Implementa seguridad avanzada con **JWT** y comunicación bidireccional mediante **WebSockets**.
* **`shared` (KMP)**: El núcleo lógico del proyecto. Escrito en **Kotlin Multiplatform**, permite compartir modelos, validaciones y lógica de red entre todas las plataformas, reduciendo drásticamente la duplicidad de código.
* **`frontend`**: Interfaz reactiva desarrollada con **Compose Multiplatform**, permitiendo una experiencia nativa y consistente tanto en **Android** como en **Escritorio (Desktop)** desde una única base de código.

---

## 🛠️ Stack Tecnológico

### Backend (Core & Persistence)
- **Framework:** Spring Boot (Spring Security, Spring Data JPA).
- **Base de Datos:** PostgreSQL con migraciones controladas.
- **ORM:** Hibernate para una gestión eficiente de la persistencia.
- **Real-time:** WebSockets para el módulo de mensajería instantánea.

### Frontend (UI/UX)
- **Multiplataforma:** Kotlin Multiplatform + Compose Multiplatform.
- **Patrón de Arquitectura:** MVVM (Model-View-ViewModel).
- **Gestión de Estado:** Programación reactiva con Compose.

---

## 🧪 Calidad de Software & Metodología

Este proyecto ha sido desarrollado bajo estrictos estándares de calidad:

-   **TDD (Test-Driven Development):** Implementación de la lógica de negocio siguiendo el ciclo Rojo-Verde-Refactor mediante **JUnit 5** y **Mockito**.
-   **Database Migrations:** Control de versiones del esquema de base de datos para garantizar la integridad en todos los entornos.
-   **Seguridad:** Encriptación de contraseñas mediante **BCrypt** (Hash + Salt) y autenticación persistente con **JSON Web Tokens (JWT)**.

---

## 🚀 Funcionalidades Principales

1.  **Marketplace de Profesores:** Buscador avanzado de tutores particulares con sistema de perfiles y tarifas.
2.  **Mensajería Real-time:** Comunicación fluida mediante WebSockets entre alumnos y profesores.
3.  **Suite de Productividad:** * Temporizador Pomodoro integrado.
    * Sistema de Flashcards para estudio activo.
    * Gestor de tareas (To-Do List) con prioridades.

---

## 🔧 Instalación y Despliegue (En desarrollo)

```bash
# Clonar el repositorio
git clone [https://github.com/AitorJury/Edevare_Project.git](https://github.com/AitorJury/Edevare_Project.git)

# Ejecutar el Backend (Requiere JDK 17 y PostgreSQL)
cd backend
./gradlew bootRun

# Ejecutar el Frontend (Android/Desktop)
cd frontend
./gradlew run
