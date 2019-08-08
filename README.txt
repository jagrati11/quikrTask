##
UserComment Model: This project contains two API calls
Add API: Accepts json payload and publish data to RabitMq 

Format :
POST: http://localhost:8080/quikrTask/userComment/add
{
"userEmail":"ram@gmail.com",
"userComment":"Ram is bad boy"
}

curl -X POST \
  http://localhost:8080/quikrTask/userComment/add \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 67' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:8080' \
  -H 'Postman-Token: 2a25fbcd-f49e-4355-89cb-a09b3341cd5f,0c5de877-7ab9-47bc-9302-208d6f3a16af' \
  -H 'User-Agent: PostmanRuntime/7.15.2' \
  -H 'cache-control: no-cache' \
  -d '{
"userEmail":"ram@gmail.com",
"userComment":"Ram is bad boy"
}'

Search API: Search the phrase given in full text query

Format:
GET: http://localhost:8080/quikrTask/userComment/search?matchPhrase="good boy"

curl -X GET \
  'http://localhost:8080/quikrTask/userComment/search?matchPhrase=%22good%20boy%22' \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:8080' \
  -H 'Postman-Token: 0dcd0eb4-f664-4834-b23e-a95baeb67aa3,6da84fbc-540e-42d8-8a14-37906a8ca10c' \
  -H 'User-Agent: PostmanRuntime/7.15.2' \
  -H 'cache-control: no-cache'

####
Basic Elastic search query:

GET /user_comment/_doc/_search
{
 "query": {
   "match": {
     "userComment": "good boy"
   }
 }
}

Elastic Query with boosting score:

GET /user_comment/_doc/_search
{
 "query": {
   "bool": {
     "must": [
       {
         "match": {
           "userComment":{
             "query":"good boy",
             "boost": 1
           }
         }
       }
     ],
     "should": [
       {
         "match_phrase": {
           "userComment":{
             "query":"good boy",
             "boost": 100
           }
         }
       },
       {
         "match": {
           "userComment":{
             "query":"good boy",
             "operator":"and",
             "boost": 10
           }
         }
       }
     ]
   }
 }
}

Inputs Given to Add Api:
{
"userEmail":"arpan@gmail.com",
"userComment":"Arpan is good boy"
}
{
"userEmail":"amit@gmail.com",
"userComment":"Amit is also good"
}
{
"userEmail":"rahim@gmail.com",
"userComment":"Rahim is good but sometimes bad boy"
}
{
"userEmail":"ram@gmail.com",
"userComment":"Ram is bad boy"
}

Expected output of search api:
[
    {
        "userEmail": "arpan@gmail.com",
        "userComment": "Arpan is good boy"
    },
    {
        "userEmail": "rahim@gmail.com",
        "userComment": "Rahim is good but sometimes bad boy"
    },
    {
        "userEmail": "amit@gmail.com",
        "userComment": "Amit is also good"
    },
    {
        "userEmail": "ram@gmail.com",
        "userComment": "Ram is bad boy"
    }
]
