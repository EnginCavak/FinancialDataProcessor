@RestController
@RequestMapping("/platform1")
@RequiredArgsConstructor      // Lombok
public class Platform1Controller {

    private final Coordinator coordinator;

    @GetMapping("/subscribe")
    public String subscribe(@RequestParam String pair) {
        double value = ThreadLocalRandom.current().nextDouble(1, 100);
        coordinator.onRateAvailable("platform1-raw", pair, value);
        return "Subscribed & pushed rate: " + value;
    }
}
