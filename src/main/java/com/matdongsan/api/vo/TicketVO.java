package com.matdongsan.api.vo;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@Alias("ticket")
public class TicketVO {
  private String id;
  private String name;
  private String description;
  private Integer price;
  private Integer quantity;
}
