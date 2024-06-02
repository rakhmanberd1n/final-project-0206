package nazym.project.controllers;

import nazym.project.models.Product;
import nazym.project.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final String redirectUrl = "/admin/product";

    @Autowired
    private ProductService productService;

    @Autowired
    private PictureController pictureController;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Product> getProducts() {
        return productService.allProducts();
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Product getProduct(@PathVariable(name = "id") Long id) {
        return productService.findProduct(id);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addProduct(@RequestBody Product product) {
        productService.addProduct(product);
        return ResponseEntity.ok(redirectUrl);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateProduct(@RequestBody Product product) {
        productService.updateProduct(product);
        return ResponseEntity.ok(redirectUrl);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable(name = "id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(redirectUrl);
    }

    @PutMapping("/add_photo/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> setPhoto(@RequestPart(name = "file") MultipartFile multipartFile,
                                           @PathVariable(name = "id") Long id) throws IOException {
        productService.setPhotoCategory(multipartFile, id);
        pictureController.addPictureLocal(multipartFile);
        return ResponseEntity.ok(redirectUrl);
    }

    @PutMapping("/reset_photo/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> removePhoto(@PathVariable(name = "id") Long id) {
        Product category = productService.findProduct(id);
        category.setPicture("noimage.jpeg");
        productService.updateProduct(category);
        return ResponseEntity.ok(redirectUrl);
    }
}

