package web.forum.imageservice.config.i18n;

import lombok.*;
import org.springframework.context.*;
import org.springframework.stereotype.*;
import org.springframework.web.context.request.*;

@Component
@AllArgsConstructor
public class I18nUtil {

    private final MessageSource messageSource;
    public String getMessage(String code, WebRequest request, String... args){
        return messageSource.getMessage(code, args, request.getLocale());
    }

}
