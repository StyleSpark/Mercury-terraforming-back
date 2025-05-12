package com.matdongsan.api.dto.reaction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReactionRequest {
  private Long id;
  private Long userId;
  private Long targetId;
  private String targetType;
  private String reactionType;
}
