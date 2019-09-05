# Restaurant rating system

## Used stack:
<b>Backend:</b> Java, Spring Boot, Spring Data, Spring Security, Spring Integration, Spring Mail, Spring Actuator<br>
<b>Frontend:</b> ReactJS, Javascript, Bootstrap <br>
<b>Database:</b> PostgreSQL<br>
<b>Tests:</b> JUnit5, AssertJ, Mockito, H2<br>
<b>Docker</b><br>

### Description
Restaurants offer the menu every day. Clients can look through menu and vote for a restaurant.<br>
There are 3 roles: user, company, admin<br>
##### Company can
- create a restaurant
- add/edit/delete current menu(dish, price)
- edit profile
- view statistics <br>
##### User can

- see current restaurants menu
- vote for a restaurant
- see his votes for previous days
- edit profile
- view statistics
- see welcome-mail, that is sent after his registration <br>

##### Admin can
- edit/delete users
- see Spring Actuator metrics

### Run program

In docker run command <br>
` docker-compose up –-build ` <br>
Open the page in browser <br>
` http://your_IP_address_in_docker:3000 ` <br><br><br>

![Screenshot](screenshot_user.png)
![Screenshot](screenshot_company.png)
![Screenshot](screenshot_admin.png)
![Screenshot](screenshot_metrics.png)