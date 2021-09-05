package com.assignment.retail.adapter.repository.convertor;

import com.assignment.retail.model.ProductDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Converter
@RequiredArgsConstructor
@Component
public class ProductDetailsConvertor implements AttributeConverter<List<ProductDetails>, String> {
  private final ObjectMapper objectMapper;

  @Override
  public String convertToDatabaseColumn(List<ProductDetails> stringObjectMap) {
    if (stringObjectMap == null) {
      return "[]";
    }
    try {
      return objectMapper.writeValueAsString(stringObjectMap);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return "[]";
    }
  }

  @Override
  public List<ProductDetails> convertToEntityAttribute(String dbColumn) {
    if (dbColumn == null) {
      return Collections.emptyList();
    }
    try {
      return objectMapper.readValue(dbColumn, new TypeReference<List<ProductDetails>>() {
      });
    } catch (IOException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }
}
