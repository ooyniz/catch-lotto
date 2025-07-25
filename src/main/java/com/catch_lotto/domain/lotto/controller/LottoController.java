package com.catch_lotto.domain.lotto.controller;

import com.catch_lotto.domain.lotto.dto.LottoStat;
import com.catch_lotto.domain.lotto.service.LottoService;
import com.catch_lotto.global.response.ApiResponse;
import com.catch_lotto.global.response.ResponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lotto")
public class LottoController {
    private final LottoService lottoService;

    public LottoController(LottoService lottoService) {
        this.lottoService = lottoService;
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<List<LottoStat>>> getStats(@RequestParam Integer count) {
        List<LottoStat> result = lottoService.getTop6(count);

        return ResponseEntity
                .status(ResponseCode.LOTTO_STATS_OK.getStatus())
                .body(ApiResponse.success(ResponseCode.LOTTO_STATS_OK, result));
    }
}
