### Design decisions:

* Modelled the address book -> contact as a many-to-many relationship with JPA, since I think it makes sense that a contact can live in multiple address books.
* H2 in-memory database, which could be easily swapped out for Postgresql in production/Kubernetes

### Running tests:

* $ ./gradlew cleanTest test

### Containerisation:

Generally I think it's best to use the Cloud Native Buildpack support in Spring Boot 2.3+ to build a docker image

* $ ./gradlew bootBuildImage

Running:

* $ docker run --rm -p 8080:8080 -e "SPRING_PROFILES_ACTIVE=sample-data" address-book:0.0.1-SNAPSHOT

Testing:

Get all address books with contacts:
* curl localhost:8080/api/v1/address-book | jq '.'

Create an address book:
* curl -X POST localhost:8080/api/v1/address-book --header "Content-Type:application/json" -d '{ "name": "Marlo''s address book" }' | jq '.'

Add a contact to an address book:
* curl -X POST localhost:8080/api/v1/address-book/1/contact --header "Content-Type:application/json" -d '{ "name": "Lester Freamon", "phoneNumbers": { "MOBILE": "+61404939499" } }' | jq '.'

Get unique contacts accross all address books:
* curl localhost:8080/api/v1/address-book/unique-contacts | jq '.'

#### Cloud Native/12 factor app

I've included the Actuator dependency for liveness and readiness probes for Kubernetes

Usually if deploying to kubernetes I'd also use the excellent Spring Cloud libraries to add support for things like service discovery, centralised config, distributed tracing, circuit breakers, etc

### Future Improvements
* DTOs
* Add the Spring REST Docs library to document the API
* End-to-end testing with RestAssured
