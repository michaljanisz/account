# account

Simple java project modelling money transfers between accounts and exposing rest endpoints to perform a money transfer. Uses java 11 and java native http server, no authentication implemented. 
Used libs:
* [dagger2](https://github.com/google/dagger) for DI
* [mjson](https://bolerio.github.io/mjson/) for json parsing
* [concurrent-junit](https://github.com/ThomasKrieger/concurrent-junit) for concurrency tests

Exposed endpoints:


1. **POST /account**
```
{
  "number":"1212",
  "balance":200,
  "currency":"EUR"
}
```
response includes id

2. **GET /account/{id}**

```
{
   "number":"1213",
   "balance":200,
   "currency":"EUR",
   "id":"cee70d3c-65a2-4485-af42-d0d5c3af76b7"
}
```

3. **POST /transfer**
```
{
	"from_account_id": "c2dd170e-100d-4460-883d-73918f6a50a3",
	"to_account_id": "cee70d3c-65a2-4485-af42-d0d5c3af76b7",
	"amount": 100
}
```
response includes id
