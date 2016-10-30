## NETGEAR

#ADD NEW USER
HTTP POST 
http://localhost:8080/services/rest/user/adduser

payload :
{
  "id": "1",
  "username": "f001111",
  "password": "bar11111",
  "email":"abc@xyz.com"
}


#LIST USER DATA
HTTP GET 
http://localhost:8080/services/rest/users


#VALIDATE USER
HTTP GET 
http://localhost:8080/services/rest/validateUser?email=abc@xyz.com&password=bar11111111


#DELETE USER
http://localhost:8080/services/rest/user/delete/f001111

#UPDATE USER
http://localhost:8080/services/rest/user/update/f001111

payload : 


{
  "id": "1",
  "username": "f001111",
  "password": "bar11111",
  "email":"abc@xyssssssssssz.com"
}