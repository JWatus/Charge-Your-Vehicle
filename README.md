## Charge Your Vehicle App :fuelpump:

#### This application allows user to find charging points according to his preference.

### Configuration 

This project use the following ports : 

| Server     | Port |
|------------|------|
| localhost  | 7000 |

All You need to do is to download this app and run SpringBootApplication.

Open your browser and type:

* [http://localhost:7000/home](http://localhost:7000/home)

### Database

This project uses embedded database H2. 

If You want to check database, there is a possibility to do it after running project and loading data.
Then You have to open link:

* [http://localhost:7000/h2](http://localhost:7000/h2)

And fill form with those values:

|   Property   | Value |
|------------|------|
| Driver Class:     | org.h2.Driver |
| JDBC URL:  | jdbc:h2:mem:db; |
| User Name:  | sa |
| Password:   | sa |

After doing this press Connect button.

### Options
Beneath are described few steps how to start and options which You can find in this application.

#### Setting properties
'*Properties*' section You can find in the '*Administration*' panel. You can set there the unit in which range for search charging points will be taken. Available options are: miles, kilometers and meters. 

If You want to be sure that Google Map will be properly displayed You should set Your own Google API key beneath unit set field. Without it there is a possibility that map with markers of charging points will not be displayed or address to coordinates converter will not work. 

#### Data loading
If You want to use this app You need to load data to database. There are few ways to do this in '*Administration*' panel. 

If Your interests are only about checking functionality you can load few example charging points from file in '*Load example charging points*'. This is the fastest way to begin using this application. 
Eventually You can '*Load charging points from API*' - there are possibilities to load all charging points for *Poland* or *India*. It might take a little more time depending on the internet speed. 

Last option is '*Load all charging points in the world*' - I do not recommend to do this unless You want to have fully functional application for every place in the world. In this case loading all data will take a lot of time so be careful. 

You can check all loaded charging points in '*All loaded points*' section. 

#### Usage of application
In '*Search*' panel there are various options of finding charging points. You can search charging point which is closest to user's current location, all points in given radius, nearest point to specific address, all points in radius from specific address, all points in town and all in country. 

In '*Statistics*' panel there is possibility to check how many times charging points were searched for specific towns or countries.

### Technology used to create and develop this application: 
- Java 8
- Spring Boot
- Spring MVC
- Spring Web Services
- Thymeleaf
- Bootstrap
- JavaScript
- Google Maps API
- H2
- JUnit 5

### Enjoy :heavy_exclamation_mark: