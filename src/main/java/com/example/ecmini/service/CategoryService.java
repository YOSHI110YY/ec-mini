package com.example.ecmini.service;

import com.example.ecmini.entity.Category;
import com.example.ecmini.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("カテゴリが見つかりません"));
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public void update(Long id, Category category) {
        Category existing = findById(id);
        existing.setName(category.getName());
        categoryRepository.save(existing);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

}
