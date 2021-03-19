package io.spring.application.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class ArticleHistoryDataList {
    @JsonProperty("histories")
    private final List<ArticleHistoryData> histories;
    @JsonProperty("historyCount")
    private final int count;

    public ArticleHistoryDataList(List<ArticleHistoryData> histories, int count) {
        this.histories = histories;
        this.count = count;
    }
}
