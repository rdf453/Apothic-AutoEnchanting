package dev.rdf453.ApothicAutoEnchant.util;

import net.minecraft.world.item.ItemStack;

import java.util.List;

// 동적 바인드를 위하여 인터페이스로 구현
/*
 * 설계 메모 (2026-07-22 기준)
 * - 현재 상태:
 *   1) 라이브러리 버퍼 삽입/전체추출/입력플러시 추상화가 고정되어 Mixin 경유 호출이 가능하다.
 * - 다음 작업:
 *   1) 요청 수량 기반 부분 추출/삽입 API를 확장한다.
 *   2) 실패 사유를 구분할 반환 규약을 설계한다.
 * - 리스크/주의:
 *   1) 메서드 명명은 충돌 회피용 접두를 쓰고 있으니 API 안정화 시점에 정리한다.
 *   2) flushBufferToInput은 도서관 위치 캐시와 함께 호출하는 전제라 호출부 책임이 크다.
 */
public interface LibraryTransfer {
    // 버퍼로 아이템을 넣는다. 내부 저장 가능량만큼만 수용된다.
    boolean AutoEnch_insertList(ItemStack stack);

    // 버퍼에 저장된 아이템 전체를 리스트로 추출한다.
    List<ItemStack> AutoEnch_extractList();

    // 버퍼의 아이템을 라이브러리 입력 슬롯으로 가능한 만큼 밀어넣는다.
    int AutoEnch_flushBufferToInput();
}