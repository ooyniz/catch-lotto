package com.catch_lotto.domain.lotto.controller;

import com.catch_lotto.domain.lotto.dto.LottoResponse;
import com.catch_lotto.domain.lotto.dto.LottoStat;
import com.catch_lotto.domain.lotto.service.LottoService;
import com.catch_lotto.global.response.ApiResponse;
import com.catch_lotto.global.response.ResponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lotto")
public class LottoController {
    private final LottoService lottoService;

    public LottoController(LottoService lottoService) {
        this.lottoService = lottoService;
    }

    @GetMapping("/random")
    public ResponseEntity<ApiResponse<LottoResponse>> getRandom() {
        LottoResponse result = lottoService.getRandom();

        return ResponseEntity
                .status(ResponseCode.LOTTO_RANDOM_OK.getStatus())
                .body(ApiResponse.success(ResponseCode.LOTTO_RANDOM_OK, result));
    }

    @PostMapping("/smart-random")
    public ResponseEntity<ApiResponse<LottoResponse>> getSmartRandom(@RequestBody List<Integer> selectedNumber) {
        LottoResponse result = lottoService.getSmartRandom(selectedNumber);

        return ResponseEntity
                .status(ResponseCode.LOTTO_RANDOM_OK.getStatus())
                .body(ApiResponse.success(ResponseCode.LOTTO_RANDOM_OK, result));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<List<LottoStat>>> getStats(@RequestParam Integer count) {
        List<LottoStat> result = lottoService.getTop6(count);

        return ResponseEntity
                .status(ResponseCode.LOTTO_STATS_OK.getStatus())
                .body(ApiResponse.success(ResponseCode.LOTTO_STATS_OK, result));
    }
}
