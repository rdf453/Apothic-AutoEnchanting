//package dev.rdf453.ApothicAutoEnchant.table;

//import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
//import net.neoforged.neoforge.fluids.FluidStack;
//import net.neoforged.neoforge.transfer.fluid.FluidResource;


//xp 주입 지원
//public class EnchantmentFluidHandler extends FluidStacksResourceHandler {
//
//    public EnchantmentFluidHandler() {
//        super(1);
//    }
//
//    @Override
//    public boolean matches(FluidStack stack, FluidResource resource) {
//        return  resource.is(Fluid.)
//    }
//}
//경험치 액체 모드마다 달라서 통일할거 아니면 안하는게 나을듯
//* 알파 테스트
//  1.doEnchant 이상 유무
//  2.아이템 핸들러 이상 유무
//  3.스크린 버튼 위치 조정
// * 기능 테스트
//* 1. 비용 선택 전에는 자동화가 실행되지 않는지
//* 2. 파이프 삽입: 0번 대상 아이템, 1번 청금석 제한이 맞는지
//* 3. 재료 부족과 전송 실패 시 자동화가 꺼지고 저장되는지
//* 4. 인챈트 후 대상 아이템이 실제로 teInv[0]에서 변경되는지
//* 5. 청금석이 정확히 소비되는지
//* 6. 결과 책이 도서관으로 정확히 이동하는지
//* 7. 상자/도서관을 제거·교체한 뒤 캐시 위치가 안전하게 처리되는지
//* 8. 화면의 비용 선택, XP, 활성 상태와 서버 값이 일치하는지
// */

