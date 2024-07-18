package org.baratie.yumyum.global.utils.pageDto;

import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
public class CustomSliceDto {
    private List<?> content;
    private int pageNumber;
    private int pageSize;
    private int numberOfElements;
    private boolean first;
    private boolean last;

    public CustomSliceDto(Slice<?> slice) {
        this.content = slice.getContent();
        this.numberOfElements = slice.getNumberOfElements();
        this.pageNumber = slice.getNumber();
        this.pageSize = slice.getSize();
        this.first = slice.isFirst();
        this.last = slice.isLast();
    }
}
