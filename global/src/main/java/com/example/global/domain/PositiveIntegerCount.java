package com.example.global.domain;

import lombok.Getter;

@Getter
public class PositiveIntegerCount {

  private int count;

  public PositiveIntegerCount() {
    this.count = 0;
  }

  public PositiveIntegerCount(int count) {
    this.count = count;
  }

  public void increase(int count) {
    this.count += count;
  }

  public void decrease(int count) {
    if (this.count <= 0) {
      return;
    }
    this.count -= count;
  }

}
