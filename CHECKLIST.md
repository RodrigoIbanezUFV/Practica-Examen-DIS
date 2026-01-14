# ✅ CHECKLIST EXAMEN DIS (2 horas)

## 📋 ANTES DE EMPEZAR (5 min)
- [ ] Leer enunciado completo 2 veces
- [ ] Identificar entidad principal y campos
- [ ] Verificar si pide Docker o solo backend+frontend
- [ ] Anotar endpoints requeridos
- [ ] Crear timer/alarma para cada fase

## 🔧 SETUP INICIAL (10 min)
### Git
```bash
git clone <URL_EXAMEN>
cd <repo>
git checkout -b develop
git push -u origin develop
git checkout -b feature/backend-setup
```

### Backend (Spring Boot)
- [ ] Crear proyecto Spring Boot 3.5.0 + Java 17
- [ ] Dependencies: Web, DevTools
- [ ] Copiar `pom.xml` desde template y adaptar
- [ ] Crear `application.properties` con `server.port=8080`
- [ ] Estructura: `model/`, `service/`, `controller/`
- [ ] Commit: "Initial backend setup"

## 🧪 TDD - TESTS PRIMERO (20 min)
- [ ] Crear `<Entidad>ControllerTest.java`
- [ ] Test: GET list (200)
- [ ] Test: GET by id OK (200)
- [ ] Test: GET by id NOT FOUND (404)
- [ ] Test: POST create (201)
- [ ] Test: PUT update (200)
- [ ] Test: DELETE (204)
- [ ] Commit: "Add controller tests"
- [ ] **Verificar que todos fallen con `mvn test`**

## 💾 BACKEND IMPLEMENTATION (30 min)
### Modelo y Datos
- [ ] Crear POJO `<Entidad>.java` con todos los campos
- [ ] Añadir `@SerializedName` si JSON usa snake_case
- [ ] Crear `<entidades>.json` en `src/main/resources/`
- [ ] Crear clase wrapper `FileContents` con `List<Entidad>`
- [ ] Commit: "Add model and JSON data"

### Service
- [ ] Crear `<Entidad>Service.java`
- [ ] Método `loadData()` con Gson para leer JSON
- [ ] CRUD: `getAll()`, `getById()`, `create()`, `update()`, `delete()`
- [ ] Commit: "Add service layer"

### Controller
- [ ] Crear `<Entidades>Controller.java`
- [ ] `@RestController` + `@RequestMapping("/api/<entidades>")`
- [ ] **IMPORTANTE: `@CrossOrigin(origins = "*")`**
- [ ] GET `/` → list
- [ ] GET `/{id}` → by id
- [ ] POST `/` → create
- [ ] PUT `/{id}` → update
- [ ] DELETE `/{id}` → delete
- [ ] Commit: "Add REST controller"

### Verificación Backend
- [ ] `mvn test` → **TODOS LOS TESTS EN VERDE** ✅
- [ ] `mvn spring-boot:run` → probar en navegador/Postman
- [ ] Merge feature/backend-setup → develop

## 🎨 FRONTEND (30 min)
### Setup
- [ ] Crear proyecto Vaadin 25 desde start.vaadin.com
- [ ] Copiar `pom.xml` desde template
- [ ] `server.port=8081` en application.properties
- [ ] Structure: `dto/`, `services/`, `views/`
- [ ] Commit: "Initial frontend setup"

### Implementation
- [ ] Crear DTO (copiar del backend o simplificar)
- [ ] Crear `BackendService.java`:
  - RestTemplate con base URL `http://localhost:8080/api/<entidades>`
  - Métodos: `getAll()`, `getById()`, `create()`, `update()`, `delete()`
- [ ] Crear `MainView.java`:
  - `@Route("")`
  - `extends VerticalLayout`
  - `@Autowired BackendService`
  - `Grid<DTO>` con columnas
  - Botones si es necesario
- [ ] Commit: "Add main view with grid"

### Verificación Frontend
- [ ] Backend corriendo en 8080
- [ ] `mvn spring-boot:run` en frontend
- [ ] Abrir http://localhost:8081
- [ ] Grid muestra datos correctamente
- [ ] Merge feature/frontend-setup → develop

## 🐳 DOCKER (Si requerido - 15 min)
- [ ] Dockerfile backend (multi-stage)
- [ ] Dockerfile frontend (multi-stage)
- [ ] docker-compose.yml
- [ ] `docker compose up --build`
- [ ] Verificar servicios en navegador
- [ ] Commit: "Add Docker support"

## 🔄 GITFLOW FINAL (10 min)
- [ ] `git checkout develop`
- [ ] Verificar que todo está mergeado
- [ ] `git checkout -b release/v1.0.0`
- [ ] Merge release → develop
- [ ] Merge release → main
- [ ] `git tag -a v1.0.0 -m "Release v1.0.0"`
- [ ] `git push --all`
- [ ] `git push --tags`

## ✅ VERIFICACIÓN FINAL (5 min)
- [ ] `mvn test` en backend → verde
- [ ] Backend corre en 8080
- [ ] Frontend corre en 8081 y muestra datos
- [ ] Docker (si aplica) levanta todos los servicios
- [ ] Git: branches develop, main, release, features
- [ ] Git: tag v1.0.0
- [ ] GitHub: todo pusheado

## ⚠️ ERRORES COMUNES A EVITAR
- ❌ Olvidar `@CrossOrigin` en controller
- ❌ Puertos incorrectos (8080/8081)
- ❌ No hacer TDD (tests primero)
- ❌ Olvidar push de tags
- ❌ No verificar que `mvn test` pasa
- ❌ Scope incorrecto en dependencies (`web` no existe, usar `test`)
- ❌ No incluir Gson en pom.xml

---
**TIEMPO TOTAL: ~120 min (2 horas)**

**RESERVA:** 5 min buffer para imprevistos
