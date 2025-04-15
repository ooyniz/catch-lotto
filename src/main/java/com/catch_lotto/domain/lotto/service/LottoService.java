package com.catch_lotto.domain.lotto.service;

import com.catch_lotto.domain.lotto.dto.LottoApiResponse;
import com.catch_lotto.domain.lotto.dto.LottoResponse;
import com.catch_lotto.domain.lotto.dto.LottoStat;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LottoService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LottoResponse getRandom() {
        Random random = new Random();
        Set<Integer> uniqueNumbers = new HashSet<>();

        while (uniqueNumbers.size() < 6) {
            uniqueNumbers.add(random.nextInt(45) + 1);
        }

        List<Integer> sorted = uniqueNumbers.stream()
                .sorted()
                .toList();

        return new LottoResponse(
                sorted.get(0),
                sorted.get(1),
                sorted.get(2),
                sorted.get(3),
                sorted.get(4),
                sorted.get(5)
        );
    }

    public List<LottoStat> getTop6(Integer count) {
        int latestRound = getLatestRound();
        int targetCount = (count != null) ? count : latestRound;

        Map<Integer, Integer> frequencyMap = new HashMap<>();

        for (int i = latestRound; i > latestRound - targetCount; i--) {
            try {
                String url = "https://www.dhlottery.co.kr/common.do?method=getLottoNumber&drwNo=" + i;
                Document doc = Jsoup.connect(url).ignoreContentType(true).get();
                String json = doc.body().text();

                LottoApiResponse res = objectMapper.readValue(json, LottoApiResponse.class);

                // 실패 회차는 건너뛴다
                if (!"success".equals(res.getReturnValue())) {
                    continue;
                }

                int[] numbers = {
                        res.getDrwtNo1(),
                        res.getDrwtNo2(),
                        res.getDrwtNo3(),
                        res.getDrwtNo4(),
                        res.getDrwtNo5(),
                        res.getDrwtNo6(),
                        res.getBnusNo()
                };

                for (int num : numbers) {
                    frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
                }

            } catch (IOException e) {
                    log.warn(i + "회차 요청 실패: " + e.getMessage());
                    continue;
            }
        }

        return frequencyMap.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(6)
                .map(e -> new LottoStat(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    private int getLatestRound() {
        Calendar base = new GregorianCalendar(2002, Calendar.DECEMBER, 7); // 1회차
        Calendar now = Calendar.getInstance();
        long days = (now.getTimeInMillis() - base.getTimeInMillis()) / (1000 * 60 * 60 * 24);
        return (int) (days / 7) + 1;
    }
}
