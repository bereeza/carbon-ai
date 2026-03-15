package com.carbon.ingestion.mapping;

import com.carbon.shared.dto.ContentResponse;
import com.carbon.shared.event.ContentEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContentMapper {
    ContentResponse mapContentEvent(ContentEvent event);
}

