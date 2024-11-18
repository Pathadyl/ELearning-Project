package com.dylan.elearning.dto.request.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class UserRegisterReq {

    @Size(max = 255, message = "First Name must not exceed 255 characters.")
    private String firstName;

    @Size(max = 255, message = "Middle name must not exceed 255 characters.")
    private String midName;

    @Size(max = 255, message = "Last name must not exceed 255 characters.")
    private String lastName;

    private List<AddressRequest> addressCreateRequests;

    @NotBlank(message = "Name must not be blank.")
    @Size(max = 50, message = "Username must not exceed 50 characters.")
    private String username;

    @Size(max = 50, message = "Username must not exceed 50 characters.")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters.")
    private String password;

    @Size(max = 255, message = "Nick name must not exceed 50 characters.")
    private String nickname;


}