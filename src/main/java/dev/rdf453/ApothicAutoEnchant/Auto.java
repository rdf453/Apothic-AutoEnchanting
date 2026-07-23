package dev.rdf453.ApothicAutoEnchant;

import dev.rdf453.ApothicAutoEnchant.table.AutoEnchantingTableBlock;
import dev.shadowsoffire.placebo.registry.DeferredHelper;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;

/*
 * 설계 메모 (2026-07-22 기준)
 * - 현재 상태:
 *   1) 메인 진입점은 ApothicAutoEnchanting에 두고, Auto는 등록 보조 클래스 역할로 분리한다.
 *   2) 블록, BlockEntityType, 메뉴, 크리에이티브 탭 같은 공용 등록을 이 파일 기준으로 모을 수 있다.
 * - 다음 작업:
 *   1) 블록/BlockEntityType/메뉴 등록 헬퍼를 이 클래스 아래에 묶는다.
 *   2) 필요하면 원본 모드의 Ench 패턴처럼 정적 내부 클래스로 항목을 분리한다.
 * - 리스크/주의:
 *   1) 메인 클래스와 등록 클래스의 책임을 겹치지 않게 유지한다.
 *   2) modid 문자열과 등록 이름은 전체 패키지에서 일관되게 맞춘다.
 */
public final class Auto {

	private static final DeferredHelper R = DeferredHelper.create(ApothicAutoEnchanting.MODID);

	private Auto() {
	}

	public static final class Blocks {

		public static final Holder<Block> AUTO_ENCHANT_TABLE = R.block("auto_enchant_table",
			AutoEnchantingTableBlock::new,
			p -> p.mapColor(MapColor.COLOR_RED).strength(5.0F, 1200.0F).requiresCorrectToolForDrops().lightLevel(s -> 7));
	}
}
