package eshop.web.util;

import org.webjars.NotFoundException;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(String msg) {
        super(msg);
    }
}
