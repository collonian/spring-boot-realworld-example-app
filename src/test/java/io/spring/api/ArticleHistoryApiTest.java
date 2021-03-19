package io.spring.api;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.spring.JacksonCustomizations;
import io.spring.api.security.WebSecurityConfig;
import io.spring.application.ArticleHistoryQueryService;
import io.spring.application.Page;
import io.spring.application.data.ArticleHistoryData;
import io.spring.application.data.ArticleHistoryDataList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

@WebMvcTest({ArticleHistoryApi.class})
@Import({WebSecurityConfig.class, JacksonCustomizations.class})
public class ArticleHistoryApiTest  extends TestWithCurrentUser  {

    @Autowired private MockMvc mvc;
    @MockBean private ArticleHistoryQueryService articleHistoryQueryService;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        RestAssuredMockMvc.mockMvc(mvc);

        when(articleHistoryQueryService.queryHistories(anyString(), any()))
                .thenReturn(new ArticleHistoryDataList(Collections.emptyList(), 0));
        when(articleHistoryQueryService.queryHistories(eq("aaaa"), any()))
                .thenReturn(new ArticleHistoryDataList(
                        Arrays.asList(new ArticleHistoryData(), new ArticleHistoryData()),
                        2
                ));

    }

    @Test
    public void should_succeed_when_slug_is_exists() {
        RestAssuredMockMvc.when()
                .get("/articles/aaaa/histories")
                .then()
                .statusCode(200)
                .body("historyCount", equalTo(2))
        ;
        Mockito.verify(articleHistoryQueryService).queryHistories(eq("aaaa"), any());
    }

    @Test
    public void should_empty_response_when_slug_is_not_exists() {

        RestAssuredMockMvc.when()
                .get("/articles/not-exists/histories")
                .then()
                .statusCode(200)
                .body("historyCount", equalTo(0))
        ;
        Mockito.verify(articleHistoryQueryService).queryHistories(eq("not-exists"), any());
    }

    @Test
    public void should_query_params_passed_to_query_service() {
        RestAssuredMockMvc.when()
                .get("/articles/some-uuid/histories?limit=6&offset=30")
                .then()
                .statusCode(200)
        ;
        ArgumentCaptor<Page> captor = ArgumentCaptor.forClass(Page.class);
        Mockito.verify(articleHistoryQueryService).queryHistories(anyString(), captor.capture());

        assertEquals(6, captor.getValue().getLimit());
        assertEquals(30, captor.getValue().getOffset());
    }
}