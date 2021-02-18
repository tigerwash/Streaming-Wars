# Streaming Wars Project - v3.0

## Authors

CS6310 | Team 28 | Xiaohong Huang, Ye Jin, Longtai Liao, Xiaodong Liu, Wanlin Zheng

## Getting Started

### Prerequisites

The application requires Java JDK 11+ and Java standard libraries (javax.swing, java.security, javax.crypto, java.io, java.util) on your local machine.

### Installation and Deployment

No additional installation and deployment work involved.

### Start the Application from an IDE

This project is built with [maven](https://maven.apache.org/index.html).

If you are runing the application from an IDE, first load the pom.xml file under the project root.
Then the project entry is "AdminMainView.java" under src/main/java/streaming/gui as specified in the pom file.

### Start the Application using its Jar
Download the zip file from Piazza to your local machine and unzip it. You have following options to start the application:
- Run the following commands in Command Line:

```
cd target
java -jar streaming_wars-jar-with-dependencies.jar
```
- Find streaming_wars-jar-with-dependencies.jar under target, and open with Jar Launch.

## Login UI
Once the application launches, a login page will be displayed to ask for username, password, and if database should be loaded.

### Login

The application supports three types of users:
1. Admin: Admin users can create, update and delete user information; and take all types of actions in the core system (see Core System section for details).
1. General: general users can take all types of actions in the core system (see Core System section for details). General users do not have access to create, update and delete user information.
1. Guest: Guest users can take limited actions in the core system for display_demo, display_studio, display_stream, display_events, display_offers, and display_time (see Core System section for details). Guest users do not have access to create, update and delete user information.

**The application comes with three default login credentials:**
- Username: admin; Password: admin; Role: Admin;
- Username: general; Password: general; Role: General;
- Username: guest; Password: guest; Role: Guest.

If you choose to log in with Admin access, you will have the ability to create / update users. If you don’t have a username and password, you can still access the Action UI with Guest access. 

### Using Database
1. User information is stored in a database with hashing, and can be loaded back to the application any time the application is ran.
2. If "Using DB" option is selected by user, user will start with some pre-selected actions in the database, and all the valid actions taken by the user will be saved back to the database. When the user restarts the application with the "Using DB" option, all the previous actions will be loaded back to the application.
3. If "Using DB" option is not selected by user, user will start a brand-new application with no previous data, and all actions taken by the user will not be saved to the database. All the actions will be lost once the application is killed.

## Action UI

Once the user finishes login, he / she will be directed to the Action page. 

User can perform different types of actions based on their roles. Refer to the Login section for details of roles. Follow examples below to enter commands and perform actions.

[User] register, login, update_password, update_role
```
register,test,123,Admin
login,test,123
update_password,test,123,456
update_role,test,456,General
```
[Demo] create_demo, display_demo, update_demo
```
create_demo,age_20_30,Viewers between 20 and 30,200
display_demo,age_20_30
update_demo,age_20_30,Viewers between 20 and 30,1000
```
[Studio] create_studio, display_studio
```
create_studio,warner,Warner Brothers
display_studio,warner
```
[Stream] create_stream, display_stream, update_stream
```
create_stream,hbo,Home Box Office,18
display_stream,hbo
update_stream,hbo,Home Box Office,10
```
[Event] create_event, display_events, watch_event, update_event
```
create_event,movie,Batman v Superman,2016,152,warner,1000
create_event,movie,Batman Begins,2005,140,warner,1000
create_event,ppv,Justice League Live,2020,180,warner,12000
display_events
update_event,Batman v Superman,2016,120,800
watch_event,age_20_30,30,hbo,Batman v Superman,2016
```
[Offer] offer_movie, offer_ppv, display_offers, retract_movie
```
offer_movie,hbo,Batman v Superman,2016
offer_movie,hbo,Batman Begins,2005
offer_ppv,hbo,Justice League Live,2020,180
display_offers
retract_movie,hbo,Batman Begins,2005
```
[Time] next_month, display_time
```
display_time
next_month
```
[Stop] stop
```
stop
```
## Error Handling
If the application is malfunctioning, please kill the application and restart the jar file. If that doesn’t help, please re-download the zip file from Piazza, and relaunch it. 

## Acknowledgments

* The password hashing algorithm uses defuse's [Secure Password Storage v2.0](https://github.com/defuse/password-hashing/blob/master/PasswordStorage.java).
* Special shout out to TA Fnu Jimmy - thanks for your prompt, dedicated, and detailed responses to guide us through this project.
