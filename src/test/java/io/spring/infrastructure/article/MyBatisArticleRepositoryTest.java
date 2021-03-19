package io.spring.infrastructure.article;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import io.spring.core.article.Article;
import io.spring.core.article.ArticleHistory;
import io.spring.core.article.ArticleRepository;
import io.spring.core.article.Tag;
import io.spring.core.user.User;
import io.spring.core.user.UserRepository;
import io.spring.infrastructure.mybatis.mapper.ArticleHistoryMapper;
import io.spring.infrastructure.repository.MyBatisArticleRepository;
import io.spring.infrastructure.repository.MyBatisUserRepository;
import java.util.Arrays;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@MybatisTest
@RunWith(SpringRunner.class)
@Import({MyBatisArticleRepository.class, MyBatisUserRepository.class})
public class MyBatisArticleRepositoryTest {
  @Autowired private ArticleRepository articleRepository;

  @Autowired private UserRepository userRepository;
  @MockBean(name="articleHistoryMapper") private ArticleHistoryMapper articleHistoryMapper;

  private Article article;

  @Before
  public void setUp() {
    User user = new User("aisensiy@gmail.com", "aisensiy", "123", "bio", "default");
    userRepository.save(user);
    article = new Article("test", "desc", "body", Arrays.asList("java", "spring"), user.getId());
  }

  @Test
  public void should_create_and_fetch_article_success() {
    articleRepository.save(article);
    Optional<Article> optional = articleRepository.findById(article.getId());
    assertTrue(optional.isPresent());
    assertEquals(optional.get(), article);
    assertTrue(optional.get().getTags().contains(new Tag("java")));
    assertTrue(optional.get().getTags().contains(new Tag("spring")));

    ArgumentCaptor<ArticleHistory> captor = ArgumentCaptor.forClass(ArticleHistory.class);
    verify(articleHistoryMapper).insert(captor.capture());
    assertEquals("CREATE", captor.getValue().getHistoryCode());
  }

  @Test
  public void should_update_and_fetch_article_success() {
    articleRepository.save(article);

    String newTitle = "new test 2";
    article.update(newTitle, "", "");
    articleRepository.save(article);
    System.out.println(article.getSlug());
    Optional<Article> optional = articleRepository.findBySlug(article.getSlug());
    assertTrue(optional.isPresent());
    Article fetched = optional.get();
    assertEquals(fetched.getTitle(), newTitle);
    assertNotEquals(fetched.getBody(), "");

    ArgumentCaptor<ArticleHistory> captor = ArgumentCaptor.forClass(ArticleHistory.class);
    verify(articleHistoryMapper, times(2)).insert(captor.capture());
    assertEquals("UPDATE", captor.getValue().getHistoryCode());
    assertEquals(newTitle, captor.getValue().getTitle());
  }

  @Test
  public void should_delete_article() {
    articleRepository.save(article);

    articleRepository.remove(article);
    assertFalse(articleRepository.findById(article.getId()).isPresent());

    ArgumentCaptor<ArticleHistory> captor = ArgumentCaptor.forClass(ArticleHistory.class);
    verify(articleHistoryMapper, times(2)).insert(captor.capture());
    assertEquals("DELETE", captor.getValue().getHistoryCode());
  }
}
