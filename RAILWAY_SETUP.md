# 🚀 Configuração Railway - Guia Completo

## ❌ **Problema Identificado**
A URL de conexão estava malformada: `jdbc:postgresql://:/?sslmode=require`
Isso acontecia porque as variáveis de ambiente não estavam sendo resolvidas corretamente.

## ✅ **Solução Aplicada**
Simplificamos a configuração para usar diretamente as variáveis do Railway.

## 🔧 **Variáveis de Ambiente Necessárias no Railway**

### 1. **Acesse o Dashboard do Railway**
- Vá para seu projeto no Railway
- Clique em "Variables" na aba lateral

### 2. **Configure as Variáveis Obrigatórias**

```env
# Perfil da aplicação
SPRING_PROFILES_ACTIVE=prod

# Banco de Dados PostgreSQL (variáveis individuais)
PGHOST=${{Postgres.RAILWAY_PRIVATE_DOMAIN}}
PGPORT=${{Postgres.PGPORT}}
PGDATABASE=${{Postgres.PGDATABASE}}
PGUSER=${{Postgres.PGUSER}}
PGPASSWORD=${{Postgres.PGPASSWORD}}

# JWT Secret
JWT_SECRET=x3Q8zvDks7pR9G2Nf4bC1mX8Yt6uVa5wZq7Lr9P0sT2U4V6X8Z0a2c4e6g8i0k2

# CORS
CORS_ALLOWED_ORIGINS=https://gestao-psi-blush.vercel.app
```

### 3. **Variáveis Opcionais (Backup)**
```env
BACKUP_ENABLED=true
BACKUP_DIRECTORY=/app/backups
BACKUP_CRON=0 0 3 * * ?
BACKUP_RETENTION_DAYS=7
```

## 🔍 **Como Verificar se Está Funcionando**

### 1. **Logs de Inicialização**
Procure por:
```
Started PrdApplication in X.XXX seconds
```

### 2. **Logs de Conexão com Banco**
Procure por:
```
HikariPool-1 - Starting...
HikariPool-1 - Started.
```

### 3. **Teste o Health Check**
Acesse: `https://seu-app.railway.app/api/actuator/health`

## 🚨 **Problemas Comuns e Soluções**

### **Erro: "Driver claims to not accept jdbcUrl"**
- ✅ **Solução**: Verifique se `DATABASE_URL` está configurada corretamente
- ✅ **Formato correto**: `jdbc:postgresql://host:port/database?sslmode=require`

### **Erro: "InvalidConfigDataPropertyException"**
- ✅ **Solução**: Já corrigido - removemos `spring.profiles.active` do arquivo

### **Erro: "Connection refused"**
- ✅ **Solução**: Verifique se o banco PostgreSQL está rodando no Railway

## 📋 **Checklist de Deploy**

- [ ] Variáveis de ambiente configuradas no Railway
- [ ] Projeto deployado no Railway
- [ ] Logs mostram "Started PrdApplication"
- [ ] Health check responde com status 200
- [ ] Conexão com banco estabelecida

## 🎯 **Próximos Passos**

1. **Configure as variáveis** no Railway
2. **Faça o redeploy** da aplicação
3. **Verifique os logs** para confirmar a inicialização
4. **Teste o endpoint** de health check

## 📞 **Se Ainda Houver Problemas**

Envie os logs completos de erro para análise adicional.
