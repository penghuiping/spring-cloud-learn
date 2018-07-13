package com.php25.userservice.server.rest;

import com.google.common.collect.Lists;
import com.php25.distributedtransaction.dto.DistributedTransactionMsgLogDto;
import com.php25.distributedtransaction.service.DistributedTransactionMsgService;
import com.php25.userservice.client.dto.CustomerDto;
import com.php25.userservice.client.dto.CustomerWrapperDto;
import com.php25.userservice.client.rest.CustomerRest;
import com.php25.userservice.server.service.CustomerService;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;
import rx.schedulers.Schedulers;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Created by penghuiping on 2017/3/8.
 */
@Validated
@RestController
@RequestMapping("/customer")
public class CustomerRestImpl implements CustomerRest {

    @Autowired
    CustomerService customerService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private DistributedTransactionMsgService distributedTransactionMsgService;


    /**
     * 新增用户
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/save")
    public CustomerDto save(@NotNull @RequestBody CustomerDto dto) {
        return customerService.save(dto).orElse(null);
    }

    /**
     * 新增用户(分布式事务，异步消息保证最终一致性)
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/customerRegister")
    public CustomerDto customerRegister(@NotNull @RequestBody CustomerWrapperDto dto) {
        CustomerDto customerDto = dto.getCustomerDto();
        List<DistributedTransactionMsgLogDto> distributedTransactionMsgLogDtos = dto.getDistributedTransactionMsgLogDtos();
        AtomicReference<CustomerDto> customerDtoReference = new AtomicReference<>();

        Boolean result = transactionTemplate.execute((TransactionStatus a) -> {
            CustomerDto temp = customerService.save(customerDto).orElse(null);
            customerDtoReference.set(temp);
            distributedTransactionMsgService.save(distributedTransactionMsgLogDtos);
            return true;
        });

        //异步发送消息
        Observable.create(subscriber -> {
            distributedTransactionMsgService.sendMsgToBroker(distributedTransactionMsgLogDtos);
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(a -> {

        });

        if (true == result) {
            return customerDtoReference.get();
        } else {
            return null;
        }
    }


    /**
     * 根据电话号码和密码查询
     *
     * @param phone
     * @param password
     * @return
     */
    @RequestMapping(value = "/findOneByPhoneAndPassword")
    public CustomerDto findOneByPhoneAndPassword(@Pattern(regexp = "[0-9]{11}", message = "请输入正确手机号") @RequestParam("phone") String phone, @Pattern(regexp = "[0-9A-Za-z]{10,}", message = "请输入正确密码") @RequestParam("password") String password) {
        return customerService.findOneByPhoneAndPassword(phone, password).orElse(null);
    }

    /**
     * 查询全部
     *
     * @return
     */
    @RequestMapping(value = "/findAll")
    public List<CustomerDto> findAll() {
        return customerService.findAll().orElse(null);
    }

    /**
     * 使用电话号码查询用户
     *
     * @param phone
     * @return
     */
    @RequestMapping(value = "/findOneByPhone")
    public CustomerDto findOneByPhone(@Pattern(regexp = "[0-9]{11}", message = "请输入正确手机号") @RequestParam("phone") String phone) {
        return customerService.findOneByPhone(phone).orElse(null);
    }


    /**
     * 根据用户id查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findOne")
    public CustomerDto findOne(@NotBlank @RequestParam("id") String id) {
        return customerService.findOne(Long.parseLong(id)).orElse(null);
    }

    /**
     * 分页查询
     *
     * @param searchParams
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/query")
    public List<CustomerDto> query(@NotBlank @RequestParam("searchParams") String searchParams, @Min(-1) @RequestParam("pageNum") Integer pageNum, @Min(1) @RequestParam("pageSize") Integer pageSize) {
        return customerService.query(searchParams, pageNum, pageSize).orElse(null);
    }

    /**
     * 根据uuid查询
     *
     * @param uuid
     * @return
     */
    @RequestMapping(value = "/findByUuidAndType")
    public CustomerDto findByUuidAndType(@NotBlank @RequestParam("uuid") String uuid, @NotNull @RequestParam("type") Integer type) {
        return customerService.findByUuidAndType(uuid, type).orElse(null);
    }

    /**
     * 软删除
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/softDelete")
    public Boolean softDelete(@Size(min = 1) @RequestParam("ids") List<String> ids) {
        List<Long> ids_ = ids.stream().map(a -> Long.parseLong(a)).collect(Collectors.toList());
        customerService.softDelete(customerService.findAll(ids_).orElse(Lists.newArrayList()));
        return true;
    }


    /**
     * 根据用户名查询
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "/findByName")
    public List<CustomerDto> findByName(@NotBlank @RequestParam("name") String name) {
        return customerService.findByName(name).orElse(null);
    }
}
