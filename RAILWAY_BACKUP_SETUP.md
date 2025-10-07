# 🚂 Checklist: Configurar Backup no Railway

## ✅ Passo a Passo

### 1. Criar Volume Persistente
- [ ] Ir no Railway Dashboard → Seu Projeto
- [ ] Clicar em "Volumes" (menu lateral)
- [ ] "New Volume"
  - **Name**: `backups-volume`
  - **Mount Path**: `/app/backups`
- [ ] Salvar

### 2. Configurar Dockerfile
- [ ] Railway Dashboard → Settings → Deploy
- [ ] **Dockerfile Path**: `./Dockerfile.railway`
- [ ] Salvar

### 3. Adicionar Variáveis de Ambiente
Railway Dashboard → Settings → Variables:

```bash
BACKUP_ENABLED=true
BACKUP_DIRECTORY=/app/backups
BACKUP_CRON=0 0 3 * * ?
BACKUP_RETENTION_DAYS=7
```

### 4. Verificar Variáveis Existentes
Certifique-se que já existem:
- [ ] `DB_URL` (Railway fornece automaticamente)
- [ ] `DB_USER` (Railway fornece automaticamente)
- [ ] `DB_PASSWORD` (Railway fornece automaticamente)
- [ ] `JWT_SECRET`
- [ ] `CORS_ALLOWED_ORIGINS`
- [ ] `SPRING_PROFILES_ACTIVE=prod`

### 5. Fazer Deploy
- [ ] Fazer push das alterações
- [ ] Railway fará deploy automático
- [ ] Aguardar build (3-5 min)

### 6. Verificar Logs
```bash
# Nos logs do Railway, procure:
✅ "Backup automático desabilitado/habilitado"
✅ "Iniciando backup automático..."
✅ "Backup concluído com sucesso"
```

### 7. Testar API
```bash
# Criar backup manual
curl -X POST https://seu-app.railway.app/api/backups/criar \
  -H "Authorization: Bearer <seu-token>"

# Listar backups
curl -X GET https://seu-app.railway.app/api/backups/listar \
  -H "Authorization: Bearer <seu-token>"
```

## 📊 Monitoramento

### Verificar Backup Funcionando
1. Aguarde próximo dia às 3h AM (UTC)
2. Cheque logs: `"Backup concluído com sucesso"`
3. Verifique API: `/api/backups/listar`

### Verificar Volume
```bash
# No Railway CLI
railway run ls -lh /app/backups
```

## 🚨 Troubleshooting

### "pg_dump: command not found"
✅ **Solução**: Dockerfile.railway já inclui `postgresql-client`

### "Permission denied: /app/backups"
✅ **Solução**: Volume deve estar montado em `/app/backups`

### "Backup timeout"
✅ **Solução**: Banco grande, aumentar timeout em `BackupService.java`

### Backups sumindo após redeploy
✅ **Solução**: Certificar que Volume está montado corretamente

## 💡 Dicas

1. **Teste local primeiro**:
   ```bash
   docker-compose up
   curl -X POST http://localhost:8081/api/backups/criar
   ```

2. **Ajustar horário do backup**:
   - Railway usa UTC
   - 3h UTC = 0h BRT (horário de Brasília)
   - Ajuste `BACKUP_CRON` se necessário

3. **Download de backups**:
   - Use a API `/api/backups/download/{nome}`
   - Ou acesse Railway Dashboard → Volume → Browse Files

4. **Segurança**:
   - Backups só devem ser acessíveis por ADMIN
   - Descomente `@PreAuthorize` no controller

## 📅 Cron Examples

```bash
# Diferentes horários
0 0 3 * * ?     # 3h todos os dias (padrão)
0 0 2 * * ?     # 2h todos os dias (23h BRT)
0 0 */6 * * ?   # A cada 6 horas
0 0 0 * * 0     # Domingo à meia-noite
0 30 2 1 * ?    # Dia 1 de cada mês às 2:30
```

## 🎯 Próximos Passos (Opcional)

- [ ] Integrar com S3/Cloudflare R2 para backup off-site
- [ ] Adicionar notificação por email em caso de falha
- [ ] Implementar testes de restauração automáticos
- [ ] Dashboard de monitoramento de backups

