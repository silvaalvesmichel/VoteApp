Para Rodar o Projeto:

Projeto Desenvolvido em: 
-Spring Boot versão: 2.5.4.
-Java: Version: 16
-Type: Maven.
-Banco: Mysql.
-Menssageria: AWS SQS
-Docker compose: para subir instancia do localstack e Mysql. 

1-Para excutar o projeto acessar a pasta local com o cmd e executar o comando -  docker compose up
2-Abrir o https://app.localstack.cloud/dashboard e ir em SQS e criar a fila - active-session
3-Acessar com o cmd a pasta raiz do projeto e executar o comando - mvn clean install.
4-Rodar a API userValidation (urlAPI - ) (api externa que essa faz faz integração)
5-Antes de Executar a classe VoteApplication colocar o seguinte valor em Enviroment variables =  SPRING_PROFILES_ACTIVE=local;

Na pasta local contem a collection, aonde contem todas as chamadas dos serviços rest..
Postman: https://www.postman.com/downloads/ -> para testar endPoints

Ordem dos Endpoints: Informacoes de corpo de requisicao consultar collection do postman que adicionei ao projeto -> bill-of-sale.postman_collection.json

1 - Cadastrar usuario: method:POST -> http://localhost:8080/api/users

2 - Fazer Login para recuperar o Token JWT para consumir os outros end-points: method:POST -> http://localhost:8080/login

3 - Recuperar usuario pelo id: method:GET -> http://localhost:8080/api/users/{id}

4 - Cadastrar Pauta: method:POST -> http://localhost:8080/api/agenda

5 - Recuperar Pauta: GET -> http://localhost:8080/api/agenda/{id}

6 - Cadastrar Sessao relacionado a Pauta: POST -> http://localhost:8080/api/session

7 - Recuperar Sessao pelo id: GET -> http://localhost:8080/api/session/{id}

8 - Cadastrar voto do associado: POST -> http://localhost:8080/api/vote

9 - Recuperar resultado ou status da Sessao: GET -> http://localhost:8080/api/session/status/{id}

Objetivo do Projeto.
No cooperativismo, cada associado possui um voto e as decisões são tomadas em assembleias, por votação. 

-Cadastro de usuário
-Cadastrar uma nova pauta.
-Abrir uma sessão de votação em uma pauta (a sessão de votação deve ficar aberta por um tempo determinado na chamada de abertura ou 1 minuto por default)
-Receber votos dos associados em pautas (os votos são apenas 'Sim'/'Não'. Cada associado é identificado por um id único e pode votar apenas uma vez por pauta)
-Contabilizar os votos e dar o resultado da votação na pauta
-Integração com api externa para validar CPF.
-Mensageria para fechar sessão de votação.

