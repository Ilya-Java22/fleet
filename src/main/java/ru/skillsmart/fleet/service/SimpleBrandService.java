package ru.skillsmart.fleet.service;

import org.springframework.stereotype.Service;
import ru.skillsmart.fleet.model.Brand;
import ru.skillsmart.fleet.repository.BrandRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SimpleBrandService implements BrandService {
    private final BrandRepository brandRepository;

    public SimpleBrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    public Optional<Brand> findById(int id) {
        return brandRepository.findById(id);
    }
}
