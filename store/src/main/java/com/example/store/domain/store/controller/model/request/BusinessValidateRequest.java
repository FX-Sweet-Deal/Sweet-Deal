package com.example.store.domain.store.controller.model.request;

import com.example.store.domain.store.repository.Business;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessValidateRequest {

  private List<Business> businesses;

}
