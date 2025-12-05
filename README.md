# OAuth2 Server

This is a simple project used to explore how OAuth2 can be used to provide secure login functionality. It works in tandem with [this client](https://github.com/yanader/oauth-client.git).

## How to run this repo:

### Clone the repo

`git clone https://github.com/yanader/oauth-server.git`

### Create application properties.

Create `src\main\resources\application.properties`

Populate it with:
```
spring.security.oauth2.client.registration.google.client-id=[YOUR_CLIENT_ID]
spring.security.oauth2.client.registration.google.client-secret=[YOUR_CLIENT_SECRET]
spring.security.oauth2.client.registration.google.scope=openid,profile,email
spring.security.oauth2.client.provider.google.issuer-uri=https://accounts.google.com

spring.datasource.url=jdbc:postgresql://localhost:5432/[DATABASE_NAME]
spring.datasource.username=[YOUR_POSTGRES_USERNAME]
spring.datasource.password=[YOUR_POSTGRES_PASSWORD]
spring.datasource.driver-class-name=org.postgresql.Driver
```
### Locally run the client

Run the server directly through your IDE. 


### Other requirements

You will need a locally running Postgres database.
