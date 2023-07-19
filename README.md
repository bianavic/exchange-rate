# Conversor de Moedas

Um serviço que exponha o valor das mercadorias na moeda corrente do cliente.

## Regras de Negócio

- Ao efetuar a requisição, obter como resultado todos os valores nas moedas que atendemos para o valor do produto
- Os valores das moedas são: USD, EUR, INR

### O que considerar

- Futuramente outras moedas poderão ser incluídas.
- O código deve estar pronto para produção.
- Critérios: Organização, manutenibilidade, testabilidade, performance, monitoria e entendimento do problema.
- Documentação
- Testes automatizados.

## Ferramentas, bibliotecas

- [Kotlin 1.8.22 (Java JDK 17)](https://kotlinlang.org/)
- [sourceCompatibility Java 17](https://openjdk.org/projects/jdk/17/)
- [Gradle 7.2](https://docs.gradle.org/7.2/release-notes.html)
- [Spring Boot version "3.1.1-SNAPSHOT"](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/html/getting-started.html#getting-started.installing)
- [Jakarta](https://beanvalidation.org/)
- [Docker](https://docs.docker.com/)
- [JaCoCo](https://www.jacoco.org/jacoco/trunk/doc/)
- [OpenFeign](https://spring.io/projects/spring-cloud-openfeign)
- [Gson](https://github.com/google/gson)
- [Log4j2](https://logging.apache.org/log4j/2.x/)
- [Wiremock](https://wiremock.org/)
- [JUnit5](https://junit.org/junit5/)
- [Mockk](https://mockk.io/)

obs: LINUX UBUNTU
 
### API Externa

- [Exhange Rate API](https://www.exchangerate-api.com/)

### ENDPOINTS


* ### Conversão

```http
  GET http://localhost:8001/latest/{baseCode}
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

* ### Cálculo

```http
  GET http://localhost:8001/calculate/{amount}
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

### COBERTURA DE TESTES
![Alt text](assets/codecoverage.png?raw=true "Teste de cobertura")

## Rodar o projeto (Linux Ubuntu)

Clonar o projeto

```bash
  git clone git@github.com:bianavic/converter.git
```

Acessar o diretório do projeto

```bash
  cd converter
```

### [Docker] build and run

```bash
  docker build -t calculator:latest .
```

```bash
  docker run -p 8001:8001 calculator
```

### [sem Docker] build and run

```bash
  ./gradlew build bootRun
```

#### Porta local

```bash
  localhost:8001
```

### Realizar as chamadas

CONVERSÃO
```bash
curl --location 'http://localhost:8001/latest/BRL'
```

CÁLCULO
```bash
curl --location 'http://localhost:8001/calculate/529.99'
```

## Roadmap | Melhorias
- Excluir a chave da api (problemas de segurança)
- Implementar logs
- Sistema de análise de performance, monitoria, tracing
- Ajustar casas decimais aos valores monetários.
- Implementar uma pipeline
- Análise de código
- Código para produção