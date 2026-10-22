# shirts-agent

![Java: 21](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white) ![Spring_Boot: 3.5.6](https://img.shields.io/badge/Spring_Boot-3.5.6-6DB33F?logo=springboot&logoColor=white) ![Spring_AI: 1.1.8](https://img.shields.io/badge/Spring_AI-1.1.8-6DB33F) ![Model: OpenAI](https://img.shields.io/badge/Model-OpenAI-412991?logo=openai&logoColor=white)

A Spring Boot and Spring AI example: an AI agent that recommends a t-shirt (*playera*) in a requested color. The model calls a `@Tool` method that searches an in-memory catalog.

> Status: learning and demo project. The catalog is fake data.

## Tech stack

| Area | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5.6 (Spring MVC) |
| AI | Spring AI 1.1.8, OpenAI model `gpt-4o-mini` |
| Persistence | None. The catalog is an in-memory list |
| Messaging | Not used |
| Cloud | Not used. The app calls the OpenAI API |
| Docker | Not used. No Dockerfile or compose file |
| Build | Maven |
| Tests | JUnit 5 (Spring Boot test starter) |

## Architecture

```
POST /api/shirts/recommend
        │
ShirtController ──► ShirtService (validation) ──► ShirtAgent (ChatClient + system prompt)
                                                      │
                                                      ▼  tool call
                                              ShirtCatalogTool (@Tool, in-memory catalog)
```

- **Validation:** `ShirtService` accepts only the items `playera`, `shirt` and `t-shirt`, and requires a non-blank color. Invalid input never reaches the model.
- **Agent:** `ShirtAgent` builds a `ChatClient` with a system prompt and gives the model the `ShirtCatalogTool`. The model answers with a structured `Shirt`.
- **Errors:** `ShirtExceptionHandler` maps `IllegalArgumentException` to HTTP 400 with the message as the body.

## Components and classes

Base package: `com.mx.development.jose.said`

| Class | Role |
|---|---|
| `ShirtApplication` | Spring Boot entry point |
| `ShirtController` | REST endpoint `POST /api/shirts/recommend` |
| `ShirtService` | Input validation, then calls the agent |
| `ShirtAgent` | `ChatClient` with system prompt and tool |
| `ShirtCatalogTool` | `@Tool findShirtByColor` over the in-memory catalog |
| `ShirtRequest` | Request body: `item`, `color` |
| `Shirt` | Response model: `color`, `size`, `model`, `price` |
| `ShirtExceptionHandler` | Maps invalid input to HTTP 400 |

## Configuration to run

### Requirements

- JDK 21
- Maven 3.9 or later
- An OpenAI API key

### Environment variables

| Variable | Required | Description |
|---|---|---|
| `OPENAI_API_KEY` | Yes | OpenAI key, read by `spring.ai.openai.api-key` |

### Properties (`application.properties`)

| Property | Value | Description |
|---|---|---|
| `spring.application.name` | `shirts-agent` | Application name |
| `spring.ai.openai.api-key` | `${OPENAI_API_KEY}` | Read from the environment |
| `spring.ai.openai.chat.options.model` | `gpt-4o-mini` | Model used |
| `spring.ai.openai.chat.options.temperature` | `0` | Deterministic answers |

No Spring profiles are defined. The server listens on the default port `8080`.

### Steps

```bash
# 1. Clone
git clone https://github.com/josesaid/shirts-agent.git
cd shirts-agent

# 2. Set the key (never commit it)
export OPENAI_API_KEY=sk-...

# 3. Run
mvn spring-boot:run
```

## Endpoint

`POST http://localhost:8080/api/shirts/recommend`

```bash
curl -X POST http://localhost:8080/api/shirts/recommend \
  -H "Content-Type: application/json" \
  -d '{"item":"playera","color":"red"}'
```

Example response (the exact output depends on the model):

```json
{"color":"red","size":"M","model":"Classic Crew","price":299.0}
```

| Case | Status | Body |
|---|---|---|
| Valid item and color | 200 | Shirt JSON |
| Unsupported item (for example `sofa`) | 400 | `Unsupported item: sofa` |
| Blank color | 400 | `Color is required` |

More examples are in the `commands` file.

## Testing

```bash
mvn test
```

Unit tests cover `ShirtService` and `ShirtCatalogTool`. They do not call the model, so they run without an API key.

## Known limitations

- **English colors in the tool.** `ShirtCatalogTool` matches colors in English (`red`, `blue`, `black`). Requests in Spanish, such as `roja`, depend on the model translating the color before calling the tool. If the color is not in the catalog, the tool throws an error.
- **Fake catalog.** Four products in memory. A real version would use a repository or an inventory API.
- **Model dependency.** Responses depend on the model and can vary between runs, even with temperature 0.
- **Pinned versions.** Spring Boot 3.5.6 and Spring AI 1.1.8 are pinned in `pom.xml`. Check the Spring AI reference before upgrading.
