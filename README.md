# 💹 financial_data_processor_3

Bu proje;

1. **Mock veri platformları** (TCP akışı + REST) üzerinden döviz kurlarını toplar.  
2. Ham kurları **Apache Kafka**'ya yayar.  
3. **Redis** üzerinde “raw / calculated” önbellekler.  
4. Anlık **hesaplanmış kurlar** üretir.  
5. *(Yol haritası)* Verileri **PostgreSQL** ve **OpenSearch**’e kalıcılaştırır.

---

## 1. Teknoloji Yığını

| Katman | Teknoloji / Sürüm |
|--------|-------------------|
| **Dil** | Java 17 |
| **Çatı** | Spring Boot 3.1 · Spring MVC · Spring Kafka |
| **Mesaj Kuyruğu** | Apache Kafka 3.4 (Docker) |
| **Önbellek** | Redis 7 |
| **Kalıcı DB** | PostgreSQL 15 (planlandı) |
| **Arama & Log** | OpenSearch + Filebeat (planlandı) |
| **Derleme** | Maven 3.9 · Lombok · Log4j2 |
| **Çalışma Ortamı** | Docker & Docker Compose |

---

## 2. Yüksek Düzey Akış

```mermaid
sequenceDiagram
    participant İstemci
    participant Controller
    participant Koordinatör
    participant Kafka
    participant Redis
    İstemci->>Controller: GET /platform1/subscribe?pair=EUR/USD
    Controller->>Koordinatör: publishRaw()
    Koordinatör->>Kafka: platform1-raw
    Kafka-->>Redis: raw:EUR/USD
    Kafka-->>HesapConsumer: ham veri
    HesapConsumer->>Redis: calculated
    HesapConsumer->>Kafka: calculated-rates
    Kafka-->>{Postgres · OpenSearch}: (planlı)
