package com.vanrin05.app.service;

import com.vanrin05.app.model.Home;
import com.vanrin05.app.model.HomeCategory;

import java.util.List;

public interface HomeService {
    public Home createHomePageData(List<HomeCategory> allCategories);
}
