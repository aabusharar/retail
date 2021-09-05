package com.assignment.retail.adapter.rest;

import com.assignment.retail.adapter.rest.dto.BillDetailsDto;
import com.assignment.retail.adapter.rest.dto.ProductDto;
import com.assignment.retail.adapter.service.BillService;
import com.assignment.retail.exception.BadRequestException;
import com.assignment.retail.exception.ElementNotFoundException;
import com.assignment.retail.model.NetPayablePayment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BillControllerTest {

  private final String apiUrl = "http://localhost:8080/assigment/retail/bill/net-payment-calculation";

  private static HttpHeaders headers;

  private static TestRestTemplate restTemplate;

  private static JSONObject requestJsonObject;

  @MockBean
  private BillService service;

  @BeforeAll
  public static void beforeAll() throws JSONException {
    restTemplate = new TestRestTemplate();
    headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    JSONObject product = new JSONObject();
    product.put("productId", 1);
    product.put("amount", 2);

    JSONArray array = new JSONArray();
    array.put(product);

    requestJsonObject = new JSONObject();
    requestJsonObject.put("userName", "Ahmad");
    requestJsonObject.put("products", array);
  }

  @BeforeEach
  public void before() {
    Mockito.clearAllCaches();
  }

  @AfterEach
  public void after() {
    Mockito.verify(service, Mockito.times(1)).calculateBillWithDiscount(any());
  }

  @Test
  public void testWhenUserHaveCallSuccessfullyApiCall() {
    BillDetailsDto billDetailsDto = BillDetailsDto.builder().userName("Ahmad")
            .products(Collections.singletonList(ProductDto.builder().productId(1).amount(2).build())).build();
    Mockito.when(service.calculateBillWithDiscount(eq(billDetailsDto))).thenReturn(NetPayablePayment.builder().totalPrice(100).build());

    HttpEntity<String> request = new HttpEntity<String>(requestJsonObject.toString(), headers);
    ResponseEntity<NetPayablePayment> result = restTemplate.postForEntity(apiUrl, request, NetPayablePayment.class);

    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());
    Assertions.assertEquals(100, result.getBody().getTotalPrice());

    Mockito.verify(service, Mockito.times(1)).calculateBillWithDiscount(eq(billDetailsDto));
  }

  @Test
  public void testWhenUserHaveCallApiCallWithInvalidProduct() {
    BillDetailsDto billDetailsDto = BillDetailsDto.builder().userName("Ahmad")
            .products(Collections.singletonList(ProductDto.builder().productId(1).amount(2).build())).build();
    Mockito.when(service.calculateBillWithDiscount(eq(billDetailsDto))).thenThrow(new ElementNotFoundException("invalid id 1"));

    HttpEntity<String> request = new HttpEntity<String>(requestJsonObject.toString(), headers);
    ResponseEntity<NetPayablePayment> result = restTemplate.postForEntity(apiUrl, request, NetPayablePayment.class);
    Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

    Mockito.verify(service, Mockito.times(1)).calculateBillWithDiscount(eq(billDetailsDto));
  }

  @Test
  public void testWhenUserHaveCallApiCallWithSomethingWentWrong() {
    BillDetailsDto billDetailsDto = BillDetailsDto.builder().userName("Ahmad")
            .products(Collections.singletonList(ProductDto.builder().productId(1).amount(2).build())).build();
    Mockito.when(service.calculateBillWithDiscount(eq(billDetailsDto))).thenThrow(new BadRequestException());

    HttpEntity<String> request = new HttpEntity<String>(requestJsonObject.toString(), headers);
    ResponseEntity<NetPayablePayment> result = restTemplate.postForEntity(apiUrl, request, NetPayablePayment.class);
    Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());

    Mockito.verify(service, Mockito.times(1)).calculateBillWithDiscount(eq(billDetailsDto));
  }
}
