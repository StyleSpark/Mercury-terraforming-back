package com.matdongsan.api.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class ReservedTimeVO {
  private LocalTime reservedTime;
}