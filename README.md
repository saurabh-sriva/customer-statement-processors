# Customer Statement Processor


### Introduction
Customer statement processor is a RESTFul API, which validates the list of customer statement records. 
These records are delivered in JSON format and passed as post method request to this API.

Sample JSON format of customer statement record is given below :

```json
{
   "records": [{
            "transactionReference": 123456,
            "accountNumber": "NLINGB124",
            "startBalance": 100,
            "mutation": -50,
            "description": "description",
            "endBalance": 50
        },
        {
            "transactionReference": 1234567,
            "accountNumber": "NLINGB1234",
            "startBalance": 100,
            "mutation": -50,
            "description": "description",
            "endBalance": 50
        }
       ]
  }

```
Before to save this record in database,these JSON record need to be validated according to following validation rule.

- All transaction reference should be unique.
- The end balance needs to be validated against the formula : `$ ( Start Balance +/- Mutation = End Balance )`

### Use Case

- API accepts batch of records as a JSON list as a sample format given above.
- Each records is validated according to validation rule as mentioned above.
- API returns batch of response. Sample response as given below.

```json
Response :
{
    "responses": [
        {
            "result": "DUPLICATE_REFERENCE",
            "errorRecords": [
                {
                    "reference": 12345679,
                    "accountNumber": "NL12345"
                }
            ]
        },
        {
            "result": "INCORRECT_END_BALANCE",
            "errorRecords": [
                {
                    "reference": 1234567899,
                    "accountNumber": "ABN12345"
                }
            ]
        },
        {
            "result": "DUPLICATE_REFERENCE",
            "errorRecords": [
                {
                    "reference": 12345678990,
                    "accountNumber": "ABN1234561111111111111"
                }
            ]
        }
    ]
}

```

### Pre Conditions

- API should have correct records in JSON list, If API has any incorrect records in JSON list, program will return `{"result" : "BAD_REQUEST", "errorRecords" : []}`

### Technical Details
    
  List of following technology stack is being used to develop this API.
  
  - Maven
  - Java 8
  - Jupiter (for Junit testing)
  - Spring boot
  - H2 database (In memory database)
  - JPA
  - JSON
  - Intellij IDEA
  
#### Database Tables :

Table Name : customer_record

Schema     : customer_statement_schema

| Colume Name            | Data Type     | Nullable |Constraint   |
| -----------------------| ------------- |----------|-------------|
| transaction_reference  | Bigint        | False    | Primary Key |
| account_number         | varchar(50)   | False    |             |
| description            | varchar(250)  | True     |             |
| start_balance          | decimal(19,2) | False    |             |
| mutation               | decimal(19,2) | False    |             |
| end_balance            | decimal(19,2) | False    |             |


### cURL and Response for following validations :

Sample cURL for validations to test the API code.

  - **Validation Rule 1 :** When there are no duplicate reference and correct end balance
  
```json

  curl --location --request POST 'http://localhost:8080/customerRecords' \
--header 'Content-Type: application/json' \
--data-raw '{
    "records": [
        {
            "transactionReference": 123456,
            "accountNumber": "NLINGB124",
            "startBalance": 100,
            "mutation": -50,
            "description": "description",
            "endBalance": 50
        },
        {
            "transactionReference": 1234567,
            "accountNumber": "ABN1234",
            "startBalance": 100,
            "mutation": -50,
            "description": "description",
            "endBalance": 50
        }
    ]
}'

```
```json
Response :
{
    "responses": [
        {
            "result": "SUCCESSFUL",
            "errorRecords": []
        },
        {
            "result": "SUCCESSFUL",
            "errorRecords": []
        }
    ]
}
```

- **Validation Rule 2 :** When there are duplicate reference and correct balance

```json
curl --location --request POST 'http://localhost:8080/customerRecords' \
--header 'Content-Type: application/json' \
--data-raw '{
    "records": [
        {
            "transactionReference": 123456,
            "accountNumber": "NLINGB124",
            "startBalance": 100,
            "mutation": -50,
            "description": "description",
            "endBalance": 50
        },
        {
            "transactionReference": 123456,
            "accountNumber": "ABN1234",
            "startBalance": 100,
            "mutation": -50,
            "description": "description",
            "endBalance": 50
        }
    ]
}'

```

```json

{
    "responses": [
        {
            "result": "DUPLICATE_REFERENCE",
            "errorRecords": [
                {
                    "reference": 123456,
                    "accountNumber": "NLINGB124"
                }
            ]
        },
        {
            "result": "DUPLICATE_REFERENCE",
            "errorRecords": [
                {
                    "reference": 123456,
                    "accountNumber": "ABN1234"
                }
            ]
        }
    ]
}

```

- **Validation Rule 3 :** When there are no duplicate reference and Incorrect balance

```json

curl --location --request POST 'http://localhost:8080/customerRecords' \
--header 'Content-Type: application/json' \
--data-raw '{
    "records": [
        {
            "transactionReference": 12345678,
            "accountNumber": "NLINGB1245",
            "startBalance": 100,
            "mutation": -50,
            "description": "description",
            "endBalance": 250
        }
    ]
}'

```

```json
{
    "responses": [
        {
            "result": "INCORRECT_END_BALANCE",
            "errorRecords": [
                {
                    "reference": 12345678,
                    "accountNumber": "NLINGB1245"
                }
            ]
        }
    ]
}

```

- **Validation Rule 4 :** When there are duplicate reference and Incorrect balance

```json

curl --location --request POST 'http://localhost:8080/customerRecords' \
--header 'Content-Type: application/json' \
--data-raw '{
    "records": [
        {
            "transactionReference": 123456,
            "accountNumber": "NLINGB124",
            "startBalance": 100,
            "mutation": -50,
            "description": "description",
            "endBalance": 250
        }
    ]
}'

```

```json
Response :
{
    "responses": [
        {
            "result": "DUPLICATE_REFERENCE_INCORRECT_END_BALANCE",
            "errorRecords": [
                {
                    "reference": 123456,
                    "accountNumber": "NLINGB124"
                },
                {
                    "reference": 123456,
                    "accountNumber": "NLINGB124"
                }
            ]
        }
    ]
}

```

- **Validation Rule 5 :** Error during parsing JSON

```json

curl --location --request POST 'http://localhost:8080/customerRecords' \
--header 'Content-Type: application/json' \
--data-raw '{
    "records": [
        {
            "transactionReference": 123456,
            "accountNumber": "NLINGB1244444444444444444444444444444444444444444444444444444444444444444444444444444444",
            "startBalance": null,
            "mutation": -50,
            "description": "description"
        }
    ]
}'

```

```json
Response :

{
    "responses": [
        {
            "result": "BAD_REQUEST",
            "errorRecords": []
        }
    ]
}
```

#### Check Records in tables :

To validate the records in H2 database, please enter the following URL in browser, enter the following details and click on Connect button.

- URL : http://localhost:8080/h2-console/login.jsp?jsessionid=e9a998910673fecf437d7565b869f2f1
- Driver Class : org.h2.Driver
- JDBC URL  : jdbc:h2:mem:test
- User Name : sa
- Password  :


### API Testing with Junit Suite

- **Junit Test :** To excute the Junit test cases, please run as a **jUnitTest**  of the Java class **CustomerRecordServiceImplTest** at directory structure **src/test/java**.

- **Integration Test :** To execute the Integration test cases, please run as as a **jUnitTest**  of the Java class **CustomerStatementProcessorServiceApplicationTests** at directory structure **src/test/java**.
  
 
