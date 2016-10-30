## netgear

##Add New user'
HTTP POST 
http://localhost:8080/services/rest/user/adduser

{
  "id": "1",
  "username": "f001111",
  "password": "bar11111",
  "email":"abc@xyz.com"
}


#List All User
HTTP GET 
http://localhost:8080/services/rest/users


#Validate User
HTTP GET 
http://localhost:8080/services/rest/validateUser?email=abc@xyz.com&password=bar11111111