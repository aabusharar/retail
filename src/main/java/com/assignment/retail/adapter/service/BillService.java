package com.assignment.retail.adapter.service;

import com.assignment.retail.adapter.rest.dto.BillDetailsDto;
import com.assignment.retail.model.NetPayablePayment;

public interface BillService {
  NetPayablePayment calculateBillWithDiscount(BillDetailsDto billDetailsDto);
}
