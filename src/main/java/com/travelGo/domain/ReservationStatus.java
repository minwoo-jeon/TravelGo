package com.travelGo.domain;

public enum ReservationStatus {
    CONFIRMED, //에약 확정 실시간 제고 차감 및 완료
    COMPLETED, // 이용완료 (체크아웃) 취소불가
    CANCELLED //취소 확정
}
