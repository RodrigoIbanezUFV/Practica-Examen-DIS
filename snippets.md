# 📝 SNIPPETS DE CÓDIGO REUTILIZABLES

## 📓 IMPORTS BACKEND

### Controller
```java
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
```

### Service con Gson
```java
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
```

### Tests
```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
```

## 🌎 CONTROLLER SKELETON
```java
@RestController
@RequestMapping("/api/entidades")
@CrossOrigin(origins = "*")
public class EntidadesController {
    
    @Autowired
    private EntidadService service;
    
    @GetMapping
    public ResponseEntity<List<Entidad>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Entidad> getById(@PathVariable Long id) {
        Optional<Entidad> entidad = service.getById(id);
        return entidad.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Entidad> create(@RequestBody Entidad entidad) {
        Entidad created = service.create(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Entidad> update(@PathVariable Long id, @RequestBody Entidad entidad) {
        Optional<Entidad> updated = service.update(id, entidad);
        return updated.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

## 🛠️ SERVICE SKELETON (JSON)
```java
@Service
public class EntidadService {
    private List<Entidad> entidades;
    private Long nextId = 1L;
    
    @PostConstruct
    public void loadData() {
        Gson gson = new Gson();
        InputStream is = getClass().getResourceAsStream("/entidades.json");
        InputStreamReader reader = new InputStreamReader(is);
        FileContents contents = gson.fromJson(reader, FileContents.class);
        this.entidades = new ArrayList<>(contents.getItems());
        // Calculate next ID
        if (!entidades.isEmpty()) {
            nextId = entidades.stream().mapToLong(Entidad::getId).max().orElse(0L) + 1;
        }
    }
    
    public List<Entidad> getAll() {
        return entidades;
    }
    
    public Optional<Entidad> getById(Long id) {
        return entidades.stream()
            .filter(e -> e.getId().equals(id))
            .findFirst();
    }
    
    public Entidad create(Entidad entidad) {
        entidad.setId(nextId++);
        entidades.add(entidad);
        return entidad;
    }
    
    public Optional<Entidad> update(Long id, Entidad updated) {
        for (int i = 0; i < entidades.size(); i++) {
            if (entidades.get(i).getId().equals(id)) {
                updated.setId(id);
                entidades.set(i, updated);
                return Optional.of(updated);
            }
        }
        return Optional.empty();
    }
    
    public void delete(Long id) {
        entidades.removeIf(e -> e.getId().equals(id));
    }
    
    // Inner class for Gson deserialization
    private static class FileContents {
        @SerializedName("items")
        private List<Entidad> items;
        
        public List<Entidad> getItems() {
            return items;
        }
    }
}
```

## 🧪 TEST CLASS SKELETON
```java
@SpringBootTest
@AutoConfigureMockMvc
public class EntidadControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get("/api/entidades"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }
    
    @Test
    public void testGetByIdOk() throws Exception {
        mockMvc.perform(get("/api/entidades/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1));
    }
    
    @Test
    public void testGetByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/entidades/9999"))
            .andExpect(status().isNotFound());
    }
    
    @Test
    public void testCreate() throws Exception {
        String json = "{\"name\":\"Test\"}";
        mockMvc.perform(post("/api/entidades")
            .contentType("application/json")
            .content(json))
            .andExpect(status().isCreated());
    }
}
```

## 🎨 VAADIN FRONTEND

### MainView
```java
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.PostConstruct;

@Route("")
public class MainView extends VerticalLayout {
    
    @Autowired
    private BackendService service;
    
    private Grid<Entidad> grid;
    
    @PostConstruct
    public void init() {
        grid = new Grid<>(Entidad.class);
        grid.setColumns("id", "name", "description");
        grid.setItems(service.getAll());
        add(grid);
    }
}
```

### BackendService
```java
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

@Service
public class BackendService {
    private final String API_URL = "http://localhost:8080/api/entidades";
    private final RestTemplate restTemplate = new RestTemplate();
    
    public List<Entidad> getAll() {
        Entidad[] array = restTemplate.getForObject(API_URL, Entidad[].class);
        return Arrays.asList(array != null ? array : new Entidad[0]);
    }
    
    public Entidad getById(Long id) {
        return restTemplate.getForObject(API_URL + "/" + id, Entidad.class);
    }
}
```

## 📋 application.properties

### Backend (8080)
```properties
server.port=8080
spring.application.name=backend
```

### Frontend (8081)
```properties
server.port=8081
spring.application.name=frontend
vaadin.productionMode=false
```
