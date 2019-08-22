package com.php25.usermicroservice.web.service;

import com.php25.common.core.specification.SearchParam;
import com.php25.usermicroservice.web.dto.AppDetailDto;
import com.php25.usermicroservice.web.dto.AppPageDto;
import com.php25.usermicroservice.web.dto.AppRegisterDto;
import org.springframework.data.domain.Sort;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;

import java.util.List;

/**
 * @author: penghuiping
 * @date: 2019/7/28 20:37
 * @description:
 */
public interface AppClientService extends ClientDetailsService, AuthorizationCodeServices {

    /**
     * 获取app客户信息详情
     *
     * @param appId app客户端唯一表示
     * @return AppDetailDto app客户端详情
     */
    AppDetailDto detailInfo(String appId);

    /**
     * 注册oauth2认证客户信息
     *
     * @param appRegisterDto 注册信息
     * @return
     */
    Boolean register(AppRegisterDto appRegisterDto);

    /**
     * 取消注册oauth2认证客户信息
     *
     * @param appId app客户端唯一表示
     * @return
     */
    Boolean unregister(String appId);


    /**
     * 分页查询 app客户端列表
     *
     * @param pageNum      当前第几页
     * @param pageSize     每页数量
     * @param searchParams 搜索参数
     * @param property     排序字段
     * @param direction    升序或者降序
     * @return
     */
    List<AppPageDto> queryPage(Integer pageNum, Integer pageSize, List<SearchParam> searchParams, String property, Sort.Direction direction);


    @Override
    ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException;


}
