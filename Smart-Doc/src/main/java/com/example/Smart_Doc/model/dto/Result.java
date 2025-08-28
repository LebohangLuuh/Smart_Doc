
package com.example.Smart_Doc.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private int statusCode;
    private String message;
    private T data;
    private boolean success;
}