package ee.piperal.veebipood.controller;

import ee.piperal.veebipood.entity.Product;
import ee.piperal.veebipood.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("products")
    public List<Product> getProducts(){
        return productRepository.findAll();
    }

    @GetMapping("products/{id}")
    public Product getOne(@PathVariable Long id){
        return productRepository.findById(id).orElseThrow();
    }

    @DeleteMapping("products/{id}")
    public List<Product> delProduct(@PathVariable Long id){
        productRepository.deleteById(id);
        return productRepository.findAll();
    }

    @PostMapping("products/add")
    public List<Product> addProduct(@RequestBody Product product){
        if(product.getId()!=null){
            throw new RuntimeException("Can't add product with id already exists");
        }
        productRepository.save(product);
        return productRepository.findAll();
    }

    @PutMapping("products")
    public List<Product> editProduct(@RequestBody Product product){
        if(product.getId() == null){
            throw new RuntimeException("Cannot edit without Id");
        }
        if(!productRepository.existsById(product.getId())){
            throw new RuntimeException("Product ID does not exist");
        }
        productRepository.save(product);
        return productRepository.findAll();
    }

}
