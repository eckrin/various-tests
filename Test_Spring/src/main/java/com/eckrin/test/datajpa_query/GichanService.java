package com.eckrin.test.datajpa_query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GichanService {

    private final GichanRepository gichanRepository;

    @Transactional
    public List<GichanDto> getSpecifiedGichan() {
        return gichanRepository.findTop3ByNameOrderByVersionDesc("목신")
                .stream()
                .map(g -> new GichanDto(g.getId(), g.getVersion(), g.getName())).toList();
    }
}
