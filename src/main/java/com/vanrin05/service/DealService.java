package com.vanrin05.service;

import com.vanrin05.model.Deal;

import java.util.List;

public interface DealService {
    List<Deal> getAllDeals();
    Deal createDeal(Deal deal);
    Deal updateDeal(Deal deal, Long dealId);
    void deleteDeal(Long dealId);
    Deal getDealById(Long id);
}
