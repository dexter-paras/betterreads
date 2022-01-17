package org.javadsalgo.home;

import org.javadsalgo.userbook.userbooks.BooksByUser;
import org.javadsalgo.userbook.userbooks.BooksByUserRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.awt.print.Pageable;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Show all user books from books_by_userId table
 * When User press home_page or "/" , it hits to this controller, get data
 * and display home page
 */

@Controller
public class HomeController {

    private final String COVER_IMAGE_ROOT = "http://covers.openlibrary.org/b/id/";

    @Autowired
    BooksByUserRepositry booksByUserRepositry;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal OAuth2User principal,
                       Model model) {

        if (principal == null || principal.getAttribute("login") == null) {
            return "index";
        }
        String loginId = principal.getAttribute("login");

        Slice<BooksByUser> booksSlice = booksByUserRepositry.findAllById(loginId, CassandraPageRequest.of(0, 100));
        List<BooksByUser> booksByUserList = booksSlice.getContent();
        booksByUserList.stream().distinct().map(book -> {
            String coverImageUrl = "/images/no-image.png";
            if (book.getCoverIds() != null & book.getCoverIds().size() > 0) {
                coverImageUrl = COVER_IMAGE_ROOT + book.getCoverIds().get(0) + "-M.jpg";
            }
            book.setCoverUrl(coverImageUrl);
            return book;
        }).collect(Collectors.toList());

        model.addAttribute("books", booksByUserList);
        return "home";
    }

}
