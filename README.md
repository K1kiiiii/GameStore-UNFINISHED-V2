GameStore-UNFINISHED-V2
=======================

Overview
--------
This is a Spring Boot demo application (Game Store) built with Spring MVC, Spring Data JPA and Thymeleaf. The app ships with a file-backed H2 database by default (so you don't need to install a separate DB server) and includes models for `Game`, `Publisher` and `Review`. The project provides both web UI pages (Thymeleaf) and REST endpoints for programmatic access.

This README documents how to add entries to the database (without editing DB files directly), explains the important parts of the codebase, and lists useful commands for building and running the app.

Quick start (build & run)
-------------------------
From the project root (Windows cmd):

```cmd
mvn -DskipTests package
mvn spring-boot:run
```

Once the app is running, open your browser at http://localhost:8080/.

Where the DB lives
------------------
- The application uses a file-backed H2 database. The JDBC URL is defined in `src/main/resources/application.properties`:
  - `jdbc:h2:file:./data/gamestore`
- The database files will be created under the `data` folder in the project root, for example:
  - `C:\Users\The Beast\IdeaProjects\GameStore-UNFINISHED-V2\data\gamestore.mv.db`
  - (There may also be `gamestore.trace.db` depending on the H2 version and settings.)

Important: do not edit these `.mv.db` files with a text editor. Use the H2 Console, the REST API, or the application UI to add/change rows.

How to add entries to the database (safe ways)
---------------------------------------------
You can add entries using one of these safe methods. All avoid directly editing DB files.

1) Use the web UI (Thymeleaf)
- The game details page (`/games/{id}`) includes a form to add a review for that game.
- Steps:
  1. Open the browser at `http://localhost:8080/games` and click a game.
  2. On the game details page use the "Add a review" form; fill `Your name`, `rating`, `content` and submit.
  3. The review is saved and visible immediately (page redirects back to the same game).

2) Use the REST API (programmatic)
- Review endpoints:
  - GET reviews for a game: `GET /api/reviews/game/{gameId}`
  - Create a review: `POST /api/reviews` with JSON body
- Example (Windows cmd `curl`):

```cmd
curl -X POST -H "Content-Type: application/json" -d "{\"author\":\"Alice\",\"content\":\"Nice game!\",\"rating\":5,\"game\":{\"id\":1}}" http://localhost:8080/api/reviews
```

- Publisher endpoints (example):
  - `GET /api/publishers` - list all publishers
  - `POST /api/publishers` - create a publisher (JSON)

3) Use the Admin reseed endpoint (recreate sample data)
- A development `AdminController` exposes a convenient endpoint to (re)seed sample games and reviews.
- POST to `/admin/seed` to delete existing `games`/`reviews` and insert a predefined set.

```cmd
curl -X POST http://localhost:8080/admin/seed
```

This is handy when you want a known dataset for testing.

4) Use the H2 Console (SQL interface)
- Start the app, open: `http://localhost:8080/h2-console`
- Connection details:
  - JDBC URL: `jdbc:h2:file:./data/gamestore`
  - User: `sa`
  - Password: leave blank
- Example SQL (insert a review for game id 1):

```sql
INSERT INTO REVIEW (AUTHOR, CONTENT, RATING, GAME_ID) VALUES ('Bob','Great game',5,1);
SELECT * FROM REVIEW;
```

Notes and cautions for the H2 Console:
- Do not connect with an external editor to the DB file while the app is running in a way that causes conflicting writes.
- Prefer running SQL through the H2 Console while the app is running — it connects over JDBC safely.

Project structure & important files (short explanations)
--------------------------------------------------------
Key packages:
- `com.example.demo.model` - JPA entities
- `com.example.demo.repository` - Spring Data JPA repositories
- `com.example.demo.service` - service layer wrappers around repositories (save/get/delete logic)
- `com.example.demo.controller` - web and REST controllers
- `src/main/resources/templates` - Thymeleaf templates (UI)
- `src/main/resources/application.properties` - configuration (datasource, JPA settings, etc.)

Models (entities)
- `Game` (`src/main/java/com/example/demo/model/Game.java`)
  - Fields: id, title, genre, developer, description, image, rating, price
  - Relations: `@OneToMany` List<Review> reviews, `@ManyToOne` Publisher publisher
- `Review` (`src/main/java/com/example/demo/model/Review.java`)
  - Fields: id, author, content, rating
  - Relations: `@ManyToOne` Game game
- `Publisher` (`src/main/java/com/example/demo/model/Publisher.java`)
  - Fields: id, name, country
  - Relations: `@OneToMany` List<Game> games

Repositories
- `GameRepository` extends `JpaRepository<Game, Long>` - basic CRUD and paging for games
- `ReviewRepository` extends `JpaRepository<Review, Long>` - contains `findByGameId(Long gameId)` for fetching reviews of a specific game
- `PublisherRepository` extends `JpaRepository<Publisher, Long>`

Services
- `GameService`, `ReviewService`, `PublisherService` implement thin layers that call the repositories. They are used by controllers to keep controller logic simple and testable.

Controllers (what they do & routes)
- `GameController` (Thymeleaf UI)
  - `GET /` -> index page (list of games)
  - `GET /games` -> games listing
  - `GET /games/{id}` -> game details page (adds `game`, `reviews`, `avgRating` to the model)
  - `POST /games/{id}/reviews` -> submit a new review via the HTML form
  - `POST /games/{gameId}/reviews/update/{reviewId}` -> update a review via inline form

- `PublisherController` (UI) — manages publisher web pages and forms (create/edit/list)

- `PublisherRestController` (REST)
  - `GET /api/publishers` -> list publishers
  - `GET /api/publishers/{id}` -> get publisher by id
  - `POST /api/publishers` -> create publisher

- `ReviewRestController` (REST)
  - `GET /api/reviews/game/{gameId}` -> list reviews for a game (JSON)
  - `POST /api/reviews` -> create a review (JSON body expected)

- `AdminController` (dev helper)
  - `POST /admin/seed` -> deletes and reseeds a predefined set of games and reviews (development only)

Data seeding
------------
- `DataSeeder` runs at application startup and seeds sample games and (now) sample reviews if the DB is empty. This makes it easy to see content without manual inserts.
- If you need to re-seed after the first run, use the `POST /admin/seed` endpoint (mentioned above).

Switching to a production DB (Postgres example)
------------------------------------------------
If you prefer to use an external DB server (Postgres), do the following:
1. Run a Postgres server (local or remote).
2. In `src/main/resources/application.properties` replace the datasource properties with something like:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gamestore
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```
3. Ensure the Postgres JDBC dependency is present in `pom.xml` (the project already includes it).
4. Restart the app. If you get connection errors, make sure Postgres is running and accessible.

Development tips and troubleshooting
----------------------------------
- If the app fails to start with a DB connection error, check `src/main/resources/application.properties` and ensure the configured DB is reachable.
- If you change entity classes, re-run `mvn -DskipTests package` to compile them. If you use Lombok, enable annotation processing in your IDE.
- If port 8080 is in use, change the server port in `application.properties` (e.g., `server.port=8081`).
- To inspect the DB schema quickly, use the H2 Console (`/h2-console`) and run `SELECT` queries.

Useful commands (Windows cmd)
-----------------------------
Build and run
```cmd
mvn -DskipTests package
mvn spring-boot:run
```
Seed/reset DB manually (Admin endpoint)
```cmd
curl -X POST http://localhost:8080/admin/seed
```
Create a review via REST
```cmd
curl -X POST -H "Content-Type: application/json" -d "{\"author\":\"Alice\",\"content\":\"Nice game!\",\"rating\":5,\"game\":{\"id\":1}}" http://localhost:8080/api/reviews
```
Open H2 Console
- Browser: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./data/gamestore`  User `sa`, blank password

Where to look in the code
-------------------------
- Startup seeding: `src/main/java/com/example/demo/config/DataSeeder.java`
- UI pages: `src/main/resources/templates/` (Thymeleaf HTML)
- Controllers (web + REST): `src/main/java/com/example/demo/controller/`
- Entities: `src/main/java/com/example/demo/model/`
- Repositories: `src/main/java/com/example/demo/repository/`
- Services: `src/main/java/com/example/demo/service/`

If you want me to also:
- Add sample SQL files you can run in the H2 Console (I can create a `sql/seed.sql`).
- Create a README section with screenshots or sample responses from the API.
- Add a `profile`-based config (dev=H2, prod=Postgres) and a short script to switch.

Tell me which of these you'd like next and I'll add it. Thanks!
