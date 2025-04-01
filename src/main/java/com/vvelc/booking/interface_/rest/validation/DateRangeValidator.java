package com.vvelc.booking.interface_.rest.validation;

import com.vvelc.booking.interface_.rest.dto.BookingOrderRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, BookingOrderRequest> {

    @Override
    public boolean isValid(BookingOrderRequest request, ConstraintValidatorContext context) {
        if (request == null) return true; // evita NPE, validaciones de @NotNull ya se encargarán
        if (request.checkIn() == null || request.checkOut() == null) return true;

        return request.checkOut().isAfter(request.checkIn());
    }
}
