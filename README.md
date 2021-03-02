**Insta APP**

The objective of this project is to develop a Media Sharing Service with following features - 

* Signup of users
* Log in/out
* Users should be able to upload pictures
* Users should be able to delete their own pictures
* Users should be able to review pictures with a score from 1 to 5 (but not theirs)
* Users should be able to see the average of reviews for all pictures



Tech Stack -

Following tech choices were made during development of this project -
1) JWT Token for user auth. 
2) Redis as cache to store User Tokens.
3) MySQL as db to store User, Media path, Review data.
4) Currently, LocalFileStorage is being use to store media for demo purpose which can be replaced 
   with other Object storage services such as S3, OCI storage services for production usage.
5) UUID is being used for communication through rest apis instead of incremental DB ids. 
   This has been done to avoid security risk of exposing userIds because.
   Ref - https://wiki.owasp.org/index.php/Top_10_2013-A4-Insecure_Direct_Object_References


APIS - 

1) Register User 

curl -X POST "http://localhost:8090/insta/api/v1/auth/signup" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"email\": \"jaydeepkhandelwal@gmail.com\", \"name\": \"jaydeep\", \"password\": \"jaydeep\", \"phone_number\": \"9916219263\"}"

Response -
{
"ext_id": "a7d299bf-a465-491b-97d2-5fa649b30d1b"
}

2) SignIn 

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

3) Upload Image - 

curl -X POST "http://localhost:8090/insta/api/v1/media/upload" -H "accept: */*" -H "X-USER-TOKEN: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwiaXNzIjoiamF5ZGVlcCIsImlhdCI6MTYxNDY4NzI2NiwiZXhwIjoxNjE0NzA1MjY2fQ.aVJmALM6uLvQUAEdp1Bt6izdkqR-gm8-3-KBttmXbGg " -H "Content-Type: multipart/form-data" -F "file=@Screenshot 2021-01-23 at 12.11.27 AM.png;type=image/png"

{
"ext_id": "215ca9bb-808c-4c91-b7e7-0f16c6bc6e19"
}