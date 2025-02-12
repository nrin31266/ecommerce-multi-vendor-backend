package com.vanrin05.service.impl;

import com.vanrin05.exception.AppException;
import com.vanrin05.model.Deal;
import com.vanrin05.model.HomeCategory;
import com.vanrin05.repository.DealRepository;
import com.vanrin05.repository.HomeCategoryRepository;
import com.vanrin05.service.DealService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DealServiceImpl implements DealService {
    DealRepository dealRepository;
    HomeCategoryRepository homeCategoryRepository;

    @Override
    public List<Deal> getAllDeals() {
        return dealRepository.findAll();
    }

    @Override
    public Deal createDeal(Deal deal) {
        HomeCategory homeCategory = homeCategoryRepository.findById(deal.getHomeCategory().getId()).orElseThrow(()->new AppException("Home category not found"));
        Deal newDeal = new Deal();
        newDeal.setHomeCategory(homeCategory);
        newDeal.setDiscount(deal.getDiscount());
        return dealRepository.save(newDeal);
    }

    @Override
    public Deal updateDeal(Deal deal, Long dealId) {
        Deal existingDeal = getDealById(dealId);
        HomeCategory homeCategory = homeCategoryRepository.findById(existingDeal.getHomeCategory().getId()).orElseThrow(()->new AppException("Home category not found"));
        existingDeal.setDiscount(deal.getDiscount());
        existingDeal.setHomeCategory(homeCategory);

        return dealRepository.save(existingDeal);
    }

    @Override
    public void deleteDeal(Long dealId) {
        getDealById(dealId);
        dealRepository.deleteById(dealId);
    }

    @Override
    public Deal getDealById(Long id) {
        return dealRepository.findById(id).orElseThrow(()-> new AppException("Deal not found"));
    }
}
