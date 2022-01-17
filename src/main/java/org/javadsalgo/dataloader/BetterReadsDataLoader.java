/*
package org.javadsalgo.dataloader;

import org.javadsalgo.author.Author;
import org.javadsalgo.author.AuthorRepository;
import org.javadsalgo.book.Book;
import org.javadsalgo.book.BookRepository;
import org.javadsalgo.connection.DataStaxAstraProperties;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
@EnableConfigurationProperties(DataStaxAstraProperties.class)
public class BetterReadsDataLoader {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    BookRepository bookRepository;

    @Value("${datadump.location.author}")
    private String authorDumpLocation;

    @Value("${datadump.location.work}")
    private String workDumpLocation;

    @PostConstruct
    public void start() throws IOException {
        initAuthors();
        initBooks();
    }

    private void initBooks() {
        Path path = Paths.get(workDumpLocation);
        try {
            Stream<String> lines = Files.lines(path);
            lines.forEach(line -> {
                // Read and parse the line
                String jsonLine = line.substring(line.indexOf("{"));
                try {
                    JSONObject jsonObject = new JSONObject(jsonLine);
                    // Create a Book instance

                    Book book = new Book();
                    book.setName(jsonObject.optString("title"));
                    book.setId(jsonObject.optString("key").replace("/works/", ""));

                    JSONObject descJsonObject = jsonObject.optJSONObject("description");
                    if (descJsonObject != null)
                        book.setDescription(descJsonObject.optString("value"));

                    JSONObject bookCreatedObject = jsonObject.optJSONObject("created");
                    if (bookCreatedObject != null)
                        book.setPublishedDate(LocalDate.parse(bookCreatedObject.getString("value"),
                                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")));

                    // Set CoverIds
                    JSONArray coversJsonArray = jsonObject.optJSONArray("covers");
                    if (coversJsonArray != null) {
                        List<String> coverIds = new ArrayList<>();
                        for (int i = 0; i < coversJsonArray.length(); i++) {
                            coverIds.add(coversJsonArray.getString(i));
                        }
                        book.setCoverIds(coverIds);
                    }

                    // Set AuthorIds
                    JSONArray authorJsonArray = jsonObject.optJSONArray("authors");
                    if (authorJsonArray != null) {
                        List<String> authorIds = new ArrayList<>();
                        for (int i = 0; i < authorJsonArray.length(); i++) {
                            String authorId = authorJsonArray
                                    .getJSONObject(i)
                                    .getJSONObject("author")
                                    .optString("key")
                                    .replace("/authors/", "");
                            authorIds.add(authorId);
                        }
                        book.setAuthorIds(authorIds);

                        // Fetch authorNames from all authorIds from cassandra DB and set in book instance
                        List<String> authorNameList = authorIds.stream()
                                .map(id -> authorRepository.findById(id))
                                .map(optionalAuthor -> {
                                            if (optionalAuthor.isPresent())
                                                return optionalAuthor.get().getName();
                                            else
                                                return "Unknown Author";
                                        }
                                ).collect(Collectors.toList());

                        book.setAuthorNames(authorNameList);

                        System.out.println("Saving book name: " + book.getName());
                        bookRepository.save(book);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
        }
    }

    private void initAuthors() throws IOException {
        Path path = Paths.get(authorDumpLocation);
        try {
            Stream<String> lines = Files.lines(path);
            lines.forEach(line -> {
                // Read and parse the line
                String jsonLine = line.substring(line.indexOf("{"));
                try {
                    JSONObject jsonObject = new JSONObject(jsonLine);

                    // Construct Author
                    Author author = new Author();
                    author.setName(jsonObject.optString("name"));
                    author.setPersonalName(jsonObject.optString("personal_name"));
                    author.setId(jsonObject.optString("key").replace("/authors/", ""));

                    System.out.println("Saving author" + author.getName() + "...");

                    // Persist using AuthorRepository
                    authorRepository.save(author);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {

        }
    }

    public static void main(String[] args) {
        System.out.println("Main method called");
        SpringApplication.run(BetterReadsDataLoader.class, args);
    }

    */
/**
     * This is necessary to have the Spring Boot app use the Astra secure bundle
     * to connect to the database
     *//*

    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
        Path bundle = astraProperties.getSecureConnectBundle().toPath();
        return builder -> builder.withCloudSecureConnectBundle(bundle);
    }
}
*/
