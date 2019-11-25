package com.example.demokstream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageViewEvent {
    private String userId, page;
    private long duration;
}
