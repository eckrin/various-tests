package com.eckrin.test.datajpa_query;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/gichan")
@RestController
@RequiredArgsConstructor
public class GichanController {

    private final GichanService gichanService;

    @GetMapping
    public List<GichanDto> getSpecifiedGichan() {
        return gichanService.getSpecifiedGichan();
    }
}
