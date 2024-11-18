package com.dylan.elearning.dto.request.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class AddressRequest {

    @Size(max = 255, message = "Country must not exceed 255 characters.")
    private String country;

    @Size(max = 255, message = "City must not exceed 255 characters.")
    private String city;

    @Size(max = 255, message = "District must not exceed 255 characters.")
    private String district;

    @Size(max = 255, message = "Street must not exceed 255 characters.")
    private String street;

    @Size(max = 255, message = "Address detail must not exceed 255 characters.")
    private String addressDetail;

}
