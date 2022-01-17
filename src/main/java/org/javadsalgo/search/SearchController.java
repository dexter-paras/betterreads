package org.javadsalgo.search;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SearchController {

    private final String COVER_IMAGE_ROOT = "http://covers.openlibrary.org/b/id/";
    private final WebClient webClient;

    /* Increase memory size to contain buffer response */
    public SearchController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.exchangeStrategies(ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                .build()).baseUrl("http://openlibrary.org/search.json").build();
    }

    @GetMapping(value = "/search")
    public String getSearchResults(@RequestParam String query, Model model,
                                   @AuthenticationPrincipal OAuth2User principal) {
        // 1. Hit to open library url and get the result
        Mono<SearchResult> searchResultMono = this.webClient.get().uri("?q={query}", query)
                .retrieve().bodyToMono(SearchResult.class);

        // 2. Get result from Mono Results
        SearchResult result = searchResultMono.block();

        // 3. Convert result coming from API to own format and set it to model object
        List<SearchResultBook> convertedBookList = result.getDocs().stream().limit(10)
                .map(bookResult ->
                {
                    bookResult.setKey(bookResult.getKey().replace("/works/", ""));
                    String coverId = bookResult.getCover_i();
                    if (StringUtils.hasText(coverId)) {
                        coverId = COVER_IMAGE_ROOT + coverId + "-M.jpg";
                    } else {
                        coverId = "/images/no-image.png";
                    }
                    bookResult.setCover_i(coverId);
                    return bookResult;
                }).collect(Collectors.toList());

        model.addAttribute("searchResults", convertedBookList);

        // 4. check if user is logged-in, if yes, show logout button else No
        if (principal != null && principal.getAttribute("login") != null)
            model.addAttribute("loginId", principal.getAttribute("login"));

        // 5. Return search book results
        return "search";
    }

}
