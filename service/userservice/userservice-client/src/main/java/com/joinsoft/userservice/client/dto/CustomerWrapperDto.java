package com.joinsoft.userservice.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.php25.distributedtransaction.dto.DistributedTransactionMsgLogDto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by penghuiping on 2017/9/20.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerWrapperDto implements Serializable {

    private CustomerDto customerDto;

    private List<DistributedTransactionMsgLogDto> distributedTransactionMsgLogDtos;

    public CustomerDto getCustomerDto() {
        return customerDto;
    }

    public void setCustomerDto(CustomerDto customerDto) {
        this.customerDto = customerDto;
    }

    public List<DistributedTransactionMsgLogDto> getDistributedTransactionMsgLogDtos() {
        return distributedTransactionMsgLogDtos;
    }

    public void setDistributedTransactionMsgLogDtos(List<DistributedTransactionMsgLogDto> distributedTransactionMsgLogDtos) {
        this.distributedTransactionMsgLogDtos = distributedTransactionMsgLogDtos;
    }
}
