### **Insta**

The objective of this project is to develop a media sharing Service with following features - 

* Signup of users
* Log in/out
* Users should be able to upload pictures
* Users should be able to delete their own pictures
* Users should be able to review pictures with a score from 1 to 5 (but not theirs)
* Users should be able to see the average of reviews for all pictures





**Tech Stack -**

Following tech choices were made during development of this project -

1) JWT Token is used for authentication. Each token will have expiry time 
   so that token can't be misused.
   

2) Redis is being used as cache for storing token and otp. It's used to keep latency minimum 
   for token validation as each api call will have token validation.
   

3) MySQL as db to store User, Media path, Review data. 
   

4) Currently, LocalFileStorage is being used to store media for demo purpose which can be replaced 
   with other Object storage services such as S3, OCI storage services in production.
 

5) In auth apis, UUID is being used (instead of incremental DB ids) for communication through rest apis. 
   This has been done to avoid security risk of exposing one person's id to another.
   Ref - https://wiki.owasp.org/index.php/Top_10_2013-A4-Insecure_Direct_Object_References


6) Kafka has used to send event e.g Review Event so that aggregation pipeline can be written to
   serve use-case like showing average reviews etc. This pipeline can consume review events and push the aggregated results in ES.


7) Swagger has been used for api docs. Here's the link -
   http://localhost:8090/swagger-ui.html#
   

   ![Alt text](src/main/resources/swagger.png?raw=true "Title")


**Entities** -

Cache Entites - 

1) Otp Cache - 
   
   
      Key - PhoneNumber -

      Value - 

     {
   
         private Integer otp;
      
         private Date issuedAt;
      
         Date expiresAt;
   
      }
  
   
2) Token Cache


    Key - JwtToken 

    Value - {
   
      private String token;
   
      private Long id;
   
      private List<Pair<Long,String>> roles;
   
      private Date issuedAt;
   
      private Date expiresAt;   
    }




### **APIS** -

**1) Register User** 


`curl -X POST "http://localhost:8090/insta/api/v1/auth/signup" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"email\": \"jaydeepkhandelwal@gmail.com\", \"name\": \"jaydeep\", \"password\": \"jaydeep\", \"phone_number\": \"9916219263\"}"
`

Response -


{

      "ext_id": "a7d299bf-a465-491b-97d2-5fa649b30d1b"
}


**2) SignIn using userName & password**

curl -X POST "http://localhost:8090/insta/api/v1/auth/login" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"otp\": 0, \"password\": \"jaydeep\", \"phone_number\": \"string\", \"username\": \"jaydeep\"}"

Response-


{


   "ext_id": "a7d299bf-a465-491b-97d2-5fa649b30d1b"


}

Response Header - 

cache-control: no-cache, no-store, max-age=0, must-revalidate
content-type: application/json;charset=UTF-8
date: Tue, 02 Mar 2021 13:51:53 GMT
expires: 0
pragma: no-cache
transfer-encoding: chunked
x-content-type-options: nosniff
x-frame-options: DENY
x-user-token: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwiaXNzIjoiamF5ZGVlcCIsImlhdCI6MTYxNDY5MzExMywiZXhwIjoxNjE0NzExMTEzfQ.INoRL5t6Ex0ZiQAJD2OQrhjMwu1d2Hxxh1cvJk9Gwtc
x-xss-protection: 1; mode=block


**3) Register Using Otp (Generate OTP)-**

curl -X GET "http://localhost:8090/insta/api/v1/auth/otp/9916219262" -H "accept: */*"

Response - 

Sends OTP as a SMS to mobile. Currenly, SMS Sender is mocked.


**4) Signin Using Otp -**

curl -X POST "http://localhost:8090/insta/api/v1/auth/login" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"otp\": 416155, \"phone_number\": \"9916219262\"}"


Response Body - 

{
"ext_id": "dfd22026-4b5f-4a34-bd62-f1dcb976199e"
}


Response Header - 

cache-control: no-cache, no-store, max-age=0, must-revalidate
content-type: application/json;charset=UTF-8
date: Tue, 02 Mar 2021 15:55:26 GMT
expires: 0
pragma: no-cache
transfer-encoding: chunked
x-content-type-options: nosniff
x-frame-options: DENY
x-user-token: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoiYW5vbnltb3VzVXNlciIsImlhdCI6MTYxNDcwMDUyNiwiZXhwIjoxNjE0NzE4NTI2fQ.b5jJu2eGT-DMEUxgQ8gBv6cQysa4g7ohHn_T3TyiBDE
x-xss-protection: 1; mode=block


**5) Upload Image -** 

curl -X POST "http://localhost:8090/insta/api/v1/media/upload" -H "accept: */*" -H "X-USER-TOKEN: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwiaXNzIjoiamF5ZGVlcCIsImlhdCI6MTYxNDY4NzI2NiwiZXhwIjoxNjE0NzA1MjY2fQ.aVJmALM6uLvQUAEdp1Bt6izdkqR-gm8-3-KBttmXbGg " -H "Content-Type: multipart/form-data" -F "file=@Screenshot 2021-01-23 at 12.11.27 AM.png;type=image/png"

Response - 
{
"id": 1
}


**6) Delete Media** 

curl -X DELETE "http://localhost:8090/insta/api/v1/media/4?attributes=%7B%7D&credentialsNonExpired=true&enabled=false" -H "accept: */*" -H "X-USER-TOKEN: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoiamF5ZGVlcCIsImlhdCI6MTYxNDY5Njg2NywiZXhwIjoxNjE0NzE0ODY3fQ.UI8ynsdU0bVdibsbNMISUjsXbJNEbtixasUbGuq_-wo"

Response body

{
"status": true
}

**7) Submit Review** 

curl -X POST "http://localhost:8090/insta/api/v1/review/?attributes=%7B%7D" -H "accept: */*" -H "X-USER-TOKEN: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoiamF5ZGVlcCIsImlhdCI6MTYxNDY5Njg2NywiZXhwIjoxNjE0NzE0ODY3fQ.UI8ynsdU0bVdibsbNMISUjsXbJNEbtixasUbGuq_-wo" -H "Content-Type: application/json" -d "{ \"location\": { \"lat\": 12.14, \"lng\": 13.56 }, \"media_id\": 6, \"review\": \"Good\", \"score\": 1}"

Response Body -

{
"id": 1
}

#### **Error Flows -**

1) 401 (Unauthorized) will be thrown if any api call (except signup/signin/otp) happens with invalid token


2) 400 (Bad Request) will be thrown if user is trying to submit review on his on photos with below message - 

`user is trying to review his own photo
`

3)  400 (Bad Request) will be thrown if user is trying to delete the media which doesn't belong to him.

#### **Future Scope -**

1) In order to serve use-case like showing average reviews etc, an aggregation pipeline can be built. 
   It can run on top of spark and consume review events from Kafka and can save aggregated results in data store.
   

2) In order to support huge scale of videos, we can follow below strategies -
   

      a) Compression of videos
   
      b) Storing videos on regional CDNs so that network latency can be minimum.
   
      c) We can even provide cache hardware to ISP so that videos can be cached at ISP level. This strategy is used by Netflix.
      

3) To build internal reporting, we can build a CDC pipeline service which can read binlogs from DB. These binlogs can be parsed and pushed in Kafka.
Later, from Kafka, this data can be pushed to data warehouse for reporting.
   
4) In order to build external reporting for external users, data can be pushed to Elastic Search which can power our external reporting.
ES supports many types of filters, aggregation out of box with very minimum latency.
   

