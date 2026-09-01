# Apothic Auto Enchanting

Apothic Enchanting 기반 자동 마법부여 보조 모드입니다.

## 기능 요약

- 자동 마법 부여대 블록 추가
- 블록 우클릭 동작 분기
	- 일반 우클릭: 자동화 UI
	- 웅크린 우클릭: 원본 Apothic Enchanting UI
- 도서관/재료 연동 기반 자동 처리 로직
- 인첸트 테이블 기준 +-5칸의 도서관과 상자(배낭,서랍등의 저장소)에 엑세스하여 자동화를 처리합니다

## 요구 사항

- Minecraft 26.1.2
- NeoForge 26.1.2.78+
- Apothic Enchanting

## 빌드

```bash
./gradlew clean jar
```

산출물:

- build/libs/apothic_auto_enchanting-<version>.jar

## 개발 메모

- 모드 ID는 apothic_auto_enchanting으로 고정합니다.

