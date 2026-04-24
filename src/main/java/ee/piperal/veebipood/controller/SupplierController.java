package ee.piperal.veebipood.controller;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@CrossOrigin(origins = "*")
@RestController
public class SupplierController {

    @GetMapping("supplier")
    public String getSupplier() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://fakestoreapi.com/products";
        return restTemplate.exchange(url, HttpMethod.GET, null, String.class).getBody();
    }
}
