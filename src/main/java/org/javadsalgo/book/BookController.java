package org.javadsalgo.book;

import org.javadsalgo.userbook.activity.UserBookActivity;
import org.javadsalgo.userbook.activity.UserBookActivityRepositry;
import org.javadsalgo.userbook.activity.UserBookPrimaryKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class BookController {

    private final String COVER_IMAGE_ROOT = "http://covers.openlibrary.org/b/id/";

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserBookActivityRepositry userBookActivityRepositry;

    @GetMapping(value = "/books/{bookId}")
    public String getBookById(@PathVariable String bookId, Model model, @AuthenticationPrincipal OAuth2User principal) {

        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            String coverImageUrl = "/images/no-image.png";
            if (book.getCoverIds() != null && book.getCoverIds().size() > 0) {
                coverImageUrl = COVER_IMAGE_ROOT + book.getCoverIds().get(0) + "-L.jpg";
            }
            model.addAttribute("coverImage", coverImageUrl);
            model.addAttribute("book", book);

            // If user is Logged-In, talk to UserBookActivity and share its tracking info to Book Page
            if (principal != null && principal.getAttribute("login") != null) {

                String userId = principal.getAttribute("login");
                model.addAttribute("loginId", userId);

                // Create Primary Key and hit to userBookActivityRepositry
                UserBookPrimaryKey primaryKey = new UserBookPrimaryKey();
                primaryKey.setUserId(userId);
                primaryKey.setBookId(bookId);

                Optional<UserBookActivity> userBookActivity = userBookActivityRepositry.findById(primaryKey);
                if (userBookActivity.isPresent()) {
                    model.addAttribute("userBooks", userBookActivity.get());
                } else {
                    model.addAttribute("userBooks", new UserBookActivity());
                }
            }
            return "book";
        }
        return "book-not-found";
    }
}
