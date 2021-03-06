package com.jakduk.api.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author pyohwan
 *         16. 7. 9 오후 11:48
 */

@Getter
@ApiModel(description = "SNS 기반 로그인 폼")
public class LoginSocialUserForm {

    @ApiModelProperty(required = true, example = "EAALwXK...", value = "OAuth 인증에서 사용하는 AccessToken")
    @NotEmpty
    private String accessToken;

}
