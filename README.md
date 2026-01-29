# IoT Geofence Monitor

Microservicio Spring Boot 3 para monitoreo de dispositivos IoT mediante geofencing. Detecta cuando un dispositivo sale de zonas geográficas predefinidas y genera alertas en tiempo real.

## 📋 Descripción

Este sistema permite:
- **Definir zonas geográficas circulares** (geofences) con centro y radio
- **Recibir señales de dispositivos IoT** con ubicación GPS
- **Detectar violaciones** cuando un dispositivo sale de una zona
- **Generar alertas** y notificar al propietario del dispositivo

---

## 🏗️ Arquitectura

El proyecto sigue **Arquitectura Hexagonal (Ports & Adapters)** con separación estricta de capas:

```
┌─────────────────────────────────────────────────────────────────┐
│                     INFRASTRUCTURE                               │
│  ┌──────────────────┐  ┌──────────────────┐  ┌───────────────┐  │
│  │   REST Layer     │  │   Persistence    │  │   External    │  │
│  │  (Controllers)   │  │   (H2 Adapters)  │  │   (Mocks)     │  │
│  └────────┬─────────┘  └────────┬─────────┘  └───────┬───────┘  │
└───────────┼─────────────────────┼────────────────────┼──────────┘
            │                     │                    │
            ▼                     ▼                    ▼
┌─────────────────────────────────────────────────────────────────┐
│                      APPLICATION                                 │
│  ┌──────────────────────────┐  ┌─────────────────────────────┐  │
│  │      Input Ports         │  │       Output Ports          │  │
│  │   (Use Cases)            │  │   (Repository Interfaces)   │  │
│  └────────────┬─────────────┘  └──────────────┬──────────────┘  │
│               │           SERVICES            │                  │
│               └───────────────────────────────┘                  │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        DOMAIN                                    │
│  ┌────────────┐ ┌────────────┐ ┌────────────┐ ┌─────────────┐   │
│  │  Location  │ │   Zone     │ │   Alert    │ │ DeviceSignal│   │
│  │  (Record)  │ │ (Abstract) │ │  (Value)   │ │  (Record)   │   │
│  └────────────┘ └────────────┘ └────────────┘ └─────────────┘   │
│                 ┌────────────────┐                               │
│                 │  CircularZone  │                               │
│                 │  (Haversine)   │                               │
│                 └────────────────┘                               │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🚀 API REST

### Señales de Dispositivo

```http
POST /api/v1/signals
Content-Type: application/json

{
  "deviceId": "test-device",
  "latitude": 40.416775,
  "longitude": -3.703790,
  "timestamp": "2026-01-29T10:00:00Z"
}
```

**Respuesta**: `200 OK`

### Crear Zona

```http
POST /api/v1/zones
Content-Type: application/json

{
  "name": "Madrid Centro",
  "latitude": 40.416775,
  "longitude": -3.703790,
  "radius": 1000.0
}
```

**Respuesta**: `201 Created`

---

## ⚙️ Tecnologías

| Componente | Tecnología |
|------------|------------|
| Framework | Spring Boot 3.5 |
| Java | 21 (LTS) |
| Base de Datos | H2 (In-Memory) |
| ORM | Spring Data JPA + Hibernate |
| Validación | Jakarta Validation |
| Testing | JUnit 5 + Mockito + AssertJ |

---

## 🏃 Ejecución

```bash
# Compilar
./mvnw clean compile

# Ejecutar tests
./mvnw test

# Iniciar aplicación
./mvnw spring-boot:run
```

**H2 Console**: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- User: `sa`
- Password: `password`

---

## 📦 Estructura de Paquetes

```
com.burutech.iotgeofencemonitor
├── domain
│   └── model          # Entidades de dominio puras
├── application
│   ├── ports
│   │   ├── input      # Casos de uso (interfaces)
│   │   └── output     # Puertos de salida (interfaces)
│   └── service        # Implementación de casos de uso
└── infrastructure
    ├── rest           # Controllers y DTOs
    ├── persistence    # Entidades JPA y Adapters
    └── external       # Servicios externos (simulados)
```

---

## 📝 Flujo de Negocio

1. **Dispositivo envía señal** con ubicación GPS
2. **Sistema carga zonas** desde base de datos
3. **Para cada zona**: calcula distancia usando **fórmula Haversine**
4. **Si está fuera de zona**:
   - Busca propietario del dispositivo
   - Crea alerta
   - Persiste alerta en BD
   - Envía notificación

---

## 🧪 Testing

- **Unit Tests**: CircularZoneTest (dominio), GeofenceMonitorServiceTest (servicio)
- **Integration Tests**: DeviceSignalControllerIntegrationTest (flujo completo)

```bash
./mvnw test
```

---

## 📄 Licencia

Proyecto desarrollado con fines educativos.
