package nazym.project.controllers;


import nazym.project.models.Category;
import nazym.project.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController()
@RequestMapping("/categories")
public class CategoryController
{
    private final String redirectUrl = "/admin/category";
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PictureController pictureController;


    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Category> getCategories()
    {
        return categoryService.allCategories();
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Category getCategory(@PathVariable(name="id") Long id)
    {
        return categoryService.findCategory(id);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addCategory(@RequestBody Category category)
    {
        categoryService.addCategory(category);
        return ResponseEntity.ok(redirectUrl);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteCategory(@RequestBody Long id)
    {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(redirectUrl);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateCategory(@RequestBody Category category)
    {
        categoryService.updateCategory(category);
        return ResponseEntity.ok(redirectUrl);
    }

    @PutMapping("/add_photo/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> setPhoto(@RequestPart(name="file") MultipartFile multipartFile,
                                           @PathVariable(name = "id") Long id) throws IOException
    {
        pictureController.addPictureLocal(multipartFile);
        categoryService.setPhotoCategory(multipartFile,id);
        return ResponseEntity.ok(redirectUrl);
    }

    @PutMapping("/reset_photo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> removePhoto(@RequestBody Long id)
    {
        Category category = categoryService.findCategory(id);
        category.setPicture("noimage.jpeg");
        categoryService.updateCategory(category);
        return ResponseEntity.ok(redirectUrl);
    }
}
