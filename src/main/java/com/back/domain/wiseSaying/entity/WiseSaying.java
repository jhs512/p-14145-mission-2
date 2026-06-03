package com.back.domain.wiseSaying.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
public class WiseSaying {
    int quoteId;
    @Setter
    String quote;
    @Setter
    String author;

    public WiseSaying(int quoteId, String quote, String author) {
        this.quoteId = quoteId;
        this.quote = quote;
        this.author = author.isEmpty() ? "입력없음": author;
    }

    public boolean compareNo(int i) {return (quoteId == i);}

    public String toString() {return quoteId + " / " + author + " / " + quote;}
}