package dev.rdf453.ApothicAutoEnchant.table;

/*
 * 설계 메모 (2026-07-22 기준)
 * - 현재 상태:
 *   1) 실제 Screen 구현은 아직 없고 UI 진입 전용 자리만 남아 있다.
 * - 다음 작업:
 *   1) AbstractContainerScreen<EnchantMenu> 구현체로 전환한다.
 *   2) 버튼(+1/+10/-10/ALL/AUTO) 배치와 클릭 패킷 전달을 연결한다.
 *   3) 자동화 상태, 저장 XP, 현재 비용 레벨을 라벨로 렌더링한다.
 * - 리스크/주의:
 *   1) 메뉴 타입과 MenuScreens.register가 없으면 UI 진입 경로 자체가 막힌다.
 */
public class EnchTableScreen {

	private EnchTableScreen() {
		// Design-only placeholder until mapping-specific UI implementation is added.
	}
}
