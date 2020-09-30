# Ordersystem
Spring boot framework based ordersystem


# 1. Introduction   
Create the project file from spring starter: [reference](https://start.spring.io/)   \
In this website you can make your own Spring project with many different libraries.In the project following dependencies are impleneted 

- Spring web 
- lombok
- h2 database
- JPA
- thymeleaf
- Sprig

**Attention**: please use Spring version **2.1 xxx version**. If you select over 2.1xx like 2.2... or 2.3... the Junit4 library is changed into Junit5 so that it can make a issue to compile

# 2.Gradle Dependencies 

**dependencies**

	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
  
	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
  
	implementation 'org.springframework.boot:spring-boot-starter-web'
  
	implementation 'org.springframework.boot:spring-boot-devtools'
  
	implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.6.2'
  
	compileOnly 'org.projectlombok:lombok'
  
	runtimeOnly 'com.h2database:h2'
  
	annotationProcessor 'org.projectlombok:lombok'
  
	testImplementation 'org.springframework.boot:spring-boot-starter-test'


I manually added **devtools** and **p6spy-spring-boot-starter** after buding gradle file. all the other things will be automatically builed when you generate your project with Spring boot start.But I strongly recommed just clone this project.


# 3. Entity structure 

<div>
<img width="500" src="https://user-images.githubusercontent.com/45092135/94732625-d03ae400-0366-11eb-9616-c66656c77acd.JPG">			 
</div>

# 4. Database structure 


<div>
<img width="500" src="https://user-images.githubusercontent.com/45092135/94732654-d6c95b80-0366-11eb-80e0-746f5fad1e2a.JPG">			 
</div>







