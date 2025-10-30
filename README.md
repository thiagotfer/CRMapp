CRM Mobile - Protótipo Funcional para Android
Este repositório contém o código-fonte de um protótipo funcional de um aplicativo de CRM (Customer Relationship Management) para Android. O projeto foi desenvolvido de forma nativa utilizando Kotlin e Jetpack Compose, seguindo as melhores práticas de arquitetura moderna, como MVVM e um sistema de navegação robusto com Jetpack Navigation Compose.

O aplicativo simula as interações entre um Operador e um Cliente, apresentando duas visões (interfaces) distintas com funcionalidades específicas e ricas para cada perfil.

✨ Principais Funcionalidades
👨‍💼 Visão do Operador
A ferramenta do operador foi projetada para ser uma central de gerenciamento completa, focada em produtividade e contexto.

Dashboard de Clientes (CRM): Uma lista poderosa e interativa de clientes com:

Busca dinâmica por nome.

Filtros avançados combináveis por Status, Score e Tags.

Ordenação personalizável por Score ou Nome.

Visão 360° do Cliente: Tela de detalhes que exibe um histórico completo de todas as interações com o cliente, incluindo anotações salvas, mensagens de chat e campanhas recebidas, dando ao operador contexto total sobre a jornada do cliente.

Chat Integrado: Ferramenta de comunicação 1:1 com clientes, incluindo:

Feedback visual instantâneo de envio de mensagens.

Comandos Rápidos Personalizados (ex: /ola) que inserem templates de mensagem com o nome do cliente (Olá, {{name}}!).

Campanhas Estratégicas: Ferramenta para enviar mensagens em massa para segmentos de clientes (filtrados por status e score), com a opção de escolher o canal de envio:

Modo Campanha: Envia para a aba de "Novidades" do cliente.

Modo Chat: Envia como uma mensagem direta na conversa individual.

👤 Visão do Cliente
Uma interface limpa e intuitiva, focada em facilitar a comunicação com a empresa e o acesso a informações importantes.

Tela de Início (Home): Uma tela de boas-vindas com atalhos para as principais ações, como "Falar com Atendimento" e "Ver Promoções", proporcionando uma experiência de usuário mais agradável e guiada.

Canal de Comunicação Direto: Acesso fácil à tela de chat para conversar com a equipe de suporte.

Área de Novidades: Uma aba dedicada para visualizar campanhas e comunicados enviados pela empresa.

Deeplinks Internos: Suporte para links clicáveis dentro das mensagens de campanha que navegam o usuário para outras partes do app (ex: um link na campanha que leva diretamente para a tela de Perfil).

Perfil com Valor Agregado: Além dos dados básicos, o cliente tem uma seção útil de "Meus Cupons" para visualizar descontos e ofertas.

🛠️ Tecnologias e Arquitetura
O projeto foi construído com um stack moderno de desenvolvimento Android, focado em código declarativo, reativo e escalável.

Linguagem: Kotlin

UI: Jetpack Compose & Material 3

Arquitetura: MVVM (Model-View-ViewModel)

Navegação: Jetpack Navigation Compose (com NavHost e grafos aninhados para as barras de navegação)

Gerenciamento de Estado: State e MutableState do Compose, gerenciados de forma centralizada pelo ViewModel.

Assincronia: Kotlin Coroutines (viewModelScope) para operações simuladas.
