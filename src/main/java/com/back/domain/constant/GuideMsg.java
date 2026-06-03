package com.back.domain.constant;

// 출력 스트링 모음
public enum GuideMsg {
    INTRO("== 명언 앱 ==\n"),
    CMD("명령) "),
    QUOTE("명언 : "),
    AUTHOR("작가 : "),
    QUOTEOLD("명언(기존) : "),
    AUTHOROLD("작가(기존) : "),
    REGISTER("%d번 명언이 등록되었습니다.\n"),
    EMPTY("명언이 입력되지 않았습니다.\n"), // 요구사항에 없는 내용
    VIEW("""
                번호 / 작가 / 명언
                ----------------------
                """),
    PAGE("----------------------\n페이지 : "),
    PAGENOTEXIST("%d번 페이지는 존재하지 않습니다.\n"),
    FILTER("""
                ----------------------
                검색타입 : %s
                검색어 : %s
                ----------------------
                """),
    DELETE("%d번 명언이 삭제되었습니다.\n"),
    ABSENT("%d번 명언은 존재하지 않습니다.\n"),
    BUILDSUCCESS("data.json 파일의 내용이 갱신되었습니다.\n"),

    ERROR("잘못된 명령입니다. 다시 입력해주세요. " +
            "(예시 - 등록 / 목록 / 목록?page=숫자 / 목록?keywordType=author&keyword=작자 / 삭제?id=숫자 / 수정?id=숫자 / 빌드 / 종료)\n"), // 요구사항에 없는 내용
    BUILDFAIL("data.json 빌드를 실패했습니다. 오류 : ");

    final String value;
    GuideMsg(String value) {this.value = value;}

    public String getValue() {
        return value;
    }
}