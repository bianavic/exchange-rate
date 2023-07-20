# Conversor de Moedas

Um serviço que exponha o valor das mercadorias na moeda corrente do cliente.

<p align="">
     <a alt="Java">
        <img src="https://img.shields.io/badge/Java-v17-blue.svg" />
    </a>
     <a alt="Kotlin">
        <img src="https://img.shields.io/badge/Kotlin-v1.8.22-purple.svg" />
    </a>
    <a alt="Spring Boot">
        <img src="https://img.shields.io/badge/Spring_Boot-3.1.1-F2F4F9?logo=spring-boot" />
    </a>
    <a alt="Gradle">
        <img src="https://img.shields.io/badge/Gradle-v7.2-lightgreen.svg" />
    </a>
    <a alt="Docker">
        <img src="https://img.shields.io/badge/Docker-2CA5E0?logoColor=white" />
    </a>
    <a alt="Swagger">
        <img src="https://img.shields.io/badge/Swagger-85EA2D?logoColor=white" />
    </a>
    <a alt="OpenFeign">
        <img src="https://img.shields.io/badge/OpenFeign-darkblue.svg" />
    </a>
    <a alt="JUnit">
        <img src="https://img.shields.io/badge/Junit-5-25A162?logoColor=white" />
    </a>
   <a alt="Ubuntu">
        <img src="https://img.shields.io/badge/Ubuntu-E95420?ubuntu&logoColor=white" />
    </a>
    <a alt="JaCoCo">
        <img src="https://img.shields.io/badge/Jacoco-0.8.8-&logoColor=darkblue.svg" />
    </a>
    <a alt="Wiremock">
        <img src="https://img.shields.io/badge/Wiremock-2.35.0-&logoColor=blue" />
    </a>
    <a alt="Mockk">
        <img src="https://img.shields.io/badge/Mockk-1.13.5-darkblue.svg" />
    </a>
    <a alt="Mockito">
        <img src="https://img.shields.io/badge/Mockito-5.2.0-darkblue.svg" />
    </a>
</p>

## Regras de Negócio

- Ao efetuar a requisição, obter como resultado todos os valores nas moedas que atendemos para o valor do produto
- Os valores das moedas são: USD, EUR, INR

### O que considerar

- Futuramente outras moedas poderão ser incluídas.
- O código deve estar pronto para produção.
- Critérios: Organização, manutenibilidade, testabilidade, performance, monitoria e entendimento do problema.
- Documentação
- Testes automatizados.

 
### API Externa

- [Documentação ExhangeRate-API](https://www.exchangerate-api.com/docs/overview)

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
![Alt text](assets/jacoco.png?raw=true "Teste de cobertura")

## Configuração (ambiente Unix)

Clonar o projeto

```bash
  git clone git@github.com:bianavic/converter.git
```

Acessar o diretório do projeto

```bash
  cd converter
```

Build and run

```bash
  docker build -t calculator:latest .
```

```bash
  docker run -p 8001:8001 calculator
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

## Documentação do Swagger
Para visualizar a documentação da API e realizar testes com outros valores, acesse: [Documentação do Swagger](http://localhost:8001/swagger-ui/index.html#/)

## Roadmap | Melhorias
- Implementar logs
- Sistema de análise de performance, monitoria, tracing
- Ajustar casas decimais aos valores monetários.
- Implementar uma pipeline
- Análise de código
- Código para produção