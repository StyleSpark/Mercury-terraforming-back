package com.matdongsan.api.dto.community;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunityCreateRequest {
    private Long id;
    private Long userId;
    private Integer categoryId;
    private Long propertyId;
    private String title;
    private String content;
    private String imageUrls;
}
