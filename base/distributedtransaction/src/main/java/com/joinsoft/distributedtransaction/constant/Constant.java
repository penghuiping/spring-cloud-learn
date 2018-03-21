package com.joinsoft.distributedtransaction.constant;

/**
 * Created by penghuiping on 2017/9/20.
 */
public interface Constant {

    interface Exchange {
        String BABY_SHOP_NAME = "baby_shop_exchange";
    }

    interface Queue {
        String MERCHANT_SERVICE = "merchant_service_queue";
        String ACTIVITY_SERVICE = "activity_service_queue";
        String COMMISSION_SERVICE = "commission_service_queue";
        String ORDER_SERVICE = "order_service_queue";
        String OTHER_SERVICE = "other_service_queue";
        String PAY_SERVICE = "pay_service_queue";
        String PRODUCT_SERVICE = "product_service_queue";
        String RETURN_SERVICE = "return_service_queue";
        String SERVICE_SERVICE = "service_service_queue";
        String USER_SERVICE = "user_service_queue";
        String VIP_SERVICE = "vip_service_queue";
    }

}
