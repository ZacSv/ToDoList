📝 ToDoList App - Android
Um gerenciador de tarefas moderno desenvolvido em Kotlin com Jetpack Compose, focado em demonstrar uma arquitetura reativa utilizando a infraestrutura do Firebase.
✨ Funcionalidades
Autenticação Segura: Login e cadastro via Firebase Auth com persistência de sessão (não desloga ao fechar o app).

Gestão de Tarefas (CRUD): Criação, edição, conclusão e exclusão de tarefas.

Sincronização Real-time: Interface atualizada instantaneamente via Firestore Snapshot Listeners.

Modo Escuro Dinâmico: Suporte nativo a Dark e Light Mode.

Feedback ao Usuário: Gerenciamento de estados de carregamento e erros via Snackbars.

🏗️ Arquitetura e Tech Stack
O projeto segue os princípios da Clean Architecture (simplificada) e o padrão MVVM (Model-View-ViewModel).

UI: Jetpack Compose para uma interface declarativa.

Injeção de Dependência: Hilt para desacoplamento e testabilidade.

Persistência: Cloud Firestore para banco de dados NoSQL em nuvem.

Assincronismo: Coroutines e Flow para fluxos de dados reativos.

Decisões Técnicas de Destaque
Callback to Flow: Conversão de listeners do Firebase para callbackFlow, permitindo tratar o banco de dados como um fluxo de dados contínuo.

Eventos de Disparo Único: Uso de Sealed Classes e LaunchedEffect para garantir que navegações e alertas não sejam disparados repetidamente durante a recomposição da UI.

🛠️ Como rodar o projeto
Clone o repositório
Configure o Firebase:

Vá até o Firebase Console.

Adicione um novo projeto Android com o package name com.example.todolist.

Baixe o arquivo google-services.json e coloque-o na pasta app/.

No console, ative o Authentication (E-mail/Senha) e o Firestore Database.

Execute o App:

Abra o projeto no Android Studio e rode no seu emulador ou dispositivo físico.

🚀 Melhorias Futuras
[ ] Implementação de Room Database para cache offline avançado.

[ ] Notificações push para lembretes de tarefas via FCM.

[ ] Categorização de tarefas com cores e ícones customizados.

Licença
Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.
