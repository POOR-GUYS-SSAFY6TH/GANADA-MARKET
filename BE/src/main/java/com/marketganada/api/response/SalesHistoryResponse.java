package com.marketganada.api.response;

import com.marketganada.db.entity.Payment;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SalesHistoryResponse {
    List<Map<String, Object>> salesHistory;

    public static SalesHistoryResponse of(List<Payment> paymentList) {
        SalesHistoryResponse res = new SalesHistoryResponse();
        List<Map<String, Object>> salesHistory = new ArrayList<>();
        for(Payment payment : paymentList){
            Map<String,Object> salesInfo = new HashMap<>();
            salesInfo.put("paymentId",payment.getPaymentId());
            salesInfo.put("productName",payment.getAuction().getProduct().getProductName());
            salesInfo.put("productBrand",payment.getAuction().getProduct().getProductBrand());
            salesInfo.put("tradeDate",payment.getTradeDate());
            salesInfo.put("price",payment.getPrice());
            salesInfo.put("status",payment.getStatus());
            salesInfo.put("trackingNum",payment.getTrackingNum());
            salesHistory.add(salesInfo);
        }
        res.setSalesHistory(salesHistory);
        return res;

    }
}
