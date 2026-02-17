package com.R8.BloggingPlatform.mappers;

import com.R8.BloggingPlatform.domain.PostStatus;
import com.R8.BloggingPlatform.domain.dtos.CategoryDto;
import com.R8.BloggingPlatform.domain.dtos.CreateCategoryRequest;
import com.R8.BloggingPlatform.domain.entites.Category;
import com.R8.BloggingPlatform.domain.entites.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(target = "postCount",source = "posts",qualifiedByName = "calculatePostCount")
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequest createCategoryRequest);

    @Named("calculatePostCount")
    default long calculatePostCount(List<Post> posts){
        if(posts==null){
            return 0;
        }
        return posts.stream()
                .filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
                .count();
    }
}
