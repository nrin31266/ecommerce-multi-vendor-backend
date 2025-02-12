package com.vanrin05.service;

import com.vanrin05.model.Home;
import com.vanrin05.model.HomeCategory;

import java.util.List;

public interface HomeService {
    public Home createHomePageData(List<HomeCategory> allCategories);
}
