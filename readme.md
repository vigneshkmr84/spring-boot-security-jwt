# Spring Security + JWT

## JWT - What is it ?
JSON Web Tokens are an open, industry standard RFC 7519 method for representing data securely between two parties; Reference : [JWT](https://jwt.io/)


## Designing Application authentication and authorization with JWT  

Below is a simple Flow of how the application can be designed to de-couple the Login (Authentication module) from the rest of the services. (***Note: For sake of simplicity, I've not included the API Gateway***).

The code is written in such a way you can plugin the necessary changes for SSO / separate User DB (like Redis / MongoDB) Or even Google FireBase accordingly. 

Also, for Roles, Enterprises rely more on LDAP based authentication as every application has multiple roles (countrywise requirements) to accomodate to. 

Once the Login is successful, the Token will be sent back to the Client which can be saved either in session storage, Local storage Or as Cookies. (all of them has it's own pros & cons).

Now for every subsequent requests, this token will be sent to all the other services which will acknowledge it and send the data accordingly.

![alt text](./images/jwt-architecture.jpg)


Sample JWT

``` eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwicm9sZSI6ImFkbWluIiwiaWF0IjoxNTE2MjM5MDIyfQ.TdwiOJdcka9cIC4tRrjbZxMKr6RjBUpZ4HBWnI60968FFJOH_v4Npe_x7je4r8T__K3dFxAdhvcIcFWEdzGLtA```

It's has 3 parts 
 - Header (contains the Algorithm etc)
 - Payload (JSON Key Value pair data, which is highly customizable as per our need)
 - Signature - claims to be digitally
   signed or integrity protected with a Message Authentication Code
   (MAC) and/or encrypted.

For the above token 
``` shell
#Header
{
  "alg": "HS512",
  "typ": "JWT"
}
#Payload
{
  "sub": "1234567890",
  "name": "John Doe",
  "role" : "admin",
  "iat": 1516239022
}

```
## Key Considerations

### Custom Authorization for API
JWT allows us to have **Custom Authorization for every REST API**

``` java
@PreAuthorize("@authorizationManager.isRoleMatching(#token, 'maker')")
@GetMapping("/protected/maker")
public ResponseEntity<String> makerAPI(@RequestHeader(name = "Authorization") String token) {
    log.info("This is a Maker-Protected API");
    return ResponseEntity.ok().body("Hello Maker - Welcome!!!");
}

```
The ```@PreAuthorize``` annotation has the ability to take SPEL (Spring Boot Expression Language) in which we can mention the Service class and it's method. In the above expression, it's clear that the header (**Authorization**) String is being checked if it has the role as maker or not. If it's true, then the request proceeds into the controller. Else a ***401-Forbidden Error*** is given to the client. 

This method can be encapsulated and expanded widely for various use cases - like roles, country wise access etc. Large Enterprises have these complexities, where a user can be a MAKER in US, but might have only VIEW access to Canada OR the roles will be varying even within states in US. If multiple API's are being fired from the Client end, only the necessary data can be populated to he UI and the rest can either be not shown or can be given an UnAuthorized access accordingly. 


### Symmetric Secret for signing and validation
Bye default the implementation for Signing the Token is a Plain Text Password. But this can be easily compromised from server end. So, we can increase the security by adopting ***Public-Private Key pair***. 

It's very simple - The Login service will encode the Token with Private Key, while the rest of the services will validate the signer using it's Public Key. Now, the key's are distributed, it becomes added challenge to obtain both of them. Additionally, Public Cloud and Enterprise Tools like CyberArk, Venafi systems provide the support to handle these keys in much secured way. 


---



## Additional Reference

### Creating a Valid Signer Certificate for JWT

``` shell
openssl req -newkey rsa:4096 \
            -x509 \
            -sha256 \
            -days 730 \
            -nodes \
            -out public.crt \
            -keyout private.key
```

Also - enter the details like Country, State, Organization, email-id (just to have a subject for the Certificate"

### Validating the Certificate

If the certificate is printed, it's in PEM format

``` shell
 openssl x509 -in public.crt -text
```

If it throw's Error, then it's in either DER / PKCS12 (Binary formats)

``` shell
# PKCS12 format check
openssl pkcs12 -in public.crt -info

# DER format check
openssl x509 -in public.crt -inform DER -text
```




