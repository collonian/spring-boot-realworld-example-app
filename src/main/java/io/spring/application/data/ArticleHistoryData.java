package io.spring.application.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleHistoryData {
    private String id;
    private String articleId;
    private String historyCode;
    private String title;
    private String description;
    private String body;
    private DateTime createdAt;
}
