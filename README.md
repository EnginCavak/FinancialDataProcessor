# 💹 financial_data_processor_3

Bu proje;

* birden fazla mock platformdan (TCP akışı + REST) döviz kuru toplar,
* ham kurları **Kafka**’ya yayar,
* **Redis**’te önbelleğe alır,
* anlık olarak hesaplanmış kurları üretir,
* (yol haritası) verileri **PostgreSQL** ve **OpenSearch**’e kalıcılaştırır.

---

## 📦 Teknoloji Yığını

| Katman | Kullanılan Teknoloji / Sürüm |
|--------|-----------------------------|
| Dil | **Java 17** |
| Çatı | **Spring Boot 3.1** • Spring MVC • Spring Kafka |
| Mesaj Kuyruğu | **Apache Kafka 3.4** (Docker) |
| Önbellek | **Redis 7** |
| Kalıcı DB | **PostgreSQL 15** *(planlandı)* |
| Arama / Log | **OpenSearch + Filebeat** *(planlandı)* |
| Derleme | **Maven 3.9** • Lombok • Log4j2 |
| Çalışma Ort. | **Docker & Docker Compose** |

---

## 🔁 Çalışma Akışı (Özet)

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
    Kafka-->>{Postgres • OpenSearch}: (planlı)
## 🛠 Kurulum

> Docker, Java 17, Maven ve Git yüklü olmalıdır

```bash
# 1. Repoyu klonla
git clone https://github.com/kullaniciadi/financial_data_processor_3.git
cd financial_data_processor_3

# 2. Gerekli Docker konteynerlerini başlat
docker compose up -d

# 3. Uygulamayı başlat
mvn clean install -DskipTests
mvn spring-boot:run -Dspring-boot.run.fork=false

curl "http://localhost:8080/platform2/subscribe?pair=EUR%2FUSD"
