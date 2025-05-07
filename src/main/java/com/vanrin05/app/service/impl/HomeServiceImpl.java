package com.vanrin05.app.service.impl;

import com.vanrin05.app.domain.HOME_CATEGORY_SECTION;
import com.vanrin05.app.model.Deal;
import com.vanrin05.app.model.Home;
import com.vanrin05.app.model.HomeCategory;
import com.vanrin05.app.repository.DealRepository;
import com.vanrin05.app.service.HomeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HomeServiceImpl implements HomeService {

    DealRepository dealRepository;

    @Override
    public Home createHomePageData(List<HomeCategory> allCategories) {
//
//        Map<HOME_CATEGORY_SECTION, List<HomeCategory>> categoryMap = allCategories.stream()
//                .collect(Collectors.groupingBy(HomeCategory::getHomeCategorySection));
//
//        List<HomeCategory> gridCategories = categoryMap.getOrDefault(HOME_CATEGORY_SECTION.GRID, Collections.emptyList());
//        List<HomeCategory> shopByCategories = categoryMap.getOrDefault(HOME_CATEGORY_SECTION.SHOP_BY_CATEGORY, Collections.emptyList());
//        List<HomeCategory> electricCategories = categoryMap.getOrDefault(HOME_CATEGORY_SECTION.ELECTRIC_CATEGORY, Collections.emptyList());
//        List<HomeCategory> dealCategories = categoryMap.getOrDefault(HOME_CATEGORY_SECTION.DEALS, Collections.emptyList());
//
//
//
//
//        List<Deal> createdDeals = dealRepository.findAll();
//
//        if(createdDeals.isEmpty()){
//            List<Deal> deals= dealCategories.stream().map(dealCategory -> new Deal(null, 10, dealCategory)).toList();
//            createdDeals = dealRepository.saveAll(deals);
//        }
//
//        Home home = new Home();
//        home.setGrids(gridCategories);
//        home.setShopByCategories(shopByCategories);
//        home.setElectricCategories(electricCategories);
//        home.setDeals(createdDeals);
//
//
//        return home;
        return null;
    }
}
