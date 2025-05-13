# 💹 financial_data_processor_3

Bu proje;  
* birden fazla **mock** platformdan (REST + TCP) **döviz kuru** toplar,  
* ham kurları **Apache Kafka** üzerine yayınlar,  
* **Redis**’te önbelleğe alır,  
* gerçek‑zamanlı **hesaplanmış kurlar** (bid / ask / mid vb.) üretir,  
* (yol haritası) verileri **PostgreSQL** ve **OpenSearch**’e kalıcılaştırır.

---

## 📦 Teknoloji Yığını

| Katman / Amaç | Kullanılan Teknoloji & Sürüm |
|---------------|-----------------------------|
| Dil & Çatı | Java 17 • Spring Boot 3.1 (MVC, Kafka) |
| Mesaj Kuyruğu | Apache Kafka 3.4 *(Docker servisi)* |
| Önbellek | Redis 7 |
| Kalıcı DB | PostgreSQL 15 *(planlanıyor)* |
| Arama / Log | OpenSearch + Filebeat *(planlanıyor)* |
| Derleme | Maven 3.9 • Lombok • Log4j2 |
| Ortam | Docker & Docker Compose |

---

## 🔁 Veri Akışı

```mermaid
sequenceDiagram
    participant İstemci
    participant Controller
    participant Koordinatör
    participant Kafka
    participant Redis
    participant HesapConsumer as Hesap&nbsp;Consumer

    İstemci->>Controller: GET /platform1/subscribe?pair=EUR/USD
    Controller->>Koordinatör: publishRaw()
    Koordinatör->>Kafka: topic **platform1-raw**
    Kafka-->>Redis: key **raw:EUR/USD**
    Kafka-->>HesapConsumer: ham veri
    HesapConsumer->>Redis: key **calculated:EUR/USD**
    HesapConsumer->>Kafka: topic **calculated-rates**
    Kafka-->>PostgreSQL: (planlı)
    Kafka-->>OpenSearch: (planlı)
