package com.codegym.sevice;

import com.codegym.model.Province;
import com.codegym.repository.ProvinceRepository;

public interface ProvinceService {
    Iterable<Province> findAll();

    Province findById(Long id);

    void save(Province province);

    void remove(Long id);
}
