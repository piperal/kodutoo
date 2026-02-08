package ee.piperal.veebipood.controller;

import ee.piperal.veebipood.entity.Product;
import ee.piperal.veebipood.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class ProductController {
    //localhost:5000

//    @GetMapping("products")
//    public String getProducts(){
//        return "Hello World!";
//    }

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("products")
    public List<Product> getProducts(){
        return productRepository.findAll();
    }

    @GetMapping("products/{id}")
    public List<Product> delProduct(@PathVariable Long id){
        productRepository.deleteById(id);
        return productRepository.findAll();
    }

    @GetMapping("products/add")
    public List<Product> addProduct(@RequestBody Product product){
        productRepository.save(product);
        return productRepository.findAll();
    }

}
