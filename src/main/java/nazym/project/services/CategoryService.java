package nazym.project.services;

import nazym.project.models.Category;
import nazym.project.repositories.CategoryRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class CategoryService
{
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> allCategories()
    {
        return categoryRepository.findAll();
    }

    public Category findCategory(Long id)
    {
        return categoryRepository.findById(id).orElse(null);
    }

    public void deleteCategory(Long id)
    {
        categoryRepository.deleteById(id);
    }

    public void addCategory(Category category)
    {
        categoryRepository.save(category);
    }

    public void updateCategory(Category category){categoryRepository.save(category);}

    public void setPhotoCategory(@NotNull MultipartFile multipartFile, Long id)
    {
        Category category = findCategory(id);
        category.setPicture(multipartFile.getOriginalFilename());
        categoryRepository.save(category);
    }
}