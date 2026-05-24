# Ghost API Detector

A Spring Boot tool that detects unused ("ghost") REST API endpoints in your backend  by comparing every registered endpoint against real runtime traffic and flagging the ones nobody ever calls.

---

## The problem

As backends grow, endpoints accumulate. Some get replaced, some get forgotten, some were only added for testing. They still start with your server, still consume memory, and still sit in your security surface  but nobody calls them.

You don't know they're there until a security audit flags them, or worse.

Ghost API Detector watches your running application and tells you exactly which endpoints have never been touched.

---

## How it works

Three things happen when you run it:

**1. Discovery**  at startup, it scans your entire Spring application and builds a complete map of every registered endpoint: path, HTTP method, controller name, handler method.

**2. Observation**  every incoming HTTP request is silently recorded by an interceptor. Method + path, deduplicated. This becomes your "actually used" list.

**3. Ghost detection**  call `GET /internal/ghost-apis` at any time to diff the two lists. Anything discovered but never observed is a ghost, returned with tags so you know what kind of endpoint you're looking at.

It also includes a **JAR scanner**  upload any compiled `.jar` and it statically analyzes the bytecode (via ASM) to inventory all Spring controller endpoints without running the application.

---

## Endpoint tags

| Tag | Meaning |
|-----|---------|
| `RISKY` | Sensitive-looking endpoints (POST/DELETE, paths like `/admin`, `/delete`, `/internal`) never called |
| `PUBLIC` | Endpoints with paths suggesting public access |
| `FRAMEWORK` | Spring-internal endpoints (actuator, error handlers, etc.) |

---

## Quickstart

### Prerequisites
- Java 17+
- Maven 3.6+
- An existing Spring Boot application (or use the included test controller)

### Run

```bash
git clone https://github.com/Mohd16kaif/ghost-api-detector.git
cd ghost-api-detector
mvn clean package -DskipTests
java -jar target/Ghost-API-detector-0.0.1-SNAPSHOT.jar
```

The app starts on `http://localhost:8080/ghostapidetector`

---

## API reference

### `GET /internal/ghost-apis`
Returns all discovered endpoints that have never been observed in runtime traffic.

```json
{
  "ghostApis": [
    {
      "path": "/age",
      "httpMethod": "POST",
      "controllerName": "testcontroller",
      "handlerMethodName": "myAge",
      "tags": ["RISKY"]
    }
  ],
  "observedCount": 3,
  "discoveredCount": 12,
  "warning": null
}
```

> If `observedCount` is 0, a warning is included  the list will show every endpoint as a ghost until real traffic is observed.

---

### `GET /internal/observed-apis`
Returns every unique method + path combination observed since startup.

```json
[
  { "httpMethod": "GET", "path": "/api/test" }
]
```

---

### `POST /api/scan-jar`
Upload a compiled Spring Boot `.jar` to statically analyze its endpoints without running it.

```bash
curl -F "file=@yourapp.jar" http://localhost:8080/ghostapidetector/api/scan-jar
```

---

## How to use it effectively

1. Deploy alongside your app (or embed it in your existing Spring Boot project)
2. Let it observe real traffic  the longer the better (a few hours for dev, a week+ for production patterns)
3. Call `GET /internal/ghost-apis` and review the list
4. Endpoints still on the ghost list after meaningful traffic are candidates for removal or security review

---

## Project structure

```
src/main/java/com/ghostapidetector/
├── config/
│   └── StartupOrchestrator.java      # Guarantees discovery → rules → report order
├── controller/
│   └── GhostApiController.java       # /internal/ghost-apis and /internal/observed-apis
├── discovery/
│   ├── ApiDiscovery.java             # Scans RequestMappingHandlerMapping at startup
│   └── SpringMvcApiDiscovery.java    # ASM-based static JAR scanner
├── model/
│   ├── APIinfo.java                  # Discovered endpoint model
│   └── ObservedApi.java              # Observed request model
├── registry/
│   └── ApiRegistry.java              # In-memory store of all discovered endpoints
├── reporting/
│   └── ApiReporter.java              # Prints endpoint report to stdout on startup
├── rules/
│   └── ApiRuleEngine.java            # Applies RISKY / PUBLIC / FRAMEWORK tags
├── runtime/
│   └── ApiUsageInterceptor.java      # Records every incoming request
└── service/
    └── GhostApiService.java          # Core ghost detection diff logic
```

---

## Known limitations

- Observation data is in-memory  resets on restart. For persistent tracking, a database-backed store would be needed.
- Currently supports Spring Boot / Spring MVC applications only.
- The JAR scanner detects `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`, and `@PatchMapping`.

---

## Tech stack

- Java 17
- Spring Boot 3.3.5
- ASM 9.x (bytecode analysis)
- Maven

---

## Contributing

PRs welcome. Some good areas to contribute:
- Persistent observation storage (JPA / Redis)
- Support for JAX-RS annotations in the JAR scanner
- A simple web UI for the ghost API report
- Configurable tag rules

---

## License

MIT
