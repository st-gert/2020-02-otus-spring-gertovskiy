package ru.otus.job11.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.job11.model.Author;
import ru.otus.job11.model.Book;
import ru.otus.job11.model.Genre;
import ru.otus.job11.model.Review;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ChangeLog
public class DatabaseChangelog {

    private final Map<String, Genre> genreMap = new HashMap<>();
    private final Map<String, Author> authorMap = new HashMap<>();
    private final Map<String, Book> bookMap = new HashMap<>();

    @ChangeSet(order = "000", id = "dropDB", author = "sgertovskiy", runAlways = true)
    public void dropDB(MongoDatabase database){
        database.drop();
    }

    @ChangeSet(order = "001", id = "addGenres", author = "sgertovskiy", runAlways = true)
    public void addGenres(MongoTemplate template) {
        addGenre(template, "Фантастика");
        addGenre(template, "Сатира");
        addGenre(template, "Детектив");
        addGenre(template, "Дневники");
    }

    private void addGenre(MongoTemplate template, String genreName ) {
        Genre genre = template.save(new Genre(genreName));
        genreMap.put(genre.getGenreName(), genre);
    }

    @ChangeSet(order = "002", id = "addAuthors", author = "sgertovskiy", runAlways = true)
    public void addAuthors(MongoTemplate template) {
        addAuthor(template, "Аркадий","Стругацкий");
        addAuthor(template, "Борис","Стругацкий");
        addAuthor(template, "Илья","Ильф");
        addAuthor(template, "Евгений","Петров");
        addAuthor(template, "Кир","Булычев");
        addAuthor(template, "Борис","Акунин");
        addAuthor(template, "Агата","Кристи");
    }

    private void addAuthor(MongoTemplate template, String firstName, String lastName) {
        Author author = template.save(new Author(firstName, lastName));
        authorMap.put(author.getFullName(), author);
    }

    @ChangeSet(order = "003", id = "addBooks", author = "sgertovskiy", runAlways = true)
    public void addBooks(MongoTemplate template) {
        addBook(template, "Понедельник начинается в субботу", "Фантастика", "Аркадий Стругацкий", "Борис Стругацкий");
        addBook(template, "Сказка о Тройке", "Фантастика", "Аркадий Стругацкий", "Борис Стругацкий");
        addBook(template, "Комментарии к пройденному", "Дневники", "Борис Стругацкий");
        addBook(template, "Путешествие Алисы", "Фантастика", "Кир Булычев");
        addBook(template, "Сто лет тому вперед", "Фантастика", "Кир Булычев");
        addBook(template, "Двенадцать стульев", "Сатира", "Илья Ильф", "Евгений Петров");
        addBook(template, "Золотой телёнок", "Сатира", "Илья Ильф", "Евгений Петров");
        addBook(template, "Записные книжки", "Дневники", "Илья Ильф");
        addBook(template, "Десять негритят", "Детектив", "Агата Кристи");
        addBook(template, "Убийство в „Восточном экспрессе“", "Детектив", "Агата Кристи");
        addBook(template, "Азазель", "Детектив", "Борис Акунин");
        addBook(template, "Статский советник", "Детектив", "Борис Акунин");
        genreMap.values().forEach(template::save);
        authorMap.values().forEach(template::save);
    }

    private void addBook(MongoTemplate template, String title, String genreName, String ... authorFullName) {
        List<Author> authors = Arrays.stream(authorFullName)
                .map(authorMap::get)
                .collect(Collectors.toList());
        Genre genre = genreMap.get(genreName);
        Book book = template.save(new Book(title, genre, authors));
        bookMap.put(book.getTitle(), book);
        genre.getBooks().add(book);
        authors.forEach(a -> a.getBooks().add(book));
    }

    @ChangeSet(order = "004", id = "addReviews", author = "sgertovskiy", runAlways = true)
    public void addReviews(MongoTemplate template) {
        addReview(template,"Понедельник начинается в субботу",
                "Хорошая книга",
                "Замечательная книга",
                "Превосходная!");
        addReview(template,"Путешествие Алисы",
                "Интересно, полезно",
                "Увлекательно");
        addReview(template,"Двенадцать стульев", "Смешно");
        addReview(template,"Золотой телёнок", "Смешно и жизненно");
        addReview(template,"Десять негритят", "Так им всем и надо");
    }

    private void addReview(MongoTemplate template, String bookTitle, String ... opinion) {
        Book book = bookMap.get(bookTitle);
        List<Review> reviews = Arrays.stream(opinion)
                .map(r -> new Review(book, r))
                .collect(Collectors.toList());
        Collection<Review> addedReviews = template.insert(reviews, Review.class);
        book.getReviews().addAll(addedReviews);
        template.save(book);
    }

}
