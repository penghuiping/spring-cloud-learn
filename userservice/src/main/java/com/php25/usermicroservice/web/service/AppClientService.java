package com.php25.usermicroservice.web.service;

import com.php25.common.core.specification.SearchParam;
import com.php25.usermicroservice.web.dto.AccountDto;
import com.php25.usermicroservice.web.dto.AppDetailDto;
import com.php25.usermicroservice.web.dto.AppPageDto;
import com.php25.usermicroservice.web.dto.AppRegisterDto;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * @author: penghuiping
 * @date: 2019/7/28 20:37
 * @description:
 */
public interface AppClientService  {

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
     * @return 返回管理员账号
     */
    AccountDto register(AppRegisterDto appRegisterDto);

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

}
