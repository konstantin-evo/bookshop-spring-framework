package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.TagRepository;
import com.example.bookshop.app.model.entity.Tag;
import com.example.bookshop.web.dto.TagDto;
import com.example.bookshop.web.dto.enumuration.TagEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TagService {
    private final TagRepository tagRepo;

    @Autowired
    public TagService(TagRepository tagRepo) {
        this.tagRepo = tagRepo;
    }

    public List<TagDto> getTagsData() {
        return tagRepo.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public TagDto getTag(Integer id){
        return convertToDto(tagRepo.findById(id).get());
    }

    private String getHtmlTag(Tag tag, double average){
        int rating = tag.getBookToTag().size();

        if (rating < average/1.3) {
            return TagEnum.XS.getName();
        } else if (rating < average) {
            return TagEnum.SM.getName();
        } else if (rating < average * 1.3) {
            return TagEnum.MD.getName();
        } else {
            return TagEnum.LG.getName();
        }
    }

    private TagDto convertToDto(Tag tag){
        double averageRating = tagRepo.findAll().stream()
                .mapToInt(tagEntity -> tagEntity.getBookToTag().size())
                .average().orElseThrow(IllegalStateException::new);

        return new TagDto(
                tag.getId(),
                tag.getName(),
                getHtmlTag(tag, averageRating));
    }
}
