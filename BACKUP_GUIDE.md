# Guia de Backup Automático

## 📦 Sistema de Backup Implementado

O sistema realiza backup automático do PostgreSQL usando **pg_dump** gerando arquivos `.dump` comprimidos.

## ⚙️ Configuração

### Variáveis de Ambiente (Produção)

```bash
# Habilitar/desabilitar backup
BACKUP_ENABLED=true

# Diretório de armazenamento
BACKUP_DIRECTORY=/app/backups

# Schedule (cron expression)
BACKUP_CRON=0 0 3 * * ?  # 3h da manhã todos os dias

# Retenção em dias
BACKUP_RETENTION_DAYS=7
```

### Formato do Arquivo

```
backup_gestao_psi_20251007_030000.dump
       └─ nome BD ─┘ └─ timestamp ─┘
```

## 🔄 Schedule Padrão

- **Horário**: 3h da manhã (UTC em produção)
- **Frequência**: Diário
- **Formato**: Custom dump (comprimido)
- **Retenção**: 30 dias (dev) / 7 dias (prod)

## 📡 Endpoints da API

### Criar Backup Manual
```http
POST /api/backups/criar
```

**Response:**
```json
{
  "status": "success",
  "arquivo": "/app/backups/backup_gestao_psi_20251007_153045.dump",
  "mensagem": "Backup criado com sucesso"
}
```

### Listar Backups
```http
GET /api/backups/listar
```

**Response:**
```json
[
  {
    "nome": "backup_gestao_psi_20251007_030000.dump",
    "tamanho": 1048576,
    "dataModificacao": 1728285600000,
    "caminho": "/app/backups/backup_gestao_psi_20251007_030000.dump"
  }
]
```

### Download de Backup
```http
GET /api/backups/download/{nomeArquivo}
```

### Restaurar Backup
```http
POST /api/backups/restaurar/{nomeArquivo}
```

⚠️ **ATENÇÃO**: Restauração **apaga todos os dados** e restaura o backup!

## 🛠️ Comandos Manuais

### Criar Backup Manualmente
```bash
pg_dump -h localhost -p 5432 -U postgres -F c -b -v -f backup.dump gestao_psi
```

### Restaurar Backup Manualmente
```bash
pg_restore -h localhost -p 5432 -U postgres -d gestao_psi -c -v backup.dump
```

### Listar Conteúdo do Backup
```bash
pg_restore -l backup.dump
```

## 📋 Checklist de Backup

- [x] Scheduler configurado (Spring @Scheduled)
- [x] Backup automático diário
- [x] Formato comprimido (.dump)
- [x] Limpeza automática de backups antigos
- [x] API para backup manual
- [x] API para download de backups
- [x] API para restauração
- [x] Logs de execução

## 🔒 Segurança

1. **Apenas Admin** deve ter acesso aos endpoints de backup/restauração
2. Backups contêm **dados sensíveis** - armazenar com segurança
3. Senha do banco é passada via variável `PGPASSWORD` (não fica no log)
4. Railway: backups ficam em `/app/backups` (efêmero - considere volume externo)

## ☁️ Railway (Produção)

### Limitações
- Sistema de arquivos efêmero (backups perdidos no redeploy)
- **Solução**: Usar Railway Volume ou S3 para persistência

### Configurar Volume Railway
```bash
# Criar volume
railway volume create backups-volume

# Montar em /app/backups
railway volume mount backups-volume /app/backups
```

### Alternativa: Upload para S3
Adicionar serviço para enviar backups ao S3/Cloudflare R2 após criação.

## 📊 Monitoramento

- Logs mostram: `"Backup concluído com sucesso: /app/backups/backup_..."`
- Erros: `ERROR ... Erro ao executar backup automático`
- Limpeza: `"Backup antigo removido: backup_..."`

## 🚨 Troubleshooting

### "pg_dump: command not found"
- Instalar PostgreSQL client no container
- Dockerfile: `RUN apk add postgresql-client` (Alpine)

### "Backup timeout após 10 minutos"
- Banco muito grande, aumentar timeout no `BackupService.java`

### "Erro ao restaurar"
- Verificar versão do PostgreSQL (dump/restore devem ser compatíveis)
- Usar `pg_restore --list` para validar arquivo

## 📅 Exemplo de Cron Expressions

```
0 0 3 * * ?     # 3h todos os dias
0 0 */6 * * ?   # A cada 6 horas
0 0 0 * * 0     # Domingo à meia-noite
0 30 2 1 * ?    # Dia 1 de cada mês às 2:30
```

