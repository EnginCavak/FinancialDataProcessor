import org.springframework.stereotype.Component;


@Component
public class RawRateKafkaConsumer {

    private final RateCalculator calculator;
    private final RateKafkaProducer producer;

    public RawRateKafkaConsumer(RateCalculator calculator,
                                RateKafkaProducer producer) {
        this.calculator = calculator;
        this.producer   = producer;
    }

    @KafkaListener(topics = "platform1-raw", groupId = "raw-consumer")
    public void listen(ConsumerRecord<String,String> record) {
        double raw = Double.parseDouble(record.value());
        // compute the full RateFields
        RateFields fields = calculator.calculate(raw);
        // forward to calculated‐rates
        producer.sendCalculatedRate(record.key(), fields);
    }
}
