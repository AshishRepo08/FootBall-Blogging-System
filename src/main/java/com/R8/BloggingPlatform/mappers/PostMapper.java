package com.R8.BloggingPlatform.mappers;

import com.R8.BloggingPlatform.domain.CreatePostRequest;
import com.R8.BloggingPlatform.domain.PostStatus;
import com.R8.BloggingPlatform.domain.UpdatePostRequest;
import com.R8.BloggingPlatform.domain.dtos.CreatePostRequestDto;
import com.R8.BloggingPlatform.domain.dtos.PostDto;
import com.R8.BloggingPlatform.domain.dtos.UpdatePostRequestDto;
import com.R8.BloggingPlatform.domain.entites.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    @Mapping(target = "status",source = "status")
    PostDto toDto(Post post);


    CreatePostRequest toCreatePostRequest(CreatePostRequestDto dto);


    UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDto dto);

//    @Named("setStatusManually")
//    default PostStatus setStatusManually(Post post){
//        if(post.getStatus()!=null){
//            return post.getStatus();
//        }
//        return null;
//    }

}


