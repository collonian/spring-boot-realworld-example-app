package io.spring.application;

import io.spring.application.data.ArticleHistoryData;
import io.spring.application.data.ArticleHistoryDataList;
import io.spring.infrastructure.mybatis.readservice.ArticleHistoryReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleHistoryQueryService {

    private final ArticleHistoryReadService articleHistoryReadService;

    @Autowired
    public ArticleHistoryQueryService(ArticleHistoryReadService articleHistoryReadService) {
        this.articleHistoryReadService = articleHistoryReadService;
    }

    public ArticleHistoryDataList queryHistories(String slug, Page page) {
        List<ArticleHistoryData> histories = articleHistoryReadService.queryHistories(slug, page);
        int count = articleHistoryReadService.countHistories(slug);
        return new ArticleHistoryDataList(histories, count);
    }
}
