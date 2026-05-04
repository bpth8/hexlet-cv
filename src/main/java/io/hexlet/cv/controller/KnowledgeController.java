package io.hexlet.cv.controller;

import io.github.inertia4j.spring.Inertia;
import io.hexlet.cv.service.KnowledgeService;
import io.hexlet.cv.util.AccountPageRenderer;
import io.hexlet.cv.util.ControllerUtils;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("account/knowledge")
public class KnowledgeController {
    private static final int RECENT_KNOWLEDGE_ITEMS_LIMIT = 2;

    private final KnowledgeService knowledgeService;
    private final Inertia inertia;
    private final ControllerUtils utils;
    private final AccountPageRenderer accountPageRenderer;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Object getKnowledgeHome() {
        log.debug("[KNOWLEDGE CONTROLLER] Get knowledge home request");

        var recentArticles = knowledgeService.getRecentArticles(RECENT_KNOWLEDGE_ITEMS_LIMIT);
        var recentInterviews = knowledgeService.getRecentInterviews(RECENT_KNOWLEDGE_ITEMS_LIMIT);

        Map<String, Object> pageProps = Map.of(
            "recentArticles", recentArticles,
            "recentInterviews", recentInterviews);

        return accountPageRenderer.render("Account/Knowledge/Index", "knowledge", pageProps);
    }

    @GetMapping("/articles")
    @ResponseStatus(HttpStatus.OK)
    public Object getArticles(@RequestParam(required = false) String category,
                              @PageableDefault Pageable pageable) {
        log.debug("[KNOWLEDGE CONTROLLER] Get all articles, category: {}, page: {}",
                category, pageable.getPageNumber());

        var articlesPage = knowledgeService.getArticles(category, pageable);

        Map<String, Object> pageProps = new HashMap<>();
        pageProps.put("articles", articlesPage.getContent());
        pageProps.put("pagination", utils.createPaginationMap(articlesPage, pageable));
        if (category != null && !category.isBlank()) {
            pageProps.put("selectedCategory", category);
        }

        return accountPageRenderer.render("Account/Knowledge/Articles/Index",
                "knowledgeArticles", pageProps);
    }

    @GetMapping("/articles/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Object getArticleById(@PathVariable Long id) {
        log.debug("[KNOWLEDGE CONTROLLER] Get article by id: {}", id);

        var article = knowledgeService.getArticleById(id);

        Map<String, Object> pageProps = Map.of(
            "article", article);

        return accountPageRenderer.render("Account/Knowledge/Articles/Show",
                "knowledgeArticles", pageProps);
    }

    @GetMapping("/interviews")
    @ResponseStatus(HttpStatus.OK)
    public Object getAllInterviews(@RequestParam(required = false) String category,
                                   @PageableDefault Pageable pageable) {
        log.debug("[KNOWLEDGE CONTROLLER] Get all interviews, category: {}, page: {}",
                category, pageable.getPageNumber());

        var interviewsPage = knowledgeService.getInterviews(category, pageable);

        Map<String, Object> pageProps = new HashMap<>();
        pageProps.put("interviews", interviewsPage.getContent());
        if (category != null && !category.isBlank()) {
            pageProps.put("selectedCategory", category);
        }
        pageProps.put("pagination", utils.createPaginationMap(interviewsPage, pageable));

        return accountPageRenderer.render("Account/Knowledge/Interviews/Index",
                "knowledgeInterviews", pageProps);
    }

    @GetMapping("/interviews/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Object getInterviewById(@PathVariable Long id) {
        log.debug("[KNOWLEDGE CONTROLLER] Get interview by id: {}", id);

        var interview = knowledgeService.getInterviewById(id);

        Map<String, Object> pageProps = new HashMap<>();
        pageProps.put("interview", interview);

        return accountPageRenderer.render("Account/Knowledge/Interviews/Show",
                "knowledgeInterviews", pageProps);
    }

    @GetMapping("/")
    public Object defaultRedirect() {
        log.debug("[KNOWLEDGE CONTROLLER] Redirect from /account/knowledge/ to /account/knowledge");
        return inertia.redirect("/account/knowledge");
    }
}
