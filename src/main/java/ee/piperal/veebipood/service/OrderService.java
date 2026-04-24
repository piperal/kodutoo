package ee.piperal.veebipood.service;

import ee.piperal.veebipood.dto.EveryPayBody;
import ee.piperal.veebipood.dto.EveryPayResponse;
import ee.piperal.veebipood.dto.OrderRowDto;
import ee.piperal.veebipood.dto.PaymentUrl;
import ee.piperal.veebipood.entity.Order;
import ee.piperal.veebipood.entity.OrderRow;
import ee.piperal.veebipood.entity.Person;
import ee.piperal.veebipood.entity.Product;
import ee.piperal.veebipood.repository.OrderRepository;
import ee.piperal.veebipood.repository.PersonRepository;
import ee.piperal.veebipood.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;


@Service
@RequiredArgsConstructor
public class OrderService {


    //@Autowired --> Dependency Injection
    //@RequiredArgsConstructor --> Dependency Injection

    //Gets pulled in

    private final OrderRepository orderRepository;
    private final PersonRepository personRepository;
    private final ProductRepository productRepository;
    private RestTemplate restTemplate = new RestTemplate();


    public Order saveOrder(Long personId, String parcelMachine, List<OrderRowDto> orderRows) {
        Order order = new Order();
        order.setCreated(new Date());
        order.setParcelMachine(parcelMachine);
 //       order.setOrderRows(orderRows);
        Person person = personRepository.findById(personId).orElseThrow();
        order.setPerson(person);
        order.setTotal(calculateOrdersTotal(orderRows, order));
        return orderRepository.save(order);
    }

    private double calculateOrdersTotal(List<OrderRowDto> orderRows, Order order) {
        double total = 0;
        List<OrderRow> orderRowsInOrder = new ArrayList<OrderRow>();
        for(OrderRowDto orderRow : orderRows){
            Product product = productRepository.findById(orderRow.getProductId()).orElseThrow();
            total += product.getPrice() * orderRow.getQuantity();

            OrderRow orderRowInOrder = new OrderRow();
            orderRowInOrder.setProduct(product);
            orderRowInOrder.setQuantity(orderRow.getQuantity());
            orderRowsInOrder.add(orderRowInOrder);
        }
        order.setOrderRows(orderRowsInOrder);
        return total;
    }



    public PaymentUrl makePayment(Long orderId, double sum) {
        EveryPayBody body = new EveryPayBody();
        body.setAccount_name("EUR3D1"); // erinevad kontod.
        body.setNonce("165784a" + ZonedDateTime.now() + Math.random()); // turvaelement, et ei läheks topeltpäring
        body.setTimestamp(ZonedDateTime.now().toString()); // turvaelement. pluss miinus 5 minutit
        body.setAmount(sum); // max 7000 eurot on default
        body.setOrder_reference("piperal" + orderId); // kui on makstud, siis teist korda maksma minna ei saa
        body.setCustomer_url("https://err.ee"); // kuhu tagasi suunatakse. localhosti ei saa
        body.setApi_username("e36eb40f5ec87fa2"); // turvaelement. Headeris olemas. aga peab ühtima sellega mis on headeris

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("e36eb40f5ec87fa2", "7b91a3b9e1b74524c2e9fc282f8ac8cd");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity entity = new HttpEntity(body, headers);

        String url = "https://igw-demo.every-pay.com/api/v4/payments/oneoff";
        EveryPayResponse response = restTemplate.exchange(url, HttpMethod.POST, entity, EveryPayResponse.class).getBody();
        PaymentUrl paymentLink = new PaymentUrl();
        assert response != null;
        paymentLink.setUrl(response.getPayment_link());
        return paymentLink;
    };
}
