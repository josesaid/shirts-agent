# shirts-agent

Spring Boot + Spring AI example: an agent that finds a t-shirt (*playera*) in a requested color using a `@Tool` method.

## Requirements

- Java 21
- Maven 3.9+
- An OpenAI API key

## Run

```bash
export OPENAI_API_KEY=sk-...
mvn spring-boot:run
```

## Call the endpoint

```bash
curl -X POST http://localhost:8080/api/shirts/recommend \
  -H "Content-Type: application/json" \
  -d '{"item":"playera","color":"roja"}'
```

Example response (the exact output depends on the model):

```json
{"color":"red","size":"M","model":"Classic Crew","price":299.0}
```

## Test

```bash
mvn test
```

The unit tests do not call the model.

## Structure

- `ShirtCatalogTool`: `@Tool` method over an in-memory catalog
- `ShirtAgent`: `ChatClient` with a system prompt and the tool
- `ShirtService`: input validation, then calls the agent
- `ShirtController`: `POST /api/shirts/recommend`
- `ShirtExceptionHandler`: maps invalid input to HTTP 400

## Notes

- The Spring Boot version (3.5.6) and Spring AI version (1.1.8) are pinned in `pom.xml`. Check the Spring AI reference documentation before upgrading.
- The catalog is fake data for the example.
