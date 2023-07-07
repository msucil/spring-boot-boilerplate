package design.boilerplate.springboot.book.mapper;

import design.boilerplate.springboot.book.dto.BookRequest;
import design.boilerplate.springboot.book.dto.BookResponse;
import design.boilerplate.springboot.book.dto.BookUpdateRequest;
import design.boilerplate.springboot.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Created on Ağustos, 2020
 *
 * @author Faruk
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BookMapper {

	BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

	Book covertToBook(BookRequest createRequest);

	Book covertToBook(BookUpdateRequest updateRequest);

	BookResponse convertToBookResponse(Book book);

	List<BookResponse> convertToBookResponses(List<Book> books);

}
