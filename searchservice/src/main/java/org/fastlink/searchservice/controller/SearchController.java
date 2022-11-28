package org.fastlink.searchservice.controller;

import org.fastlink.searchservice.dto.UserDto;
import org.fastlink.searchservice.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SearchController {
    @Autowired
    private  SearchService searchService;

    @GetMapping("api/search")
    @ResponseBody
    public UserDto[] search(@RequestParam(name = "value") String value) {
        return searchService.search(value);
    }
    @GetMapping("api/search/media_number")
    @ResponseBody
    public Integer getMediaNumber(@RequestParam(name = "value") Long userId) {
        return searchService.getMediaNumber(userId);
    }

}

