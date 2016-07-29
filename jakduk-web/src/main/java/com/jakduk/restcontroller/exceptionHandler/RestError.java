package com.jakduk.restcontroller.exceptionHandler;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jakduk.common.CommonConst;
import com.jakduk.exception.ServiceError;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.i18n.LocaleContextHolder;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;


/**
 * @author pyohwan
 * 16. 3. 5 오전 12:31
 */

@Data
@JsonTypeName(value = "error")
@JsonTypeInfo(use= JsonTypeInfo.Id.NONE, include= JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RestError {

    private String code;
    private String message;
    private List<String> fields;

    public RestError(String message) {
        this.code = CommonConst.RESPONSE_ERROR_DEFAULT_CODE;
        this.message = message;
    }

    public RestError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public RestError(ServiceError serviceError) {
        this.code = serviceError.getCode();

        Locale locale = LocaleContextHolder.getLocale();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages.common", locale);

        this.message = resourceBundle.getString(serviceError.getMessage());
    }

    public RestError(ServiceError serviceError, List<String> fields) {
        this.code = serviceError.getCode();
        this.fields = fields;

        Locale locale = LocaleContextHolder.getLocale();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages.common", locale);

        this.message = resourceBundle.getString(serviceError.getMessage());
    }
}
