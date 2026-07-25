# Apothic Auto Enchanting

Apothic Enchanting 기반 자동 마법부여 보조 모드입니다.

## 기능 요약

- 자동 마법 부여대 블록 추가
- 블록 우클릭 동작 분기
	- 일반 우클릭: 자동화 UI
	- 웅크린 우클릭: 원본 Apothic Enchanting UI
- 도서관/재료 연동 기반 자동 처리 로직

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
- 믹스인 설정 파일은 루트의 mixin.json을 사용합니다.
