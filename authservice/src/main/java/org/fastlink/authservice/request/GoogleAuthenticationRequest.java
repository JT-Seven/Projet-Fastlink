package org.fastlink.authservice.request;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class GoogleAuthenticationRequest
{
    private String authuser;
    private String code;
    private String prompt;
    private String scope;
}
