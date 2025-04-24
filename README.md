# 💹 financial_data_processor_3

Bu proje, farklı platformlardan gelen döviz kuru verilerini toplayarak Kafka aracılığıyla yayımlayan, Redis’te geçici olarak saklayan ve hesaplanmış verileri PostgreSQL ile OpenSearch’e aktaran bir mikroservis mimarisi örneğidir.

## 📦 Proje Yapısı ve Teknolojiler

- **Java 17** ve **Spring Boot 3.1**
- **Kafka** (Docker ile çalışır)
- **Redis 7** (raw & calculated cache için)
- **PostgreSQL** (hesaplanan verileri kalıcı olarak saklamak için - planlanıyor)
- **OpenSearch** (veri arama için - planlanıyor)
- **Filebeat** (log forwarding için - planlanıyor)
- **Docker** & **Docker Compose**

## 🔁 Akış

1. Kullanıcı, `GET /platform1/subscribe?pair=EUR/USD` gibi bir endpoint'e istek atar.
2. Bu istek sonucunda rastgele bir fiyat oluşturulur.
3. `Coordinator`, bu veriyi `RateKafkaProducer` aracılığıyla Kafka'ya gönderir.
4. Kafka'dan gelen raw-rate verisi Redis'e `raw:<pair>` şeklinde kaydedilir.
5. `RawRateKafkaConsumer` bu veriyi okur, `RateCalculator` ile hesaplanmış değerler üretir.
6. Hesaplanan değerler `calculated-rates` Kafka konusuna gönderilir.
7. (Planlı) `CalculatedRateConsumer`, bu verileri PostgreSQL ve OpenSearch’e aktaracaktır.

## 🛠 Kurulum

> Docker, Java 17, Maven ve Git yüklü olmalıdır.

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
