package ee.piperal.veebipood.controller;

import ee.piperal.veebipood.dto.Supplier0Product;
import ee.piperal.veebipood.dto.Supplier2Product;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class SupplierController {
    RestTemplate restTemplate = new RestTemplate();

    @GetMapping("supplier0")
    public List<Supplier0Product> getSupplier0() {
        String url = "https://fakestoreapi.com/products";
        Supplier0Product[] response = restTemplate.exchange(url, HttpMethod.GET, null, Supplier0Product[].class).getBody();
        assert response != null;
        return Arrays.stream(response).filter(e->e.getRating().getRate() > 4.0).toList();
    }

    @GetMapping("supplier1")
    public String getSupplier1() {
        String url = "https://api.escueljs.co/api/products";
        return restTemplate.exchange(url, HttpMethod.GET, null, String.class).getBody();
    }

    @GetMapping("supplier2")
    public List<Supplier2Product> getProductsSupplier2()  {
        String url = "https://api.escuelajs.co/api/v1/products";
        Supplier2Product[] response = restTemplate.exchange(url, HttpMethod.GET, null, Supplier2Product[].class).getBody();
        return Arrays.stream(response)
                .sorted(Comparator.comparing(Supplier2Product::getPrice))
                .toList();
    }
}
