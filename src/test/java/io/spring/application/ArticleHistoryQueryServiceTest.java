package io.spring.application;

import io.spring.application.data.ArticleHistoryDataList;
import io.spring.core.article.Article;
import io.spring.core.article.ArticleRepository;
import io.spring.core.favorite.ArticleFavoriteRepository;
import io.spring.core.user.User;
import io.spring.core.user.UserRepository;
import io.spring.infrastructure.repository.MyBatisArticleFavoriteRepository;
import io.spring.infrastructure.repository.MyBatisArticleRepository;
import io.spring.infrastructure.repository.MyBatisUserRepository;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@MybatisTest
@Import({
        ArticleHistoryQueryService.class,
        MyBatisUserRepository.class,
        MyBatisArticleRepository.class,
        MyBatisArticleFavoriteRepository.class
})
public class ArticleHistoryQueryServiceTest {
    @Autowired private ArticleHistoryQueryService historyQueryService;
    @Autowired private ArticleRepository articleRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ArticleFavoriteRepository articleFavoriteRepository;

    private User user;
    private Article article;
    @Before
    public void setUp() {
        user = new User("john.doe@gmail.com", "john.doe", "123", "", "");
        userRepository.save(user);
        article = new Article(
                        "simple one", "desc", "body", Arrays.asList("java", "spring"), user.getId(), new DateTime()
        );
        articleRepository.save(article);
    }

    @Test
    public void should_get_empty_history_when_query_not_exists_article() {
        ArticleHistoryDataList result = historyQueryService.queryHistories("this-is-not-exists", new Page(1, 10));
        assertEquals(0, result.getCount());
    }

    @Test
    public void should_get_single_history_when_query_created_article() {
        ArticleHistoryDataList result = historyQueryService.queryHistories("simple-one", new Page(0, 10));
        assertEquals(1, result.getCount());
    }

    @Test
    public void should_get_triple_histories_when_query_updated_twice() {
        article.update(
                "second title",
                "second description",
                "second body");
        articleRepository.save(article);

        article.update(
                "third title",
                "third description",
                "third body");
        articleRepository.save(article);

        ArticleHistoryDataList result = historyQueryService.queryHistories("third-title", new Page(0, 10));
        assertEquals(3, result.getCount());

        assertEquals("simple one", result.getHistories().get(2).getTitle());
        assertEquals("CREATE", result.getHistories().get(2).getHistoryCode());

        assertEquals("second title", result.getHistories().get(1).getTitle());
        assertEquals("UPDATE", result.getHistories().get(1).getHistoryCode());

        assertEquals("third title", result.getHistories().get(0).getTitle());
        assertEquals("UPDATE", result.getHistories().get(0).getHistoryCode());
    }

    @Test
    public void should_empty_history_when_article_deleted() {
        articleRepository.remove(article);

        ArticleHistoryDataList result = historyQueryService.queryHistories("third-title", new Page(0, 10));
        assertEquals(0, result.getCount());
    }
}