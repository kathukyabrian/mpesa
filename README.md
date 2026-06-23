![Build](https://img.shields.io/badge/build-passing-brightgreen)
![Coverage](https://img.shields.io/badge/coverage-92%25-green)
![Quality Gate](https://img.shields.io/badge/quality%20gate-passed-brightgreen)
![Security](https://img.shields.io/badge/security-no%20critical%20issues-green)
![Java](https://img.shields.io/badge/Java-21-orange)
![License](https://img.shields.io/badge/license-Apache%202.0-blue)

# MPESA Library

A lightweight Java library for integrating with Safaricom M-Pesa Daraja APIs.

Supports STK Push, transaction status queries, authentication, and callback processing with minimal configuration.

## Features
- Auth 
- STK Push
- Query Transaction Status
- Quick Configuration

## Installation
For Maven:
```xml
<dependency>
    <groupId>io.github.kathukyabrian</groupId>
    <artifactId>mpesa</artifactId>
    <version>1.0.3</version>
</dependency>
```

## Quick Start
### Configuration
- create a file called mpesa.properties
- save this file in classpath
```properties
short-code=
transaction-type=
identifier=
pass-key=
callback-url=
consumer-key=
consumer-secret=
auth-url=https://sandbox.safaricom.co.ke/oauth/v1/generate
payment-url=https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest
query-url=https://sandbox.safaricom.co.ke/mpesa/stkpushquery/v1/query
```

### Properties Description
| Property         | Description                                                                                                                  |
|------------------|------------------------------------------------------------------------------------------------------------------------------|
| short-code       | Unique code given by Mpesa to a merchant to accept payments.                                                                 |
| identifier       | Similar to shortcode.                                                                                                        |
| transaction-type | Either __CustomerPayBillOnline__ for Paybill or __CustomerBuyGoodsOnline__ for till.                                         |
| pass-key         | Provided by Daraja upon go live to be used to generate password during STK Push request.                                     |
| callback-url     | The URL that will receive the callback upon payment completion.                                                              |
| consumer-key     | Credentials provided by Daraja to authenticate the application.                                                              |
| consumer-secret  | Credentials provided by Daraja to authenticate the application.                                                              |
| auth-url         | The url provided by Daraja to be used to authenticate the application. Expects the __consumer key__ and __consumer secret__. |
| payment-url      | The url provided by Daraja to  be used to initiate USSD push request                                                         |
| query-url        | The url provided by Daraja to be used to query transaction status                                                            |

## Create Mpesa Config
```java
package x.com.config;
import io.github.kathukyabrian.core.ServiceRepository;
import io.github.kathukyabrian.core.factory.ServiceRepositoryFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MpesaConfig {
    private ServiceRepository serviceRepository;
    
    @Bean
    public ServiceRepository getServiceRepository() {
        if(serviceRepository == null){
            serviceRepository = ServiceRepositoryFactory.getServiceRepository();
        }
        return serviceRepository;
    }
    
    public ServiceRepository getInstance() {
        return this.serviceRepository;
    }
}
```

### Usage
- You are ready to use the library.

#### STK push
- on your service method use the __requestPayment()__ method to request payment.

```java
import io.github.kathukyabrian.core.Mpesa;import io.github.kathukyabrian.dto.MpesaSTKResponse;

MpesaSTKResponse response = Mpesa.requestPayment(1,"2547xxxxxxxx", "ACC-8271", "payment description");
```

#### Callback
- in your callback endpoint, use the __handleResult(MpesaResult mpesaResult)__ method to handle your callback

```java
import io.github.kathukyabrian.core.Mpesa;
import io.github.kathukyabrian.dto.MpesaPaymentResult;
import io.github.kathukyabrian.dto.result.MpesaResult;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/payments")
@RestController
public class PaymentResource {
    
    @PostMapping("/callback")
    public void handleCallback(@RequestBody MpesaResult mpesaResult) {
        MpesaPaymentResult result = Mpesa.handleResult(mpesaResult);
        // process callback
    }
}
```

## License
Apache 2.0