package ee.piperal.veebipood.controller;

import ee.piperal.veebipood.entity.Order;
import ee.piperal.veebipood.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderRepository OrderRepository;

    @GetMapping("order")
    public List<Order> getOrder(){
        return OrderRepository.findAll();
    }

    @DeleteMapping("order/{id}")
    public List<Order> delOrder(@PathVariable Long id){
        OrderRepository.deleteById(id);
        return OrderRepository.findAll();
    }

    @PostMapping("order/add")
    public List<Order> addOrder(@RequestBody Order order){
        OrderRepository.save(order);
        return OrderRepository.findAll();
    }
}
