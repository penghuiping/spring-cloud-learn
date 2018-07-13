package com.php25.userservice.client.rest;

import com.php25.userservice.client.dto.CustomerDto;
import com.php25.userservice.client.dto.CustomerWrapperDto;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by penghuiping on 2017/3/8.
 */
@Validated
public interface CustomerRest {

    String baseUri = "/customer";

    /**
     * 新增用户
     *
     * @param dto
     * @return
     */
    @RequestLine("POST " + baseUri + "/save")
    @Headers("Content-Type: application/json")
    public CustomerDto save(@NotNull CustomerDto dto);

    /**
     * 新增用户(分布式事务，异步消息保证最终一致性)
     *
     * @param dto
     * @return
     */
    @RequestLine("POST " + baseUri + "/customerRegister")
    @Headers("Content-Type: application/json")
    public CustomerDto customerRegister(@NotNull CustomerWrapperDto dto);


    /**
     * 根据电话号码和密码查询
     *
     * @param phone
     * @param password
     * @return
     */
    @RequestLine("GET " + baseUri + "/findOneByPhoneAndPassword?phone={phone}&password={password}")
    public CustomerDto findOneByPhoneAndPassword(@Pattern(regexp = "[0-9]{11}", message = "请输入正确手机号") @Param("phone") String phone, @Pattern(regexp = "[0-9A-Za-z]{10,}", message = "请输入正确密码") @Param("password") String password);

    /**
     * 查询全部
     *
     * @return
     */
    @RequestLine("GET " + baseUri + "/findAll")
    public List<CustomerDto> findAll();

    /**
     * 使用电话号码查询用户
     *
     * @param phone
     * @return
     */
    @RequestLine("GET " + baseUri + "/findOneByPhone?phone={phone}")
    public CustomerDto findOneByPhone(@Pattern(regexp = "[0-9]{11}", message = "请输入正确手机号") @Param("phone") String phone);


    /**
     * 根据用户id查询
     *
     * @param id
     * @return
     */
    @RequestLine("GET " + baseUri + "/findOne?id={id}")
    public CustomerDto findOne(@NotBlank @Param("id") String id);

    /**
     * 分页查询
     *
     * @param searchParams
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestLine("GET " + baseUri + "/query?searchParams={searchParams}&pageNum={pageNum}&pageSize={pageSize}")
    public List<CustomerDto> query(@NotBlank @Param("searchParams") String searchParams, @Min(-1) @Param("pageNum") Integer pageNum, @Min(1) @Param("pageSize") Integer pageSize);

    /**
     * 根据uuid查询
     *
     * @param uuid
     * @return
     */
    @RequestLine("GET " + baseUri + "/findByUuidAndType?uuid={uuid}&type={type}")
    public CustomerDto findByUuidAndType(@NotBlank @Param("uuid") String uuid, @NotNull @Param("type") Integer type);

    /**
     * 软删除
     *
     * @param ids
     * @return
     */
    @RequestLine("GET " + baseUri + "/softDelete?ids={ids}")
    public Boolean softDelete(@Size(min = 1) @Param("ids") List<String> ids);


    /**
     * 根据用户名查询
     *
     * @param name
     * @return
     */
    @RequestLine("GET " + baseUri + "/findByName?name={name}")
    public List<CustomerDto> findByName(@NotBlank @Param("name") String name);
}
