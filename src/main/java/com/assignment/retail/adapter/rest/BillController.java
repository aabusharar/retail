package com.assignment.retail.adapter.rest;

import com.assignment.retail.adapter.rest.dto.BillDetailsDto;
import com.assignment.retail.adapter.service.BillService;
import com.assignment.retail.exception.BadRequestException;
import com.assignment.retail.exception.ElementNotFoundException;
import com.assignment.retail.model.NetPayablePayment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("assigment/retail/bill")
@Slf4j
@RequiredArgsConstructor
public class BillController {

  private final BillService service;

  @PostMapping("/net-payment-calculation")
  @ResponseStatus(HttpStatus.OK)
  public NetPayablePayment calculateTotalAmount(@Valid @RequestBody BillDetailsDto details) {
    try {
      return service.calculateBillWithDiscount(details);
    }
    catch (ElementNotFoundException e) {
      throw e;
    }
    catch (Exception e) {
      log.error("Error While calculate the payment", e);
      throw new BadRequestException();
    }
  }
}
