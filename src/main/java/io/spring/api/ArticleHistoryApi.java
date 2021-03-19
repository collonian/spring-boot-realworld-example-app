package io.spring.api;

import io.spring.application.ArticleHistoryQueryService;
import io.spring.application.Page;
import io.spring.application.data.ArticleHistoryDataList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articles/{slug}/histories")
public class ArticleHistoryApi {
    private final ArticleHistoryQueryService articleHistoryQueryService;

    @Autowired
    public ArticleHistoryApi(ArticleHistoryQueryService articleHistoryQueryService) {
        this.articleHistoryQueryService = articleHistoryQueryService;
    }

    @GetMapping
    public ArticleHistoryDataList queryHistories(
            @PathVariable("slug") String slug,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        return articleHistoryQueryService.queryHistories(slug, new Page(offset, limit));
    }
}
