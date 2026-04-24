package ee.piperal.veebipood.controller;

import ee.piperal.veebipood.dto.OrderRowDto;
import ee.piperal.veebipood.dto.ParcelMachine;
import ee.piperal.veebipood.dto.PaymentUrl;
import ee.piperal.veebipood.entity.Order;
import ee.piperal.veebipood.entity.OrderRow;
import ee.piperal.veebipood.repository.OrderRepository;
import ee.piperal.veebipood.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderController {

    private OrderRepository OrderRepository;
    private OrderService orderService;
    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("parcel")
    public List<ParcelMachine> getParcelMachine(@RequestParam String country) {
        String url = "https://www.omniva.ee/locations.json";
        ParcelMachine[] response = restTemplate.exchange(url, HttpMethod.GET, null, ParcelMachine[].class).getBody();
        return Arrays.stream(response)
                .filter(e -> e.getA0_name().equals(country.toUpperCase()))
                .toList();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("order")
    public List<Order> getOrder() {
        return OrderRepository.findAll();
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("order/{id}")
    public List<Order> delOrder(@PathVariable Long id) {
        OrderRepository.deleteById(id);
        return OrderRepository.findAll();
    }

    @CrossOrigin(origins = "*")

    @PostMapping("order")
    public PaymentUrl addOrder(@RequestParam Long personId,
                          @RequestParam(required = false) String parcelMachine,
                          @RequestBody List<OrderRowDto> orderRows) {
        Order order = orderService.saveOrder(personId, parcelMachine, orderRows);
        return orderService.makePayment(order.getId(), order.getTotal());
    }

    /*@PostMapping("pay")
    public PaymentUrl makePayment(@RequestParam Long orderId, double sum){
        return orderService.makePayment(orderId, sum);
    }*/
}
