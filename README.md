# Conversor de Moedas

Um serviço que permita que exponha o valor das mercadorias na moeda corrente do cliente.

## Regras de Negócio

- Ao efetuar a requisição, obter como resultado todos os valores nas moedas que atendemos para o valor do produto
- Os valores das moedas são: USD, EUR, INR

### O que considerar

- Futuramente outras moedas poderão ser incluídas.
- O código deve estar pronto para produção.
- Critérios: Organização, manutenibilidade, testabilidade, performance, monitoria e entendimento do problema.
- Documentação
- Testes automatizados.

## Ferramentas

- [Kotlin 1.8.22 (Java JDK 17)](https://kotlinlang.org/)
- [sourceCompatibility Java 17](https://openjdk.org/projects/jdk/17/)
- [Gradle 7.2](https://docs.gradle.org/7.2/release-notes.html)
- [Spring Boot version "3.1.1-SNAPSHOT"](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/html/getting-started.html#getting-started.installing)
- [Jakarta](https://beanvalidation.org/)

### API Externa

- [Exhange Rate API](https://www.exchangerate-api.com/)

### ENDPOINTS


- Conversão de moedas para EUR, INR e USD

```http
  GET http://localhost:9091/latest/BRL
```

| Parameter | Type     | Description                   |
| :-------- | :------- |:------------------------------|
| `base_code`      | `string` | **Required**. Código da moeda |

###### Resposta da requisição 200 OK

``` json
{
    "BRL": 1.0,
    "EUR": 0.1856,
    "INR": 17.1185,
    "USD": 0.2084
}
```


- Cálculo e conversão de um valor conforme o câmbio do dia para as moedas: EUR, INR e USD

```http
  GET http://localhost:9091/calculate/529.99
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `529.99`      | `BigDecimal` | **Required**. Valor que será convertido|


###### Resposta da requisição 200 OK

``` json
{
    "EUR": 98.36614399999999,
    "USD": 110.449916,
    "INR": 9072.633815000001
}
```

### TESTE DE COBERTURA
![Alt text](assets/codecoverage.png?raw=true "Teste de cobertura")

## Rodar Local (Linux Ubuntu)

Clonar o projeto

```bash
  git clone git@github.com:bianavic/converter.git
```

Acessar o diretorio do projeto

```bash
  cd converter
```

Build e Iniciar

```bash
  ./gradlew build  bootRun
```

#### Porta local

```bash
  localhost:8001
```

Realizar as chamadas pelo postman/insomnia

```bash

Conversão
GET CurrencyConversion http://localhost:9091/calculate/529.99
```


## Roadmap | Melhorias
- Excluir a chave da api para evitar problema de segurança
- Implementar testes: robot framework
- Implementar logs ex: log4j, slf4j, log4j2
- Sistema de análise de performance, monitoria, tracing ex: New Relic, Splunk, DataDog (avaliar gastos)
- Ajustar casas decimais aos valores monetários.
- Implementar uma pipeline ex: Jenkins
- Análise de código ex: SonarQube
- Organizar o código para produção: arquivo env ou application-properties (local, dev, qa e prod)