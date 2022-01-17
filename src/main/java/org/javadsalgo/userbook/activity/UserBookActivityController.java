package org.javadsalgo.userbook.activity;

import org.javadsalgo.book.Book;
import org.javadsalgo.book.BookRepository;
import org.javadsalgo.userbook.userbooks.BooksByUser;
import org.javadsalgo.userbook.userbooks.BooksByUserRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.Optional;

/*
 * Get Book Form info from user and does 2 action
 *   1. Save book + user info in UserBookActivity table
 *   2. Save Book info in BooksByUser table which collect all books read by User
 */

@Controller
public class UserBookActivityController {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserBookActivityRepositry userBookActivityRepositry;

    @Autowired
    BooksByUserRepositry booksByUserRepositry;

    @PostMapping("/addUserBook")
    public ModelAndView addBookForUser(@RequestBody MultiValueMap<String, String>
                                               formData, @AuthenticationPrincipal OAuth2User principal) {
        /* 1. return if user not logged in */
        if (principal == null || principal.getAttribute("login") == null) {
            return null;
        }

        /* 2. Get BookId from form and find book info in table*/
        String bookId = formData.getFirst("bookId");
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (!optionalBook.isPresent()) {
            return new ModelAndView("redirect:/");
        }
        Book book = optionalBook.get();

        /* 3. Create UserBookActivity Model and persist the info
              Persist in book_by_userId_and_bookid table
        */
        String userId =principal.getAttribute("login");
        UserBookPrimaryKey key = new UserBookPrimaryKey();
        key.setBookId(bookId);
        key.setUserId(userId);

        UserBookActivity userBookActivity = new UserBookActivity();
        userBookActivity.setKey(key);

        userBookActivity.setBookRating(Integer.parseInt(formData.getFirst("bookRating")));
        userBookActivity.setStartDate(LocalDate.parse(formData.getFirst("startDate")));
        userBookActivity.setEndDate(LocalDate.parse(formData.getFirst("endDate")));
        userBookActivity.setReadingStatus(formData.getFirst("readingStatus"));

        userBookActivityRepositry.save(userBookActivity);

        /* 4. Persist info in books_by_userId table as well so to show the same in home page */
        BooksByUser booksByUser = new BooksByUser();
        booksByUser.setBookId(bookId);
        booksByUser.setId(userId);
        booksByUser.setReadingStatus(formData.getFirst("readingStatus"));
        booksByUser.setBookName(book.getName());
        booksByUser.setAuthorNames(book.getAuthorNames());
        booksByUser.setCoverIds(book.getCoverIds());
        booksByUser.setAuthorNames(book.getAuthorNames());
        booksByUser.setRating(Integer.parseInt(formData.getFirst("bookRating")));
        booksByUserRepositry.save(booksByUser);

        return new ModelAndView("redirect:/books/" + bookId);
    }

}