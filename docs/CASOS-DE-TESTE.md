# Casos de teste

Os testes cobrem duas funcionalidades do Automation Exercise: login com dados incorretos e cadastro de usuário. Foram escolhidas cinco entradas para cada uma.

Nas tabelas, `E` é um e-mail gerado no teste, no formato `teste.<UUID>@example.com`. No login, ele é usado como um endereço não cadastrado. No cadastro, serve para criar uma conta nova. `""` indica um campo vazio.

## Login com e-mail e senha incorretos

As classes consideradas foram: e-mail com formato válido e sem cadastro, e-mail com formato inválido, senha preenchida e senha vazia. Para análise de valor limite, foram usadas senhas com zero, um e dois caracteres. O limite analisado é o preenchimento obrigatório, não o tamanho mínimo de uma senha válida para autenticação.

| Caso | E-mail | Senha | Critério | Resultado esperado |
| --- | --- | --- | --- | --- |
| L01 | E | `senhaIncorreta123` | E-mail não cadastrado e senha incorreta | Mostrar `Your email or password is incorrect!` |
| L02 | E | `x` | Senha com um caractere, no limite de preenchimento | Mostrar a mesma mensagem de erro |
| L03 | E | `xy` | Senha com dois caracteres, próxima do limite | Mostrar a mesma mensagem de erro |
| L04 | E | `""` | Senha com zero caracteres, abaixo do limite | O navegador deve bloquear o envio por falta de senha |
| L05 | `emailsemarroba.com` | `senhaIncorreta123` | E-mail com formato inválido | O navegador deve bloquear o envio pelo formato do e-mail |

L01, L02 e L03 seguem o Test Case 3: abrir o navegador, verificar a página inicial, clicar em Signup / Login, verificar Login to your account, preencher os dados, clicar Login e conferir a mensagem de erro.

L04 e L05 verificam os campos antes do envio. O teste lê `validity.valueMissing` para a senha vazia e `validity.typeMismatch` para o e-mail inválido. Nesses casos, a página deve continuar em `/login`, sem mensagem de autenticação do servidor e sem usuário conectado.

As três senhas preenchidas são variações da mesma classe de equivalência. Elas não foram contadas como três classes diferentes.

## Registrar usuário

As classes consideradas foram: nome preenchido, nome vazio, e-mail novo com formato válido e e-mail com formato inválido. Para análise de valor limite, foram usados nomes com zero, um e dois caracteres.

| Caso | Nome | E-mail | Critério | Resultado esperado |
| --- | --- | --- | --- | --- |
| R01 | `Pedro Teste` | E | Nome preenchido e e-mail novo com formato válido | Criar a conta, verificar o usuário conectado e excluir a conta |
| R02 | `P` | E | Nome com um caractere, no limite de preenchimento | Completar o cadastro e excluir a conta |
| R03 | `Pe` | E | Nome com dois caracteres, próximo do limite | Completar o cadastro e excluir a conta |
| R04 | `""` | E | Nome com zero caracteres, abaixo do limite | O navegador deve bloquear o envio por falta de nome |
| R05 | `Pedro Teste` | `emailsemarroba.com` | E-mail com formato inválido | O navegador deve bloquear o envio pelo formato do e-mail |

R01, R02 e R03 seguem o Test Case 1. Depois de verificar a página inicial e abrir Signup / Login, o teste confere New User Signup!, preenche nome e e-mail e clica Signup. Na etapa seguinte, confere Enter Account Information e os valores de nome e e-mail.

O teste seleciona o título Mr., preenche a senha e a data de nascimento, e marca as opções de newsletter e ofertas. Também preenche todos os campos de endereço pedidos no cenário:

| Campo | Valor usado |
| --- | --- |
| Senha | `SenhaTeste123` |
| Data de nascimento | 15/06/2000 |
| First name | Pedro |
| Last name | Teste |
| Company | Empresa Teste |
| Address | Rua Teste 123 |
| Address2 | Apartamento 1 |
| Country | Canada |
| State | Ontario |
| City | Toronto |
| Zipcode | M5V 1A1 |
| Mobile Number | 2025550100 |

Ao clicar Create Account, o teste deve encontrar Account Created!. Depois de clicar Continue, confere Logged in as e o nome informado. Por fim, clica Delete Account, verifica Account Deleted!, clica Continue e confere o retorno à página inicial sem usuário conectado.

R04 e R05 ficam na primeira etapa do cadastro. O teste verifica `valueMissing` no nome vazio e `typeMismatch` no e-mail inválido. A página deve continuar em `/login`, sem confirmação de criação e sem usuário conectado.

## Sobre os limites escolhidos

Os campos iniciais de nome, e-mail e senha de login têm preenchimento obrigatório. Os e-mails também precisam passar pela validação de formato do navegador. Esses campos não declaram `minlength` ou `maxlength`, por isso a análise usa a fronteira entre campo vazio e preenchido. Não foi definido um limite máximo sem uma regra do site que o informe.

Os casos com um e dois caracteres verificam o comportamento esperado nessa fronteira. No login, preencher a senha permite enviar o formulário, mas as credenciais continuam incorretas. No cadastro, a expectativa é que nomes não vazios sejam aceitos. Os resultados ainda precisam ser confirmados na execução.

## Conferência dos resultados

Após executar `mvn clean test`, conferir os relatórios em `target/surefire-reports/`. O total esperado é dez testes. Um caso passa quando todas as suas verificações passam; se houver falha, registrar o caso e a diferença entre o resultado esperado e o observado. Problemas de navegador, rede ou espera também precisam ser conferidos antes de concluir que há um defeito no site.

Referência: [Test Cases 1 e 3](https://automationexercise.com/test_cases).
