package com.matdongsan.api.dto.community;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunityDeleteRequest {
  private Long id;
  private Long userId;
}
