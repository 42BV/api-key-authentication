package nl._42.apikeyauthentication.autoconfigure.test;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/public-api/v1/hello")
    Map<String, Object> greet() {
        return Map.of("message", "hi");
    }

    @RequestMapping("/public-api/v1/goodbye")
    Map<String, Object> goodbye() {
        return Map.of("message", "goodbye");
    }

    @RequestMapping("/public-api/v1/sleep-well")
    Map<String, Object> sleepWell() {
        return Map.of("message", "sleep well");
    }

    @RequestMapping("/private-api/users")
    List<Map<String, Object>> users() {
        return List.of(
                Map.of("username", "John", "firstName", "John", "lastName", "Appleseed"),
                Map.of("username", "Rick", "firstName", "Rick", "lastName", "Astley")
        );
    }
}
