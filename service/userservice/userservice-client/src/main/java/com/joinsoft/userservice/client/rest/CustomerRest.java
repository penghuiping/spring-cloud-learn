package com.joinsoft.userservice.client.rest;

import com.joinsoft.userservice.client.dto.CustomerDto;
import com.joinsoft.userservice.client.dto.CustomerWrapperDto;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by penghuiping on 2017/3/8.
 */
public interface CustomerRest {

    /**
     * 新增用户
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/save")
    public CustomerDto save(@NotNull @RequestBody CustomerDto dto);

    /**
     * 新增用户(分布式事务，异步消息保证最终一致性)
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/customerRegister")
    public CustomerDto customerRegister(@NotNull @RequestBody CustomerWrapperDto dto);


    /**
     * 根据电话号码和密码查询
     *
     * @param phone
     * @param password
     * @return
     */
    @RequestMapping(value = "/findOneByPhoneAndPassword")
    public CustomerDto findOneByPhoneAndPassword(@Pattern(regexp = "[0-9]{11}", message = "请输入正确手机号") @RequestParam("phone") String phone, @Pattern(regexp = "[0-9A-Za-z]{10,}", message = "请输入正确密码") @RequestParam("password") String password);

    /**
     * 查询全部
     *
     * @return
     */
    @RequestMapping(value = "/findAll")
    public List<CustomerDto> findAll();

    /**
     * 使用电话号码查询用户
     *
     * @param phone
     * @return
     */
    @RequestMapping(value = "/findOneByPhone")
    public CustomerDto findOneByPhone(@Pattern(regexp = "[0-9]{11}", message = "请输入正确手机号") @RequestParam("phone") String phone);


    /**
     * 根据用户id查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findOne")
    public CustomerDto findOne(@NotBlank @RequestParam("id") String id);

    /**
     * 分页查询
     *
     * @param searchParams
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/query")
    public List<CustomerDto> query(@NotBlank @RequestParam("searchParams") String searchParams, @Min(-1) @RequestParam("pageNum") Integer pageNum, @Min(1) @RequestParam("pageSize") Integer pageSize);

    /**
     * 根据uuid查询
     *
     * @param uuid
     * @return
     */
    @RequestMapping(value = "/findByUuidAndType")
    public CustomerDto findByUuidAndType(@NotBlank @RequestParam("uuid") String uuid, @NotNull @RequestParam("type") Integer type);

    /**
     * 软删除
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/softDelete")
    public Boolean softDelete(@Size(min = 1) @RequestParam("ids") List<String> ids);


    /**
     * 根据用户名查询
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "/findByName")
    public List<CustomerDto> findByName(@NotBlank @RequestParam("name") String name);
}
