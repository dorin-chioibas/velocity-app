# velocity-app

Simple REST API service for storing birds and sightings

=====================================================

This App is used for storing and querying data about Birds and Sightings.
The architecture of the app follows the MVC layering. `Spring Boot Docker Compose` together
with `Spring Boot DevTools` are being used in order to make the development smoother.
`Postman Collection` has been exported in the `resources` folder for easy manual testing of the app.
<br/>
<br/>
<br/>
The application can be run in IntelliJ IDEA importing `AppApplication.run.xml` file from the `.run` folder.
The application also runs directly by executing `mvn spring-boot:run` inside the root of the project.
Default port is `5000`.
<br/>
<br/>
<br/>
The API provides endpoints to manage birds and sightings.

Birds
> GET /api/v1/birds: List all birds.

> GET /api/v1/birds/{id}: Get a specific bird by ID.

> POST /api/v1/birds: Create a new bird.

> PUT /api/v1/birds/{id}: Update an existing bird by ID.

> DELETE /api/v1/birds/{id}: Delete a bird by ID.

> GET /api/v1/birds/search?name={name}&color={color}: Search birds by name and color. The query params are optional.

Sightings
> GET /api/v1/sightings: List all sightings.

> GET /api/v1/sightings/{id}: Get a specific sighting by ID.

> POST /api/v1/sightings: Create a new sighting.

> PUT /api/v1/sightings/{id}: Update an existing sighting by ID.

> DELETE /api/v1/sightings/{id}: Delete a sighting by ID.

> GET /api/v1/sightings/search?birdId={birdId}&location={location}&startTime={startTime}&endTime={endTime}: Search
> sightings
> by bird ID, location, and time interval. The query params are optional.
