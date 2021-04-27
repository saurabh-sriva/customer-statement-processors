# Customer Statement Processor

### Introduction

Customer statement processor is a RESTFul API, which validates the list of customer statement records. These records are
delivered in JSON format and passed as post method request to this API.

Sample JSON format of customer statement record is given below :

```json
{
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
      "accountNumber": "NLINGB1234",
      "startBalance": 100,
      "mutation": -50,
      "description": "description",
      "endBalance": 50
    }
  ]
}

```

Before to validate these records in the List,these JSON record need to be validated according to following validation
rule.

- All transaction reference should be unique.
- The end balance needs to be validated against the formula : `$ ( Start Balance +/- Mutation = End Balance )`

### Use Case

- API accepts batch of records as a JSON list as a sample format given above.
- Each records are validated according to validation rule as mentioned above. Sample response as given below.

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
}

```

### Pre Conditions

- API should have correct records in JSON list, If API has any incorrect records in JSON list, program will
  return `{"result" : "BAD_REQUEST", "errorRecords" : []}`

### Technical Details

List of following technology stack is being used to develop this API.

- Maven
- Java 8
- Junit
- Spring boot
- JPA
- JSON
- Intellij IDEA

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

### API Testing with Junit Suite

- **Junit Test :** To excute the Junit test cases, please run as a **jUnitTest**  of the Java class **
  CustomerRecordServiceImplTest** at directory structure **src/test/java**.

- **Integration Test :** To execute the Integration test cases, please run as as a **jUnitTest**  of the Java class **
  CustomerStatementProcessorServiceApplicationTests** at directory structure **src/test/java**.
  
 
