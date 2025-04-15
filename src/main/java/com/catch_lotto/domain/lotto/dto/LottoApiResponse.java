package com.catch_lotto.domain.lotto.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LottoApiResponse {
    private String returnValue;
    private int drwtNo1;
    private int drwtNo2;
    private int drwtNo3;
    private int drwtNo4;
    private int drwtNo5;
    private int drwtNo6;
    private int bnusNo;
}