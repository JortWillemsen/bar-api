package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.category.CategoryQueryHandler;
import com.tungstun.barapi.application.category.CategoryService;
import com.tungstun.barapi.application.category.query.GetCategory;
import com.tungstun.barapi.application.category.query.ListCategoriesOfBar;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.presentation.dto.converter.CategoryConverter;
import com.tungstun.barapi.presentation.dto.request.CategoryRequest;
import com.tungstun.barapi.presentation.dto.response.CategoryResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/bars/{barId}/categories")
public class CategoryController {
    private final CategoryQueryHandler categoryQueryHandler;
    private final CategoryService categoryService;
    private final CategoryConverter converter;

    public CategoryController(CategoryQueryHandler categoryQueryHandler, CategoryService categoryService, CategoryConverter converter) {
        this.categoryQueryHandler = categoryQueryHandler;
        this.categoryService = categoryService;
        this.converter = converter;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Finds all categories of bar",
            notes = "Provide categoryId of bar to look up all categories that are linked to the bar",
            response = CategoryResponse.class
    )
    public List<CategoryResponse> getCategoriesOfBar(
            @ApiParam(value = "ID value for the bar you want to retrieve categories from") @PathVariable("barId") UUID barId
    ) throws EntityNotFoundException {
        List<Category> categories = categoryQueryHandler.handle(new ListCategoriesOfBar(barId));
        return converter.convertAll(categories);
    }

    @GetMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Finds category of bar",
            notes = "Provide categoryId of bar and category to look up the specific category from the bar",
            response = CategoryResponse.class
    )
    public CategoryResponse getCategoryOfBar(
            @ApiParam(value = "ID value for the bar you want to retrieve the category from") @PathVariable("barId") UUID barId,
            @ApiParam(value = "ID value for the category you want to retrieve") @PathVariable("categoryId") UUID categoryId
    ) throws EntityNotFoundException {
        Category category = categoryQueryHandler.handle(new GetCategory(categoryId, barId));
        return converter.convert(category);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Creates new category for bar",
            notes = "Provide categoryId of bar to add new category with information from the request body to the bar",
            response = CategoryResponse.class
    )
    public UUID addCategoryToBar(
            @ApiParam(value = "ID value for the bar you want to create a new category for") @PathVariable("barId") UUID barId,
            @Valid @RequestBody CategoryRequest categoryRequest
    ) throws EntityNotFoundException {
        return categoryService.addCategoryToBar(barId, categoryRequest);
    }

    @PutMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Updates the category of bar",
            notes = "Provide categoryId of bar to update the category with information from the request body",
            response = CategoryResponse.class
    )
    public UUID updateCategoryOfBar(
            @ApiParam(value = "ID value for the bar you want to update the category from") @PathVariable("barId") UUID barId,
            @ApiParam(value = "ID value for the category you want to update") @PathVariable("categoryId") UUID categoryId,
            @Valid @RequestBody CategoryRequest categoryRequest
    ) throws EntityNotFoundException {
        return categoryService.updateCategoryOfBar(barId, categoryId, categoryRequest);
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Deletes the category of bar",
            notes = "Provide categoryId of bar to delete the category of bar",
            response = CategoryResponse.class
    )
    public void deleteCategoryOfBar(
            @ApiParam(value = "ID value for the bar you want to delete the category from") @PathVariable("barId") UUID barId,
            @ApiParam(value = "ID value for the category you want to delete") @PathVariable("categoryId") UUID categoryId
    ) throws EntityNotFoundException {
        categoryService.deleteCategoryFromBar(barId, categoryId);
    }
}
