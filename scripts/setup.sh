#!/bin/bash

# Script de automatización para configurar proyecto examen DIS

echo "🚀 Setup Script - Examen DIS"
echo "==============================="

# Configurar proyecto backend
setup_backend() {
    echo "
🛠️  Configurando Backend..."
    
    if [ ! -d "backend" ]; then
        echo "⚠️  Directorio backend no encontrado"
        echo "Genera el proyecto desde start.spring.io y extraé lo aquí"
        return 1
    fi
    
    cd backend
    
    # Verificar que exista pom.xml
    if [ ! -f "pom.xml" ]; then
        echo "❌ pom.xml no encontrado"
        return 1
    fi
    
    # Crear estructura de directorios
    mkdir -p src/main/java/com/exam/model
    mkdir -p src/main/java/com/exam/service
    mkdir -p src/main/java/com/exam/controller
    mkdir -p src/main/resources
    mkdir -p src/test/java/com/exam/controller
    
    echo "✅ Estructura de directorios creada"
    
    # Verificar dependencies
    if grep -q "spring-boot-starter-web" pom.xml; then
        echo "✅ spring-boot-starter-web encontrado"
    else
        echo "⚠️  Falta spring-boot-starter-web"
    fi
    
    if grep -q "gson" pom.xml; then
        echo "✅ gson encontrado"
    else
        echo "⚠️  Falta gson"
    fi
    
    cd ..
}

# Configurar proyecto frontend
setup_frontend() {
    echo "
🎨 Configurando Frontend..."
    
    if [ ! -d "frontend" ]; then
        echo "⚠️  Directorio frontend no encontrado"
        echo "Genera el proyecto desde start.vaadin.com y extraé lo aquí"
        return 1
    fi
    
    cd frontend
    
    # Crear estructura de directorios
    mkdir -p src/main/java/com/exam/dto
    mkdir -p src/main/java/com/exam/services
    mkdir -p src/main/java/com/exam/views
    
    echo "✅ Estructura de directorios creada"
    
    cd ..
}

# Inicializar Git
setup_git() {
    echo "
🔀 Configurando Git..."
    
    if [ ! -d ".git" ]; then
        git init
        echo "✅ Repositorio Git inicializado"
    else
        echo "ℹ️  Repositorio Git ya existe"
    fi
    
    # Crear branch develop si no existe
    if ! git show-ref --verify --quiet refs/heads/develop; then
        git checkout -b develop
        echo "✅ Branch develop creado"
    else
        echo "ℹ️  Branch develop ya existe"
    fi
}

# Menú interactivo
if [ "$1" == "backend" ]; then
    setup_backend
elif [ "$1" == "frontend" ]; then
    setup_frontend
elif [ "$1" == "git" ]; then
    setup_git
elif [ "$1" == "all" ]; then
    setup_git
    setup_backend
    setup_frontend
else
    echo "Uso: ./setup.sh [backend|frontend|git|all]"
    echo ""
    echo "Opciones:"
    echo "  backend  - Configura estructura del backend"
    echo "  frontend - Configura estructura del frontend"
    echo "  git      - Inicializa Git y crea branch develop"
    echo "  all      - Ejecuta todas las configuraciones"
fi

echo ""
echo "✅ Setup completado"
