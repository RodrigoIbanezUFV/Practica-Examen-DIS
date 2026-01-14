# Práctica-Examen-DIS #
---
Todo lo necesario para poder practicar todos los conceptos que forman parte de la asignatura, junto con los procesos que se van realizando a lo largo de todas las prácticas de la asignatura así como ejemplos y explicaciones que puedan ser necesarias de cara a la plena realización de un ejercicio 100% funcional con el contenido mínimo dado.
---

## ESQUEMA TEMPORAL (2 HORAS)

```
⏱️ 0:00-0:05 → GitFlow inicial (5 min)
⏱️ 0:05-0:45 → TDD + Backend (40 min)  
⏱️ 0:45-1:25 → Frontend Vaadin (40 min)
⏱️ 1:25-1:50 → Docker (25 min)
⏱️ 1:50-2:00 → Release + Push final (10 min)
```

---

## PARTE 1: GITFLOW (Hazlo PRIMERO - 5 min)

### Setup inicial
```bash
# 1. Clonar repositorio del examen  
git clone [URL-DEL-EXAMEN]
cd [nombre-proyecto]

# 2. Crear rama develop
git checkout -b develop
git push -u origin develop

# 3. Crear primera feature para setup
git checkout -b feature/setup-inicial
```

### Comandos GitFlow durante el examen

#### Para cada funcionalidad nueva:
```bash
# Crear feature desde develop
git checkout develop
git checkout -b feature/[nombre-descriptivo]

# Ejemplos comunes:
git checkout -b feature/backend-tdd
git checkout -b feature/backend-crud  
git checkout -b feature/frontend-ui
git checkout -b feature/docker-setup
```

#### Al terminar una feature:
```bash
# Guardar cambios
git add .
git commit -m "[Descripción clara de lo implementado]"

# Volver a develop y mergear
git checkout develop
git merge feature/[nombre]
git push origin develop

# Borrar la feature local (opcional)
git branch -d feature/[nombre]
```

#### Release final (HACER AL FINAL DEL EXAMEN):
```bash
# 1. Crear release desde develop
git checkout develop
git checkout -b release/v1.0.0

# 2. Ajustes finales si los hay
git add .
git commit -m "Release v1.0.0"

# 3. Merge a develop
git checkout develop
git merge release/v1.0.0
git push origin develop

# 4. Merge a main/master
git checkout main  # o 'master' según tu repo
git merge release/v1.0.0
git push origin main

# 5. Crear tag OBLIGATORIO
git tag v1.0.0
git push origin v1.0.0

# 6. Verificar que el tag está en GitHub
git tag  # debe mostrar v1.0.0
git push --tags  # asegura que todos los tags están en remoto
```

### ERRORES COMUNES A EVITAR
- ❌ No hacer commits con mensajes claros
- ❌ Olvidar hacer push de develop antes del release
- ❌ NO crear el tag v1.0.0
- ❌ NO hacer `git push --tags`

---

## PARTE 2: BACKEND SPRING BOOT (40 min)

### Paso 1: Crear proyecto con Spring Initializr

**Opción rápida (recomendada):**
1. Ve a https://start.spring.io/
2. Configuración:
   - **Project**: Maven
   - **Language**: Java  
   - **Spring Boot**: 3.5.0
   - **Java**: 17
   - **Group**: `es.ufv`
   - **Artifact**: `backend`
   - **Packaging**: Jar
3. **Dependencias**: 
   - Spring Web
   - Spring Boot DevTools
4. **Descargar** y descomprimir en carpeta `backend/`

### Paso 2: pom.xml del Backend

Reemplaza el `pom.xml` generado con este:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>es.ufv</groupId>
    <artifactId>backend</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.0</version>
    </parent>
    
    <properties>
        <java.version>17</java.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.13.1</version>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

### Paso 3: application.properties

`backend/src/main/resources/application.properties`:

```properties
spring.application.name=backend
server.port=8080
```

### Paso 4: Crear estructura de paquetes

```bash
cd backend/src/main/java/es/ufv/backend/
mkdir model service controller
cd ../../test/java/es/ufv/backend/
mkdir controller
```

### Paso 5: JSON de datos

`backend/src/main/resources/characters.json`:

```json
{
  "items": [
    {
      "id": "1",
      "name": "Goku",
      "ki": "60000",
      "gender": "Male",
      "affiliation": "Z Fighter"
    },
    {
      "id": "2",
      "name": "Vegeta",
      "ki": "54000",
      "gender": "Male",
      "affiliation": "Z Fighter"
    },
    {
      "id": "3",
      "name": "Piccolo",
      "ki": "45000",
      "gender": "Male",
      "affiliation": "Z Fighter"
    }
  ]
}
```

### Paso 6: TESTS PRIMERO (TDD)

`backend/src/test/java/es/ufv/backend/controller/CharacterControllerTest.java`:

```java
package es.ufv.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CharacterControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void shouldReturnAllCharacters() throws Exception {
        mockMvc.perform(get("/api/characters"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());
    }
    
    @Test
    void shouldReturnCharacterById() throws Exception {
        mockMvc.perform(get("/api/characters/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("1"))
            .andExpect(jsonPath("$.name").exists());
    }
    
    @Test
    void shouldReturn404ForInvalidId() throws Exception {
        mockMvc.perform(get("/api/characters/999"))
            .andExpect(status().isNotFound());
    }
    
    @Test
    void shouldCreateCharacter() throws Exception {
        String json = """
            {
                "name": "Test Character",
                "ki": "5000",
                "gender": "Male"
            }
        """;
        
        mockMvc.perform(post("/api/characters")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Test Character"));
    }
    
    @Test
    void shouldUpdateCharacter() throws Exception {
        String json = """
            {
                "name": "Updated Name",
                "ki": "99999"
            }
        """;
        
        mockMvc.perform(put("/api/characters/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.ki").value("99999"));
    }
    
    @Test
    void shouldDeleteCharacter() throws Exception {
        mockMvc.perform(delete("/api/characters/1"))
            .andExpect(status().isNoContent());
    }
}
```

### Paso 7: Modelo

`backend/src/main/java/es/ufv/backend/model/Character.java`:

```java
package es.ufv.backend.model;

import com.google.gson.annotations.SerializedName;

public class Character {
    
    @SerializedName("id")
    private String id;
    private String name;
    private String ki;
    private String gender;
    private String affiliation;
    
    public Character() {}
    
    public Character(String id, String name, String ki, String gender, String affiliation) {
        this.id = id;
        this.name = name;
        this.ki = ki;
        this.gender = gender;
        this.affiliation = affiliation;
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getKi() { return ki; }
    public void setKi(String ki) { this.ki = ki; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getAffiliation() { return affiliation; }
    public void setAffiliation(String affiliation) { this.affiliation = affiliation; }
    
    @Override
    public String toString() {
        return "Character{id='" + id + "', name='" + name + "', ki='" + ki + "'}";
    }
}
```

`backend/src/main/java/es/ufv/backend/model/FileContents.java`:

```java
package es.ufv.backend.model;

import java.util.List;

public class FileContents {
    private List<Character> items;
    
    public List<Character> getItems() { return items; }
    public void setItems(List<Character> items) { this.items = items; }
}
```

### Paso 8: Servicio

`backend/src/main/java/es/ufv/backend/service/CharacterService.java`:

```java
package es.ufv.backend.service;

import com.google.gson.Gson;
import es.ufv.backend.model.Character;
import es.ufv.backend.model.FileContents;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class CharacterService {
    
    private List<Character> characters = new ArrayList<>();
    
    public CharacterService() {
        loadCharactersFromJson();
    }
    
    private void loadCharactersFromJson() {
        try {
            InputStream is = getClass().getResourceAsStream("/characters.json");
            if (is == null) throw new FileNotFoundException("characters.json not found");
            
            Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            Gson gson = new Gson();
            FileContents data = gson.fromJson(reader, FileContents.class);
            this.characters = new ArrayList<>(data.getItems());
        } catch (Exception e) {
            System.err.println("Error loading JSON: " + e.getMessage());
            this.characters = new ArrayList<>();
        }
    }
    
    public List<Character> findAll() {
        return new ArrayList<>(characters);
    }
    
    public Character findById(String id) {
        return characters.stream()
            .filter(c -> c.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
    
    public Character save(Character character) {
        character.setId(String.valueOf(characters.size() + 1));
        characters.add(character);
        return character;
    }
    
    public Character update(String id, Character updated) {
        Character existing = findById(id);
        if (existing != null) {
            existing.setName(updated.getName());
            existing.setKi(updated.getKi());
            existing.setGender(updated.getGender());
            existing.setAffiliation(updated.getAffiliation());
            return existing;
        }
        return null;
    }
    
    public boolean delete(String id) {
        return characters.removeIf(c -> c.getId().equals(id));
    }
}
```

### Paso 9: Controller

`backend/src/main/java/es/ufv/backend/controller/CharacterController.java`:

```java
package es.ufv.backend.controller;

import es.ufv.backend.model.Character;
import es.ufv.backend.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java
