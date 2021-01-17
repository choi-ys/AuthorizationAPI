package io.example.authorization.domain.partner.entity;

public enum PartnerRole {
    FORBIDDEN,          // API 사용 권한 없음
    STARTER,            // 일부 API만 통신 가능 및 허용량 제한
    TRIAL,              // 모든 API 통신은 가능하나 허용량 제한
    STANDARD,           // 제휴사
    PREMIUM             // 돈많이 낸 제휴사
}