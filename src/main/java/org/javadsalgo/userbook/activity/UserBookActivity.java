package org.javadsalgo.userbook.activity;

import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;

/**
 * Model that represents a user's interaction with a specific book.
 * Its a 1 to 1 mapping..
 */
@Table(value = "book_by_userId_and_bookid")
public class UserBookActivity {

    @PrimaryKey
    private UserBookPrimaryKey key;

    @Column("start_date")
    @CassandraType(type = CassandraType.Name.DATE)
    private LocalDate startDate;

    @Column("complete_date")
    @CassandraType(type = CassandraType.Name.DATE)
    private LocalDate endDate;

    @Column("reading_status")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String readingStatus;

    @Column("book_rating")
    @CassandraType(type = CassandraType.Name.INT)
    private int bookRating;

    public UserBookPrimaryKey getKey() {
        return key;
    }

    public void setKey(UserBookPrimaryKey key) {
        this.key = key;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getReadingStatus() {
        return readingStatus;
    }

    public void setReadingStatus(String readingStatus) {
        this.readingStatus = readingStatus;
    }

    public int getBookRating() {
        return bookRating;
    }

    public void setBookRating(int bookRating) {
        this.bookRating = bookRating;
    }
}
