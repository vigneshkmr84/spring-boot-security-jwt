# JWT example with Spring-Boot

### Creating a Valid Signer Certificate

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

IF the certificate is printed, it's in PEM format

``` shell
 openssl x509 -in public.crt -text
```

If it throw's Error, then it's in either DER/ PKCS12 (Binary formats)

``` shell
# PKCS12 format check
openssl pkcs12 -in public.crt -info

# DER format check
openssl x509 -in public.crt -inform DER -text
```




