# MviKmpServerExample

Backend HTTP para la app **MviKmpExample**. Expone un **CRUD de operaciones** construido con **Ktor Server**, **Exposed** y **SQLite**.

## Tech stack

| Capa        | Tecnología                            |
|-------------|---------------------------------------|
| Framework   | Ktor Server 3 (Netty)                 |
| Persistencia| Exposed + SQLite (fichero local)      |
| Serialización | kotlinx.serialization (JSON)        |
| Logs        | Logback                               |
| Lenguaje    | Kotlin 2.1 (JVM)                      |

## Endpoints

| Método | Ruta               | Descripción                              |
|--------|--------------------|------------------------------------------|
| GET    | `/`                | Health check                             |
| GET    | `/operaciones`     | Lista todas las operaciones              |
| GET    | `/operaciones/{id}`| Devuelve una operación (404 si no existe)|
| POST   | `/operaciones`     | Crea una operación (201)                 |
| PUT    | `/operaciones/{id}`| Actualiza una operación (404 si no existe)|
| DELETE | `/operaciones/{id}`| Borra una operación (204)                |

### Modelo `Operacion`

```json
{
  "id": 1,
  "titulo": "Transferencia de nómina",
  "descripcion": "Nómina del mes en la cuenta corriente.",
  "imagenUrl": "https://picsum.photos/seed/operacion1/600/400",
  "tipo": "Ingreso",
  "autor": "Finanzas",
  "fechaCreacion": 1786367474047
}
```

### Crear / actualizar

```json
{
  "titulo": "Compra de material",
  "descripcion": "Rotuladores, papel y post-its.",
  "imagenUrl": null,
  "tipo": "Gasto",
  "autor": "Operaciones"
}
```

## Run

```
./gradlew run
```

El servidor escucha en `http://0.0.0.0:8080` y crea el fichero `mvikmp-server.db`
en el directorio de trabajo. Si la base está vacía, inserta 5 operaciones de ejemplo.

## Consumo desde la app

- **Android emulator:** `http://10.0.2.2:8080`
- **iOS simulator:** `http://localhost:8080`
