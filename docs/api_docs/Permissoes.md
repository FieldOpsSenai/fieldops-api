## 🔒 Perfis e Permissões (RBAC)

A API utiliza autenticação via JWT e autorização baseada em roles/perfis.

| Endpoint | Método | Descrição | Perfis Permitidos |
| :--- | :---: | :--- | :--- |
| `/api/v1/auth/login` | `POST` | Autenticação do usuário | Público |
| `/api/v1/auth/refresh` | `POST` | Atualização do Access Token | Público |
| `/api/v1/usuarios` | `GET` | Listar todos os usuários | `ADMINISTRADOR` |
| `/api/v1/usuarios/{id}` | `GET` | Buscar usuário por ID | `ADMINISTRADOR` |
| `/api/v1/usuarios` | `POST` | Cadastrar novo usuário | `ADMINISTRADOR` |
| `/api/v1/clientes/**` | `GET/POST/PUT` | Gestão de Clientes | `ADMINISTRADOR`, `TECNICO` |
| `/api/v1/equipamentos/**` | `GET/POST/PUT` | Gestão de Equipamentos | `ADMINISTRADOR`, `TECNICO` |
| `/api/v1/inspecoes/**` | `GET/POST/PUT` | Registros de Inspeção | `ADMINISTRADOR`, `TECNICO` |

### Regras de Acesso
- **ADMINISTRADOR**: Acesso total ao sistema, gerenciamento de usuários e auditoria.
- **TECNICO**: Acesso às rotas operacionais (Clientes, Locais, Equipamentos, Inspeções).