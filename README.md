# geolocation
Vehicle geolocation  There is a large amount of vehicles (millions) Each vehicle is equipped with a GPS tracker The tracker periodically sends vehicle geolocation to server (about every ten seconds) The task is to create a RESTful web service that will:

Receive current coordinates from vehicle GPS trackers

On request return the list of vehicles that are located within a given rectangle on the map. The rectangle coordinates are provided as the request params

##Build:
```
mvnw clean package
```
##Run
```
docker-compose up
```
##Query Examples

Project accepts "Decimal degrees (DD)" coordinates format.

Create or update current geo coordinates for the object

```
POST:127.0.0.1:8080/geo
{
    "id": 123456789,
    "latitude": 41.987654321,
    "longitude": 2.3456789
}
```

List of vehicles that are located within a given rectangle

```
GET:127.0.0.1:8080/geo?leftDownLongitude=2&leftDownLatitude=41&rightUpLongitude=3&rightUpLatitude=42
```
rectangle defined as pair of coordinates (left down and right up corners)

##Grafana
```
http://127.0.0.1:3000
```

##Run tests

To start the application and run tests locally the next ENVIRONMENT variables should be defined:
```
SPRING_DATASOURCE_URL= (on ex. jdbc:postgresql://127.0.0.1:5432/geouser)
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```