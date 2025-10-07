# Guia de Otimização de Memória

## 🚀 Configurações JVM para Produção

### Opção 1: Memória Mínima (256-512MB)
```bash
java -Xms256m -Xmx512m \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:+UseStringDeduplication \
  -XX:+UseCompressedOops \
  -XX:+UseCompressedClassPointers \
  -jar gestao-psi-backend.jar
```

### Opção 2: Memória Moderada (512MB-1GB)
```bash
java -Xms512m -Xmx1g \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:+UseStringDeduplication \
  -XX:+UseCompressedOops \
  -XX:+UseCompressedClassPointers \
  -XX:MaxMetaspaceSize=256m \
  -jar gestao-psi-backend.jar
```

### Opção 3: Memória Otimizada com ZGC (Java 17+)
```bash
java -Xms512m -Xmx1g \
  -XX:+UseZGC \
  -XX:+ZGenerational \
  -XX:+UseStringDeduplication \
  -XX:+UseCompressedOops \
  -XX:+UseCompressedClassPointers \
  -jar gestao-psi-backend.jar
```

## 📊 Explicação das Flags

### Heap Memory
- `-Xms256m`: Heap inicial (ajuste conforme disponibilidade)
- `-Xmx512m`: Heap máximo (evita crescimento descontrolado)

### Garbage Collector
- `-XX:+UseG1GC`: G1 Garbage Collector (baixa latência, boa para servidores)
- `-XX:+UseZGC`: ZGC (pausa < 10ms, requer Java 17+)
- `-XX:MaxGCPauseMillis=200`: Pausa máxima de GC de 200ms

### Otimizações de Memória
- `-XX:+UseStringDeduplication`: Deduplica strings idênticas (economiza ~10-15%)
- `-XX:+UseCompressedOops`: Comprime ponteiros de objetos (economiza ~30% em 64-bit)
- `-XX:+UseCompressedClassPointers`: Comprime ponteiros de classes
- `-XX:MaxMetaspaceSize=256m`: Limita metaspace (evita crescimento infinito)

## 🐳 Docker Configuration

```dockerfile
FROM openjdk:17-jdk-slim

ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+UseStringDeduplication -XX:+UseCompressedOops -XX:+UseCompressedClassPointers"

COPY target/gestao-psi-backend.jar app.jar

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app.jar"]
```

## 📈 Monitoramento de Memória

### Verificar uso de memória em tempo real:
```bash
# Ver estatísticas de GC
java -XX:+PrintGCDetails -XX:+PrintGCDateStamps -jar app.jar

# Native Memory Tracking
java -XX:NativeMemoryTracking=summary -jar app.jar
jcmd <pid> VM.native_memory summary
```

### Métricas importantes:
- Heap usado vs. total
- Frequência de GC
- Pausa de GC (deve ser < 200ms)
- Metaspace usado

## ⚙️ Otimizações Aplicadas no Backend

### 1. Connection Pool (HikariCP)
- `maximum-pool-size=10`: Máximo de 10 conexões simultâneas
- `minimum-idle=2`: Mínimo de 2 conexões ociosas
- Reduz overhead de conexões

### 2. JPA/Hibernate
- `batch_size=20`: Agrupa operações em lotes
- `order_inserts=true`: Ordena INSERTs para melhor performance
- `cache desabilitado`: Reduz memória (habilite se houver muitas leituras repetidas)

### 3. HTTP Compression
- Compressão GZIP ativada
- Reduz tráfego de rede em ~70%
- Mime-types configurados para JSON/JS/CSS

### 4. Logging
- Level WARN em produção
- INFO apenas para pacote da aplicação
- Reduz I/O e processamento

## 🎯 Resultados Esperados

Com essas otimizações:
- **Heap**: 256-512MB (vs. 1-2GB padrão)
- **Total JVM**: 350-700MB (vs. 1.5-3GB padrão)
- **GC Pause**: < 200ms (vs. 500ms+ padrão)
- **Throughput**: +15-20% (devido a batch processing)

## ⚠️ Considerações

1. **Ajuste conforme carga**: Se tiver muitos usuários simultâneos, aumente heap e pool size
2. **Monitore**: Use ferramentas como VisualVM, JConsole ou Java Mission Control
3. **Teste**: Sempre teste em ambiente similar ao de produção
4. **Backup**: Mantenha configuração padrão como fallback

## 🔧 Troubleshooting

### OutOfMemoryError: Java heap space
- Aumente `-Xmx` ou otimize queries que retornam muitos dados

### OutOfMemoryError: Metaspace
- Aumente `-XX:MaxMetaspaceSize` ou reduza classes carregadas

### GC overhead limit exceeded
- Aumente heap ou otimize algoritmo de GC

### Too many connections
- Aumente `maximum-pool-size` ou revise queries lentas

