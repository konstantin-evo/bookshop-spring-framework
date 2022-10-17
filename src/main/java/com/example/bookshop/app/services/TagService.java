package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.BookToTagRepository;
import com.example.bookshop.app.model.dao.TagRepository;
import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.app.model.entity.BookToTag;
import com.example.bookshop.app.model.entity.Tag;
import com.example.bookshop.web.dto.TagDto;
import com.example.bookshop.web.dto.enumuration.TagEnum;
import com.example.bookshop.web.exception.BookshopEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepo;
    private final BookToTagRepository bookToTagRepo;

    public List<TagDto> getTagsData() {
        return tagRepo.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public TagDto getTag(Integer id) {
        return convertToDto(tagRepo.findById(id).orElseThrow(() -> new BookshopEntityNotFoundException(id, "Tag")));
    }

    public void setTagsToBook(String tagsDto, Book book) {
        List<String> tagNames = getListOfTags(tagsDto);
        for (String tagName : tagNames) {
            Tag tag = tagRepo.getTagByName(tagName.toLowerCase().trim())
                    .orElseThrow(() -> new BookshopEntityNotFoundException(Tag.class.getSimpleName(), "Name", tagName));
            bookToTagRepo.save(new BookToTag(book, tag));
        }
    }

    public boolean areTagsExist(String tagsDto) {
        List<String> tagNames = getListOfTags(tagsDto);
        for (String tagName : tagNames) {
            if (!tagRepo.existsByNameIgnoreCase(tagName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param tag              - Book's Tag for which the htmlTag value will be calculated
     *                         in order to add an appropriate style (text size)
     * @param averageTagsCount indicates the size of a tag on an HTML page
     *                         the more books contain a tag, the bigger the font
     * @return the HTML Tag name which determines the size of the font
     * that the tag will display on the HTML page
     */
    private String getHtmlTag(Tag tag, double averageTagsCount) {
        int tagCount = tag.getBookToTag().size();

        if (tagCount < averageTagsCount / 1.3) {
            return TagEnum.XS.getName();
        } else if (tagCount < averageTagsCount) {
            return TagEnum.SM.getName();
        } else if (tagCount < averageTagsCount * 1.3) {
            return TagEnum.MD.getName();
        } else {
            return TagEnum.LG.getName();
        }
    }

    /**
     * @param tagsDto contains a list of tags as a comma-separated string
     *                For example, "children, psychology, magic"
     * @return a list of strings containing tag names in lower case
     */
    private List<String> getListOfTags(String tagsDto) {
        tagsDto = tagsDto.replace(" ", "").toLowerCase();
        return List.of(tagsDto.split(","));
    }

    private TagDto convertToDto(Tag tag) {
        double averageRating = tagRepo.findAll().stream()
                .mapToInt(tagEntity -> tagEntity.getBookToTag().size())
                .average().orElseThrow(IllegalStateException::new);

        return new TagDto(
                tag.getId(),
                tag.getName(),
                getHtmlTag(tag, averageRating));
    }
}
