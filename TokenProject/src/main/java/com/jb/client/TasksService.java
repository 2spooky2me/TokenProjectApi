package com.jb.client;

import com.jb.repository.CouponsRepository;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Service
@Log4j2
public class TasksService {

    private final Map<String, ClientSession> tokens;
    private final CouponsRepository couponsRepository;


    @Async
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void deleteExpiredSessions() {
        tokens.entrySet().removeIf(entry -> entry.getValue().isExpired());
        log.info("Expired sessions deleted.");
    }

    @Async
    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    @Transactional
    public void deleteExpiredCoupons() {
        LocalDateTime currentDate = LocalDateTime.now();
        couponsRepository.deleteByEndBefore(currentDate);
        log.info("Expired coupons deleted.");
    }
}
