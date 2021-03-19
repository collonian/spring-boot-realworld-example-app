package io.spring.core.article;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleHistory {
    private String id;
    private String historyCode; // FIXME: use enum
    private String articleId;
    private String title;
    private String description;
    private String body;
    private DateTime createdAt;

    public static ArticleHistory deleted(Article article) {
        ArticleHistory history = fromArticle(article);
        history.historyCode = "DELETE";
        history.createdAt = new DateTime(); // now
        return history;
    }
    public static ArticleHistory created(Article article) {
        ArticleHistory history = fromArticle(article);
        history.historyCode = "CREATE";
        history.createdAt = article.getCreatedAt();
        return history;
    }

    public static ArticleHistory updated(Article article) {
        ArticleHistory history = fromArticle(article);
        history.historyCode = "UPDATE";
        history.createdAt = article.getUpdatedAt();
        return history;
    }

    private static ArticleHistory fromArticle(Article article) {
        ArticleHistory history = new ArticleHistory();
        history.id =  UUID.randomUUID().toString();
        history.articleId = article.getId();
        history.title = article.getTitle();
        history.description = article.getDescription();
        history.body = article.getBody();
        return history;
    }
}
